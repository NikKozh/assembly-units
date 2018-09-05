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
  
  // �������� ������� ������, ������� ��� ��������� � ��� �������� � ��� ������:
  public boolean deleteAssembly() {
    if (currentAssemblyUnit.getParentId() == -1) {
      return false; // ������� �������� ������ ������
    }
    
    int currentId = currentAssemblyUnit.getId(); // ������� ���������� id ������� ������, ����� �������� ��� � DAO
    setCurrentAssemblyUnit(currentAssemblyUnit.getParentId()); // ����������� ������ �� ������������, �.�. ������� ����� �������
    
    return AssemblyUnitDao.getInstance().deleteDescendants(currentId);
  }
  
  // ���������� ��������� � ������� ������:
  public boolean addAssembly() {
    return AssemblyUnitDao.getInstance().addAssemblyUnit(currentAssemblyUnit.getId());
  }
  
  // ��������� ��� ���� ��������� ������� ��������� �������
  public List<String> getAssemblyUnitChildrenNames() {
    // �.�. � ����� ��������� ������� ������ ������������ � id, �� ����� ������ ����� ����������� AssemblyUnit ���������� ������ �� �����
    List<String> childrenNames = new ArrayList<>();
    
    for (int childId : AssemblyUnitDao.getInstance().getAssemblyUnitChildrenIds(currentAssemblyUnit.getId())) {
      childrenNames.add("��������� ������� �" + childId);
    }
    
    return childrenNames;
  }
  
  // TODO: �������� ��� ���, ����� ������ �� �������� ������� id ��������� �������, �.�. �� ����� ��������� � �� �������, � GUI ����� ��� ��� ���������� ������
  public boolean addPart(String partName, String partAmountString) {
    if (!partName.isEmpty()) {
      try {
        int partAmount = Integer.valueOf(partAmountString);
        int assemblyUnitId = currentAssemblyUnit.getId();
        
        // TODO: ��� ����� �������� ���������� ���������, ��������� �� ������ � ����� ��� ������ ��
        // �����, ������� ������������ ��� �� ����������, � ���-������ ���������?
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
        // TODO: �������� �� ���������� ������� ������ � GUI
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
            // ���� �������� ������ ������ ���, ������, ������������ �������� ��������������� ���������� �������
            // �� ������� �� � �������, � ����� ��� ������� �������. ����� ����������� �������� ��:
            return AssemblyUnitDao.getInstance().updatePart(assemblyUnitId, newPartName, newPartName, partAmount);
          }
        }
      } catch (NumberFormatException e) {
        // TODO: �������� �� ���������� ������� ������ � GUI
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
  
  public void setCurrentAssemblyUnit(String id) {
    try {
      currentAssemblyUnit = AssemblyUnitDao.getInstance().getAssemblyUnit(Integer.valueOf(id));
    } catch (NumberFormatException e) {
      e.printStackTrace();
      // TODO: ������� �����-�� ��������� ������
    }
  }
}