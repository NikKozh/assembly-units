package ru.nikkozh.assemblyUnits.models;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import ru.nikkozh.assemblyUnits.dao.AssemblyUnitDao;

public class AssemblyUnitService {
  private static AssemblyUnitService assemblyUnitService;
  
  // Отдельно храним ссылку на сборочную единицу, с которой работали последний раз.
  // Если мы будем работать с ней же повторно, нам не придётся лишний раз обращаться к БД.
  // TODO: получается не самый лучший подход - мы по два раза открываем соединение с БД + другие косяки. Мб выпилить?
  private AssemblyUnit assemblyUnit;
  
  private AssemblyUnitService() {
    assemblyUnit = null;
  }
  
  public static AssemblyUnitService getInstance() {
    if (assemblyUnitService == null) {
      assemblyUnitService = new AssemblyUnitService();
    }
    return assemblyUnitService;
  }
  
  public boolean addPart(int assemblyUnitId, String partName, int partAmount) {
    // TODO: при такой проверке невозможно различить, произошла ли ошибка в вводе или внутри БД
    // Может, сделать возвращаемый тип не логический, а что-нибудь посложнее?
    if (!partName.isEmpty() && partAmount > 0) {
      return AssemblyUnitDao.getInstance().addPart(assemblyUnitId, partName, partAmount);
    }
    return false;
  }
  
  public String getAssemblyUnitName(int id) {
    setAssemblyUnit(id);
    
    return assemblyUnit.getName();
  }
  
  public List<String> getPartList(int id) {
    setAssemblyUnit(id);
    
    List<String> partList = new ArrayList<>();
    assemblyUnit.getParts().entrySet().stream().forEach(part ->
      partList.add(part.getKey() + " | " + part.getValue() + " шт.")
    );
    return partList;
  }
  
  private void setAssemblyUnit(int id) {
      assemblyUnit = AssemblyUnitDao.getInstance().getAssemblyUnit(id);
  }
}