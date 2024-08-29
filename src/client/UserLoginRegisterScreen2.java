//package client;
//
//import gamefactory.IGameFactory;
//import gamesession.IGameSession;
//
//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.rmi.RemoteException;
//
//    public class UserLoginRegisterScreen2 extends JPanel {
//        private JTextField usernameField;
//        private JPasswordField passwordField;
//        private JButton loginButton;
//        private JButton registerButton;
//        private boolean operationSuccessful;
//        private IGameFactory gameFactory;
//        private JDialog dialog;
//
//        public UserLoginRegisterScreen2(IGameFactory gameFactory) {
//            this.gameFactory = gameFactory;
//            this.operationSuccessful = false;
//
//            setLayout(new GridLayout(3, 2));
//
//            JLabel usernameLabel = new JLabel("Username:");
//            usernameField = new JTextField();
//            JLabel passwordLabel = new JLabel("Password:");
//            passwordField = new JPasswordField();
//            loginButton = new JButton("Login");
//            registerButton = new JButton("Register");
//
//            loginButton.addActionListener(new ActionListener() {
//                @Override
//                public void actionPerformed(ActionEvent e) {
//                    try {
//                        operationSuccessful = performLogin();
//                        if (operationSuccessful && dialog != null) {
//                            dialog.dispose();
//                        }
//                    } catch (RemoteException ex) {
//                        throw new RuntimeException(ex);
//                    }
//                }
//            });
//
//            registerButton.addActionListener(new ActionListener() {
//                @Override
//                public void actionPerformed(ActionEvent e) {
//                    try {
//                        operationSuccessful = performRegister();
//                        if (operationSuccessful && dialog != null) {
//                            dialog.dispose();
//                        }
//                    } catch (RemoteException ex) {
//                        throw new RuntimeException(ex);
//                    }
//                }
//            });
//
//            add(usernameLabel);
//            add(usernameField);
//            add(passwordLabel);
//            add(passwordField);
//            add(loginButton);
//            add(registerButton);
//        }
//
//        private boolean performLogin() throws RemoteException {
//            String username = usernameField.getText();
//            String password = new String(passwordField.getPassword());
//
//            try {
//                IGameSession session = gameFactory.login(username, password);
//                if (session != null) return true;
//            } catch (RemoteException e) {
//                e.printStackTrace();
//            }
//
//            JOptionPane.showMessageDialog(this,
//                    "Username: " + username + "\nPassword: " + password,
//                    "Login", JOptionPane.INFORMATION_MESSAGE);
//            return false;
//        }
//
//        private boolean performRegister() throws RemoteException {
//            String username = usernameField.getText();
//            String password = new String(passwordField.getPassword());
//            boolean registerStatus = false;
//
//            try {
//                registerStatus = gameFactory.register(username, password);
//            } catch (RemoteException e) {
//                e.printStackTrace();
//            }
//
//            JOptionPane.showMessageDialog(this,
//                    "New user registered:\nUsername: " + username + "\nPassword: " + password,
//                    "Register", JOptionPane.INFORMATION_MESSAGE);
//            return registerStatus;
//        }
//
//        public boolean isOperationSuccessful() {
//            return operationSuccessful;
//        }
//
//        public void setDialog(JDialog dialog) {
//            this.dialog = dialog;
//        }
//
//        public static void main(String[] args) {
//            // Ana uygulama penceresini başlatmak için Swing'in Event Dispatch Thread'ini kullanıyoruz
//            SwingUtilities.invokeLater(() -> new UserLoginRegisterScreen2(null));
//        }
//    }
//
//}
