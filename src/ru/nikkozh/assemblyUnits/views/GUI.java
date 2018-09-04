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
  public static final JFrame frame = new JFrame("Управление сборочными единицами");
  
  private JLabel assemblyUnitName, partCount;
  
  private final JTextField partNameForCreating, partAmountForCreating,
                           partNameForEditing, partAmountForEditing;
  
  private final JButton createPartButton, editPartButton, deletePartButton,
                        createAssemblyButton, deleteAssemblyButton;
  
  private final JList<String> assemblyList;
  
  private final JTable partTable;
  private final String[] partTableColumnNames = { "Наименование", "Количество" };
  private final ListSelectionModel partTableSelectionModel;
  private final DefaultTableModel partTableModel;
  
  private final JPanel centerPanel, eastPanel, westPanel,
                       creatingPanel, editingPanel,
                       assemblyListPanel, assemblyButtonsPanel;
  
  private boolean isPartTableClearing;
  private int selectedRow;
  
  // TODO: рефакторинг: разделить конструктор на несколько методов
  // TODO: если будет время, сделать всякие удобные мелочи типа фокуса на полях ввода, отправки по нажатию энтера и т.д.
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
    assemblyListPanel.add(new JLabel("Управление сборками"));
    assemblyListPanel.add(assemblyListSP);
    
    createAssemblyButton = new JButton("Добавить");
    deleteAssemblyButton = new JButton("Удалить");
    
    assemblyButtonsPanel = new JPanel(new FlowLayout());
    assemblyButtonsPanel.add(createAssemblyButton);
    assemblyButtonsPanel.add(deleteAssemblyButton);
    
    westPanel = new JPanel(new BorderLayout());
    westPanel.add(new JLabel("Управление сборками"), BorderLayout.NORTH);
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
      // TODO: вынести в отдельный внутренний класс
      @Override
      public void valueChanged(ListSelectionEvent e) {
        // Если событие сейчас НЕ в цепочке других событий, тогда выполняем этот код
        // Для того, чтобы код в условии выполнялся всегда только один раз
        if (!e.getValueIsAdjusting()) {
          try {
            partNameForEditing.setText(partTable.getValueAt(partTable.getSelectedRow(), 0).toString());
            partAmountForEditing.setText(partTable.getValueAt(partTable.getSelectedRow(), 1).toString());
            selectedRow = partTable.getSelectedRow();
          } catch (Exception ex) {
            System.out.println("****** ERROR"); // TODO: что-то с этим сделать
          }
        }
      }
    });
        
    JScrollPane partListSP = new JScrollPane(partTable);
    
    assemblyUnitName = new JLabel(AssemblyUnitService.getInstance().getAssemblyUnitName(1));
    initPartTable();
    
    partCount = new JLabel("Количество деталей: " + partTableModel.getRowCount());
    
    createPartButton = new JButton("Создать");
    // TODO: рефакторинг: вынести в отдельный внутренний класс,
    // чтобы можно было обращаться к полям класса в другом месте:
    createPartButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        // TODO: addPart должен возвращать пустую строку в случае успеха или текст ошибки для пользователя
        //       Принимать должен String и делать преобразование к числу на своей стороне
        //       В GUI надо сделать отдельный метод showError(String text) и в случае, если addPart вернул не пустую строку, передавать её в тот метод
        try {
          if (AssemblyUnitService.getInstance().addPart(1, partNameForCreating.getText(), Integer.valueOf(partAmountForCreating.getText()))) {
            partNameForCreating.setText("");
            partAmountForCreating.setText("");
            initPartTable();
            partCount.setText("Количество деталей: " + partTableModel.getRowCount());
          }
        } catch (NumberFormatException ex) {
          // TODO: добавить сообщение о некорректном числе
          ex.printStackTrace();
        }
      }
    });
    
    // TODO: сделать ещё одну панель с GridLayout внутри editingPanel и поместить туда лейблы с textEditor'ами
    creatingPanel = new JPanel(new FlowLayout());
    creatingPanel.add(new JLabel("Добавить новую деталь"));
    creatingPanel.add(new JLabel("Наименование детали:"));
    creatingPanel.add(partNameForCreating);
    creatingPanel.add(new JLabel("Количество деталей:"));
    creatingPanel.add(partAmountForCreating);
    creatingPanel.add(createPartButton);
    
    partNameForEditing = new JTextField(10);
    partAmountForEditing = new JTextField(10);
    
    editPartButton = new JButton("Редактировать");
    editPartButton.addActionListener(new ActionListener() {
      // TODO: вынести в отдельный внутренний класс
      @Override
      public void actionPerformed(ActionEvent e) {
        // TODO: подумать над рефакторингом, условие и вызов метода получаются вообще дикими (и то же самое в других местах):
        //       и ещё подумать над тем, чтобы преобразование string to int делал сервис, а не view
        if (AssemblyUnitService.getInstance().updatePart(1, partTable.getValueAt(selectedRow, 0).toString(), partNameForEditing.getText(), Integer.valueOf(partAmountForEditing.getText()))) {
          partNameForEditing.setText("");
          partAmountForEditing.setText("");
          partTable.clearSelection();
          selectedRow = -1;
          initPartTable();
        }
      }
    });
    
    deletePartButton = new JButton("Удалить");
    deletePartButton.addActionListener(new ActionListener() {
      // TODO: вынести в отдельный внутренний класс
      @Override
      public void actionPerformed(ActionEvent e) {
        // TODO: добавить реакцию на ошибки сюда
        if (AssemblyUnitService.getInstance().deletePart(1, partNameForEditing.getText())) {
          partNameForEditing.setText("");
          partAmountForEditing.setText("");
          partTable.clearSelection();
          selectedRow = -1;
          initPartTable();
          partCount.setText("Количество деталей: " + partTableModel.getRowCount());
        }
      }
    });
    
    // TODO: сделать ещё одну панель с GridLayout внутри editingPanel и поместить туда лейблы с textEditor'ами
    editingPanel = new JPanel(new FlowLayout());
    editingPanel.add(new JLabel("Редактировать деталь"));
    editingPanel.add(new JLabel("Наименование детали:"));
    editingPanel.add(partNameForEditing);
    editingPanel.add(new JLabel("Количество деталей:"));
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
    
    // TODO: настроить пространство между компонентами главного BorderLayout (gap)
    container.add(BorderLayout.WEST, westPanel);
    container.add(BorderLayout.CENTER, centerPanel);
    container.add(BorderLayout.EAST, eastPanel);
    
    frame.setSize(680, 300);
    frame.setVisible(true);
  }
  
  // TODO: оптимизация: вместо того, чтобы каждый раз занулять таблицу и строить заново,
  //       нужно передавать методу изменённый\удалённый элемент и работать только с ним
  private void initPartTable() {
    /*
     * Перед каждым добавлением нового элемента:
     * 1. Проверить, выделена ли какая-либо строка
     * 2. Если да, то сохраняем наименование детали из этой строки (из таблицы, не из полей ввода!)
     * 3. Обновляем таблицу, выделение теряется.
     * 4. Ищем сохранённое наименование детали, которая была выделена
     * 5. Если находим, выясняем номер её строки.
     * 6. Выделяем эту строку программно.
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