package ru.nikkozh.assemblyUnits.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import ru.nikkozh.assemblyUnits.dao.AssemblyUnitDao;

// Сервис, выступающий в качестве контроллера: обрабатывает информацию из GUI и передаёт дальше в DAO
public class AssemblyUnitService {
  private static AssemblyUnitService assemblyUnitService;
  
  // Отдельно храним ссылку на сборочную единицу, с которой работаем в текущий момент.
  // Это позволяет не обращаться лишний раз к БД для выполнения промежуточных операций
  // и обеспечивает гибкость Сервиса.
  private AssemblyUnit currentAssemblyUnit;
  
  private AssemblyUnitService() {
    setCurrentAssemblyUnit(1); // при инициализации сервиса получаем головную сборочную единицу, чтобы сразу её отобразить в GUI
  }
  
  public static AssemblyUnitService getInstance() {
    if (assemblyUnitService == null) {
      assemblyUnitService = new AssemblyUnitService();
    }
    return assemblyUnitService;
  }
  
  public AssemblyUnit getCurrentAssemblyUnit() {
    return currentAssemblyUnit;
  }
  
  public void setCurrentAssemblyUnit(int id) {
    currentAssemblyUnit = AssemblyUnitDao.getInstance().getAssemblyUnit(id);
  }

  public void setCurrentAssemblyUnit(String id) {
    try {
      currentAssemblyUnit = AssemblyUnitDao.getInstance().getAssemblyUnit(Integer.valueOf(id));
    } catch (NumberFormatException e) {
      System.out.println("Integer.ValueOf ERROR in setCurrentAssemblyUnit method"); 
    }
  }
  
  // Удаление текущей сборки, включая все подсборки и все входящие в них детали:
  public boolean deleteAssembly() {
    if (currentAssemblyUnit.getParentId() == -1) {
      return false; // удалить головную сборку нельзя
    }
    
    int currentId = currentAssemblyUnit.getId(); // заранее записываем id текущей сборки, чтобы передать его в DAO
    setCurrentAssemblyUnit(currentAssemblyUnit.getParentId()); // переключаем сборку на родительскую, т.к. текущая будет удалена
    
    return AssemblyUnitDao.getInstance().deleteDescendants(currentId);
  }
  
  // Добавление подсборки в текущую сборку:
  public boolean addAssembly() {
    return AssemblyUnitDao.getInstance().addAssemblyUnit(currentAssemblyUnit.getId());
  }
  
  // Получение имён всех подсборок текущей сборочной единицы
  public List<String> getAssemblyUnitChildrenNames() {
    // Т.к. к имени сборочной единицы всегда прибавляется её id, мы можем вместо целых экземпляров AssemblyUnit возвращать только их имена
    List<String> childrenNames = new ArrayList<>();
    
    for (int childId : AssemblyUnitDao.getInstance().getAssemblyUnitChildrenIds(currentAssemblyUnit.getId())) {
      childrenNames.add("Сборочная единица №" + childId);
    }
    
    return childrenNames;
  }
  
  public boolean addPart(String partName, String partAmountString) {
    if (!partName.isEmpty()) {
      try {
        int partAmount = Integer.valueOf(partAmountString);
        int assemblyUnitId = currentAssemblyUnit.getId();
        
        if (partAmount > 0) {
          for (Map.Entry<String, Integer> nameAndAmount : currentAssemblyUnit.getParts().entrySet()) {
            if (nameAndAmount.getKey().equals(partName)) {
              return AssemblyUnitDao.getInstance().updatePart(assemblyUnitId, partName, partName, nameAndAmount.getValue() + partAmount);
            }
          }
          return AssemblyUnitDao.getInstance().addPart(assemblyUnitId, partName, partAmount);
        }
      } catch (NumberFormatException e) {
        System.out.println("Integer.ValueOf ERROR in addPart method");
      }
    }
    
    return false;
  }

  public boolean updatePart(String oldPartName, String newPartName, String partAmountString) {
    if (!newPartName.isEmpty()) {
      try {
        int partAmount = Integer.valueOf(partAmountString);
        int assemblyUnitId = currentAssemblyUnit.getId();
        
        if (partAmount > 0) {
          if (!oldPartName.isEmpty()) {
            return AssemblyUnitDao.getInstance().updatePart(assemblyUnitId, oldPartName, newPartName, partAmount);
          } else {
            // Если передали пустое старое имя, значит, пользователь пытается отредактировать количество деталей
            // не выделив их в таблице, а введя имя деталей вручную. Стоит попробовать поискать их:
            return AssemblyUnitDao.getInstance().updatePart(assemblyUnitId, newPartName, newPartName, partAmount);
          }
        }
      } catch (NumberFormatException e) {
        System.out.println("Integer.ValueOf ERROR in updatePart method"); 
      }
    }

    return false;
  }

  public boolean deletePart(String partName) {
    if (!partName.isEmpty()) {
      int assemblyUnitId = currentAssemblyUnit.getId();
      return AssemblyUnitDao.getInstance().deletePart(assemblyUnitId, partName);
    }
    return false;
  }

  // Внутренний Vector нужен для того, чтобы сразу передать его в таблицу одним аргументом без доп. преобразований и приведений
  public List<Vector<String>> getPartTable(int id) {
    setCurrentAssemblyUnit(id);
    
    List<Vector<String>> partTable = new ArrayList<>();
    if (currentAssemblyUnit != null) {
      for (Map.Entry<String, Integer> part : currentAssemblyUnit.getParts().entrySet()) {
        Vector<String> partColumns = new Vector<String>();
        partColumns.add(part.getKey());
        partColumns.add(part.getValue().toString());
        partTable.add(partColumns);
      }
    }
    return partTable;
  }
}