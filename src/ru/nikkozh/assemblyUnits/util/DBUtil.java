package ru.nikkozh.assemblyUnits.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import ru.nikkozh.assemblyUnits.dao.AssemblyUnitDao;

// Класс, в котором хранится вся логика для подключения к БД
public class DBUtil {
  private String user = "root";
  private String password = "root";
  private String url = "jdbc:mysql://localhost:3306/assembly_units_db" +
                       "?useLegacyDatetimeCode=false&amp&serverTimezone=UTC" + // необходимые добавочные атрибуты для корректного учёта часового пояса сервера
                       "&useSSL=false"; 
  private String driver = "com.mysql.cj.jdbc.Driver";
  
  private static DBUtil db;
  
  private Connection connection;
  private PreparedStatement preparedStatement;
  private ResultSet resultSet;
  
  private DBUtil() {}
  
  public static DBUtil getDBUtil() {
    if (db == null) {
      db = new DBUtil();
    }
    return db;
  }
  
  // Когда нужно передать параметры, но результата достаточно в виде int:
  public int executeUpdate(String sql, Object[] objects) {
    int result = -1;
    if (getConnection() == null) {
      return result;
    }
    
    try {
      preparedStatement = connection.prepareStatement(sql);
      for (int i = 0; i < objects.length; i++) {
        preparedStatement.setObject(i + 1, objects[i]);
      }
      result = preparedStatement.executeUpdate();
      close(); // для того, чтобы вызывать этот метод из одного метода в DAO несколько раз
    } catch (Exception e) {
      e.printStackTrace();
    }
    
    return result;
  }
  
  // Когда нужно и передать параметры, и получить результат запроса:
  public ResultSet executeQuery(String sql, Object[] objects) {
    if (getConnection() == null) {
      return null;
    }
    
    try {
      preparedStatement = connection.prepareStatement(sql);
      for (int i = 0; i < objects.length; i++) {
        preparedStatement.setObject(i + 1, objects[i]);
      }
      resultSet = preparedStatement.executeQuery();
    } catch (Exception e) {
      e.printStackTrace();
    }
    
    return resultSet;
  }
  
  // Когда нужно получить результат, но не нужно передавать параметры:
  public ResultSet executeQuery(String sql) {
    if (getConnection() == null) {
      return null;
    }
    
    try {
      resultSet = connection.prepareStatement(sql).executeQuery();
    } catch (Exception e) {
      e.printStackTrace();
    }
    
    return resultSet;
  }
  
  public Connection getConnection() {
    try {
      if (connection == null || connection.isClosed()) {
        connection = DriverManager.getConnection(url, user, password);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    
    return connection;
  }
  
  public void close() {
    try {
      if (resultSet != null) {
        resultSet.close();
      }
      if (preparedStatement != null) {
        preparedStatement.close();
      }
      if (connection != null) {
        connection.close();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}