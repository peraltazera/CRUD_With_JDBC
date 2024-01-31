package com.vpereira.repository.generic.mongo;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

import java.sql.*;

public class ConnectionFactory implements AutoCloseable{

    private MongoClient mongoClient;
    private MongoDatabase database;

    public ConnectionFactory() {
        mongoClient = MongoClients.create("mongodb://localhost:27017");
        database = mongoClient.getDatabase("CRUD_With_MongoDB");
    }

    public MongoDatabase getDatabase() {
        return database;
    }

    @Override
    public void close() {
        if (mongoClient != null) {
            mongoClient.close();
        }
    }
}
