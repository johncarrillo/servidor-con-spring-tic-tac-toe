package co.edu.ufps.tictactoe.JugadorApi;

import co.edu.ufps.tictactoe.models.EstadoPartida;
import java.io.IOException;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.edu.ufps.tictactoe.models.Partida;
import co.edu.ufps.tictactoe.models.Usuario;
import co.edu.ufps.tictactoe.socket.ServidorSocket;
import co.edu.ufps.tictactoe.util.Utils;


public class JugadorApi {
    private Usuario usuario;
    private Partida partida;
    private List<Usuario> listaUsuario;
    private Utils utils;
    private String tableApi;
    
    public JugadorApi (Utils utils) {
    	this.utils = utils;
    	this.listaUsuario = utils.getListaUsuario();
	}

	public JugadorApi (Partida partida, Utils utils) {
		this.utils = utils;
		this.partida = partida;
		listaUsuario = utils.getListaUsuario();
		// this.validarPartida();
	}

	public boolean buscarUsuario(String nombre) {
		try {
			/*
			 * Si retrna true se añadio con exito de lo contrario el jugador ya se encuentra jugando
			 * */
			return buscarUsuario(this.listaUsuario, nombre);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	/*
    metodo encargado de buscar al usuario para saber su puntaje,
    si no lo encuentra lo crea por defecto puntaje 0,
    los nombres de los usuarios son unicos
*/
	private boolean buscarUsuario(List<Usuario> listaUsuario, String nombreUsuario) throws IOException {
	    boolean agregarNuevoUsuario = true;
	    while (true) {
	        agregarNuevoUsuario = true;
	        for (Usuario usuario: listaUsuario) {
	            if (usuario.getNombre().equals(nombreUsuario)) {
//	                if (usuario.isJugando()) {
//	                    /*
//	                        Ya esta jugando en otro cliente con este mismo nombre
//	                    */
//	                	// this.partida = utils.buscarPartida(nombreUsuario);
//	                    return true;
//	                }
	                this.usuario = usuario;
	                usuario.setJugando(true);
	                return true;
	            }
	        }
	        if (agregarNuevoUsuario) {
	            this.usuario = new Usuario(nombreUsuario);
	            this.usuario.setMarca('X');
	            listaUsuario.add(usuario);
	            usuario.setJugando(true);
	            return true;
	        }
	    }
	}

	public boolean validarPartida (Usuario usuario) {
        if (this.partida.getUsuario1() == null) {
            if (usuario.equals(this.partida.getUsuario2())) {
                return true;
            }
            this.usuario = usuario;
            this.usuario.setJugando(true);
            this.partida.setUsuario1(usuario);
            this.partida.setSiguienteJugador(usuario);
            this.usuario.setMarca('X');
            this.partida.toString();
            this.listaUsuario.add(usuario);
            return true;
        } else if (this.partida.getUsuario2() == null) {
            if (usuario.equals(this.partida.getUsuario1())) {
                return true;
            }
            usuario.setJugando(true);
            if (this.partida.getUsuario1().getMarca() == 'X') {
                usuario.setMarca('O');
            } else {
                usuario.setMarca('X');
            }
            this.partida.setUsuario2(usuario);
            this.partida.toString();
            this.listaUsuario.add(usuario);
            return true;
        }
        this.partida.toString();
        return false;
    }

	public Partida agregarUsuario(String nombreUsuario) {
		Partida partida = utils.buscarPartida(nombreUsuario);
		if (partida == null) {
                    System.out.println("añadio una nueva partida");
			partida = new Partida();
			utils.agregarPartida(partida);
			return partida;
		}
                System.out.println("no añadio una nueva partida");
		return partida;
	}

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Partida getPartida() {
        return partida;
    }

    public void setPartida(Partida partida) {
        this.partida = partida;
    }

    public Utils getUtils() {
        return utils;
    }

    public void setUtils(Utils utils) {
        this.utils = utils;
    }

    public List<Usuario> getListaUsuario() {
        return listaUsuario;
    }

    public void setListaUsuario(List<Usuario> listaUsuario) {
        this.listaUsuario = listaUsuario;
    }


    public EstadoPartida agregarMovimiento (String x, String y) {
        boolean movimientoValido = false;
        String mensaje = null;
        if (partida.validarAmbosJugadores()) {
            if (!x.equals("") && !y.equals("") && this.usuario != null && usuario.equals(partida.getSiguienteJugador())) {
                movimientoValido = partida.agregarMovimiento(Integer.parseInt(x),
                        Integer.parseInt(y), this.usuario.getMarca());
                mensaje = "Movimiento correcto";
            }
            if (movimientoValido) {
                if (usuario.equals(partida.getUsuario1())) {
                    partida.setSiguienteJugador(partida.getUsuario2());
                } else if (usuario.equals(partida.getUsuario2())) {
                    partida.setSiguienteJugador(partida.getUsuario1());
                }
            } else {
                mensaje = "Movimiento Ilegal, intentalo de nuevo";
            }
            EstadoPartida estadoPartida = this.partida.getEstadoPartida(movimientoValido, usuario);
            estadoPartida.setMensaje(mensaje);
            return estadoPartida;
        }
        EstadoPartida estadoPartida = this.partida.getEstadoPartida(false, usuario);
        estadoPartida.setMensaje("Esperando otro Usuario para empezar a jugar...");
        return estadoPartida;
    }

    public EstadoPartida getEstadoPartida() {
        return this.partida.getEstadoPartida(false, usuario);
    }

}
