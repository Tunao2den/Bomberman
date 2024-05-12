package gamesession;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface IGameSession extends Remote {
    public void logout() throws RemoteException;

    public String getOwnId() throws RemoteException;

    public boolean joinGame(String g, int commander) throws RemoteException;

    public ArrayList<String> getGames() throws RemoteException;//<GameLobby>
//
//    public GameLobby addGame(String map, String ID, int commander) throws RemoteException;
//
//
//    public GameLobby getGameIDfromLobby(String game) throws RemoteException;
}
