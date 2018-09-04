package ru.nikkozh.assemblyUnits.models;

import java.util.Map;
import java.util.TreeMap;

public class AssemblyUnit {
  private int id;
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
    // TODO: ������� �������� �� ������������� ������ ������� � ������������ � ����� ������ �� ����������
    parts.put(partName, partAmount);
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }
}
