package ru.nikkozh.assemblyUnits.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.TreeMap;

import ru.nikkozh.assemblyUnits.models.AssemblyUnit;
import ru.nikkozh.assemblyUnits.util.DBUtil;

public class AssemblyUnitDao {
  private final DBUtil db;
  private ResultSet resultSet;
  private static AssemblyUnitDao assemblyUnitDao;
  
  private AssemblyUnitDao() {
    db = DBUtil.getDBUtil();
  }
  
  public static AssemblyUnitDao getInstance() {
    if (assemblyUnitDao == null) {
      assemblyUnitDao = new AssemblyUnitDao();
    }
    return assemblyUnitDao;
  }
  
  public boolean addPart(int assemblyUnitId, String partName, int partAmount) {
    boolean result = false;
    
    try {
      String sql = "INSERT INTO parts (assembly_unit_id, name, amount) VALUES (?, ?, ?);";
      Object[] params = { assemblyUnitId, partName, partAmount };
      if (db.executeUpdate(sql, params) == 1) {
        result = true;
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      closeAll();
    }
    
    return result;
  }
  
  public boolean updatePart(int assemblyUnitId, String oldPartName, String newPartName, int partAmount) {
    boolean result = false;
    
    try {
      String sql = "UPDATE parts SET name = ?, amount = ? WHERE assembly_unit_id = ? AND name = ?;";
      Object[] params = { newPartName, partAmount, assemblyUnitId, oldPartName };
      if (db.executeUpdate(sql, params) == 1) {
        result = true;
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      closeAll();
    }
    
    return result;
  }
  
  public boolean deletePart(int assemblyUnitId, String partName) {
    boolean result = false;
    
    try {
      String sql = "DELETE FROM parts WHERE assembly_unit_id = ? AND name = ?;";
      Object[] params = { assemblyUnitId, partName };
      if (db.executeUpdate(sql, params) == 1) {
        result = true;
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      closeAll();
    }
    
    return result;
  }
  
  // TODO: подумать над тем, чтобы вынести создание AssemblyUnit из DAO в Сервис
  public AssemblyUnit getAssemblyUnit(int id) {
    AssemblyUnit assemblyUnit = null;
    
    try {
      String sql = "SELECT * FROM parts WHERE assembly_unit_id = ?;";
      Integer[] parameter = { id };
      resultSet = db.executeQuery(sql, parameter);
      
      if (resultSet.next()) {     
        assemblyUnit = new AssemblyUnit();
        assemblyUnit.setId(resultSet.getInt("assembly_unit_id"));
        assemblyUnit.setName("Сборочная единица " + resultSet.getInt("assembly_unit_id"));
        
        do {
          assemblyUnit.putPart(resultSet.getString("name"), resultSet.getInt("amount"));
        } while (resultSet.next());
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      closeAll();
    }
    
    return assemblyUnit;
  }
  
  private void closeAll() {
    try {
      if (resultSet != null) {
        resultSet.close();
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      db.close();
    }
  }
}