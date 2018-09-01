package ru.nikkozh.assemblyUnits.models;

import java.util.Map;
import java.util.TreeMap;

public class AssemblyUnit {
  private final String name;
  private Map<String, Integer> parts = new TreeMap<>();
  
  public AssemblyUnit(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public Map<String, Integer> getParts() {
    return parts;
  }

  public void setPart(String partName, int amountParts) {
    // TODO: заглушка
  }
}
