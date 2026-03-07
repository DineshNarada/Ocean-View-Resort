package com.orrs.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource;

/**
 * Database Configuration with HikariCP Connection Pooling
 * Singleton pattern for managing database connection pool
 */
public class DatabaseConfig {
    
    private static volatile DatabaseConfig instance;
    private HikariDataSource dataSource;
    
    // Database Connection Properties
    private static final String DB_URL = "jdbc:mysql://localhost:3306/ocean_view_resort";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = ""; // Change this!
    private static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final int POOL_SIZE = 10;
    
    /**
     * Private constructor to prevent instantiation
     */
    private DatabaseConfig() {
        initializeDataSource();
    }
    
    /**
     * Get singleton instance of DatabaseConfig
     */
    public static DatabaseConfig getInstance() {
        if (instance == null) {
            synchronized (DatabaseConfig.class) {
                if (instance == null) {
                    instance = new DatabaseConfig();
                }
            }
        }
        return instance;
    }
    
    /**
     * Initialize HikariCP datasource
     */
    private void initializeDataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(DB_URL);
        config.setUsername(DB_USER);
        config.setPassword(DB_PASSWORD);
        config.setDriverClassName(DB_DRIVER);
        config.setMaximumPoolSize(POOL_SIZE);
        config.setMinimumIdle(5);
        config.setConnectionTimeout(20000); // 20 seconds
        config.setIdleTimeout(300000); // 5 minutes
        config.setMaxLifetime(1200000); // 20 minutes
        config.setAutoCommit(true);
        
        dataSource = new HikariDataSource(config);
    }
    
    /**
     * Get datasource for connection acquisition
     */
    public DataSource getDataSource() {
        return dataSource;
    }
    
    /**
     * Get a database connection from the pool
     */
    public java.sql.Connection getConnection() throws java.sql.SQLException {
        return dataSource.getConnection();
    }
    
    /**
     * Close the datasource and connection pool
     */
    public void close() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }
}
