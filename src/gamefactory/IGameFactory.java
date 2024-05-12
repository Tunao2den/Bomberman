package gamefactory;

import gamesession.IGameSession;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IGameFactory extends Remote {
    public boolean register(String username, String password) throws RemoteException;
    public IGameSession login(String username, String password) throws RemoteException;
}
