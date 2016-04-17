package com.cxfly.test;

import java.sql.Connection;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidPooledConnection;

public class DataSourceManager {
    final static String            urlLocal      = "jdbc:mysql://10.204.38.185:3306/mytest?characterEncoding=utf8&connectTimeout=2000&autoReconnect=true";
    final static String            userLocal     = "root";
    final static String            passwordLocal = "007";

    private static final DruidDataSource dsLocal       = new DruidDataSource();

    static {
        dsLocal.setUrl(urlLocal);
        dsLocal.setUsername(userLocal);
        dsLocal.setPassword(passwordLocal);
        dsLocal.setInitialSize(10);
        dsLocal.setMaxActive(50);
    }
    
    public static javax.sql.DataSource getDataSource() {
    	return dsLocal;
    }

    public static Connection getConnectionLocal() throws Exception {
        DruidPooledConnection connection = dsLocal.getConnection();
        return connection;
    }

    public static void destoryLocal() {
        Runtime.getRuntime().addShutdownHook(new Thread() {
        	@Override
        	public void run() {
        		dsLocal.close();
        	}
        });
    }

}
