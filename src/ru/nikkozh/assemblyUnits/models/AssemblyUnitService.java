package ru.nikkozh.assemblyUnits.models;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.stream.Collectors;

import com.sun.source.tree.ParenthesizedTree;

import ru.nikkozh.assemblyUnits.dao.AssemblyUnitDao;

public class AssemblyUnitService {
  private static AssemblyUnitService assemblyUnitService;
  
  // Отдельно храним ссылку на сборочную единицу, с которой работали последний раз.
  // Если мы будем работать с ней же повторно, нам не придётся лишний раз обращаться к БД.
  // TODO: получается не самый лучший подход - мы по два раза открываем соединение с БД + другие косяки. Мб выпилить?
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
  
  // TODO: подумать над тем, чтобы убрать из подобных методов id сборочной единицы, т.к. мы можем доставать её из сервиса, а GUI везде как раз использует сервис
  public boolean addPart(String partName, String partAmountString) {
    if (!partName.isEmpty()) {
      try {
        int partAmount = Integer.valueOf(partAmountString);
        int assemblyUnitId = currentAssemblyUnit.getId();
        
        // TODO: при такой проверке невозможно различить, произошла ли ошибка в вводе или внутри БД
        // Может, сделать возвращаемый тип не логический, а что-нибудь посложнее?
        if (partAmount > 0) {
          setCurrentAssemblyUnit(assemblyUnitId);

          for (Map.Entry<String, Integer> nameAndAmount : currentAssemblyUnit.getParts().entrySet()) {
            if (nameAndAmount.getKey().equals(partName)) {
              return AssemblyUnitDao.getInstance().updatePart(assemblyUnitId, partName, partName, nameAndAmount.getValue() + partAmount);
            }
          }
          
          return AssemblyUnitDao.getInstance().addPart(assemblyUnitId, partName, partAmount);
        }
      } catch (NumberFormatException e) {
        // TODO: заменить на нормальный возврат ошибки в GUI
        System.out.println("Integer.ValueOf ERROR");
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
        // TODO: заменить на нормальный возврат ошибки в GUI
        System.out.println("Integer.ValueOf ERROR"); 
      }
    }

    return false;
  }
  
  public boolean deletePart(int assemblyUnitId, String partName) {
    if (assemblyUnitId > 0 && !partName.isEmpty()) {
      return AssemblyUnitDao.getInstance().deletePart(assemblyUnitId, partName);
    }
    return false;
  }
 
  // Возвращаем вложенные Lists вместо String[][] для того, чтобы можно было сделать перебор в функциональном стиле
  // Внутренний Vector нужен для того, чтобы сразу передать его в JTable как row без доп. преобразований и приведений
  public List<Vector<String>> getPartTable(int id) {
    setCurrentAssemblyUnit(id);
    
    List<Vector<String>> partTable = new ArrayList();
    if (currentAssemblyUnit != null) {
      currentAssemblyUnit.getParts().entrySet().stream().forEach(part -> {
        Vector<String> partColumns = new Vector<String>();
        partColumns.add(part.getKey());
        partColumns.add(part.getValue().toString());
        partTable.add(partColumns);
      });
    }
    return partTable;
  }
  
  // TODO: по сути, я сейчас одну строчку кода подлиннее заменил другой строчкой кода покороче. Есть ли смысл?
  public void setCurrentAssemblyUnit(int id) {
    currentAssemblyUnit = AssemblyUnitDao.getInstance().getAssemblyUnit(id);
  }
  
  public void setCurrentAssemblyUnit(String id) {
    try {
      currentAssemblyUnit = AssemblyUnitDao.getInstance().getAssemblyUnit(Integer.valueOf(id));
    } catch (NumberFormatException e) {
      e.printStackTrace();
      // TODO: сделать какую-то обработку ошибок
    }
  }
}