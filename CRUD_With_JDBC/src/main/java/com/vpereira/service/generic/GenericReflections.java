package com.vpereira.service.generic;

import com.vpereira.Main;
import com.vpereira.annotation.Column;
import com.vpereira.annotation.Id;
import com.vpereira.annotation.Table;
import com.vpereira.core.domain.Entity;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class GenericReflections {

    private static GenericReflections genericReflections;

    private GenericReflections() {

    }

    public static GenericReflections getInstance() {
        if (genericReflections == null) {
            genericReflections = new GenericReflections();
        }
        return genericReflections;
    }

    public static <T extends Annotation> Set<Class<?>> getClassesWithAnnotation(Class<T> annotationClass) {
        String packageName = Main.class.getPackageName();
        Set<Class<?>> classes = null;
        try {
            Reflections reflections = new Reflections(packageName);
            classes = reflections.getTypesAnnotatedWith(annotationClass);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return classes;
    }

    public static <T extends Annotation> Field getFieldWithAnnotation(Field[] fields, Class<T> annotationClass) {
        Field idField = null;
        for (Field field : fields) {
            if (field.isAnnotationPresent(annotationClass)) {
                idField = field;
            }
        }
        return idField;
    }

    public static <T extends Annotation> List<Field> getFieldsWithAnnotation(Field[] fields, Class<T> annotationClass) {
        List<Field> fieldsWithAnnotation = new ArrayList<>();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Column.class)) {
                fieldsWithAnnotation.add(field);
            }
        }
        return fieldsWithAnnotation;
    }

    public static  <T extends Annotation> List<T> getAnnotationsInFields(Field[] fields, Class<T> annotationClass) {
        List<T> annotatedFields = new ArrayList<>();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Column.class)) {
                annotatedFields.add(field.getAnnotation(annotationClass));
            }
        }
        return annotatedFields;
    }

    public static String javaTypeConverterToSQLType(Field field){
        String strReturn = "";
        if(field.getType() == Short.class || field.getType() == short.class) strReturn = "SMALLINT";
        else if(field.getType() == Integer.class || field.getType() == int.class) strReturn = "INT";
        else if(field.getType() == Long.class || field.getType() == long.class) strReturn = "BIGINT";
        else if(field.getType() == Float.class || field.getType() == float.class) strReturn = "FLOAT";
        else if(field.getType() == Double.class || field.getType() == double.class) strReturn = "double precision";
        else if(field.getType() == Character.class || field.getType() == char.class) strReturn = "CHAR";
        else if(field.getType() == Boolean.class || field.getType() == boolean.class) strReturn = "BOOLEAN";
        else if(field.getType() == Byte.class || field.getType() == byte.class) strReturn = "bytea";
        else if(field.getType() == String.class){
            strReturn = "VARCHAR(" + field.getAnnotation(Column.class).length() + ")";
        }
        else {
            strReturn = field.getType().getSimpleName();
        }
        return strReturn;
    }

    public static <T extends Entity> T converterJsonToEntity(String json, Class<T> clazz) {
        Table table = clazz.getAnnotation(Table.class);
        Field[] fields = clazz.getDeclaredFields();
        List<Column> columnFields = GenericReflections.getAnnotationsInFields(fields, Column.class);
        List<Field> fieldsWithColumn = GenericReflections.getFieldsWithAnnotation(fields, Column.class);
        Constructor<T> constructor = null;
        try {
            constructor = clazz.getDeclaredConstructor();
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        T entity = null;
        try {
            entity = constructor.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }

        for (int i = 0; i < fieldsWithColumn.stream().count(); i++) {
            String setField = null;
            String nameField = fieldsWithColumn.get(i).getName();
            if(columnFields.get(i).set().isEmpty()){
                setField = "set" + nameField.substring(0, 1).toUpperCase() + nameField.substring(1);
            }else {
                setField = columnFields.get(i).set();
            }

            Class<?> fieldType = fieldsWithColumn.get(i).getType();
            Class<?>[] parameterTypes = {fieldType};
            Method method = null;
            try {
                method = clazz.getDeclaredMethod(setField, parameterTypes);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
            Object arg = null;

            String[] tokens = json.split("[{},:\"]");

            for (int ind = 0; ind < tokens.length; ind++) {
                tokens[ind] = tokens[ind].trim();
            }

            List<String> lista = new ArrayList<>(Arrays.asList(tokens));
            lista.removeIf(String::isEmpty);

            tokens = lista.toArray(new String[0]);

            for (int ind = 0; ind < tokens.length; ind++) {
                if(tokens[ind].trim().equals(nameField)){
                    arg = converterTypeArg(fieldType, tokens[ind+1]);
                }
            }

            try {
                method.invoke(entity, arg);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
        return entity;
    }

    private static Object converterTypeArg(Class<?> classType, String jsonAtribute) {
        Object obj = jsonAtribute;
        if (classType == Integer.TYPE || classType == Integer.class) {
            obj = Integer.parseInt(jsonAtribute);
        }
        else if (classType == Long.TYPE || classType == Long.class) {
            obj = Long.parseLong(jsonAtribute);
        }
        else if (classType == Float.TYPE || classType == Float.class) {
            obj = Float.parseFloat(jsonAtribute);
        }
        else if (classType == Double.TYPE || classType == Double.class) {
            obj = Double.parseDouble(jsonAtribute);
        }
        else if (classType == Character.TYPE || classType == Character.class) {
            obj = (Character) jsonAtribute.charAt(0);
        }
        else if (classType == Boolean.TYPE || classType == Boolean.class) {
            obj = Boolean.parseBoolean(jsonAtribute);
        }
        else if (classType == Byte.TYPE || classType == Byte.class) {
            obj = Byte.parseByte(jsonAtribute);
        }
        return obj;
    }

    public static <T extends Entity> String converterEntityToJson(T entity) {
        Class<T> clazz = (Class<T>) entity.getClass();
        Field[] fields = clazz.getDeclaredFields();
        List<Column> columnFields = GenericReflections.getAnnotationsInFields(fields, Column.class);
        List<Field> fieldsWithColumn = GenericReflections.getFieldsWithAnnotation(fields, Column.class);
        Field idField = GenericReflections.getFieldWithAnnotation(fields, Id.class);
        Class<?> idType = idField.getType();
        String json = "{";
        json += "\"id\": ";
        if (idType == Character.TYPE || idType == Character.class ||
                idType == String.class || idType == String.class) {
            json += "\""+entity.getId()+"\",";
        }else {
            json += entity.getId()+",";
        }
        for (int i = 0; i < fieldsWithColumn.stream().count(); i++) {
            String getField = null;
            String nameField = fieldsWithColumn.get(i).getName();
            if(columnFields.get(i).get().isEmpty()){
                getField = "get" + nameField.substring(0, 1).toUpperCase() + nameField.substring(1);
            }else {
                getField = columnFields.get(i).get();
            }
            Class<?> fieldType = fieldsWithColumn.get(i).getType();
            Method method = null;
            try {
                method = clazz.getDeclaredMethod(getField);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
            Object value = null;
            try {
                value = method.invoke(entity);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            }
            json += "\""+nameField+"\": ";
            if (fieldType == Character.TYPE || fieldType == Character.class ||
                    fieldType == String.class || fieldType == String.class) {
                json += "\""+value+"\",";
            }
            else {
                json += value+",";
            }
        }
        if (!json.isEmpty()) {
            json = json.substring(0, json.length() - 1);
        }
        json += "}";
        return json;
    }

    public static <T extends Entity> String converterEntitysToJson(List<T> listEntity) {
        Class<T> clazz = (Class<T>) listEntity.get(0).getClass();
        Field[] fields = clazz.getDeclaredFields();
        List<Column> columnFields = GenericReflections.getAnnotationsInFields(fields, Column.class);
        List<Field> fieldsWithColumn = GenericReflections.getFieldsWithAnnotation(fields, Column.class);
        Field idField = GenericReflections.getFieldWithAnnotation(fields, Id.class);
        Class<?> idType = idField.getType();
        String json = "[";
        List<Class<?>> fieldType = new ArrayList<>();
        List<Method> method = new ArrayList<>();
        List<String> nameField = new ArrayList<>();
        for (int i = 0; i < fieldsWithColumn.stream().count(); i++) {
            String getField = null;
            nameField.add(fieldsWithColumn.get(i).getName());
            if(columnFields.get(i).get().isEmpty()){
                getField = "get" + nameField.get(i).substring(0, 1).toUpperCase() + nameField.get(i).substring(1);
            }else {
                getField = columnFields.get(i).get();
            }
            fieldType.add(fieldsWithColumn.get(i).getType());
            try {
                method.add(clazz.getDeclaredMethod(getField));
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }
        for (T entity : listEntity)
        {
            json += "{";
            json += "\"id\": ";
            if (idType == Character.TYPE || idType == Character.class ||
                    idType == String.class || idType == String.class) {
                json += "\""+entity.getId()+"\",";
            }else {
                json += entity.getId()+",";
            }
            for (int i = 0; i < method.stream().count(); i++) {
                Object value = null;
                try {
                    value = method.get(i).invoke(entity);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                } catch (InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
                json += "\""+nameField.get(i)+"\": ";
                if (fieldType.get(i) == Character.TYPE || fieldType.get(i) == Character.class ||
                        fieldType.get(i) == String.class || fieldType.get(i) == String.class) {
                    json += "\""+value+"\",";
                }
                else {
                    json += value+",";
                }
            }
            if (!json.isEmpty()) {
                json = json.substring(0, json.length() - 1);
            }
            json += "},";
        }
        if (!json.isEmpty()) {
            json = json.substring(0, json.length() - 1);
        }
        json += "]";
        return json;
    }
}
