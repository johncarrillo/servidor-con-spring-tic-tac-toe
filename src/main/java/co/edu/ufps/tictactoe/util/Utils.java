/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.ufps.tictactoe.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.stereotype.Component;

import co.edu.ufps.tictactoe.models.Partida;
import co.edu.ufps.tictactoe.models.Usuario;

/**
 *
 * @author johnjairo
 */
@Component
public class Utils {
    private static List<Usuario> listaUsuario;
    private static List<Partida> partidas;
    private Usuario[] listaOrdenada;

    public Utils() {
    	if (this.listaUsuario == null) {
    		this.listaUsuario = new ArrayList<Usuario>();
    	}
    	if (this.partidas == null) {
    		this.partidas = new ArrayList<Partida>();
    	}
    }

    public Utils(Usuario usuario) {
    	this.listaUsuario.add(usuario);
    }	

    public Utils(List<Usuario> listaUsuario) {
        this.listaUsuario = listaUsuario;
    }

    public List<Usuario> calcularRanking () {
        this.listaOrdenada = new Usuario[listaUsuario.size()];
        this.listaOrdenada = listaUsuario.toArray(this.listaOrdenada);
        Arrays.sort(this.listaOrdenada);
        List<Usuario> listaUsuario = new ArrayList<Usuario>();
        for (Usuario usuario: this.listaOrdenada) {
            listaUsuario.add(usuario);
        }
        return listaUsuario;
    }

    public String imprimirRanking () {
        String rankingPrint = "";
        for (Usuario usuario: calcularRanking()) {
            rankingPrint += usuario.toString() + "\n";
        }
        return rankingPrint;
    }

    public List<Usuario> getListaUsuario () {
    	return this.listaUsuario;
    }

    public void setUsuario (Usuario usuario) {
    	this.listaUsuario.add(usuario);
    }

    public Partida buscarPartida (String nombre) {
    	Partida partida = null;
    	for (Partida par: this.partidas) {
                if (par.getUsuario1() == null) {
    			partida = par;
    		} else if (par.getUsuario2() == null) {
    			partida = par;
    		} else if (par.getUsuario1().getNombre().equals(nombre)) {
    			partida = par;
    			break;
    		} else if (par.getUsuario2().getNombre().equals(nombre)) {
    			partida = par;
    			break;
    		} 
    	}
    	return partida;
    }
    
    public void agregarPartida (Partida partida) {
    	this.partidas.add(partida);
    }

    public List<Partida> getListaPartidas () {
    	return this.partidas;
    }
}
