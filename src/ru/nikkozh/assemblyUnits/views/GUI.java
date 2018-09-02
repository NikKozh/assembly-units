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
  private final JTextField partNameForCreating, partAmountForCreating;
  private final JTextField partNameForEditing, partAmountForEditing;
  private final JButton jButtonCreatePart, jButtonEditPart;
  private final JList<String> partList;
  private final JPanel centerPanel, partListPanel;
  private final JPanel creatingPanel, editingPanel;
  
  public GUI() {
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    Container container = frame.getContentPane();
    
    partNameForCreating = new JTextField(10);
    partAmountForCreating = new JTextField(10);
    
    DefaultListModel<String> partListModel = new DefaultListModel<>();
    partList = new JList<>(partListModel);
    JScrollPane sp = new JScrollPane(partList);
    
    jButtonCreatePart = new JButton("Создать");
    jButtonCreatePart.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        partListModel.addElement("Новая деталь (1 шт.)");
      }
    });
    
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
    
    partListPanel = new JPanel();
    partListPanel.setLayout(new BoxLayout(partListPanel, BoxLayout.Y_AXIS));
    partListPanel.add(new JLabel("Сборочная единица 1:"));
    partListPanel.add(sp);
    
    container.add(BorderLayout.CENTER, centerPanel);
    container.add(BorderLayout.EAST, partListPanel);
    
    frame.setSize(550, 600);
    frame.setVisible(true);
  }
}