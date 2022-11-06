package client;

import common.IntCallbackCliente;
import common.IntServidorViajes;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import server.GestorViajes;
import server.ImplServidorViajes;

import javax.swing.*;
import java.io.IOException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;


public class ClienteViajesRMI {



        /**
         * Muestra el menu de opciones y lee repetidamente de teclado hasta obtener una opcion válida
         * @param teclado	stream para leer la opción elegida de teclado
         * @return			opción elegida
         */
        public static int menu(Scanner teclado) {
            int opcion;
            System.out.println("\n\n");
            System.out.println("=====================================================");
            System.out.println("============            MENU        =================");
            System.out.println("=====================================================");
            System.out.println("0. Salir");
            System.out.println("1. Consultar viajes con un origen dado");
            System.out.println("2. Reservar un viaje");
            System.out.println("3. Anular una reserva");
            System.out.println("4. Ofertar un viaje");
            System.out.println("5. Borrar un viaje");
            do {
                System.out.print("\nElige una opcion (0..5): ");
                opcion = teclado.nextInt();
            } while ( (opcion<0) || (opcion>5) );
            teclado.nextLine(); // Elimina retorno de carro del buffer de entrada
            return opcion;
        }


        /**
         * Programa principal. Muestra el menú repetidamente y atiende las peticiones del cliente.
         *
         * @param args	no se usan argumentos de entrada al programa principal
         */
        public static void main(String[] args) throws IOException, NotBoundException {

            Scanner teclado = new Scanner(System.in);
            String hostName = "localhost";
            int RMIPortNum = 1099;
            String registryURL = "rmi://" + hostName + ":" + RMIPortNum + "/viaje";
            // Crea un gestor de viajes
            IntServidorViajes gestor = (IntServidorViajes) Naming.lookup(registryURL);

            System.out.print("Introduce tu codigo de cliente: ");
            String codcli = teclado.nextLine();
            ImplCallBackCliente callback = new ImplCallBackCliente();

            int opcion;
            do {
                opcion = menu(teclado);
                switch (opcion) {
                    case 0: // Guardar los datos en el fichero y salir del programa
                        // TODO

                        gestor.cierraSesion();
                        System.out.println("Datos Almacenado correctamente :) ");
                        System.exit(0);

                        break;

                    case 1: { // Consultar viajes con un origen dado
                        //TODO

                        System.out.println("Introduce el origen");
                        String origen = teclado.nextLine();
                        JSONArray viajesOfertados = gestor.consultaViajes(origen);

                        if(viajesOfertados.size()==0) {
                            System.out.println("No se ha encontrado viajes desde este origen");
                            System.out.println("¿Quieres que se te notifique cuando haya viajes disponibles?\nS = sí, N = No ");
                            String decision = teclado.nextLine();
                            if(decision.equalsIgnoreCase("S")){
                                gestor.registrarNotificacion(origen,callback);
                            }
                        }else {
                            System.out.println("Viajes disponibles" + "\n");
                            for(Object viaje: viajesOfertados)
                                System.out.println(viaje.toString());
                        }
                        break;
                    }

                    case 2: { // Reservar un viaje
                        //TODO

                        System.out.println("Por favor introduce el código del viaje que quieres hacer la reserva ");
                        String codViajeReserva = teclado.nextLine();
                        JSONObject viaje = gestor.reservaViaje(codViajeReserva,codcli);
                        if(viaje.size()==0){
                            System.out.println("Error en los datos de la reserva ");

                        }
                        else
                            System.out.println("Datos del viaje reservado " + viaje.toJSONString());

                        break;
                    }

                    case 3: { // Anular una reserva
                        // TODO

                        System.out.println("Introduce el código del viaje que quieres anular la reserva.");
                        String codViajeAnular = teclado.nextLine();
                        JSONObject viaje = gestor.anulaReserva(codViajeAnular, codcli);
                        if(viaje.size()!=0)
                            System.out.println("Datos de la reserva anulada ->" + viaje.toJSONString());
                        else
                            System.out.println("Error al intentar anular la reserva ");
                        break;
                    }

                    case 4: { // Ofertar un viaje
                        // TODO

                        System.out.println("Por favor rellene los datos que se le pide para reservar un viaje. ");
                        System.out.println("Introduce el Origen");
                        String origenOferta = teclado.nextLine();
                        System.out.println("Introduce el destino ");
                        String destinoOferta = teclado.nextLine();
                        System.out.println("Introduce la fecha con formato dd-MM-yyyy");
                        String fechaOferta = teclado.nextLine();
                        System.out.println("Introduce el Precio en €");
                        long precioOferta = teclado.nextLong();
                        System.out.println("Introduce el número de plazas");
                        long plazasOferta = teclado.nextLong();
                        JSONObject viajeOfertado = gestor.ofertaViaje(codcli, origenOferta, destinoOferta, fechaOferta, precioOferta, plazasOferta);

                        if(viajeOfertado.size() != 0)
                            System.out.println("Datos del viaje Ofertado ->" + viajeOfertado.toJSONString());
                        else
                            System.out.println("Error en los datos proporcionados ");

                        break;
                    }

                    case 5: { // Borrar un viaje ofertado
                        // TODO

                        System.out.println("Introduce el código del viaje que quieres borrar ");
                        String codViajeBorrar = teclado.nextLine();
                        JSONObject viaje = gestor.borraViaje(codViajeBorrar, codcli);
                        if(viaje.size()==0)
                            System.out.println("Error al intentar borrar el viaje ");
                        else
                            System.out.println("Datos del viaje borrado ->" + viaje.toJSONString());

                        break;
                    }
                    case 6: { // quitarse de que me notifiquen

                        // TODO

                        System.out.println("Introduce el destino del que no quieras recibir más notificaciones");
                        String destino = teclado.nextLine();
                        gestor.eliminarNotificacion(destino,callback);

                    }
                } // fin switch


            } while (opcion != 0);

        } // fin de main


} // fin class

