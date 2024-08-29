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
import rmisetup.SetupRMI;
import sender.Sender;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

import javax.swing.*;
import client.UserLoginRegisterScreen1;

public class Client {
   private Socket socket = null;
   public static PrintStream out = null;
   public static Scanner in = null;
   public static int id;

   public final static int rateStatusUpdate = 115;
   public static Coordinate map[][] = new Coordinate[Const.LIN][Const.COL];

   public static Coordinate spawn[] = new Coordinate[Const.QTY_PLAYERS];
   public static boolean alive[] = new boolean[Const.QTY_PLAYERS];


   protected static IGameFactory gameFactory;

   private Registry gameFactoryRegistry;
   private SetupRMI rmiContext;



   Client(String host, int porta) throws NotBoundException, RemoteException {
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


      lookupGameFactory();
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

   public void lookupGameFactory() throws RemoteException, NotBoundException {
      try {
         this.rmiContext = new SetupRMI();
         Registry registry = rmiContext.getRegistry();

         String serviceUrl = rmiContext.getServicesUrl(0);


         IGameFactory gameFactory = (IGameFactory) registry.lookup(serviceUrl);

         this.gameFactory = gameFactory;

      } catch (RemoteException e) {
         System.err.println("RemoteException: " + e.getMessage());
         e.printStackTrace();
         throw e;
      } catch (UnknownHostException e) {
          throw new RuntimeException(e);
      }
   }

//   public boolean loginRegisterAPI(){
//      Runnable createAndShowGui = new Runnable() {
//         @Override
//         public void run() {
//            UserLoginRegisterScreen loginRegisterScreen = new UserLoginRegisterScreen(gameFactory);
//         }
//      };
//
//      // createAndShowGui runnable'ını invokeLater ile çalıştırarak GUI başlat
//      SwingUtilities.invokeLater(createAndShowGui);
//
//      if(loginRegisterScreen.loginperformed() || loginRegisterScreen.registerperformed() ) return true;
//      else return false;
//   }

   public static boolean loginRegisterAPI() {
      // Ana uygulama penceresini başlatmak için Swing'in Event Dispatch Thread'ini kullanıyoruz
      final UserLoginRegisterScreen1[] loginRegisterScreen = {null};

      Runnable createAndShowGui = () -> {
         // UserLoginRegisterScreen örneğini oluştur
         loginRegisterScreen[0] = new UserLoginRegisterScreen1(gameFactory);
      };

      // createAndShowGui runnable'ını invokeLater ile çalıştırarak GUI başlat
      SwingUtilities.invokeLater(createAndShowGui);

      while (loginRegisterScreen[0] == null || (!loginRegisterScreen[0].isLoginPerformed() && !loginRegisterScreen[0].isRegisterPerformed())) {
         try {
            Thread.sleep(100);  // Bekleme döngüsü
         } catch (InterruptedException e) {
            e.printStackTrace();
         }
      }

      return loginRegisterScreen[0].isLoginPerformed() || loginRegisterScreen[0].isRegisterPerformed();
   }



   
   public static void main(String[] args) throws NotBoundException, RemoteException {
      new Client("127.0.0.1", 8383);
      SwingUtilities.invokeLater(() -> new UserLoginRegisterScreen1(null));
      boolean st = loginRegisterAPI();
      if (st) new Window();
   }
}