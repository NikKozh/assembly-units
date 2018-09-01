package ru.nikkozh.assemblyUnits.views;

import java.io.*;
import javax.swing.*;

import java.util.HashMap;
import java.util.Map;

import java.awt.*;

public class GUI {
  public static final JFrame frame = new JFrame("Управление сборочными единицами");
  private final JTextField partName, partAmount;
  private final JButton jButtonCreatePart;
  // private final JPanel panel;
  
  public GUI() {
    frame.getContentPane().add(new Label("Наименование детали:"));
    partName = new JTextField(20);
    frame.getContentPane().add(partName);
    
    frame.getContentPane().add(new Label("Количество деталей:"));
    partAmount = new JTextField(20);
    frame.getContentPane().add(partAmount);
    
    jButtonCreatePart = new JButton("Создать");
    frame.getContentPane().add(jButtonCreatePart);
    
    frame.setSize(300, 300);
    frame.setVisible(true);
  }
}
