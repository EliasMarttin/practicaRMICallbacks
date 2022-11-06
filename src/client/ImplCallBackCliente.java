package client;

import common.IntCallbackCliente;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ImplCallBackCliente extends UnicastRemoteObject implements IntCallbackCliente {
    public ImplCallBackCliente() throws RemoteException {
        super();
    }

    @Override
    public void notificame(String mensaje) throws RemoteException {
        System.out.println();
        System.out.println(mensaje);
    }
}
