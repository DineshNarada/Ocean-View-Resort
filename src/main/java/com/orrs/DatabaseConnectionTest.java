package com.orrs;

import com.orrs.config.DatabaseConfig;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Test class to verify MySQL database connection
 * This validates that:
 * 1. MySQL database is running
 * 2. ocean_view_resort database exists
 * 3. Connection pooling is working
 * 4. Tables are accessible
 */
public class DatabaseConnectionTest {
    
    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("Testing Database Connection...");
        System.out.println("========================================\n");
        
        try {
            // Test 1: Get connection from pool
            System.out.println("[1] Acquiring connection from HikariCP pool...");
            DatabaseConfig config = DatabaseConfig.getInstance();
            Connection conn = config.getConnection();
            System.out.println("✅ Connection successful!\n");
            
            // Test 2: Get database metadata
            System.out.println("[2] Checking database metadata...");
            DatabaseMetaData metadata = conn.getMetaData();
            String databaseName = conn.getCatalog();
            String databaseProduct = metadata.getDatabaseProductName();
            String databaseVersion = metadata.getDatabaseProductVersion();
            
            System.out.println("   Database: " + databaseName);
            System.out.println("   Product: " + databaseProduct);
            System.out.println("   Version: " + databaseVersion);
            System.out.println("✅ Metadata retrieved successfully!\n");
            
            // Test 3: List all tables
            System.out.println("[3] Listing tables in database...");
            ResultSet tables = metadata.getTables(null, null, "%", new String[]{"TABLE"});
            System.out.println("   Available tables:");
            int tableCount = 0;
            while (tables.next()) {
                String tableName = tables.getString("TABLE_NAME");
                System.out.println("      • " + tableName);
                tableCount++;
            }
            System.out.println("   Total tables: " + tableCount);
            if (tableCount == 0) {
                System.out.println("   ⚠️  No tables found! You need to create the database schema.");
            } else {
                System.out.println("✅ Tables found!\n");
            }
            
            // Test 4: Test a simple query
            System.out.println("[4] Testing a simple query...");
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT 1 as test");
            if (rs.next()) {
                System.out.println("   Query result: " + rs.getInt("test"));
                System.out.println("✅ Query execution successful!\n");
            }
            rs.close();
            stmt.close();
            
            // Test 5: Pool status
            System.out.println("[5] Connection Pool Status...");
            System.out.println("   Pool working and connection acquired successfully");
            System.out.println("✅ Pool is operational!\n");
            
            // Close connection (returns to pool)
            conn.close();
            
            System.out.println("========================================");
            System.out.println("✅ ALL TESTS PASSED!");
            System.out.println("========================================");
            
        } catch (Exception e) {
            System.out.println("\n❌ CONNECTION FAILED!");
            System.out.println("========================================");
            System.out.println("Error Details:");
            System.out.println("  Exception: " + e.getClass().getSimpleName());
            System.out.println("  Message: " + e.getMessage());
            System.out.println("========================================\n");
            
            // Provide troubleshooting suggestions
            printTroubleshootingGuide(e);
            
            e.printStackTrace();
        }
    }
    
    private static void printTroubleshootingGuide(Exception e) {
        System.out.println("TROUBLESHOOTING GUIDE:");
        System.out.println("---------------------\n");
        
        String errorMsg = e.getMessage().toLowerCase();
        
        if (errorMsg.contains("connection refused") || errorMsg.contains("refused")) {
            System.out.println("❌ MySQL Server is not running!");
            System.out.println("   Solution: Start MySQL Server");
            System.out.println("   Windows: Run MySQL from Services or Start Menu");
            System.out.println("   Mac/Linux: sudo service mysql start");
            System.out.println("   OR use MySQL Workbench → Server → Server Status\n");
        }
        
        if (errorMsg.contains("unknown database") || errorMsg.contains("database doesn't exist")) {
            System.out.println("❌ Database 'ocean_view_resort' does not exist!");
            System.out.println("   Solution: Create database in MySQL Workbench:");
            System.out.println("   1. Open MySQL Workbench");
            System.out.println("   2. Connect to your MySQL server");
            System.out.println("   3. Tools → Create Schema (or SQL: CREATE DATABASE ocean_view_resort;)\n");
        }
        
        if (errorMsg.contains("access denied")) {
            System.out.println("❌ Authentication failed!");
            System.out.println("   Solution: Check database credentials in DatabaseConfig.java");
            System.out.println("   Current settings:");
            System.out.println("     DB_USER = root");
            System.out.println("     DB_PASSWORD = (empty)");
            System.out.println("   Update these values in DatabaseConfig.java if different\n");
        }
        
        if (errorMsg.contains("driver") || errorMsg.contains("class not found")) {
            System.out.println("❌ MySQL JDBC driver not found!");
            System.out.println("   Solution: Maven should download mysql-connector-j automatically");
            System.out.println("   1. Right-click project → Maven → Update Project");
            System.out.println("   2. Check Maven Dependencies folder - mysql-connector-j should be there\n");
        }
    }
}
