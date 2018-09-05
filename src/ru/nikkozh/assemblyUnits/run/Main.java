package ru.nikkozh.assemblyUnits.run;

import javax.swing.*;

import ru.nikkozh.assemblyUnits.views.MainView;

// TODO: заменить абсолютно везде элементы Java 8+ на Java 7
// TODO: в самом конце: подумать над тем, чтобы сделать свой класс Exception и пользоваться им при обработке ошибок пользовательских форм
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