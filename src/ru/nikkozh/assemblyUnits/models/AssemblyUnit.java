package ru.nikkozh.assemblyUnits.models;

import java.util.Map;
import java.util.TreeMap;

public class AssemblyUnit {
  private int id;
  private int parentId; // если -1 значит это корневая сборка
  private String name;
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
    // TODO: сделать проверку на существование данных деталей и суммирование в таком случае их количества
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
