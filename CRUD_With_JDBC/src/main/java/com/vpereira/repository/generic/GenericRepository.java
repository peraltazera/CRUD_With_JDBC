package com.vpereira.repository.generic;

import com.vpereira.annotation.Column;
import com.vpereira.annotation.Id;
import com.vpereira.annotation.Table;
import com.vpereira.core.domain.Entity;
import com.vpereira.service.generic.GenericReflections;

import java.io.Serializable;
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

import static com.vpereira.repository.generic.jdbc.ConnectionFactory.closeConnection;
import static com.vpereira.repository.generic.jdbc.ConnectionFactory.getConnection;

public class GenericRepository<T extends Entity, E extends Serializable> implements IGenericRepository<T,E>{

    @Override
    public void create(T entity)  {
        String sql = generateCreateSQL(entity);
        String greenMsg = "Registrado com sucesso";
        String redMsg = "Não registrado";
        executeSQL(sql, greenMsg, redMsg);
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
            Object value = null;
            try{
                value = clazz.getMethod(getFields.get(i)).invoke(entity);
            }catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e){
                e.printStackTrace();
            }
            if(i != columnFields.stream().count()-1){
                sql += typeSQL(value) + ", ";
            }else {
                sql += typeSQL(value);
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
                List<Field> fieldsWithColumn = GenericReflections.getFieldsWithAnnotation(fields, Column.class);
                Constructor<T> constructor = entityClass.getDeclaredConstructor();
                T entity = constructor.newInstance();
                for (int i = 0; i < fieldsWithColumn.stream().count(); i++) {
                    String setField = "";
                    if(columnFields.get(i).set().isEmpty()){
                        var nameField = fieldsWithColumn.get(i).getName();
                        nameField = "set" + nameField.substring(0, 1).toUpperCase() + nameField.substring(1);
                        setField = nameField;
                    }else {
                        setField = columnFields.get(i).set();
                    }
                    Class<?> fieldType = fieldsWithColumn.get(i).getType();
                    Class<?>[] parameterTypes = {fieldType};
                    Method method = entityClass.getDeclaredMethod(setField, parameterTypes);
                    Object arg = converterTypeArg(fieldType, resultSet, columnFields.get(i).value());
                    method.invoke(entity, arg);
                }
                Field idField = GenericReflections.getFieldWithAnnotation(fields, Id.class);
                Class<?> fieldType = idField.getType();
                Class<?>[] parameterTypes = {fieldType};
                Method method = entityClass.getDeclaredMethod("setId", parameterTypes);
                Object arg = converterTypeArg(fieldType, resultSet, idField.getName());
                method.invoke(entity, arg);
                System.out.println(entity);
            }else {
                System.out.println("Registro não encontrado");
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
        String greenMsg = "Atualizado com sucesso";
        String redMsg = "Registro " + entity.getId() + " Não encontado";
        executeSQL(sql, greenMsg, redMsg);
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
            Object value = null;
            try{
                value = clazz.getMethod(getFields.get(i)).invoke(entity);
            }catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e){
                e.printStackTrace();
            }
            if(i != columnFields.stream().count()-1){
                sql +=  columnFields.get(i).value() + " = " + typeSQL(value) + ", ";
            }else {
                sql +=  columnFields.get(i).value() + " = " + typeSQL(value);
            }
        }
        sql += " WHERE " + idField.getName() + " = " + typeSQL(entity.getId());
        System.out.println(sql);
        return sql;
    }

    @Override
    public void delete(Class<T> entityClass, E id) {
        String sql = generateDeleteSQL(entityClass, id);
        String greenMsg = "Registro " + id + " Deletado com sucesso";
        String redMsg = "Registro " + id + " Não encontado";
        executeSQL(sql, greenMsg, redMsg);
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

    private void executeSQL(String sql, String greenMsg, String redMsg) {
        Connection connection = null;
        PreparedStatement stm = null;
        ResultSet resultSet = null;
        try{
            connection = getConnection();
            stm = connection.prepareStatement(sql);
            int affectedRows = stm.executeUpdate();
            if (affectedRows > 0) {
                System.out.println(greenMsg);
            } else {
                System.out.println(redMsg);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        closeConnection(connection, stm, resultSet);
    }

    private ResultSet querySQL(String sql) {
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

    private Object typeSQL(Object obj) {
        String strReturn = "";
        if(obj instanceof String || obj instanceof Character){
            strReturn = "'"+obj+"'";
            return strReturn;
        }
        return obj;
    }

    private Object converterTypeArg(Class<?> classType, ResultSet resultSet, String rsParameter) throws SQLException {
        Object obj = null;
        if (classType == Integer.TYPE || classType == Integer.class) {
            obj = resultSet.getInt(rsParameter);
        }
        else if (classType == Long.TYPE || classType == Long.class) {
            obj = resultSet.getLong(rsParameter);
        }
        else if (classType == Float.TYPE || classType == Float.class) {
            obj = resultSet.getFloat(rsParameter);
        }
        else if (classType == Double.TYPE || classType == Double.class) {
            obj = resultSet.getDouble(rsParameter);
        }
        else if (classType == Character.TYPE || classType == Character.class) {
            obj = resultSet.getCharacterStream(rsParameter);
        }
        else if (classType == Boolean.TYPE || classType == Boolean.class) {
            obj = resultSet.getBoolean(rsParameter);
        }
        else if (classType == Byte.TYPE || classType == Byte.class) {
            obj = resultSet.getByte(rsParameter);
        }
        else if (classType == String.class) {
            obj = resultSet.getString(rsParameter);
        }
        return obj;
    }
}
