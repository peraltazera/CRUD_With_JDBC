package com.vpereira;

import com.vpereira.repository.generic.jdbc.TablesFactory;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class Init implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent event) {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        TablesFactory.createTablesJDBC();
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {

    }
}
