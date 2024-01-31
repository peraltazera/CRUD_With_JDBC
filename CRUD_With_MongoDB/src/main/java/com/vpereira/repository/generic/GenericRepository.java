package com.vpereira.repository.generic;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.vpereira.annotation.FieldMongoDB;
import com.vpereira.annotation.Id;
import com.vpereira.annotation.DocumentMongoDB;
import com.vpereira.core.domain.Entity;
import com.vpereira.repository.generic.mongo.ConnectionFactory;
import com.vpereira.service.generic.GenericReflections;

import java.io.Serializable;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.List;
import org.bson.Document;
import org.bson.types.ObjectId;

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
        Class<?> clazz = entity.getClass();
        DocumentMongoDB documentMongoDB = clazz.getAnnotation(DocumentMongoDB.class);
        Field[] fields = clazz.getDeclaredFields();
        List<FieldMongoDB> fieldMongoDBFields = GenericReflections.getAnnotationsInFields(fields, FieldMongoDB.class);
        List<Field> fieldsWithColumn = GenericReflections.getFieldsWithAnnotation(fields, FieldMongoDB.class);
        Field idField = GenericReflections.getFieldWithAnnotation(fields, Id.class);
        List<String> getFields = new ArrayList<>();
        Document newDoc = new Document();
        for (int i = 0; i < fieldsWithColumn.stream().count(); i++) {
            if(fieldMongoDBFields.get(i).get().isEmpty()){
                String nameField = fieldsWithColumn.get(i).getName();
                nameField = "get" + nameField.substring(0, 1).toUpperCase() + nameField.substring(1);
                getFields.add(nameField);
            }else {
                getFields.add(fieldMongoDBFields.get(i).get());
            }
        }
        String nameCollection = documentMongoDB.value().isEmpty() ? clazz.getSimpleName() : documentMongoDB.value();
        for (int i = 0; i < fieldMongoDBFields.stream().count(); i++) {
            String nameColumn = fieldMongoDBFields.get(i).value();
            if (nameColumn.isEmpty()){
                nameColumn = fieldsWithColumn.get(i).getName();
            }
            Object valueDoc = null;
            try {
                valueDoc = clazz.getMethod(getFields.get(i)).invoke(entity);
            } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
                e.printStackTrace();
            }
            newDoc.append(nameColumn, valueDoc);
        }
        ConnectionFactory connectionFactory = new ConnectionFactory();
        MongoDatabase database = connectionFactory.getDatabase();
        MongoCollection<Document> collection = database.getCollection(nameCollection);
        collection.insertOne(newDoc);
        connectionFactory.close();
        return "";
    }

    @Override
    public T findById(E id) {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        MongoDatabase database = connectionFactory.getDatabase();
        DocumentMongoDB documentMongoDB = classEntyti.getAnnotation(DocumentMongoDB.class);
        String nameCollection = documentMongoDB.value().isEmpty() ? classEntyti.getSimpleName() : documentMongoDB.value();
        MongoCollection<Document> collection = database.getCollection(nameCollection);
        Document filter = new Document("_id", new ObjectId((String) id));
        Document foundUser = collection.find(filter).first();
        T entity = null;
        if (foundUser != null) {
            entity = GenericReflections.converterJsonMongoDBToEntity(foundUser.toJson(), classEntyti);
        } else {
            System.out.println("Usuário não encontrado.");
        }
        connectionFactory.close();
        return entity;
    }

    @Override
    public List<T> findAll() {
        List<T> listEntity = new ArrayList<>();
        ConnectionFactory connectionFactory = new ConnectionFactory();
        MongoDatabase database = connectionFactory.getDatabase();
        DocumentMongoDB documentMongoDB = classEntyti.getAnnotation(DocumentMongoDB.class);
        String nameCollection = documentMongoDB.value().isEmpty() ? classEntyti.getSimpleName() : documentMongoDB.value();
        MongoCollection<Document> collection = database.getCollection(nameCollection);
        for (Document document : collection.find()) {
            T entity = GenericReflections.converterJsonMongoDBToEntity(document.toJson(), classEntyti);
            listEntity.add(entity);
        }
        connectionFactory.close();
        return listEntity;
    }

    @Override
    public String update(T entity, E id) {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        MongoDatabase database = connectionFactory.getDatabase();
        DocumentMongoDB documentMongoDB = classEntyti.getAnnotation(DocumentMongoDB.class);
        String nameCollection = documentMongoDB.value().isEmpty() ? classEntyti.getSimpleName() : documentMongoDB.value();
        MongoCollection<Document> collection = database.getCollection(nameCollection);
        Class<?> clazz = entity.getClass();
        Field[] fields = clazz.getDeclaredFields();
        List<FieldMongoDB> fieldMongoDBFields = GenericReflections.getAnnotationsInFields(fields, FieldMongoDB.class);
        List<Field> fieldsWithColumn = GenericReflections.getFieldsWithAnnotation(fields, FieldMongoDB.class);
        Field idField = GenericReflections.getFieldWithAnnotation(fields, Id.class);
        Document filter = new Document("_id", new ObjectId((String) id));
        Document updateFields = new Document();
        List<String> getFields = new ArrayList<>();
        for (int i = 0; i < fieldsWithColumn.stream().count(); i++) {
            if(fieldMongoDBFields.get(i).get().isEmpty()){
                String nameField = fieldsWithColumn.get(i).getName();
                nameField = "get" + nameField.substring(0, 1).toUpperCase() + nameField.substring(1);
                getFields.add(nameField);
            }else {
                getFields.add(fieldMongoDBFields.get(i).get());
            }
        }
        for (int i = 0; i < fieldMongoDBFields.stream().count(); i++) {
            String nameColumn = fieldMongoDBFields.get(i).value();
            if (nameColumn.isEmpty()){
                nameColumn = fieldsWithColumn.get(i).getName();
            }
            Object value = null;
            try{
                value = clazz.getMethod(getFields.get(i)).invoke(entity);
            }catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e){
                e.printStackTrace();
            }
            updateFields.append(nameColumn, value);
        }
        Document updateData = new Document("$set", updateFields);
        collection.updateOne(filter, updateData);
        connectionFactory.close();
        return "";
    }

    @Override
    public String delete(E id) {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        MongoDatabase database = connectionFactory.getDatabase();
        DocumentMongoDB documentMongoDB = classEntyti.getAnnotation(DocumentMongoDB.class);
        String nameCollection = documentMongoDB.value().isEmpty() ? classEntyti.getSimpleName() : documentMongoDB.value();
        MongoCollection<Document> collection = database.getCollection(nameCollection);
        Document filter = new Document("_id", new ObjectId((String) id));
        collection.deleteOne(filter);
        connectionFactory.close();
        return "";
    }
}
