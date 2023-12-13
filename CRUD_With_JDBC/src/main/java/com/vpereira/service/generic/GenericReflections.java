package com.vpereira.service.generic;

import com.vpereira.Main;
import com.vpereira.annotation.Column;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
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
}
