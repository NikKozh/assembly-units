package ru.nikkozh.assemblyUnits.views;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

import ru.nikkozh.assemblyUnits.models.AssemblyUnitService;

// Класс, полностью отвечающий за весь GUI приложения
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
  private ListSelectionModel partTableSelectionModel;
  private DefaultTableModel partTableModel;
  
  private JPanel centerPanel, eastPanel, westPanel, assemblyButtonsPanel;
  
  private int selectedRow; // последняя выделенная пользователем деталь; нужно для того, чтобы сохранять выделение при обновлении таблицы
  
  public MainView() {
    selectedRow = -1;
    
    frame = new JFrame("Управление сборочными единицами");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    Container container = frame.getContentPane();
        
    initWestPanel();
    initCenterPanel();
    initEastPanel();
    
    container.add(BorderLayout.WEST, westPanel);
    container.add(BorderLayout.CENTER, centerPanel);
    container.add(BorderLayout.EAST, eastPanel);
    
    refreshPartTable();
    refreshAssemblyUnitList();
    
    frame.setMinimumSize(new Dimension(700, 300));
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
    assemblyListSP.setPreferredSize(new Dimension(100, 0));
    
    createAssemblyButton = new JButton("Добавить");
    createAssemblyButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        addAssembly();
      }
    });
    
    deleteAssemblyButton = new JButton("Удалить");
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
  }
  
  private void initCenterPanel() {
    centerPanel = new JPanel();
    centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
    
    GridBagConstraints c = new GridBagConstraints();
    JPanel centerSubPanel = new JPanel(new GridBagLayout());
    
    initCreatingSubPanel(centerSubPanel, c);
    initEditingSubPanel(centerSubPanel, c);
    centerPanel.add(centerSubPanel);
  }
  
  private void initCreatingSubPanel(JPanel centerSubPanel, GridBagConstraints c) {
    partNameForCreating = new JTextField(10);
    partAmountForCreating = new JTextField(10);
    
    createPartButton = new JButton("Добавить");
    createPartButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        addPart();
      }
    });
    
    c.gridx = 0;
    c.gridy = 0;
    c.insets = new Insets(3, 3, 3, 3);
    c.gridwidth = GridBagConstraints.REMAINDER;
    centerSubPanel.add(new JLabel("Добавить новую деталь"), c);
    
    c.gridx = 0;
    c.gridy = 1;
    c.gridwidth = 1;
    centerSubPanel.add(new JLabel("Наименование:"), c);
    
    c.gridx = 1;
    c.gridy = 1;
    centerSubPanel.add(partNameForCreating, c);
    
    c.gridx = 0;
    c.gridy = 2;
    centerSubPanel.add(new JLabel("Количество:"), c);
    
    c.gridx = 1;
    c.gridy = 2;
    centerSubPanel.add(partAmountForCreating, c);
    
    c.gridx = 0;
    c.gridy = 3;
    c.gridwidth = GridBagConstraints.REMAINDER;
    centerSubPanel.add(createPartButton, c);
  }
  
  private void initEditingSubPanel(JPanel centerSubPanel, GridBagConstraints c) {
    partNameForEditing = new JTextField(10);
    partAmountForEditing = new JTextField(10);
    
    editPartButton = new JButton("Редактировать");
    editPartButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        updatePart();
      }
    });
    
    deletePartButton = new JButton("Удалить");
    deletePartButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        deletePart();
      }
    });
    
    c.gridx = 0;
    c.gridy = 4;
    c.insets = new Insets(30, 3, 3, 3);
    c.gridwidth = GridBagConstraints.REMAINDER;
    centerSubPanel.add(new JLabel("Редактировать деталь"), c);
    
    c.gridx = 0;
    c.gridy = 5;
    c.insets = new Insets(3, 3, 3, 3);
    c.gridwidth = 1;
    centerSubPanel.add(new JLabel("Наименование:"), c);
    
    c.gridx = 1;
    c.gridy = 5;
    centerSubPanel.add(partNameForEditing, c);
    
    c.gridx = 0;
    c.gridy = 6;
    centerSubPanel.add(new JLabel("Количество:"), c);
    
    c.gridx = 1;
    c.gridy = 6;
    centerSubPanel.add(partAmountForEditing, c);
    
    c.gridx = 0;
    c.gridy = 7;
    centerSubPanel.add(editPartButton, c);
    
    c.gridx = 1;
    c.gridy = 7;
    centerSubPanel.add(deletePartButton, c);
  }
  
  private void initEastPanel() {
    partCount = new JLabel();
  
    String[] partTableColumnNames = { "Наименование", "Количество (шт.)" };
    
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
    eastPanel.setPreferredSize(new Dimension(230, 0));
  }
  
  private void refreshAssemblyUnitList() {
    assemblyListModel.clear();
    
    if (AssemblyUnitService.getInstance().getCurrentAssemblyUnit().getParentId() != -1) {
      assemblyListModel.addElement("Родительская сборка");
    }
    for (String assemblyUnitName : AssemblyUnitService.getInstance().getAssemblyUnitChildrenNames()) {
      assemblyListModel.addElement(assemblyUnitName);
    }
    
    assemblyUnitName.setText(AssemblyUnitService.getInstance().getCurrentAssemblyUnit().getName());
  }
  
  /*
  * При любом изменений набора деталей текущая таблица обнуляется и список загружается из БД заново.
  * Это далеко не самый оптимальный подход, но учитывая, что проект небольшой и не рассчитан
  * на большое количество информации, я посчитал его допустимым.
  * 
  * В противном случае необходимо было бы отдельно отслеживать добавление, изменение и удаление
  * элементов, при необходимости сдвигать список внутри модели таблицы и при небольших списках
  * выигрыш в прозводительности был бы минимален.
  */
  private void refreshPartTable() {
    // При изменении количества элементов таблицы необходимо сохранять выделение, сделанное пользователем:
    String selectedPart = null;
    if (selectedRow != -1) {
      selectedPart = partTable.getValueAt(partTable.getSelectedRow(), 0).toString(); 
    }
    
    partTableModel.setRowCount(0);
    int currentId = AssemblyUnitService.getInstance().getCurrentAssemblyUnit().getId();
    for (Vector<String> partRow : AssemblyUnitService.getInstance().getPartTable(currentId)) {
      partTableModel.addRow(partRow);
    }
    partTableModel.fireTableDataChanged();
    
    partCount.setText("Количество деталей: " + partTableModel.getRowCount());
    
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
    if (!assemblyList.isSelectionEmpty()) {
      // Поскольку у пользователя нет возможности задать имя сборки и мы можем явно этим управлять,
      // то полагаемся только на символ "№", после которого гарантированно идёт номер сборки:
      int indexOf = assemblyList.getSelectedValue().indexOf("№");
      
      if (indexOf != -1) {
        String selectedAssemblyUnitId = assemblyList.getSelectedValue().substring(indexOf + 1);
        AssemblyUnitService.getInstance().setCurrentAssemblyUnit(selectedAssemblyUnitId);
      } else {
        // Если не удалось найти символ "№", значит выбран пункт возврата к родительской сборке
        int parentId = AssemblyUnitService.getInstance().getCurrentAssemblyUnit().getParentId();
        AssemblyUnitService.getInstance().setCurrentAssemblyUnit(parentId);
      }
      
      // При переходе в другую подсборку указываем, что выделение детали, выбранной пользователем, сохранять не нужно:
      selectedRow = -1; 
      partNameForEditing.setText("");
      partAmountForEditing.setText("");
      refreshAssemblyUnitList();
      refreshPartTable();
    }
  }
  
  private void addAssembly() {
    if (AssemblyUnitService.getInstance().addAssembly()) {
      refreshAssemblyUnitList();
      refreshPartTable();
    }
  }
  
  private void deleteAssembly() {
    if (AssemblyUnitService.getInstance().deleteAssembly()) {
      selectedRow = -1;
      partNameForEditing.setText("");
      partAmountForEditing.setText("");
      
      refreshAssemblyUnitList();
      refreshPartTable();
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
      refreshPartTable();
    }
  }
  
  private void updatePart() {
    String newName = partNameForEditing.getText();
    String newAmount = partAmountForEditing.getText();
    String oldName = (selectedRow != -1) ?
                     partTable.getValueAt(selectedRow, 0).toString() :
                     "";
 
    if (AssemblyUnitService.getInstance().updatePart(oldName, newName, newAmount)) {
      partNameForEditing.setText("");
      partAmountForEditing.setText("");
      partTable.clearSelection();
      selectedRow = -1;
      refreshPartTable();
    }
  }
  
  private void deletePart() {
    String partName = (selectedRow != -1) ?
                      partTable.getValueAt(selectedRow, 0).toString() :
                      partNameForEditing.getText();
    
    if (AssemblyUnitService.getInstance().deletePart(partName)) {
      partNameForEditing.setText("");
      partAmountForEditing.setText("");
      selectedRow = -1;
      refreshPartTable();
    }
  }
}