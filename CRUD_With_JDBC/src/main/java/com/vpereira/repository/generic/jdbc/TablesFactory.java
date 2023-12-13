package com.vpereira.repository.generic.jdbc;

import com.vpereira.annotation.Column;
import com.vpereira.annotation.Id;
import com.vpereira.annotation.Table;
import com.vpereira.service.generic.GenericReflections;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.vpereira.repository.generic.jdbc.ConnectionFactory.getConnection;

public class TablesFactory {

    public static void createTablesJDBC() {
        try{
            Set<Class<?>> classes = GenericReflections.getClassesWithAnnotation(Table.class);
            List<Class<?>> filteredClasses = verifyTableDataBase(classes);
            List<String> listSQL = generateSQL(filteredClasses);
            executeSQL(listSQL);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    private static List<Class<?>> verifyTableDataBase(Set<Class<?>> classes) throws SQLException {
        List<Class<?>> classesTableNotExist = new ArrayList<>();
        for (Class<?> clazz : classes) {
            String nameTable = clazz.getAnnotation(Table.class).value().toLowerCase();
            String sql = """
                    SELECT EXISTS (
                       SELECT 1
                       FROM   information_schema.tables\s
                       WHERE  table_name = '%s'
                    );
                    """.formatted(nameTable);
            ResultSet resultSet = sqlQuery(sql);
            if (resultSet.next()) {
                // O valor retornado é um booleano, onde true significa que a tabela existe
                boolean tableExist = resultSet.getBoolean(1);
                if (!tableExist){
                    classesTableNotExist.add(clazz);
                }
            }
        }
        return classesTableNotExist;
    }

    private static ResultSet sqlQuery(String sql) throws SQLException {
        Connection connection = null;
        PreparedStatement stm = null;
        ResultSet resultSet = null;
        connection = getConnection();
        stm = connection.prepareStatement(sql);
        resultSet = stm.executeQuery();
        return resultSet;
    }

    private static void executeSQL(List<String> listSQL) throws SQLException {
        Connection connection = null;
        PreparedStatement stm = null;
        ResultSet resultSet = null;
        connection = getConnection();
        for (int i = 0; i < listSQL.stream().count(); i++) {
            String sql = listSQL.get(i);
            stm = connection.prepareStatement(sql);
            stm.executeUpdate();
        }
    }

    private static void closeConnection(Connection connection, PreparedStatement stm, ResultSet rs) {
        try {
            if (rs != null && !rs.isClosed()) {
                rs.close();
            }
            if (stm != null && !stm.isClosed()) {
                stm.close();
            }
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }

    private static List<String> generateSQL(List<Class<?>> classes){
        List<String> listSQL = new ArrayList<>();
        for (Class<?> clazz : classes) {
            Table table = clazz.getAnnotation(Table.class);
            Field[] fields = clazz.getDeclaredFields();
            List<Column> columnFields = GenericReflections.getAnnotationsInFields(fields, Column.class);
            Field idField = GenericReflections.getFieldWithAnnotation(fields, Id.class);

            String sql = "CREATE TABLE " + table.value() + " (";
            sql += idField.getName() + " SERIAL PRIMARY KEY";
            //sql += idField.getName() + " " +  GenericReflections.javaTypeConverterToSQLType(idField) + " PRIMARY KEY";
            for (int i = 0; i < columnFields.stream().count(); i++) {
                sql += ", " + columnFields.get(i).value() + " " +  GenericReflections.javaTypeConverterToSQLType(fields[i+1]);
                if(columnFields.get(i).notNull()){
                    sql += " NOT NULL";
                }
            }
            sql += ")";
            System.out.println(sql);

            listSQL.add(sql);
        }
        return listSQL;
    }
}

