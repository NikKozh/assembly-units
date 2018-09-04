package ru.nikkozh.assemblyUnits.views;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import ru.nikkozh.assemblyUnits.models.AssemblyUnitService;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI {
  public static final JFrame frame = new JFrame("���������� ���������� ���������");
  
  private JLabel assemblyUnitName, partCount;
  
  private final JTextField partNameForCreating, partAmountForCreating,
                           partNameForEditing, partAmountForEditing;
  
  private final JButton createPartButton, editPartButton, deletePartButton,
                        createAssemblyButton, deleteAssemblyButton;
  
  private final JList<String> assemblyList;
  
  private final JTable partTable;
  private final String[] partTableColumnNames = { "������������", "����������" };
  private final ListSelectionModel partTableSelectionModel;
  private final DefaultTableModel partTableModel;
  
  private final JPanel centerPanel, eastPanel, westPanel,
                       creatingPanel, editingPanel,
                       assemblyListPanel, assemblyButtonsPanel;
  
  private boolean isPartTableClearing;
  private int selectedRow;
  
  // TODO: �����������: ��������� ����������� �� ��������� �������
  // TODO: ���� ����� �����, ������� ������ ������� ������ ���� ������ �� ����� �����, �������� �� ������� ������ � �.�.
  public GUI() {
    isPartTableClearing = false;
    selectedRow = -1;
    
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    Container container = frame.getContentPane();
    
    DefaultListModel<String> assemblyListModel = new DefaultListModel<>();
    assemblyList = new JList<>(assemblyListModel);
    JScrollPane assemblyListSP = new JScrollPane(assemblyList);
    
    assemblyListPanel = new JPanel();
    assemblyListPanel.setLayout(new BoxLayout(assemblyListPanel, BoxLayout.Y_AXIS));
    assemblyListPanel.add(new JLabel("���������� ��������"));
    assemblyListPanel.add(assemblyListSP);
    
    createAssemblyButton = new JButton("��������");
    deleteAssemblyButton = new JButton("�������");
    
    assemblyButtonsPanel = new JPanel(new FlowLayout());
    assemblyButtonsPanel.add(createAssemblyButton);
    assemblyButtonsPanel.add(deleteAssemblyButton);
    
    westPanel = new JPanel(new BorderLayout());
    westPanel.add(new JLabel("���������� ��������"), BorderLayout.NORTH);
    westPanel.add(assemblyListSP, BorderLayout.CENTER);
    westPanel.add(assemblyButtonsPanel, BorderLayout.SOUTH);
    westPanel.setPreferredSize(new Dimension(200, 0));
    
    partNameForCreating = new JTextField(10);
    partAmountForCreating = new JTextField(10);
      
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
      // TODO: ������� � ��������� ���������� �����
      @Override
      public void valueChanged(ListSelectionEvent e) {
        // ���� ������� ������ �� � ������� ������ �������, ����� ��������� ���� ���
        // ��� ����, ����� ��� � ������� ���������� ������ ������ ���� ���
        if (!e.getValueIsAdjusting()) {
          try {
            partNameForEditing.setText(partTable.getValueAt(partTable.getSelectedRow(), 0).toString());
            partAmountForEditing.setText(partTable.getValueAt(partTable.getSelectedRow(), 1).toString());
            selectedRow = partTable.getSelectedRow();
          } catch (Exception ex) {
            System.out.println("****** ERROR"); // TODO: ���-�� � ���� �������
          }
        }
      }
    });
        
    JScrollPane partListSP = new JScrollPane(partTable);
    
    assemblyUnitName = new JLabel(AssemblyUnitService.getInstance().getAssemblyUnitName(1));
    initPartTable();
    
    partCount = new JLabel("���������� �������: " + partTableModel.getRowCount());
    
    createPartButton = new JButton("�������");
    // TODO: �����������: ������� � ��������� ���������� �����,
    // ����� ����� ���� ���������� � ����� ������ � ������ �����:
    createPartButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        // TODO: addPart ������ ���������� ������ ������ � ������ ������ ��� ����� ������ ��� ������������
        //       ��������� ������ String � ������ �������������� � ����� �� ����� �������
        //       � GUI ���� ������� ��������� ����� showError(String text) � � ������, ���� addPart ������ �� ������ ������, ���������� � � ��� �����
        try {
          if (AssemblyUnitService.getInstance().addPart(1, partNameForCreating.getText(), Integer.valueOf(partAmountForCreating.getText()))) {
            partNameForCreating.setText("");
            partAmountForCreating.setText("");
            initPartTable();
            partCount.setText("���������� �������: " + partTableModel.getRowCount());
          }
        } catch (NumberFormatException ex) {
          // TODO: �������� ��������� � ������������ �����
          ex.printStackTrace();
        }
      }
    });
    
    // TODO: ������� ��� ���� ������ � GridLayout ������ editingPanel � ��������� ���� ������ � textEditor'���
    creatingPanel = new JPanel(new FlowLayout());
    creatingPanel.add(new JLabel("�������� ����� ������"));
    creatingPanel.add(new JLabel("������������ ������:"));
    creatingPanel.add(partNameForCreating);
    creatingPanel.add(new JLabel("���������� �������:"));
    creatingPanel.add(partAmountForCreating);
    creatingPanel.add(createPartButton);
    
    partNameForEditing = new JTextField(10);
    partAmountForEditing = new JTextField(10);
    
    editPartButton = new JButton("�������������");
    editPartButton.addActionListener(new ActionListener() {
      // TODO: ������� � ��������� ���������� �����
      @Override
      public void actionPerformed(ActionEvent e) {
        // TODO: �������� ��� �������������, ������� � ����� ������ ���������� ������ ������ (� �� �� ����� � ������ ������):
        //       � ��� �������� ��� ���, ����� �������������� string to int ����� ������, � �� view
        if (AssemblyUnitService.getInstance().updatePart(1, partTable.getValueAt(selectedRow, 0).toString(), partNameForEditing.getText(), Integer.valueOf(partAmountForEditing.getText()))) {
          partNameForEditing.setText("");
          partAmountForEditing.setText("");
          partTable.clearSelection();
          selectedRow = -1;
          initPartTable();
        }
      }
    });
    
    deletePartButton = new JButton("�������");
    deletePartButton.addActionListener(new ActionListener() {
      // TODO: ������� � ��������� ���������� �����
      @Override
      public void actionPerformed(ActionEvent e) {
        // TODO: �������� ������� �� ������ ����
        if (AssemblyUnitService.getInstance().deletePart(1, partNameForEditing.getText())) {
          partNameForEditing.setText("");
          partAmountForEditing.setText("");
          partTable.clearSelection();
          selectedRow = -1;
          initPartTable();
          partCount.setText("���������� �������: " + partTableModel.getRowCount());
        }
      }
    });
    
    // TODO: ������� ��� ���� ������ � GridLayout ������ editingPanel � ��������� ���� ������ � textEditor'���
    editingPanel = new JPanel(new FlowLayout());
    editingPanel.add(new JLabel("������������� ������"));
    editingPanel.add(new JLabel("������������ ������:"));
    editingPanel.add(partNameForEditing);
    editingPanel.add(new JLabel("���������� �������:"));
    editingPanel.add(partAmountForEditing);
    editingPanel.add(editPartButton);
    editingPanel.add(deletePartButton);
    
    centerPanel = new JPanel();
    centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
    centerPanel.add(creatingPanel);
    centerPanel.add(editingPanel);
    
    eastPanel = new JPanel();
    eastPanel.setLayout(new BoxLayout(eastPanel, BoxLayout.Y_AXIS));
    eastPanel.add(assemblyUnitName);
    eastPanel.add(partCount);
    eastPanel.add(partListSP);
    eastPanel.setPreferredSize(new Dimension(200, 0));
    
    // TODO: ��������� ������������ ����� ������������ �������� BorderLayout (gap)
    container.add(BorderLayout.WEST, westPanel);
    container.add(BorderLayout.CENTER, centerPanel);
    container.add(BorderLayout.EAST, eastPanel);
    
    frame.setSize(680, 300);
    frame.setVisible(true);
  }
  
  // TODO: �����������: ������ ����, ����� ������ ��� �������� ������� � ������� ������,
  //       ����� ���������� ������ ���������\�������� ������� � �������� ������ � ���
  private void initPartTable() {
    /*
     * ����� ������ ����������� ������ ��������:
     * 1. ���������, �������� �� �����-���� ������
     * 2. ���� ��, �� ��������� ������������ ������ �� ���� ������ (�� �������, �� �� ����� �����!)
     * 3. ��������� �������, ��������� ��������.
     * 4. ���� ���������� ������������ ������, ������� ���� ��������
     * 5. ���� �������, �������� ����� � ������.
     * 6. �������� ��� ������ ����������.
     */
    
    String selectedPart = null;
    if (selectedRow != -1) {
      selectedPart = partTable.getValueAt(partTable.getSelectedRow(), 0).toString(); 
    }
    
    partTableModel.setRowCount(0);
    partTableModel.fireTableDataChanged();
    AssemblyUnitService.getInstance().getPartTable(1).forEach(partRow -> {
      partTableModel.addRow(partRow);
    });
    
    if (selectedPart != null) {
      for (int i = 0; i < partTable.getRowCount(); i++) {
        if (selectedPart.equals(partTable.getValueAt(i, 0))) {
          partTable.changeSelection(i, 0, false, false);
          break;
        }
      }
    }
  }
}