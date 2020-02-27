/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.ufps.tictactoe.models;

/**
 *
 * @author jjcarrillo
 */
public class EstadoPartida {
    
    private Partida partida;
    private boolean movimientoValido;
    private Usuario victoria;
    private boolean turno;
    private char[][] posicionesTablero;
    private String mensaje;
    private String tableApi;

    public void agregarParametros (boolean movimientoValido, Usuario usuario) {
        if (this.partida.getSiguienteJugador() == null) {
            this.mensaje = "Esperando otro jugador...";
            return;
        }
        this.turno = (this.partida.getSiguienteJugador().equals(usuario));
        this.movimientoValido = movimientoValido;
        this.posicionesTablero = this.partida.posiciones();
//        this.victoria =  this.partida.getJugadorGanador().equals(usuario);
        if (this.partida.getJugadorGanador() != null &&
                this.partida.getJugadorGanador().equals(usuario)) {
            this.victoria = this.partida.getJugadorGanador();
            this.partida.reiniciarPartida();
        } else {
            this.victoria = null;
        }
    }
    public EstadoPartida (Partida partida) {
        this.partida = partida;
    }

    public boolean isMovimientoValido() {
        return movimientoValido;
    }

    public void setMovimientoValido(boolean movimientoValido) {
        this.movimientoValido = movimientoValido;
    }

    public Usuario getVictoria() {
        return victoria;
    }

    public void setVictoria(Usuario victoria) {
        this.victoria = victoria;
    }

    public boolean isTurno() {
        return turno;
    }

    public void setTurno(boolean turno) {
        this.turno = turno;
    }

    public char[][] getPosicionesTablero() {
        return posicionesTablero;
    }

    public void setPosicionesTablero(char[][] posicionesTablero) {
        this.posicionesTablero = posicionesTablero;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getTableApi () {
    	return this.tableApi;
    }

    public void setTableApi (String tableApi) {
    	this.tableApi = tableApi;
    }
}
