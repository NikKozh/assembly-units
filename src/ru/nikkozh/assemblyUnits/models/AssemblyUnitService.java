package ru.nikkozh.assemblyUnits.models;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.stream.Collectors;

import ru.nikkozh.assemblyUnits.dao.AssemblyUnitDao;

public class AssemblyUnitService {
  private static AssemblyUnitService assemblyUnitService;
  
  // �������� ������ ������ �� ��������� �������, � ������� �������� ��������� ���.
  // ���� �� ����� �������� � ��� �� ��������, ��� �� ������� ������ ��� ���������� � ��.
  // TODO: ���������� �� ����� ������ ������ - �� �� ��� ���� ��������� ���������� � �� + ������ ������. �� ��������?
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
    // TODO: ��� ����� �������� ���������� ���������, ��������� �� ������ � ����� ��� ������ ��
    // �����, ������� ������������ ��� �� ����������, � ���-������ ���������?
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
      partList.add(part.getKey() + " | " + part.getValue() + " ��.")
    );
    return partList;
  }
  
  // ���������� ��������� Lists ������ String[][] ��� ����, ����� ����� ���� ������� ������� � �������������� �����
  // ���������� Vector ����� ��� ����, ����� ����� �������� ��� � JTable ��� row ��� ���. �������������� � ����������
  public List<Vector<String>> getPartTable(int id) {
    setAssemblyUnit(id);
    
    List<Vector<String>> partTable = new ArrayList();
    assemblyUnit.getParts().entrySet().stream().forEach(part -> {
      Vector<String> partColumns = new Vector<String>();
      partColumns.add(part.getKey());
      partColumns.add(part.getValue().toString());
      partTable.add(partColumns);
    });
    return partTable;
  }
  
  private void setAssemblyUnit(int id) {
      assemblyUnit = AssemblyUnitDao.getInstance().getAssemblyUnit(id);
  }
}