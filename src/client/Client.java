package client;

import constants.Const;
import constants.Coordinate;
import database.DatabaseMockup;
import game.Game;
import game.Sprite;
import gamefactory.GameFactoryImpl;
import gamefactory.IGameFactory;
import gamesession.IGameSession;
import receiver.Receiver;
import sender.Sender;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

import javax.swing.*;

public class Client {
   private Socket socket = null;
   public static PrintStream out = null;
   public static Scanner in = null;
   public static int id;

   public final static int rateStatusUpdate = 115;
   public static Coordinate map[][] = new Coordinate[Const.LIN][Const.COL];

   public static Coordinate spawn[] = new Coordinate[Const.QTY_PLAYERS];
   public static boolean alive[] = new boolean[Const.QTY_PLAYERS];

   Client(String host, int porta) {
      try {
         System.out.print("Establishing connection with the server...");
         this.socket = new Socket(host, porta);
         out = new PrintStream(socket.getOutputStream(), true);  //To send to the server
         in = new Scanner(socket.getInputStream()); //To receive from the server
      } 
      catch (UnknownHostException e) {
         System.out.println(" error: " + e + "\n");
         System.exit(1);
      }
      catch (IOException e) {
         System.out.println(" error: " + e + "\n");
         System.exit(1);
      }
      System.out.print(" ok\n");
      
      receiveInitialSettings();
      new Receiver().start();
   }

   void receiveInitialSettings() {
      id = in.nextInt();

      //mapa
      for (int i = 0; i < Const.LIN; i++)
         for (int j = 0; j < Const.COL; j++)
            map[i][j] = new Coordinate(Const.SIZE_SPRITE_MAP * j, Const.SIZE_SPRITE_MAP * i, in.next());
      
      //situação (vivo ou morto) inicial de todos os jogadores
      for (int i = 0; i < Const.QTY_PLAYERS; i++)
         Client.alive[i] = in.nextBoolean();

      //coordenadas inicias de todos os jogadores
      for (int i = 0; i < Const.QTY_PLAYERS; i++)
         Client.spawn[i] = new Coordinate(in.nextInt(), in.nextInt());
   }
   
   public static void main(String[] args) {
      new Client("127.0.0.1", 8383);
      SwingUtilities.invokeLater(LoginFrame::new);
      boolean logged = true;
      if (logged == true) new Window();
   }
}

class Window extends JFrame {
   private static final long serialVersionUID = 1L;

   Window() {
      Sprite.loadImages();
      Sprite.setMaxLoopStatus();
      
      add(new Game(Const.COL*Const.SIZE_SPRITE_MAP, Const.LIN*Const.SIZE_SPRITE_MAP));
      setTitle("bomberman");
      pack();
      setVisible(true);
      setLocationRelativeTo(null);
      setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

      addKeyListener(new Sender());
   }
}

class LoginFrame extends JFrame implements ActionListener {
   public JTextField usernameField;
   public JPasswordField passwordField;

   public void actionPerformed(ActionEvent e) {
      String username = usernameField.getText();
      String password = new String(passwordField.getPassword());

      try {
         IGameFactory gameFactory = new GameFactoryImpl();
         DatabaseMockup databaseMockup = new DatabaseMockup();
         if(!databaseMockup.exists(username)){
            gameFactory.register(username,password);

         } else {
            IGameSession session = gameFactory.login(username,password);
            session.joinGame("",1);
         }

      } catch (NoSuchAlgorithmException ex) {
         throw new RuntimeException(ex);
      } catch (RemoteException ex) {
         throw new RuntimeException(ex);
      }
   }

   public LoginFrame() {
      setTitle("User Login");
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      setSize(300, 200);
      setLocationRelativeTo(null);

      JPanel panel = new JPanel();
      panel.setLayout(new GridLayout(3, 1));

      JLabel usernameLabel = new JLabel("Username:");
      usernameField = new JTextField();
      panel.add(usernameLabel);
      panel.add(usernameField);

      JLabel passwordLabel = new JLabel("Password:");
      passwordField = new JPasswordField();
      panel.add(passwordLabel);
      panel.add(passwordField);

      JButton loginButton = new JButton("Login");
      loginButton.addActionListener(this);
      panel.add(loginButton);

      add(panel);
      setVisible(true);
   }
}