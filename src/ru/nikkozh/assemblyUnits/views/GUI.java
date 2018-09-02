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
  public static final JFrame frame = new JFrame("���������� ���������� ���������");
  
  private final JTextField partNameForCreating, partAmountForCreating,
                           partNameForEditing, partAmountForEditing;
  
  private final JButton jButtonCreatePart, jButtonEditPart,
                        jButtonCreateAssembly, jButtonDeleteAssembly;
  
  private final JList<String> partList, assemblyList;
  
  private final JPanel centerPanel, eastPanel, westPanel,
                       creatingPanel, editingPanel,
                       assemblyListPanel, assemblyButtonsPanel;
  
  // TODO: �����������: ��������� ����������� �� ��������� �������
  public GUI() {
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    Container container = frame.getContentPane();
    
    DefaultListModel<String> assemblyListModel = new DefaultListModel<>();
    assemblyList = new JList<>(assemblyListModel);
    JScrollPane assemblyListSP = new JScrollPane(assemblyList);
    
    assemblyListPanel = new JPanel();
    assemblyListPanel.setLayout(new BoxLayout(assemblyListPanel, BoxLayout.Y_AXIS));
    assemblyListPanel.add(new JLabel("���������� ��������"));
    assemblyListPanel.add(assemblyListSP);
    
    jButtonCreateAssembly = new JButton("��������");
    jButtonDeleteAssembly = new JButton("�������");
    
    assemblyButtonsPanel = new JPanel(new FlowLayout());
    assemblyButtonsPanel.add(jButtonCreateAssembly);
    assemblyButtonsPanel.add(jButtonDeleteAssembly);
    
    westPanel = new JPanel(new BorderLayout());
    // westPanel.setLayout(new BoxLayout(westPanel, BoxLayout.Y_AXIS));
    // westPanel.add(assemblyListPanel);
    // westPanel.add(assemblyButtonsPanel);
    westPanel.add(new JLabel("���������� ��������"), BorderLayout.NORTH);
    westPanel.add(assemblyListSP, BorderLayout.CENTER);
    westPanel.add(assemblyButtonsPanel, BorderLayout.SOUTH);
    westPanel.setPreferredSize(new Dimension(200, 0));
    
    partNameForCreating = new JTextField(10);
    partAmountForCreating = new JTextField(10);
    
    DefaultListModel<String> partListModel = new DefaultListModel<>();
    partList = new JList<>(partListModel);
    JScrollPane partListSP = new JScrollPane(partList);
    
    jButtonCreatePart = new JButton("�������");
    // TODO: �����������: ������� � ��������� ���������� �����,
    // ����� ����� ���� ���������� � ����� ������ � ������ �����:
    jButtonCreatePart.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        partListModel.addElement("����� ������ (1 ��.)");
      }
    });
    
    // TODO: ������� ��� ���� ������ � GridLayout ������ editingPanel � ��������� ���� ������ � textEditor'���
    creatingPanel = new JPanel(new FlowLayout());
    creatingPanel.add(new JLabel("�������� ����� ������"));
    creatingPanel.add(new JLabel("������������ ������:"));
    creatingPanel.add(partNameForCreating);
    creatingPanel.add(new JLabel("���������� �������:"));
    creatingPanel.add(partAmountForCreating);
    creatingPanel.add(jButtonCreatePart);
    
    partNameForEditing = new JTextField(10);
    partAmountForEditing = new JTextField(10);
    
    jButtonEditPart = new JButton("�������������");
    
    // TODO: ������� ��� ���� ������ � GridLayout ������ editingPanel � ��������� ���� ������ � textEditor'���
    editingPanel = new JPanel(new FlowLayout());
    editingPanel.add(new JLabel("������������� ������"));
    editingPanel.add(new JLabel("������������ ������:"));
    editingPanel.add(partNameForEditing);
    editingPanel.add(new JLabel("���������� �������:"));
    editingPanel.add(partAmountForEditing);
    editingPanel.add(jButtonEditPart);
    
    centerPanel = new JPanel();
    centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
    centerPanel.add(creatingPanel);
    centerPanel.add(editingPanel);
    
    eastPanel = new JPanel();
    eastPanel.setLayout(new BoxLayout(eastPanel, BoxLayout.Y_AXIS));
    eastPanel.add(new JLabel("��������� ������� 1"));
    eastPanel.add(partListSP);
    eastPanel.setPreferredSize(new Dimension(200, 0));
    
    // TODO: ��������� ������������ ����� ������������ �������� BorderLayout (gap)
    container.add(BorderLayout.WEST, westPanel);
    container.add(BorderLayout.CENTER, centerPanel);
    container.add(BorderLayout.EAST, eastPanel);
    
    frame.setSize(680, 450);
    frame.setVisible(true);
  }
}