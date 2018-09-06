package ru.nikkozh.assemblyUnits.models;

import java.util.Map;
import java.util.TreeMap;

//  ласс, представл€ющий сборочную единицу
public class AssemblyUnit {
  private int id;
  private int parentId; // идентификатор родительской сборки; -1 означает, что это корнева€ сборка
  private String name;
  
  // Ѕлагодар€ хранению деталей в TreeMap, мы получаем автоматическую сортировку их наименований без дополнительных действий:
  private Map<String, Integer> parts = new TreeMap<>();
  
  public void setName(String name) {
    this.name = name;
  }
  
  public String getName() {
    return name;
  }

  public Map<String, Integer> getParts() { 
    return parts;
  }

  public void putPart(String partName, int partAmount) {
    parts.put(partName, partAmount);
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getParentId() {
    return parentId;
  }

  public void setParentId(int parentId) {
    this.parentId = parentId;
  }
}
