package ru.nikkozh.assemblyUnits.dao;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import ru.nikkozh.assemblyUnits.models.AssemblyUnit;
import ru.nikkozh.assemblyUnits.util.DBUtil;

// Data Access Object, который принимает конкретные команды от Сервиса с уже обработанными данными
// и выполняет необходимые действия в БД через вспомогательный класс DBUtil
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

  public boolean addAssemblyUnit(int parentId) {
    boolean result = false;
    
    try {
      String sql = "INSERT INTO assembly_units (parent_id) VALUE (?);";
      Object[] parameter = { parentId };
      if (db.executeUpdate(sql, parameter) == 1) {
        result = true;
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      closeAll();
    }
    
    return result;
  }
  
  /*
   * Рекурсивный метод для удаления переданной сборки и всех её потомков.
   * Обычно с рекурсией стоит проявлять осторожность, и да, этот метод действительно медленный,
   * однако учитывается малый масштаб программы и допущение, что дерево подсборок не будет большим.
   * При большом количестве данных определённо стоит использовать иные алгоритмы и способы хранения деревьев в БД.
   */
  public boolean deleteDescendants(int assemblyUnitId) {
    boolean result = true;
    
    try {
      // Проверяем список детей текущей сборки:
      String sql = "SELECT * FROM assembly_units WHERE parent_id = ?;";
      Integer[] parameter = { assemblyUnitId };
      resultSet = db.executeQuery(sql, parameter);
      
      // При рекурсивном обходе ResultSet будет закрыт, поэтому переносим результаты запроса в отдельный локальный массив:
      List<Integer> childrenIds = new ArrayList<>();
      while (resultSet.next()) {
        childrenIds.add(resultSet.getInt("id"));
      }
      
      // Для каждого ребёнка повторяём выполнение метода:
      for (int childId : childrenIds) {
        result = deleteDescendants(childId);
      }
      
      if (result) {
        // И, наконец, удаляем саму текущую сборку:
        sql = "DELETE FROM assembly_units WHERE id = ?;";
        if (db.executeUpdate(sql, parameter) != 1) {
          result = false;
        }
        // Благодаря внешнему ключу в БД все каскадно связанные с этой сборкой детали будут автоматически удалены
      }
    } catch (Exception e) {
      result = false;
      e.printStackTrace();
    } finally {
      closeAll();
    }
    
    return result;
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
  
  public List<Integer> getAssemblyUnitChildrenIds(int assemblyUnitId) {
    List<Integer> childrenList = new ArrayList<Integer>();
     
    try {
      String sql = "SELECT * FROM assembly_units WHERE parent_id = ?;";
      Integer[] parameter = { assemblyUnitId };
      resultSet = db.executeQuery(sql, parameter);
      
      while (resultSet.next()) {
        childrenList.add(resultSet.getInt("id"));
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      closeAll();
    }
    
    return childrenList;
  }
  
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