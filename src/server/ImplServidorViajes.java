package server;

import common.IntCallbackCliente;
import common.IntServidorViajes;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class ImplServidorViajes extends UnicastRemoteObject implements IntServidorViajes {
    private static FileWriter os;			// stream para escribir los datos en el fichero
    private static FileReader is;			// stream para leer los datos del fichero

    /**
     * 	Diccionario para manejar los datos en memoria.
     * 	La clave es el codigo único del viaje.
     */
    private static HashMap<String, Viaje> mapa;
    private static HashMap<String, List<IntCallbackCliente>> objRemotes;
    static GestorViajes gestor;

    /**
     * Constructor del gestor de viajes
     * Crea o Lee un fichero con datos de prueba
     */
    public ImplServidorViajes() throws RemoteException {
        mapa =  new HashMap<String, Viaje>();
        objRemotes = new HashMap<>();
        gestor = new GestorViajes();
    }


    @Override
    public JSONArray consultaViajes(String origen) throws RemoteException {
        return gestor.consultaViajes(origen);
    }

    @Override
    public JSONObject reservaViaje(String codviaje, String codcliente) throws RemoteException {
        return gestor.reservaViaje(codviaje, codcliente);
    }

    @Override
    public JSONObject anulaReserva(String codviaje, String codcliente) throws RemoteException {
        return gestor.anulaReserva(codviaje, codcliente);
    }

    @Override
    public JSONObject ofertaViaje(String codprop, String origen, String destino, String fecha, long precio, long numplazas) throws RemoteException {
        this.notificarClientes(origen);
        return gestor.ofertaViaje(codprop, origen, destino, fecha, precio, numplazas);

    }

    @Override
    public JSONObject borraViaje(String codviaje, String codcliente) throws RemoteException {
        return gestor.borraViaje(codviaje, codcliente);
    }

    @Override
    public void cierraSesion() throws RemoteException {
        gestor.guardaDatos();
    }

    @Override
    public void registrarNotificacion(String destino, IntCallbackCliente registroCallback) throws RemoteException {
        String mayusDestino = destino.toUpperCase();
        if (!objRemotes.containsKey(mayusDestino)) {
            objRemotes.put(mayusDestino, new ArrayList<>());

        }
        objRemotes.get(mayusDestino).add(registroCallback);
        registroCallback.notificame("Registro correcto, se te avisará cuando haya viajes :)");

    }

    @Override
    public void eliminarNotificacion(String destino, IntCallbackCliente eliminarNotiCallBack) throws RemoteException {
        String mayusDestino = destino.toUpperCase();
        if(objRemotes.containsKey(mayusDestino)){
            if(objRemotes.get(mayusDestino).contains(eliminarNotiCallBack)){
                objRemotes.get(mayusDestino).remove(eliminarNotiCallBack);
                try {
                    eliminarNotiCallBack.notificame("Te has eliminado correctamente, no te llegarán mas notificaciones");
                    } catch (Exception ignored) {
                    }
            } else {
                eliminarNotiCallBack.notificame("No te puedo eliminar si no estás apuntado...");
            }
        }else {
            eliminarNotiCallBack.notificame("Acción no válida");
        }

    }
    public void notificarClientes(String destino ) throws RemoteException {
        String mayusDestino = destino.toUpperCase();
        List<IntCallbackCliente> clientes = objRemotes.get(mayusDestino);

            try {
                for (IntCallbackCliente cliente : clientes)
                    try {
                        cliente.notificame("Amigo que ya hay viajes desde " + destino + " jejejejeje, aprovecha la oferta ; ) ");

                    } catch (Exception e) {
                        eliminarNotificacion(destino, cliente);
                        System.out.println("Se ha eliminado al cliente con referencia " + cliente);
                    }
            }catch (Exception ignored ){

            }

    }
}
