package org.redrock.web.util;

import org.apache.commons.dbcp.BasicDataSource;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class JdbcUtil {

    private static BasicDataSource dataSource;

    static {
        Properties properties = null;
        try {
            properties = new Properties();
            properties.load(JdbcUtil.class.getResourceAsStream("/Mysql.properties"));
            dataSource = new BasicDataSource();
            dataSource.setDriverClassName(properties.getProperty("mysql.driver"));
            dataSource.setUrl(properties.getProperty("mysql.url"));
            dataSource.setUsername(properties.getProperty("mysql.user"));
            dataSource.setPassword(properties.getProperty("mysql.password"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
