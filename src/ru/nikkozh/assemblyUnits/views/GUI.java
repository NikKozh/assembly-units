package ru.nikkozh.assemblyUnits.views;

import java.io.*;
import javax.swing.*;

import javafx.scene.layout.Border;
import ru.nikkozh.assemblyUnits.models.AssemblyUnitService;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI {
  public static final JFrame frame = new JFrame("���������� ���������� ���������");
  
  private JLabel assemblyUnitName;
  
  private final JTextField partNameForCreating, partAmountForCreating,
                           partNameForEditing, partAmountForEditing;
  
  private final JButton createPartButton, editPartButton, deletePartButton,
                        createAssemblyButton, deleteAssemblyButton;
  
  private final JList<String> partList, assemblyList;
  private final DefaultListModel<String> partListModel;
  
  /*private final JTable partTable;
  private final ListSele*/
  
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
    
    createAssemblyButton = new JButton("��������");
    deleteAssemblyButton = new JButton("�������");
    
    assemblyButtonsPanel = new JPanel(new FlowLayout());
    assemblyButtonsPanel.add(createAssemblyButton);
    assemblyButtonsPanel.add(deleteAssemblyButton);
    
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
    
    partListModel = new DefaultListModel<>();
    partList = new JList<>(partListModel);
    JScrollPane partListSP = new JScrollPane(partList);
    
    assemblyUnitName = new JLabel(AssemblyUnitService.getInstance().getAssemblyUnitName(1));
    initPartList();
    
    createPartButton = new JButton("�������");
    // TODO: �����������: ������� � ��������� ���������� �����,
    // ����� ����� ���� ���������� � ����� ������ � ������ �����:
    createPartButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        // partList.clearSelection(); TODO: ��������� ��� � ������ ��������������
        
        // TODO: addPart ������ ���������� ������ ������ � ������ ������ ��� ����� ������ ��� ������������
        //       ��������� ������ String � ������ �������������� � ����� �� ����� �������
        //       � GUI ���� ������� ��������� ����� showError(String text) � � ������, ���� addPart ������ �� ������ ������, ���������� � � ��� �����
        try {
          if (AssemblyUnitService.getInstance().addPart(1, partNameForCreating.getText(), Integer.valueOf(partAmountForCreating.getText()))) {
            partNameForCreating.setText("");
            partAmountForCreating.setText("");
            initPartList();
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
    deletePartButton = new JButton("�������");
    
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
    eastPanel.add(partListSP);
    eastPanel.setPreferredSize(new Dimension(200, 0));
    
    // TODO: ��������� ������������ ����� ������������ �������� BorderLayout (gap)
    container.add(BorderLayout.WEST, westPanel);
    container.add(BorderLayout.CENTER, centerPanel);
    container.add(BorderLayout.EAST, eastPanel);
    
    frame.setSize(680, 450);
    frame.setVisible(true);
  }
  
  // TODO: �����������: ������ ����, ����� ������ ��� �������� ������� � ������� ������,
  //       ����� ���������� ������ ����������\�������� ������� � �������� ������ � ���
  private void initPartList() {
    partListModel.clear();
    AssemblyUnitService.getInstance().getPartList(1).forEach(part ->
      partListModel.addElement(part)
    );
  }
}