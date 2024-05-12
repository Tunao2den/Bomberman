package gamefactory;

import java.rmi.RemoteException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import database.DatabaseMockup;
import database.User;
import gamesession.GameSessionImpl;
import gamesession.IGameSession;


public class GameFactoryImpl implements IGameFactory{

    private DatabaseMockup databaseMockup;
    private RSAPublicKey publickey;
    private RSAPrivateKey privatekey;
    private HashMap<String, GameSessionImpl> sessions;



    public GameFactoryImpl() throws NoSuchAlgorithmException {
        super();
        this.databaseMockup = new DatabaseMockup();

        KeyPair kp = keyPairGenerator();
        this.publickey = (RSAPublicKey) kp.getPublic();
        this.privatekey = (RSAPrivateKey) kp.getPrivate();

        this.sessions = new HashMap<>();
    }


    @Override
    public boolean register(String username, String password) throws RemoteException {
        if(databaseMockup.exists(username)) return false;
        databaseMockup.register(username, password);
        return true;
    }

    @Override
    public IGameSession login(String username, String password) throws RemoteException {
        if (databaseMockup.exists(username) && databaseMockup.validateUser(username, password)){
            if (sessions.containsKey(username)) {
                User user = databaseMockup.getUser(username);
                user.setJwt(generateJWT(username));

                return sessions.get(username);
            } else {
                GameSessionImpl session = new GameSessionImpl(this, username);
                User user = databaseMockup.getUser(username);
                user.setJwt(generateJWT(username));
                sessions.put(username, session);
                return session;
            }
        }
        return null;
    }


    public String generateJWT(String username) {
        try {
            Algorithm algorithm = Algorithm.RSA256(publickey,privatekey);
            return JWT.create().withIssuer(username).sign(algorithm);
        } catch (JWTCreationException exception){
            System.out.println("Invalid Signing configuration / Couldn't convert Claims.");
        }
        return null;
    }


    public KeyPair keyPairGenerator () throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        return keyPairGenerator.generateKeyPair();
    }
}
