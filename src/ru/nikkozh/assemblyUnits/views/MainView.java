package ru.nikkozh.assemblyUnits.views;

import javax.swing.*;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import javafx.scene.control.TableSelectionModel;
import ru.nikkozh.assemblyUnits.models.AssemblyUnitService;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MainView {
  public JFrame frame;
  
  private JLabel assemblyUnitName, partCount;
  
  private JTextField partNameForCreating, partAmountForCreating,
                     partNameForEditing, partAmountForEditing;
  
  private JButton createPartButton, editPartButton, deletePartButton,
                  createAssemblyButton, deleteAssemblyButton;
  
  private JList<String> assemblyList;
  private DefaultListModel<String> assemblyListModel;
  
  private JTable partTable;
  private String[] partTableColumnNames = { "������������", "���������� (��.)" };
  private ListSelectionModel partTableSelectionModel;
  private DefaultTableModel partTableModel;
  
  private JPanel centerPanel, eastPanel, westPanel,
                 creatingPanel, editingPanel,
                 assemblyButtonsPanel;
  
  private int selectedRow;
  
  // TODO: �����������: ��������� ����������� �� ��������� �������
  // TODO: ���� ����� �����, ������� ������ ������� ������ ���� ������ �� ����� �����, �������� �� ������� ������ � �.�.
  public MainView() {
    selectedRow = -1;
    
    frame = new JFrame("���������� ���������� ���������");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    Container container = frame.getContentPane();
        
    initWestPanel();
    initCenterPanel();
    initEastPanel();
    
    // TODO: ��������� ������������ ����� ������������ �������� BorderLayout (gap)
    container.add(BorderLayout.WEST, westPanel);
    container.add(BorderLayout.CENTER, centerPanel);
    container.add(BorderLayout.EAST, eastPanel);
    
    initPartTable();
    initAssemblyUnitList();
    
    frame.setMinimumSize(new Dimension(750, 300));
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);
  }
  
  private void initWestPanel() {
    assemblyUnitName = new JLabel();
    
    assemblyListModel = new DefaultListModel<>();
    assemblyList = new JList<>(assemblyListModel);
    assemblyList.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        changeAssembly();
      }
    });
    JScrollPane assemblyListSP = new JScrollPane(assemblyList);
    
    createAssemblyButton = new JButton("��������");
    createAssemblyButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        addAssembly();
      }
    });
    
    deleteAssemblyButton = new JButton("�������");
    deleteAssemblyButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        deleteAssembly();
      }
    });
    
    assemblyButtonsPanel = new JPanel(new FlowLayout());
    assemblyButtonsPanel.add(createAssemblyButton);
    assemblyButtonsPanel.add(deleteAssemblyButton);
    
    westPanel = new JPanel(new BorderLayout());
    westPanel.add(assemblyUnitName, BorderLayout.NORTH);
    westPanel.add(assemblyListSP, BorderLayout.CENTER);
    westPanel.add(assemblyButtonsPanel, BorderLayout.SOUTH);
    westPanel.setPreferredSize(new Dimension(200, 0));
  }
  
  private void initCenterPanel() {
    initCreatingPanel();
    initEditingPanel();
    
    centerPanel = new JPanel();
    centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
    centerPanel.add(creatingPanel);
    centerPanel.add(editingPanel);
    centerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
  }
  
  private void initCreatingPanel() {
    partNameForCreating = new JTextField(10);
    partAmountForCreating = new JTextField(10);
    
    createPartButton = new JButton("��������");
    createPartButton.setAlignmentX(Component.CENTER_ALIGNMENT);
    createPartButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        addPart();
      }
    });
    
    creatingPanel = new JPanel();
    // creatingPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    creatingPanel.setLayout(new BoxLayout(creatingPanel, BoxLayout.Y_AXIS));
    creatingPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
    JLabel label = new JLabel("�������� ����� ������");
    label.setAlignmentX(Component.CENTER_ALIGNMENT);
    creatingPanel.add(label);
    
    GridBagConstraints c = new GridBagConstraints();
    JPanel creatingFieldsPanel = new JPanel(new GridBagLayout());
    creatingFieldsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
    
    c.gridx = 1;
    c.gridy = 0;
    creatingFieldsPanel.add(partNameForCreating, c);
    
    c.gridx = 1;
    c.gridy = 1;
    creatingFieldsPanel.add(partAmountForCreating, c);
    
    c.insets = new Insets(0, 0, 0, 10);
    c.gridx = 0;
    c.gridy = 0;
    creatingFieldsPanel.add(new JLabel("������������:"), c);
    
    c.gridx = 0;
    c.gridy = 1;
    creatingFieldsPanel.add(new JLabel("����������:"), c);
    
    creatingFieldsPanel.setMaximumSize(new Dimension(350, 80));
    // creatingFieldsPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    
    creatingPanel.add(creatingFieldsPanel);
    creatingPanel.add(createPartButton);
  }
  
  private void initEditingPanel() {
    partNameForEditing = new JTextField(10);
    partAmountForEditing = new JTextField(10);
    
    editPartButton = new JButton("�������������");
    editPartButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        updatePart();
      }
    });
    
    deletePartButton = new JButton("�������");
    deletePartButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        deletePart();
      }
    });
    
    // TODO: ������� ��� ���� ������ � GridLayout ������ editingPanel � ��������� ���� ������ � textEditor'���
    editingPanel = new JPanel();
    editingPanel.setLayout(new BoxLayout(editingPanel, BoxLayout.Y_AXIS));
    editingPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
    /*JLabel label = new JLabel("������������� ������");
    label.setAlignmentX(Component.CENTER_ALIGNMENT);
    editingPanel.add(label);*/
    
    GridBagConstraints c = new GridBagConstraints();
    JPanel editingFieldsPanel = new JPanel(new GridBagLayout());
    editingFieldsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
    
    c.gridx = 0;
    c.gridy = 0;
    c.insets = new Insets(3, 3, 3, 3);
    c.gridwidth = GridBagConstraints.REMAINDER;
    editingFieldsPanel.add(new JLabel("������������� ������"), c);
    
    c.gridx = 1;
    c.gridy = 1;
    c.gridwidth = 1;
    editingFieldsPanel.add(partNameForEditing, c);
    
    c.gridx = 1;
    c.gridy = 2;
    editingFieldsPanel.add(partAmountForEditing, c);
    
    c.gridx = 0;
    c.gridy = 3;
    editingFieldsPanel.add(editPartButton, c);
    
    c.gridx = 1;
    c.gridy = 3;
    editingFieldsPanel.add(deletePartButton, c);

    c.insets = new Insets(0, 0, 0, 10);
    c.gridx = 0;
    c.gridy = 1;
    editingFieldsPanel.add(new JLabel("������������:"), c);
    
    c.gridx = 0;
    c.gridy = 2;
    editingFieldsPanel.add(new JLabel("����������:"), c);
    
    editingFieldsPanel.setMaximumSize(new Dimension(350, 350));
    editingPanel.add(editingFieldsPanel);
  }
  
  private void initEastPanel() {
    partCount = new JLabel();
    
    partTable = new JTable() {
      @Override
      public boolean isCellEditable(int row, int column) {
        return false;
      }
    };
    partTableModel = (DefaultTableModel)partTable.getModel();
    partTableModel.setColumnIdentifiers(partTableColumnNames);
    
    partTableSelectionModel = partTable.getSelectionModel();
    partTableSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    partTableSelectionModel.addListSelectionListener(new ListSelectionListener() {
      @Override
      public void valueChanged(ListSelectionEvent e) {
        selectPart(e);
      }
    });
    
    JScrollPane partListSP = new JScrollPane(partTable);
    
    eastPanel = new JPanel();
    eastPanel.setLayout(new BoxLayout(eastPanel, BoxLayout.Y_AXIS));
    eastPanel.add(partCount);
    eastPanel.add(partListSP);
    eastPanel.setPreferredSize(new Dimension(200, 0));
  }
  
  private void initAssemblyUnitList() {
    assemblyListModel.clear();
    
    if (AssemblyUnitService.getInstance().getCurrentAssemblyUnit().getParentId() != -1) {
      assemblyListModel.addElement("������������ ������");
    }
    for (String assemblyUnitName : AssemblyUnitService.getInstance().getAssemblyUnitChildrenNames()) {
      assemblyListModel.addElement(assemblyUnitName);
    }
    
    assemblyUnitName.setText(AssemblyUnitService.getInstance().getCurrentAssemblyUnit().getName());
  }
  
  /*
   * ��� ����� ��������� ������ ������� ������� ������� ���������� � ������ ����������� �� �� ������.
   * ��� ������ �� ����� ����������� ������, �� ��������, ��� ������ ��������� � �� ���������
   * �� ������� ���������� ����������, � �������� ��� ����������.
   * 
   * � ��������� ������ ���������� ���� �� �������� ����������� ����������, ��������� � ��������
   * ���������, ��� ������������� �������� ������ ������ ������ ������� � ��� ��������� �������
   * ������� � ����������������� ��� �� ���������.
   */
  private void initPartTable() {
    // ��� ��������� ���������� ��������� ������� ���������� ��������� ���������, ��������� �������������:
    String selectedPart = null;
    if (selectedRow != -1) {
      selectedPart = partTable.getValueAt(partTable.getSelectedRow(), 0).toString(); 
    }
    
    partTableModel.setRowCount(0);
    AssemblyUnitService.getInstance().getPartTable(AssemblyUnitService.getInstance().getCurrentAssemblyUnit().getId()).forEach(partRow -> {
      partTableModel.addRow(partRow);
    });
    partTableModel.fireTableDataChanged();
    
    partCount.setText("���������� �������: " + partTableModel.getRowCount());
    
    if (selectedPart != null) {
      for (int i = 0; i < partTable.getRowCount(); i++) {
        if (selectedPart.equals(partTable.getValueAt(i, 0))) {
          partTable.changeSelection(i, 0, false, false);
          break;
        }
      }
    }
  }
  
  private void changeAssembly() {
    int indexOf = assemblyList.getSelectedValue().indexOf("�");
    
    if (indexOf != -1) {
      String selectedAssemblyUnitId = assemblyList.getSelectedValue().substring(indexOf + 1);
      AssemblyUnitService.getInstance().setCurrentAssemblyUnit(selectedAssemblyUnitId);
    } else {
      // ���� �� ������� ����� ������ "�", ������ ������ ����� �������� � ������������ ������
      int parentId = AssemblyUnitService.getInstance().getCurrentAssemblyUnit().getParentId();
      AssemblyUnitService.getInstance().setCurrentAssemblyUnit(parentId);
    }
    
    selectedRow = -1; // ��� �������� � ������ ��������� ���������, ��� ��������� ������������� ������ ��������� �� �����
    partNameForEditing.setText("");
    partAmountForEditing.setText("");
    initAssemblyUnitList();
    initPartTable();
  }
  
  private void addAssembly() {
    if (AssemblyUnitService.getInstance().addAssembly()) {
      initAssemblyUnitList();
      initPartTable();
    }
  }
  
  private void deleteAssembly() {
    if (AssemblyUnitService.getInstance().deleteAssembly()) {
      selectedRow = -1;
      partNameForEditing.setText("");
      partAmountForEditing.setText("");
      
      initAssemblyUnitList();
      initPartTable();
    }
  }
  
  private void selectPart(ListSelectionEvent e) {
    if (!e.getValueIsAdjusting() && partTable.getSelectedRow() != -1) {
      String selectedName = partTable.getValueAt(partTable.getSelectedRow(), 0).toString();
      String selectedAmount = partTable.getValueAt(partTable.getSelectedRow(), 1).toString();
      
      partNameForEditing.setText(selectedName);
      partAmountForEditing.setText(selectedAmount);
      selectedRow = partTable.getSelectedRow();
    }
  }
  
  private void addPart() {
    String partName = partNameForCreating.getText();
    String partAmount = partAmountForCreating.getText();
    
    if (AssemblyUnitService.getInstance().addPart(partName, partAmount)) {
      partNameForCreating.setText("");
      partAmountForCreating.setText("");
      initPartTable();
    }
  }
  
  private void updatePart() {
    String newName = partNameForEditing.getText();
    String oldName = (selectedRow != -1) ? partTable.getValueAt(selectedRow, 0).toString() : "";
    String newAmount = partAmountForEditing.getText();
 
    if (AssemblyUnitService.getInstance().updatePart(oldName, newName, newAmount)) {
      partNameForEditing.setText("");
      partAmountForEditing.setText("");
      partTable.clearSelection();
      selectedRow = -1;
      initPartTable();
    }
  }
  
  private void deletePart() {
    int currentId = AssemblyUnitService.getInstance().getCurrentAssemblyUnit().getId();
    String partName = (selectedRow != -1) ? partTable.getValueAt(selectedRow, 0).toString() : partNameForEditing.getText();
    
    if (AssemblyUnitService.getInstance().deletePart(currentId, partName)) {
      partNameForEditing.setText("");
      partAmountForEditing.setText("");
      selectedRow = -1;
      initPartTable();
    }
  }
}