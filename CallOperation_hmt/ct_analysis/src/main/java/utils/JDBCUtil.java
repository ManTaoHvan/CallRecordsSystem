package utils;

import java.sql.*;

public class JDBCUtil {
    //定义JDBC连接器实例化所需要的固定参数
    private static final String MYSQL_DRIVER_CLASS = "com.mysql.jdbc.Driver";
    //地址/数据库
    private static final String MYSQL_URL = "jdbc:mysql://bigData121nn:3306/db_call?useUnicode=true&characterEncoding=UTF-8";
    //用户
    private static final String MYSQL_USERNAME = "root";
    //密码
    private static final String MYSQL_PASSWORD = "a";

    /**
     * 实例化JDBC连接器对象
     */
    public static Connection getConnection(){
        try {
            Class.forName(MYSQL_DRIVER_CLASS);
            return DriverManager.getConnection(MYSQL_URL, MYSQL_USERNAME, MYSQL_PASSWORD);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 释放连接器资源
     */
    public static void close(Connection connection, Statement statement, ResultSet resultSet){
            try {
                if(resultSet != null && !resultSet.isClosed()){
                    resultSet.close();
                }

                if(statement != null && !statement.isClosed()){
                    statement.close();
                }

                if(connection != null && !connection.isClosed()){
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
    }
}
