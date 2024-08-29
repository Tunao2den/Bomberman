package client;

import gamefactory.IGameFactory;
import gamesession.IGameSession;
import lombok.Getter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

public class UserLoginRegisterScreen1 extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;
    @Getter
    private boolean loginPerformed;
    @Getter
    private boolean registerPerformed;

    private IGameFactory gameFactory;

    public UserLoginRegisterScreen1(IGameFactory gameFactory) {
        this.gameFactory = gameFactory;
        this.loginPerformed = false;
        this.registerPerformed = false;

        setTitle("User Login/Register Screen");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(400, 200);
        setLayout(new GridLayout(3, 2));

        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField();
        loginButton = new JButton("Login");
        registerButton = new JButton("Register");

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    loginPerformed = performLogin();
                } catch (RemoteException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    registerPerformed = performRegister();
                } catch (RemoteException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        add(usernameLabel);
        add(usernameField);
        add(passwordLabel);
        add(passwordField);
        add(loginButton);
        add(registerButton);

        setVisible(true);
    }

    private boolean performLogin() throws RemoteException {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        try {
            IGameSession session = gameFactory.login(username, password);
            if (session != null) return true;
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        JOptionPane.showMessageDialog(this,
                "Username: " + username + "\nPassword: " + password,
                "Login", JOptionPane.INFORMATION_MESSAGE);
        return false;
    }

    private boolean performRegister() throws RemoteException {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        boolean registerStatus = false;

        try {
            registerStatus = gameFactory.register(username, password);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        JOptionPane.showMessageDialog(this,
                "New user registered:\nUsername: " + username + "\nPassword: " + password,
                "Register", JOptionPane.INFORMATION_MESSAGE);
        return registerStatus;
    }

    public static void main(String[] args) {
        //using Event Dispatch Thread from swing in order to start main app window
        SwingUtilities.invokeLater(() -> new UserLoginRegisterScreen1(null));
    }
}
