package ru.nikkozh.assemblyUnits.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

// �����, � ������� �������� ��� ������ ��� ����������� � �� � �������� ���� ������
public class DBUtil {
 /* 
  * ������ ������ ��� ����� �� ��������� ������ MySQL:

  private final String user = "root";
  private final String password = "root123";
  private final String url = "jdbc:mysql://localhost:3306/assembly_units_db" +
                       "?useLegacyDatetimeCode=false&amp&serverTimezone=UTC" + // ����������� ���������� �������� ��� ����������� ����� �������� ����� �������
                       "&useSSL=false";
  */
  
  private final String user = "sql7255404";
  private final String password = "TuXvz2vxvB";
  private final String url = "jdbc:mysql://sql7.freemysqlhosting.net:3306/sql7255404" +
                             "?charset=utf8&useUnicode=true&characterEncoding=utf8";
  
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
  
  public Connection getConnection() {
    try {
      if (connection == null || connection.isClosed()) {
        connection = DriverManager.getConnection(url, user, password);
        // connection.prepareStatement("SET NAMES utf8");
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    
    return connection;
  }
  
  // ����� ����� �������� ���������, �� ���������� ���������� � ���� int:
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
      close(); // ��� ����, ����� �������� ���� ����� �� ������ ������ � DAO ��������� ���
    } catch (Exception e) {
      e.printStackTrace();
    }
    
    return result;
  }
  
  // ����� ����� �������� ���������, �� �� ����� ���������� ���������:
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
  
  // ����� ����� � �������� ���������, � �������� ��������� �������:
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