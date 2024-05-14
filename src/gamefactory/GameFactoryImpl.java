package gamefactory;

import java.rmi.RemoteException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;
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
    private RSAPublicKey publicKey;
    private RSAPrivateKey privateKey;
    private HashMap<String, GameSessionImpl> sessions;



    public GameFactoryImpl() throws NoSuchAlgorithmException {
        super();
        this.databaseMockup = new DatabaseMockup();

        KeyPair kp = keyPairGenerator();
        this.publicKey = (RSAPublicKey) kp.getPublic();
        this.privateKey = (RSAPrivateKey) kp.getPrivate();

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
        if (!databaseMockup.exists(username)) {
            throw new RemoteException("User does not exist");
        }

        if (!databaseMockup.validateUser(username, password)) {
            throw new RemoteException("Invalid username or password");
        }

        User user = databaseMockup.getUser(username);
        String jwt = generateJWT(username);
        if (jwt == null) {
            throw new RemoteException("Failed to generate JWT");
        }
        user.setJwt(jwt);

        return sessions.computeIfAbsent(username, k -> new GameSessionImpl(this, username));
    }



    public String generateJWT(String username) {
        try {
            Algorithm algorithm = Algorithm.RSA256(publicKey, privateKey);
            return JWT.create()
                    .withIssuer("auth0")
                    .withSubject(username)
                    .withIssuedAt(new Date())
                    .withExpiresAt(new Date(System.currentTimeMillis() + 3600000)) // 1 saat geçerli
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Invalid Signing configuration / Couldn't convert Claims.", exception);
        }
    }



    public KeyPair keyPairGenerator () throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        return keyPairGenerator.generateKeyPair();
    }
}
