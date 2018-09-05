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
  private AssemblyUnit currentAssemblyUnit;
  
  private AssemblyUnitService() {
    setCurrentAssemblyUnit(1); // ��� ������������� ������� �������� �������� ��������� �������, ����� ����� � ���������� � GUI
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
  
  // ���� �� �������� �������� - ������, ����� ��������� ������� ��������� �������
  public List<String> getAssemblyUnitChildren() {
    return AssemblyUnitDao.getInstance().getAssemblyUnitChildrenNames(currentAssemblyUnit.getId());
  }
  
  public boolean addPart(int assemblyUnitId, String partName, int partAmount) {
    // TODO: ��� ����� �������� ���������� ���������, ��������� �� ������ � ����� ��� ������ ��
    // �����, ������� ������������ ��� �� ����������, � ���-������ ���������?
    if (assemblyUnitId > 0 && !partName.isEmpty() && partAmount > 0) {
      setCurrentAssemblyUnit(assemblyUnitId);

      for (String name : currentAssemblyUnit.getParts().keySet()) {
        if (name.equals(partName)) {
          return AssemblyUnitDao.getInstance().updatePart(assemblyUnitId, partName, partName, partAmount);
        }
      }
      return AssemblyUnitDao.getInstance().addPart(assemblyUnitId, partName, partAmount);
    }
    return false;
  }
  
  public boolean updatePart(int assemblyUnitId, String oldPartName, String newPartName, int partAmount) {
    if (assemblyUnitId > 0 && !oldPartName.isEmpty() && !newPartName.isEmpty() && partAmount > 0) {
      return AssemblyUnitDao.getInstance().updatePart(assemblyUnitId, oldPartName, newPartName, partAmount);
    }
    return false;
  }
  
  public boolean deletePart(int assemblyUnitId, String partName) {
    if (assemblyUnitId > 0 && !partName.isEmpty()) {
      return AssemblyUnitDao.getInstance().deletePart(assemblyUnitId, partName);
    }
    return false;
  }
  
  public List<String> getPartList(int id) {
    setCurrentAssemblyUnit(id);
    
    List<String> partList = new ArrayList<>();
    currentAssemblyUnit.getParts().entrySet().stream().forEach(part ->
      partList.add(part.getKey() + " | " + part.getValue() + " ��.")
    );
    return partList;
  }
  
  // ���������� ��������� Lists ������ String[][] ��� ����, ����� ����� ���� ������� ������� � �������������� �����
  // ���������� Vector ����� ��� ����, ����� ����� �������� ��� � JTable ��� row ��� ���. �������������� � ����������
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
  
  // TODO: �� ����, � ������ ���� ������� ���� ��������� ������� ������ �������� ���� ��������. ���� �� �����?
  public void setCurrentAssemblyUnit(int id) {
    currentAssemblyUnit = AssemblyUnitDao.getInstance().getAssemblyUnit(id);
  }
}