package com.vpereira.repository.generic;

import com.vpereira.core.domain.Entity;

import java.io.Serializable;
import java.sql.ResultSet;

public interface IGenericRepository <T extends Entity, E extends Serializable>{

    public void create(T entity);
    public String generateCreateSQL(T entity);
    public void findById(Class<T> entityClass, E id);
    public String generateFindByIdSQL(Class<T> entityClass, E id);
    public void update(T entity);
    public String generateUpdateSQL(T entity);
    public void delete(Class<T> entityClass, E id);
    public String generateDeleteSQL(Class<T> entityClass, E id);
    //public void executeSQL(String sql, String greenMsg, String redMsg);
    //public ResultSet querySQL(String sql);
}
