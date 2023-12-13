package com.vpereira.repository.generic;

import com.vpereira.annotation.Column;
import com.vpereira.annotation.Id;
import com.vpereira.annotation.Table;
import com.vpereira.model.Entity;
import com.vpereira.service.generic.GenericReflections;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.vpereira.repository.generic.jdbc.ConnectionFactory.getConnection;

public class GenericRepository<T extends Entity, E extends Serializable> implements IGenericRepository<T,E>{

    @Override
    public void create(T entity)  {
        String sql = generateCreateSQL(entity);
        executeSQL(sql);
    }

    @Override
    public String generateCreateSQL(T entity)  {
        Class<?> clazz = entity.getClass();
        Table table = clazz.getAnnotation(Table.class);
        Field[] fields = clazz.getDeclaredFields();
        List<Column> columnFields = GenericReflections.getAnnotationsInFields(fields, Column.class);
        List<Field> fieldsWithColumn = GenericReflections.getFieldsWithAnnotation(fields, Column.class);
        List<String> getFields = new ArrayList<>();
        for (int i = 0; i < fieldsWithColumn.stream().count(); i++) {
            if(columnFields.get(i).get().isEmpty()){
                var nameField = fieldsWithColumn.get(i).getName();
                nameField = "get" + nameField.substring(0, 1).toUpperCase() + nameField.substring(1);
                getFields.add(nameField);
            }else {
                getFields.add(columnFields.get(i).get());
            }
        }
        String sql = "INSERT INTO " + table.value() + " (";
        for (int i = 0; i < columnFields.stream().count(); i++) {
            sql += columnFields.get(i).value();
            if(i != columnFields.stream().count()-1){
                sql +=  ", ";
            }
        }
        sql += ")";
        sql += " VALUES (";
        for (int i = 0; i < columnFields.stream().count(); i++) {
            String value = null;
            try{
                value = (String) clazz.getMethod(getFields.get(i)).invoke(entity);
            }catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e){
                e.printStackTrace();
            }
            if(i != columnFields.stream().count()-1){
                sql +=   "'" + value + "'" + ", ";
            }else {
                sql +=  "'" + value + "'";
            }
        }
        sql += ");";
        System.out.println(sql);
        return sql;
    }

    @Override
    public void findById(Class<T> entityClass, E id) {
        String sql = generateFindByIdSQL(entityClass, id);
        ResultSet resultSet = querySQL(sql);
        try{
            if(resultSet.next())
            {
                Table table = entityClass.getAnnotation(Table.class);
                Field[] fields = entityClass.getDeclaredFields();
                List<Column> columnFields = GenericReflections.getAnnotationsInFields(fields, Column.class);
                System.out.println(resultSet.getLong("id"));
                for (int i = 0; i < columnFields.stream().count(); i++) {
                    System.out.println(resultSet.getString(columnFields.get(i).value()));
                }
                Constructor<T> constructor = entityClass.getDeclaredConstructor();
                T entity = constructor.newInstance();
                System.out.println(entity);
                Method[] methods = entityClass.getDeclaredMethods();
                for (Method method : methods) {
                    if (method.getParameterCount() == 0) {
                        Object result = method.invoke(entity);
                        System.out.println(method.getName() + ": " + result);
                    }
                }
            }
        }catch (SQLException | InstantiationException | IllegalAccessException | InvocationTargetException e){
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String generateFindByIdSQL(Class<T> entityClass, E id) {
        Table table = entityClass.getAnnotation(Table.class);
        Field[] fields = entityClass.getDeclaredFields();
        Field idField = GenericReflections.getFieldWithAnnotation(fields, Id.class);
        String sql = "SELECT * FROM " + table.value() + " WHERE " + idField.getName() + " = " + id;
        System.out.println(sql);
        return sql;
    }

    @Override
    public void update(T entity) {
        String sql = generateUpdateSQL(entity);
        executeSQL(sql);
    }

    @Override
    public String generateUpdateSQL(T entity) {
        Class<?> clazz = entity.getClass();
        Table table = clazz.getAnnotation(Table.class);
        Field[] fields = clazz.getDeclaredFields();
        List<Column> columnFields = GenericReflections.getAnnotationsInFields(fields, Column.class);
        List<Field> fieldsWithColumn = GenericReflections.getFieldsWithAnnotation(fields, Column.class);
        Field idField = GenericReflections.getFieldWithAnnotation(fields, Id.class);
        List<String> getFields = new ArrayList<>();
        for (int i = 0; i < fieldsWithColumn.stream().count(); i++) {
            if(columnFields.get(i).get().isEmpty()){
                var nameField = fieldsWithColumn.get(i).getName();
                nameField = "get" + nameField.substring(0, 1).toUpperCase() + nameField.substring(1);
                getFields.add(nameField);
            }else {
                getFields.add(columnFields.get(i).get());
            }
        }
        String sql = "UPDATE " + table.value() + " SET ";
        for (int i = 0; i < columnFields.stream().count(); i++) {
            String value = null;
            try{
                value = (String) clazz.getMethod(getFields.get(i)).invoke(entity);
            }catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e){
                e.printStackTrace();
            }
            if(i != columnFields.stream().count()-1){
                sql +=  columnFields.get(i).value() + " = '" + value + "', ";
            }else {
                sql +=  columnFields.get(i).value() + " = '" + value + "'";
            }
        }
        sql += " WHERE " + idField.getName() + " = " + entity.getId();
        System.out.println(sql);
        return sql;
    }

    @Override
    public void delete(Class<T> entityClass, E id) {
        String sql = generateDeleteSQL(entityClass, id);
        executeSQL(sql);
    }

    @Override
    public String generateDeleteSQL(Class<T> entityClass, E id) {
        Table table = entityClass.getAnnotation(Table.class);
        Field[] fields = entityClass.getDeclaredFields();
        Field idField = GenericReflections.getFieldWithAnnotation(fields, Id.class);
        String sql = "DELETE FROM " + table.value() + " WHERE " + idField.getName() + " = " + id;
        System.out.println(sql);
        return sql;
    }

    @Override
    public void executeSQL(String sql) {
        Connection connection = null;
        PreparedStatement stm = null;
        ResultSet resultSet = null;
        try{
            connection = getConnection();
            stm = connection.prepareStatement(sql);
            stm.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }


    public ResultSet querySQL(String sql) {
        Connection connection = null;
        PreparedStatement stm = null;
        ResultSet resultSet = null;
        try{
            connection = getConnection();
            stm = connection.prepareStatement(sql);
            resultSet = stm.executeQuery();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return resultSet;
    }
}
