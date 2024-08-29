package rmisetup;

import lombok.Getter;

import java.lang.reflect.Array;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SetupRMI {
    @Getter
    private Registry registry;
    private InetAddress localHostInetAddress;
    private String localHostName;
    private String localHostAddress;

//    private final Class subsystemClass;
    private final String registryHostIP = "127.0.0.1";
    private final int registryHostPort = 1099;
//    private final String serviceNames[];
    private final String serviceUrls[] = new String[20];
//    private final Logger logger;

    public SetupRMI() throws UnknownHostException, RemoteException {
        getLocalHostContext();

        for (int i = 0; i < this.serviceUrls.length; i++) {
            serviceUrls[i] = "rmi://" + this.registryHostIP + ":" + this.registryHostPort + "/service" + i;
        }

        setupRegistryContext();
    }

    public String getServicesUrl(int i) {
        return (i < this.serviceUrls.length ? serviceUrls[i] : null);
    }


    private void getLocalHostContext() throws UnknownHostException {
        try {
            localHostInetAddress = InetAddress.getLocalHost();
            localHostName = localHostInetAddress.getHostName();
            localHostAddress = localHostInetAddress.getHostAddress();

            InetAddress[] allLocalInetAddresses = InetAddress.getAllByName(localHostName);
            for (InetAddress addr : allLocalInetAddresses) {
                System.out.println("localhost inet address: " + addr);
            }
        } catch (UnknownHostException e){
            e.getMessage();
        }
    }


    private void setupRegistryContext() throws RemoteException {
        registry = LocateRegistry.getRegistry(this.registryHostIP, this.registryHostPort);
        if (registry != null) {
            //List available services
            String[] registriesList = registry.list();
            System.out.println("registry list length: " + registriesList.length);

            for (int i = 0; i < registriesList.length; i++) {
                System.out.println("registry[" + i + "] :" + registriesList[i]);
            }
        } else {
            System.out.println("registry is null!!");
        }
    }

}