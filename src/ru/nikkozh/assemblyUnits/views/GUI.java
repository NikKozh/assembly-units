package ru.nikkozh.assemblyUnits.views;

import java.io.*;
import javax.swing.*;

import javafx.scene.layout.Border;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI {
  public static final JFrame frame = new JFrame("Управление сборочными единицами");
  
  private final JTextField partNameForCreating, partAmountForCreating,
                           partNameForEditing, partAmountForEditing;
  
  private final JButton jButtonCreatePart, jButtonEditPart,
                        jButtonCreateAssembly, jButtonDeleteAssembly;
  
  private final JList<String> partList, assemblyList;
  
  private final JPanel centerPanel, eastPanel, westPanel,
                       creatingPanel, editingPanel,
                       assemblyListPanel, assemblyButtonsPanel;
  
  // TODO: рефакторинг: разделить конструктор на несколько методов
  public GUI() {
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    Container container = frame.getContentPane();
    
    DefaultListModel<String> assemblyListModel = new DefaultListModel<>();
    assemblyList = new JList<>(assemblyListModel);
    JScrollPane assemblyListSP = new JScrollPane(assemblyList);
    
    assemblyListPanel = new JPanel();
    assemblyListPanel.setLayout(new BoxLayout(assemblyListPanel, BoxLayout.Y_AXIS));
    assemblyListPanel.add(new JLabel("Управление сборками"));
    assemblyListPanel.add(assemblyListSP);
    
    jButtonCreateAssembly = new JButton("Добавить");
    jButtonDeleteAssembly = new JButton("Удалить");
    
    assemblyButtonsPanel = new JPanel(new FlowLayout());
    assemblyButtonsPanel.add(jButtonCreateAssembly);
    assemblyButtonsPanel.add(jButtonDeleteAssembly);
    
    westPanel = new JPanel(new BorderLayout());
    // westPanel.setLayout(new BoxLayout(westPanel, BoxLayout.Y_AXIS));
    // westPanel.add(assemblyListPanel);
    // westPanel.add(assemblyButtonsPanel);
    westPanel.add(new JLabel("Управление сборками"), BorderLayout.NORTH);
    westPanel.add(assemblyListSP, BorderLayout.CENTER);
    westPanel.add(assemblyButtonsPanel, BorderLayout.SOUTH);
    westPanel.setPreferredSize(new Dimension(200, 0));
    
    partNameForCreating = new JTextField(10);
    partAmountForCreating = new JTextField(10);
    
    DefaultListModel<String> partListModel = new DefaultListModel<>();
    partList = new JList<>(partListModel);
    JScrollPane partListSP = new JScrollPane(partList);
    
    jButtonCreatePart = new JButton("Создать");
    // TODO: рефакторинг: вынести в отдельный внутренний класс,
    // чтобы можно было обращаться к полям класса в другом месте:
    jButtonCreatePart.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        partListModel.addElement("Новая деталь (1 шт.)");
      }
    });
    
    // TODO: сделать ещё одну панель с GridLayout внутри editingPanel и поместить туда лейблы с textEditor'ами
    creatingPanel = new JPanel(new FlowLayout());
    creatingPanel.add(new JLabel("Добавить новую деталь"));
    creatingPanel.add(new JLabel("Наименование детали:"));
    creatingPanel.add(partNameForCreating);
    creatingPanel.add(new JLabel("Количество деталей:"));
    creatingPanel.add(partAmountForCreating);
    creatingPanel.add(jButtonCreatePart);
    
    partNameForEditing = new JTextField(10);
    partAmountForEditing = new JTextField(10);
    
    jButtonEditPart = new JButton("Редактировать");
    
    // TODO: сделать ещё одну панель с GridLayout внутри editingPanel и поместить туда лейблы с textEditor'ами
    editingPanel = new JPanel(new FlowLayout());
    editingPanel.add(new JLabel("Редактировать деталь"));
    editingPanel.add(new JLabel("Наименование детали:"));
    editingPanel.add(partNameForEditing);
    editingPanel.add(new JLabel("Количество деталей:"));
    editingPanel.add(partAmountForEditing);
    editingPanel.add(jButtonEditPart);
    
    centerPanel = new JPanel();
    centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
    centerPanel.add(creatingPanel);
    centerPanel.add(editingPanel);
    
    eastPanel = new JPanel();
    eastPanel.setLayout(new BoxLayout(eastPanel, BoxLayout.Y_AXIS));
    eastPanel.add(new JLabel("Сборочная единица 1"));
    eastPanel.add(partListSP);
    eastPanel.setPreferredSize(new Dimension(200, 0));
    
    // TODO: настроить пространство между компонентами главного BorderLayout (gap)
    container.add(BorderLayout.WEST, westPanel);
    container.add(BorderLayout.CENTER, centerPanel);
    container.add(BorderLayout.EAST, eastPanel);
    
    frame.setSize(680, 450);
    frame.setVisible(true);
  }
}