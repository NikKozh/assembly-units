package ru.nikkozh.assemblyUnits.run;

import javax.swing.*;

import ru.nikkozh.assemblyUnits.views.MainView;

// TODO: �������� ��������� ����� �������� Java 8+ �� Java 7
// TODO: � ����� �����: �������� ��� ���, ����� ������� ���� ����� Exception � ������������ �� ��� ��������� ������ ���������������� ����
public class Main {
  public static void main(String[] args) {
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
          new MainView();
        }
    });
  }
}