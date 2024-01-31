package com.vpereira.repository.generic.mongo;

import com.mongodb.client.MongoDatabase;
import com.vpereira.annotation.DocumentMongoDB;
import com.vpereira.service.generic.GenericReflections;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CollectionFactory {

    public static void createCollections() {
        Set<Class<?>> classes = GenericReflections.getClassesWithAnnotation(DocumentMongoDB.class);
        createCollectionsDataBase(classes);
    }

    private static void createCollectionsDataBase(Set<Class<?>> classes) {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        MongoDatabase database = connectionFactory.getDatabase();
        List<Class<?>> classesTableNotExist = new ArrayList<>();
        for (Class<?> clazz : classes) {
            DocumentMongoDB documentMongoDB = clazz.getAnnotation(DocumentMongoDB.class);
            String nameCollection = documentMongoDB.value().isEmpty() ? clazz.getSimpleName() : documentMongoDB.value();
            boolean collectionExists = false;
            for (String name : database.listCollectionNames()) {
                if (name.equals(nameCollection)) {
                    collectionExists = true;
                }
            }
            if (!collectionExists) {
                database.createCollection(nameCollection);
            }
        }
        connectionFactory.close();
    }
}

