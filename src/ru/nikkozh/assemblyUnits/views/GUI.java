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
  private final JTextField partName, partAmount;
  private final JButton jButtonCreatePart;
  private final JList<String> partList;
  private final JPanel partPanel, partListPanel;
  
  public GUI() {
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    Container container = frame.getContentPane();
    
    partPanel = new JPanel();
    
    partName = new JTextField(10);
    
    partAmount = new JTextField(10);    
    
    partListPanel = new JPanel();
    
    DefaultListModel<String> partListModel = new DefaultListModel<>();
    partList = new JList<>(partListModel);
    JScrollPane sp = new JScrollPane(partList);
    sp.setPreferredSize(new Dimension(200, 300));
    
    jButtonCreatePart = new JButton("Создать");
    jButtonCreatePart.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        partListModel.addElement("Новая деталь (1 шт.)");
      }
    });
    
    partPanel.add(new JLabel("Наименование детали:"));
    partPanel.add(partName);
    partPanel.add(new JLabel("Количество деталей:"));
    partPanel.add(partAmount);
    partPanel.add(jButtonCreatePart);
    partListPanel.add(new JLabel("Сборочная единица 1:"));
    partListPanel.add(sp);
    
    container.add(BorderLayout.CENTER, partPanel);
    container.add(BorderLayout.EAST, partListPanel);
    
    frame.setSize(600, 600);
    frame.setVisible(true);
  }
}