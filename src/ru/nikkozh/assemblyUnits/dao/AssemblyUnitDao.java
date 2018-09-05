package ru.nikkozh.assemblyUnits.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
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
  
  // Т.к. к имени сборочной единицы всегда прибавляется её id, мы можем вместо целых экземпляров AssemblyUnit возвращать только их имена:
   public List<String> getAssemblyUnitChildrenNames(int assemblyUnitId) {
     List<String> childrenList = new ArrayList<String>();
     
     try {
       // Получаем список подсборок переданной сборки:
       String sql = "SELECT * FROM assembly_units WHERE parent_id = ?;";
       Integer[] parameter = { assemblyUnitId };
       resultSet = db.executeQuery(sql, parameter);
       
       while (resultSet.next()) {
         childrenList.add("Сборочная единица №" + resultSet.getInt("id"));
       }
     } catch (Exception e) {
       e.printStackTrace();
     } finally {
       closeAll();
     }
     
     return childrenList;
   }
  
  // TODO: подумать над тем, чтобы вынести создание AssemblyUnit из DAO в Сервис
  public AssemblyUnit getAssemblyUnit(int id) {
    AssemblyUnit assemblyUnit = null;
    
    try {
      String sql = "SELECT * FROM assembly_units WHERE id = ?;";
      Integer[] parameter = { id };
      resultSet = db.executeQuery(sql, parameter);
      
      if (resultSet.next()) {
        assemblyUnit = new AssemblyUnit();
        assemblyUnit.setId(id);
        assemblyUnit.setParentId(resultSet.getInt("parent_id"));
        assemblyUnit.setName("Сборочная единица №" + id);
        
        sql = "SELECT * FROM parts WHERE assembly_unit_id = ?;";
        resultSet = db.executeQuery(sql, parameter);
        
        while (resultSet.next()) {
          assemblyUnit.putPart(resultSet.getString("name"), resultSet.getInt("amount"));
        };
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