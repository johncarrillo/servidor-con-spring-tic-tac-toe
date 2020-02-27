/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.ufps.tictactoe.socket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import co.edu.ufps.tictactoe.models.Partida;
import co.edu.ufps.tictactoe.models.Usuario;
import co.edu.ufps.tictactoe.util.Utils;

/**
 *
 * @author john
 */
public class HiloJugador implements Runnable{

    private Socket socket;
    private LinkedList<Socket> usuariosSocket;
    private OutputStreamWriter out;
    private InputStreamReader in;
    private Usuario usuario;
    private Partida partida;
    private Thread hilo;
    private Utils utils;
    private char[] cbuf;
    private String recibido;

    public HiloJugador(Socket soc, LinkedList<Socket> usuariosSocket, 
            Partida partida, List<Usuario> listaUsuario, OutputStreamWriter out, InputStreamReader in) throws IOException{
        socket = soc;
        utils = new Utils();
        this.in = in;
        this.out = out;
        cbuf = new char[512];
        this.usuariosSocket = usuariosSocket;
        this.partida = partida;
        //Metodo encargado de buscar el usuario correspondiente
        this.buscarUsuario(listaUsuario);
        System.out.println("3");
        out.write("\n--------------RANKING-----------------"
                + "\n" + utils.imprimirRanking() + "\n\n"
                + "TU PUNTAJE ES DE: " + this.usuario.getPuntaje());
        out.flush();
        this.validarPartida();
    }

    private void validarPartida () {
        if (this.partida.getUsuario1() == null) {
            this.partida.setUsuario1(usuario);
            this.partida.setSiguienteJugador(usuario);
            usuario.setMarca('X');
        } else if (this.partida.getUsuario2() == null) {
            this.partida.setUsuario2(usuario);
            if (this.partida.getUsuario1().getMarca() == 'X') {
                usuario.setMarca('O');
            } else {
                usuario.setMarca('X');
            }
        }
        this.hilo = new Thread(this);
        this.hilo.start();
    }
    /*
        metodo encargado de buscar al usuario para saber su puntaje,
        si no lo encuentra lo crea por defecto puntaje 0,
        los nombres de los usuarios son unicos
    */
    private void buscarUsuario(List<Usuario> listaUsuario) throws IOException {
        String nombreUsuario = null;
        boolean agregarNuevoUsuario = true;
        while (true) {
        	nombreUsuario = this.recibirSocket();
            agregarNuevoUsuario = true;
            for (Usuario usuario: listaUsuario) {
                if (usuario.getNombre().equals(nombreUsuario)) {
                    if (usuario.isJugando()) {
                        /*
                            Ya esta jugando en otro cliente con este mismo nombre
                        */
                    	out.write("false");
                        out.flush();
                        agregarNuevoUsuario = false;
                        break;
                    }
                    this.usuario = usuario;
                    usuario.setJugando(true);
                    return;
                }
            }
            if (agregarNuevoUsuario) {
                this.usuario = new Usuario(nombreUsuario);
                this.usuario.setMarca('X');
                listaUsuario.add(usuario);
                usuario.setJugando(true);
                return;
            }
        }
    }

    private void mostrarTablero ()  {
        try {
        	out.write(this.partida.mostrarTablero());
            out.flush();
        } catch (IOException e) {
            System.out.println("Se perdio la conexion con un usuario");
            this.matarHilo();
        }
    }

    private void mostrarTablero (String mensaje)  {
        try {
        	out.write(mensaje + "\n" + this.partida.mostrarTablero());
            out.flush();
        } catch (IOException e) {
            System.out.println("Se perdio la conexion con un usuario");
            this.matarHilo();
        }
    }

    private String mostrarEstadoPartida (boolean movimientoValido) {
        String estadoPartida = this.partida.tableroTransformado() + "\n";
        estadoPartida += (movimientoValido) + "\n";
        estadoPartida += (this.partida.getSiguienteJugador() == this.usuario) + "\n";
        if (this.partida.getJugadorGanador() != null &&
                this.partida.getJugadorGanador() == this.usuario) {
            estadoPartida += "1";
            this.partida.reiniciarPartida();
        } else if (this.partida.getJugadorGanador() != null &&
                this.partida.getJugadorGanador() != this.usuario) {
            estadoPartida += "-1";
            this.partida.reiniciarPartida();
        } else if (this.partida.tableroLleno()) {
            estadoPartida += "0";
            this.partida.reiniciarPartida();
        } else {
            estadoPartida += "";
        }
        return estadoPartida;
    }

    private String mostrarEstadoPartida () {
        String estadoPartida = this.partida.tableroTransformado() + "\n";
        if (this.usuario.getGanoPartida() != null) {
            estadoPartida += this.usuario.getGanoPartida();
            this.usuario.setGanoPartida(null);
            this.partida.setJugadorGanador(null);
        }
        return estadoPartida;
    }

    private boolean esperarMovimiento () throws IOException {
    	System.out.println("4");
    	this.out.write("Es tu TURNO");
        out.flush();
        System.out.println("5");
        return partida.agregarMovimiento(this.recibirSocket(), this.usuario.getMarca());
    }

    private void matarHilo() {
        usuario.setJugando(false);
        this.partida.removerUsuario(usuario);
        this.partida.reiniciarPartida();
        this.usuariosSocket.remove(this.socket);
        this.hilo.stop();
    }

    @Override
    public void run() {
        while (true) {
            if (this.partida.getUsuario2() == null) {
                System.out.println("Esperando a otro jugador...");
            } else if (this.partida.getSiguienteJugador() == this.usuario){
//                this.mostrarTablero();
            	System.out.println("ME TOCA");
                try {
                    /*
                        Muestra el estado de la partida cuando le seda el turno
                    */
                	System.out.println("1");
                	out.write(this.mostrarEstadoPartida());
                    out.flush();
                    System.out.println("2");
                    /*
                        Muestra el estado de la partida cuando termina el turno
                    */
                    System.out.println("3");
                    String respuesta = this.mostrarEstadoPartida(this.esperarMovimiento());
                    out.write(respuesta);
                    out.flush();
//                    this.mostrarTablero("Esperando el movimiento de tu compañero");
                } catch (IOException e) {
                    System.out.println("Se perdio la conexion con un usuario");
                    this.matarHilo();
                }
            }
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                System.out.println("Error en el Sleep");
            }
        }
    }

    private String recibirSocket () throws IOException {
        recibido = "";
        in.read(cbuf);
        for (char c : cbuf) {
            recibido += c;
            if (c == 00) {
                break;
            }
        }
        return recibido;
    }
}
