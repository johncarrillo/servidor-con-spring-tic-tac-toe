/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.ufps.tictactoe.models;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Objects;

/**
 *
 * @author john
 */
public class Usuario implements Comparable<Usuario>{
    private String nombre;
    private int puntaje;
    private int victorias;
    private char marca;
    private Socket cliente;
    private DataOutputStream out;
    private DataInputStream in;
    private String ganoPartida;
    private boolean jugando;
    //El puerto debe ser el mismo en el que escucha el servidor
    private int puerto = 8090;
    //Si estamos en nuestra misma maquina usamos localhost si no la ip de la maquina servidor
    private String host = "localhost";

    public Usuario () throws IOException {
        this.nombre = "";
        this.puntaje = 0;
        this.victorias = 0;
    }
    
    public Usuario (String nombreDefecto) throws IOException {
        this.nombre = nombreDefecto.substring(0, nombreDefecto.length()-1);
        this.puntaje = 0;
        this.victorias = 0;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.equals("")) {
            return;
        }
        this.nombre = nombre;
    }

    public int getPuntaje() {
        return puntaje;
    }

    public void setPuntaje(int puntaje) {
        this.puntaje = puntaje;
    }

    public int getVictorias() {
        return victorias;
    }

    public void setVictorias(int victorias) {
        this.victorias = victorias;
    }

    public char getMarca() {
        return marca;
    }

    public void setMarca(char marca) {
        this.marca = marca;
    }

    public String getGanoPartida() {
        return ganoPartida;
    }

    public void setGanoPartida(String ganoPartida) {
        this.ganoPartida = ganoPartida;
    }

    public boolean isJugando() {
        return jugando;
    }

    public void setJugando(boolean jugando) {
        this.jugando = jugando;
    }

    @Override
    public int compareTo(Usuario o) {
        if (puntaje > o.getPuntaje()) {
            return -1;
        }
        if (puntaje < o.getPuntaje()) {
            return 1;
        }
        return 0;
    }

    @Override
    public String toString() {
        return "Usuario: " + nombre + ", puntaje=" + puntaje + ", victorias=" + victorias;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + Objects.hashCode(this.nombre);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Usuario other = (Usuario) obj;
        if (!Objects.equals(this.nombre, other.nombre)) {
            return false;
        }
        return true;
    }

    
}
