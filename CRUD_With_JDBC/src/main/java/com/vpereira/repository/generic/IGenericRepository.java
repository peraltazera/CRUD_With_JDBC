package com.vpereira.repository.generic;

import com.vpereira.model.Entity;

import java.io.Serializable;
import java.sql.SQLException;

public interface IGenericRepository <T extends Entity, E extends Serializable>{

    public void create(T entity) throws SQLException;
    public String generateCreateSQL(T entity) throws SQLException;
    public void findById();
    public String generateFindByIdSQL() throws SQLException;
    public void update() throws SQLException;
    public String generateUpdateSQL() throws SQLException;
    public void delete() throws SQLException;
    public String generateDeleteSQL() throws SQLException;
}
