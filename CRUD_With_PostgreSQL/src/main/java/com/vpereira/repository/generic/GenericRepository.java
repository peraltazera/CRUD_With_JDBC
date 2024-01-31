package com.vpereira.repository.generic;

import com.vpereira.annotation.Column;
import com.vpereira.annotation.Id;
import com.vpereira.annotation.Table;
import com.vpereira.core.domain.Entity;
import com.vpereira.service.generic.GenericReflections;

import java.io.Serializable;
import java.lang.reflect.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.vpereira.repository.generic.jdbc.ConnectionFactory.closeConnection;
import static com.vpereira.repository.generic.jdbc.ConnectionFactory.getConnection;

public class GenericRepository<T extends Entity, E extends Serializable> implements IGenericRepository<T,E>{

    private Class<T> classEntyti;

    @SuppressWarnings("unchecked")
    public GenericRepository(){
        Type type = getClass().getGenericSuperclass();
        ParameterizedType paramType = (ParameterizedType) type;
        this.classEntyti = (Class<T>) paramType.getActualTypeArguments()[0];
    }

    @Override
    public String create(T entity)  {
        String sql = generateCreateSQL(entity);
        String greenMsg = "Registrado com sucesso";
        String redMsg = "Não registrado";
        return executeSQL(sql, greenMsg, redMsg);
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
        String nameTable = table.value().isEmpty() ? clazz.getSimpleName() : table.value();
        String sql = "INSERT INTO " + nameTable + " (";
        for (int i = 0; i < columnFields.stream().count(); i++) {
            String nameColumn = columnFields.get(i).value();
            if (nameColumn.isEmpty()){
                nameColumn = fieldsWithColumn.get(i).getName();
            }
            sql += nameColumn;
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
    public T findById(E id) {
        String sql = generateFindByIdSQL(id);
        ResultSet resultSet = querySQL(sql);
        T entity = null;
        try{
            if(resultSet.next())
            {
                Table table = classEntyti.getAnnotation(Table.class);
                Field[] fields = classEntyti.getDeclaredFields();
                List<Column> columnFields = GenericReflections.getAnnotationsInFields(fields, Column.class);
                List<Field> fieldsWithColumn = GenericReflections.getFieldsWithAnnotation(fields, Column.class);
                Constructor<T> constructor = classEntyti.getDeclaredConstructor();
                entity = constructor.newInstance();
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
                    Method method = classEntyti.getDeclaredMethod(setField, parameterTypes);
                    String nameColumn = columnFields.get(i).value();
                    if (nameColumn.isEmpty()){
                        nameColumn = fieldsWithColumn.get(i).getName();
                    }
                    Object arg = converterTypeArg(fieldType, resultSet, nameColumn);
                    method.invoke(entity, arg);
                }
                Field idField = GenericReflections.getFieldWithAnnotation(fields, Id.class);
                Class<?> fieldType = idField.getType();
                Class<?>[] parameterTypes = {fieldType};
                Method method = classEntyti.getDeclaredMethod("setId", parameterTypes);
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
        return entity;
    }

    @Override
    public String generateFindByIdSQL(E id) {
        Table table = classEntyti.getAnnotation(Table.class);
        Field[] fields = classEntyti.getDeclaredFields();
        Field idField = GenericReflections.getFieldWithAnnotation(fields, Id.class);
        String nameTable = table.value().isEmpty() ? classEntyti.getSimpleName() : table.value();
        String sql = "SELECT * FROM " + nameTable + " WHERE " + idField.getName() + " = " + id;
        System.out.println(sql);
        return sql;
    }

    @Override
    public List<T> findAll() {
        List<T> listEntity = new ArrayList<>();
        String sql = generatefindAllSQL();
        ResultSet resultSet = querySQL(sql);
        try{
            while (resultSet.next())
            {
                Table table = classEntyti.getAnnotation(Table.class);
                Field[] fields = classEntyti.getDeclaredFields();
                List<Column> columnFields = GenericReflections.getAnnotationsInFields(fields, Column.class);
                List<Field> fieldsWithColumn = GenericReflections.getFieldsWithAnnotation(fields, Column.class);
                Constructor<T> constructor = classEntyti.getDeclaredConstructor();
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
                    Method method = classEntyti.getDeclaredMethod(setField, parameterTypes);
                    String nameColumn = columnFields.get(i).value();
                    if (nameColumn.isEmpty()){
                        nameColumn = fieldsWithColumn.get(i).getName();
                    }
                    Object arg = converterTypeArg(fieldType, resultSet, nameColumn);
                    method.invoke(entity, arg);
                }
                Field idField = GenericReflections.getFieldWithAnnotation(fields, Id.class);
                Class<?> fieldType = idField.getType();
                Class<?>[] parameterTypes = {fieldType};
                Method method = classEntyti.getDeclaredMethod("setId", parameterTypes);
                Object arg = converterTypeArg(fieldType, resultSet, idField.getName());
                method.invoke(entity, arg);
                listEntity.add(entity);
            }
            for (T entity : listEntity) {
                System.out.println(entity);
            }
        }catch (SQLException | InstantiationException | IllegalAccessException | InvocationTargetException e){
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return listEntity;
    }

    @Override
    public String generatefindAllSQL() {
        Table table = classEntyti.getAnnotation(Table.class);
        String nameTable = table.value().isEmpty() ? classEntyti.getSimpleName() : table.value();
        String sql = "SELECT * FROM " + nameTable;
        System.out.println(sql);
        return sql;
    }

    @Override
    public String update(T entity, E id) {
        String sql = generateUpdateSQL(entity, id);
        String greenMsg = "Atualizado com sucesso";
        String redMsg = "Registro " + entity.getId() + " Não encontado";
        return executeSQL(sql, greenMsg, redMsg);
    }

    @Override
    public String generateUpdateSQL(T entity, E id) {
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
        String nameTable = table.value().isEmpty() ? clazz.getSimpleName() : table.value();
        String sql = "UPDATE " + nameTable + " SET ";
        for (int i = 0; i < columnFields.stream().count(); i++) {
            Object value = null;
            try{
                value = clazz.getMethod(getFields.get(i)).invoke(entity);
            }catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e){
                e.printStackTrace();
            }
            String nameColumn = columnFields.get(i).value();
            if (nameColumn.isEmpty()){
                nameColumn = fieldsWithColumn.get(i).getName();
            }
            if(i != columnFields.stream().count()-1){
                sql +=  nameColumn + " = " + typeSQL(value) + ", ";
            }else {
                sql +=  nameColumn + " = " + typeSQL(value);
            }
        }
        sql += " WHERE " + idField.getName() + " = " + typeSQL(id);
        System.out.println(sql);
        return sql;
    }

    @Override
    public String delete(E id) {
        String sql = generateDeleteSQL(id);
        String greenMsg = "Registro " + id + " Deletado com sucesso";
        String redMsg = "Registro " + id + " Não encontado";
        return executeSQL(sql, greenMsg, redMsg);
    }

    @Override
    public String generateDeleteSQL(E id) {
        Table table = classEntyti.getAnnotation(Table.class);
        Field[] fields = classEntyti.getDeclaredFields();
        Field idField = GenericReflections.getFieldWithAnnotation(fields, Id.class);
        String nameTable = table.value().isEmpty() ? classEntyti.getSimpleName() : table.value();
        String sql = "DELETE FROM " + nameTable + " WHERE " + idField.getName() + " = " + id;
        System.out.println(sql);
        return sql;
    }

    private String executeSQL(String sql, String greenMsg, String redMsg) {
        Connection connection = null;
        PreparedStatement stm = null;
        ResultSet resultSet = null;
        int affectedRows = 0;
        try{
            connection = getConnection();
            stm = connection.prepareStatement(sql);
            affectedRows = stm.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
        closeConnection(connection, stm, resultSet);
        if (affectedRows > 0) {
            System.out.println(greenMsg);
            return greenMsg;
        } else {
            System.out.println(redMsg);
            return redMsg;
        }
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
            obj = (Character) resultSet.getString(rsParameter).charAt(0);
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
