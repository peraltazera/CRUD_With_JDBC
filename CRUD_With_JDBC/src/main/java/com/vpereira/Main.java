package com.vpereira;

import com.vpereira.repository.generic.jdbc.TablesFactory;

public class Main {
    public static void main(String[] args) {
        TablesFactory.createTablesJDBC();
    }
}