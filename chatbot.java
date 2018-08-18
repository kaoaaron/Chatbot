package chatbot;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.Border;


import java.sql.Connection;

public class Chatbot extends JFrame implements KeyListener, ActionListener {

 private static final long serialVersionUID = 1 L;

 JPanel pan = new JPanel();
 JTextArea dialog = new JTextArea(15, 70);
 JTextArea usertxt = new JTextArea(3, 60);
 JButton enter = new JButton("Enter");
 JScrollPane scroll = new JScrollPane(dialog, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

 public Chatbot() {
  super("Wallace the Wizard");
  setSize(800, 350);
  setResizable(false);
  setDefaultCloseOperation(EXIT_ON_CLOSE);

  usertxt.addKeyListener(this);

  Border border = BorderFactory.createLineBorder(new Color(0, 191, 250), 2);

  dialog.setBorder(border);
  dialog.setLineWrap(true);
  dialog.setWrapStyleWord(true);
  usertxt.setBorder(border);
  pan.setBackground(new Color(193, 205, 200));
  dialog.setEditable(false);
  pan.add(scroll);
  pan.add(usertxt);
  pan.add(enter);
  enter.addActionListener(this);
  add(pan);
 }

 @Override
 public void keyPressed(KeyEvent e) {
  if (e.getKeyCode() == KeyEvent.VK_ENTER) {
   String[] sentence;
   int flag = 0;
   int counter = 0;
   ArrayList < String > catagory = new ArrayList < > ();
   ArrayList < String > keywords = new ArrayList < > ();
   usertxt.setEditable(false);
   String text = usertxt.getText();
   usertxt.setText("");
   String keyword = null;
   addText("You: " + text);
   text = text.toLowerCase().replaceAll("[.?\\d!]", "");;

   //fix regular expressions
   if (text.matches("(?i)hi\\s*.*") || text.matches("(?i)hello\\s*.*") || text.matches("(?i)hola\\s*.*")) {
    try {
     Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/greetings", "root", "");
     PreparedStatement statement = con.prepareStatement("SELECT greet FROM greet ORDER BY RAND() LIMIT 1");

     ResultSet result = statement.executeQuery();

     while (result.next()) {
      addText("\nWallace: " + result.getString(1));
     }
     con.close();
    } catch (SQLException eh) {
     System.out.println("Connection Failed");
    }
   } else {
    sentence = text.split("[\\s]");
    // for(String s:sentence){
    // System.out.println(s);
    // }

    try {
     Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/greetings", "root", "");
     PreparedStatement statement = con.prepareStatement("SELECT DISTINCT catagory FROM chdproblems");

     ResultSet result = statement.executeQuery();


     while (result.next()) {
      catagory.add(result.getString(1));
     }
     con.close();
    } catch (SQLException eh) {
     System.out.println("Connection Failed");
    }

    for (int i = 0; i < sentence.length; i++) {
     if (catagory.contains(sentence[i]) == true) {
      flag = 1;
      keyword = sentence[i];
     }
    }


    if (flag == 1) {
     try {
      Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/greetings", "root", "");
      String temp = "SELECT keywords FROM `chdproblems` WHERE catagory like \"" + keyword + "\"";
      PreparedStatement statement = con.prepareStatement(temp);
      ResultSet result = statement.executeQuery();
      while (result.next()) {
       keywords.add(result.getString(1));
      }
     } catch (SQLException e1) {
      e1.printStackTrace();
     }

     int countStore = 0;

     String temp5 = new String();
     for (int i = 0; i < keywords.size(); i++) {
      String temp3[];

      String temp2 = keywords.get(i);
      temp3 = temp2.split(",[\\s]");


      for (int a = 0; a < sentence.length; a++) {
       for (int aa = 0; aa < temp3.length; aa++) {
        if (sentence[a].equals(temp3[aa])) {
         counter++;
        }
       }
      }
      if (counter > countStore) {
       countStore = counter;
       temp5 = keywords.get(i);
      }
      counter = 0;

     }

     /////
     try {
      Connection con2 = DriverManager.getConnection("jdbc:mysql://localhost:3306/greetings", "root", "");
      String temp6 = "SELECT description FROM `chdproblems` WHERE keywords like \"" + temp5 + "\"";
      PreparedStatement statement = con2.prepareStatement(temp6);
      ResultSet result2 = statement.executeQuery();


      while (result2.next()) {
       addText("\nWallace: " + result2.getString(1));
      }
      // System.out.println(catagory.get(0));
      // System.out.println(catagory.get(1));
      con2.close();
     } catch (SQLException eh) {
      System.out.println("Connection Failed");
     }
     /////
    } else {
     addText("\nWallace: Sorry, I don't know about the thing that you are talking about");
    }
   } //else

   addText("\n");
  }
 }
 @Override
 public void keyReleased(KeyEvent e) {
  if (e.getKeyCode() == KeyEvent.VK_ENTER) {
   usertxt.setEditable(true);
  }
 }


 @Override
 public void keyTyped(KeyEvent e) {
  // TODO Auto-generated method stub

 }

 public void addText(String str) {
  dialog.setText(dialog.getText() + str);
 }

 public static void main(String args[]) {
  try {
   Class.forName("com.mysql.jdbc.Driver");
   System.out.println("Driver Loaded");
  } catch (ClassNotFoundException e) {
   // TODO Auto-generated catch block
   System.out.println("Driver Failed");
  }


  Chatbot t = new Chatbot();
  t.setVisible(true);
 }

 @Override
 public void actionPerformed(ActionEvent e) {
  // TODO Auto-generated method stub
  String[] sentence;
  int flag = 0;
  int counter = 0;
  ArrayList < String > catagory = new ArrayList < > ();
  ArrayList < String > keywords = new ArrayList < > ();
  String text = usertxt.getText();
  usertxt.setText("");
  String keyword = null;
  addText("You: " + text);
  text = text.toLowerCase().replaceAll("[.?\\d!]", "");;

  //fix regular expressions
  if (text.matches("(?i)hi\\s*.*") || text.matches("(?i)hello\\s*.*") || text.matches("(?i)hola\\s*.*")) {
   try {
    Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/greetings", "root", "");
    PreparedStatement statement = con.prepareStatement("SELECT greet FROM greet ORDER BY RAND() LIMIT 1");

    ResultSet result = statement.executeQuery();

    while (result.next()) {
     addText("\nWallace: " + result.getString(1));
    }
    con.close();
   } catch (SQLException eh) {
    System.out.println("Connection Failed");
   }
  } else {
   sentence = text.split("[\\s]");
   // for(String s:sentence){
   // System.out.println(s);
   // }

   try {
    Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/greetings", "root", "");
    PreparedStatement statement = con.prepareStatement("SELECT DISTINCT catagory FROM chdproblems");

    ResultSet result = statement.executeQuery();


    while (result.next()) {
     catagory.add(result.getString(1));
    }
    con.close();
   } catch (SQLException eh) {
    System.out.println("Connection Failed");
   }

   for (int i = 0; i < sentence.length; i++) {
    if (catagory.contains(sentence[i]) == true) {
     flag = 1;
     keyword = sentence[i];
    }
   }


   if (flag == 1) {
    try {
     Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/greetings", "root", "");
     String temp = "SELECT keywords FROM `chdproblems` WHERE catagory like \"" + keyword + "\"";
     PreparedStatement statement = con.prepareStatement(temp);
     ResultSet result = statement.executeQuery();
     while (result.next()) {
      keywords.add(result.getString(1));
     }
    } catch (SQLException e1) {
     e1.printStackTrace();
    }

    int countStore = 0;

    String temp5 = new String();
    for (int i = 0; i < keywords.size(); i++) {
     String temp3[];

     String temp2 = keywords.get(i);
     temp3 = temp2.split(",[\\s]");


     for (int a = 0; a < sentence.length; a++) {
      for (int aa = 0; aa < temp3.length; aa++) {
       if (sentence[a].equals(temp3[aa])) {
        counter++;
       }
      }
     }
     if (counter > countStore) {
      countStore = counter;
      temp5 = keywords.get(i);
     }
     counter = 0;

    }
    try {
     Connection con2 = DriverManager.getConnection("jdbc:mysql://localhost:3306/greetings", "root", "");
     String temp6 = "SELECT description FROM `chdproblems` WHERE keywords like \"" + temp5 + "\"";
     PreparedStatement statement = con2.prepareStatement(temp6);
     ResultSet result2 = statement.executeQuery();


     while (result2.next()) {
      addText("\nWallace: " + result2.getString(1));
     }
     // System.out.println(catagory.get(0));
     // System.out.println(catagory.get(1));
     con2.close();
    } catch (SQLException eh) {
     System.out.println("Connection Failed");
    }
   } else {
    addText("\nWallace: Sorry, I don't know about the thing that you are talking about");
   }
  } //else

  addText("\n");
 }

}