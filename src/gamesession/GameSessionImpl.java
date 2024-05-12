package gamesession;

import gamefactory.GameFactoryImpl;

import java.rmi.RemoteException;
import java.util.ArrayList;

public class GameSessionImpl implements IGameSession{

    private GameFactoryImpl gameFactory;

    private String username;

    public GameSessionImpl(GameFactoryImpl gameFactory, String username) {
        super();
        this.gameFactory = gameFactory;
        this.username = username;
    }


    @Override
    public void logout() throws RemoteException {

    }

    @Override
    public String getOwnId() throws RemoteException {
        return null;
    }

    @Override
    public boolean joinGame(String s, int id) throws RemoteException {
        return false;
    }

    @Override
    public ArrayList<String> getGames() throws RemoteException {
        return null;
    }
}
