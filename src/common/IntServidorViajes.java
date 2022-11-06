package common;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IntServidorViajes extends Remote {
    public JSONArray consultaViajes(String origen) throws RemoteException;
    public JSONObject reservaViaje(String codviaje, String codcliente) throws RemoteException;
    public JSONObject anulaReserva(String codviaje, String codcliente) throws RemoteException;
    public JSONObject ofertaViaje(String codprop, String origen, String destino, String fecha, long precio, long numplazas) throws RemoteException;
    public JSONObject borraViaje(String codviaje, String codcliente) throws RemoteException;
    public void cierraSesion( ) throws RemoteException;
    public void registrarNotificacion (String destino, IntCallbackCliente registroCallback ) throws RemoteException;
    public void eliminarNotificacion (String destino, IntCallbackCliente eliminarNotiCallBack ) throws RemoteException;



}
