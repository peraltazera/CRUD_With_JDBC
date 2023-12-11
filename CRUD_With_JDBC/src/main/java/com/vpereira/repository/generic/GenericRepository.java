package com.vpereira.repository.generic;

import com.vpereira.annotation.Column;
import com.vpereira.annotation.Table;
import com.vpereira.model.Entity;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static com.vpereira.repository.generic.jdbc.ConnectionFactory.getConnection;

public class GenericRepository<T extends Entity, E extends Serializable> implements IGenericRepository<T,E>{

    @Override
    public void create(T entity) throws SQLException {
        Connection connection = null;
        PreparedStatement stm = null;
        connection = getConnection();
        String sql = generateCreateSQL(entity)
;       stm = connection.prepareStatement(sql);
        stm.executeUpdate();
    }

    @Override
    public String generateCreateSQL(T entity) throws SQLException {
        Class<?> clazz = entity.getClass();
        Annotation table = clazz.getAnnotation(Table.class);
        Field[] fields = clazz.getDeclaredFields();
        List<Column> columnFields = getFieldsWithAnnotationColumn(fields);
        Field idField = getFieldWithAnnotationId(fields);
        String sql = "CREATE TABLE " + table.value() + " (";
        sql += idField.getName() + " " + javaTypeConverterToSQLType(idField) + " PRIMARY KEY";
        for (int i = 0; i < columnFields.stream().count(); i++) {
            sql += ", " + columnFields.get(i).value() + " " + javaTypeConverterToSQLType(fields[i+1]);
            if(columnFields.get(i).notNull()){
                sql += " NOT NULL";
            }
        }
        sql += ")";
        System.out.println(sql);

        return sql;
    }

    @Override
    public void findById() {

    }

    @Override
    public String generateFindByIdSQL() throws SQLException {
        return "";
    }

    @Override
    public void update() throws SQLException {
        Connection connection = null;
        PreparedStatement stm = null;
        ResultSet resultSet = null;
        connection = getConnection();
        String sql = "";
        stm = connection.prepareStatement(sql);
        stm.executeUpdate();
    }

    @Override
    public String generateUpdateSQL() throws SQLException {
        return "";
    }

    @Override
    public void delete() throws SQLException {
        Connection connection = null;
        PreparedStatement stm = null;
        ResultSet resultSet = null;
        connection = getConnection();
        String sql = "";
        ;       stm = connection.prepareStatement(sql);
        stm.executeUpdate();
    }

    @Override
    public String generateDeleteSQL() throws SQLException {
        return "";
    }
}
