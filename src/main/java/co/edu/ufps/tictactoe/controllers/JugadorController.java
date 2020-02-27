package co.edu.ufps.tictactoe.controllers;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import co.edu.ufps.tictactoe.JugadorApi.JugadorApi;
import co.edu.ufps.tictactoe.models.EstadoPartida;
import co.edu.ufps.tictactoe.models.Partida;
import co.edu.ufps.tictactoe.models.Usuario;
import co.edu.ufps.tictactoe.socket.ServidorSocket;
import co.edu.ufps.tictactoe.util.Utils;
import java.util.List;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
public class JugadorController {

    @Autowired
	private ServidorSocket socket;

    @PostMapping("/iniciar")
    public ModelAndView iniciar(@RequestParam String nombre) throws IOException {
        System.out.println(nombre);
        Utils util = socket.getUtils();
        JugadorApi jugadorApi = new JugadorApi(util);
        JugadorApi jugadorApiAux = new JugadorApi(jugadorApi.agregarUsuario(nombre), util);
        Usuario usuario = new Usuario();
        usuario.setNombre(nombre);
        jugadorApiAux.validarPartida(usuario);
        jugadorApiAux.getPartida();
        return new ModelAndView("redirect:http://localhost:8080/estado_partida?usuario=" + nombre);
	//return new Greeting(counter.incrementAndGet(), String.format(template, name));
    }

    @PutMapping("/movimiento")
    public EstadoPartida jugador(@RequestParam String usuario, @RequestParam String x, @RequestParam String y) throws IOException {
        System.out.println(usuario + "  " + x + " - " + y);
        Utils util = socket.getUtils();
        JugadorApi jugadorApi = new JugadorApi(util);
        JugadorApi jugadorApiAux = new JugadorApi(jugadorApi.agregarUsuario(usuario), util);
        jugadorApiAux.buscarUsuario(usuario);
//        System.out.println(jugadorApiAux.validarPartida(new Usuario()));
        return jugadorApiAux.agregarMovimiento(x, y);
	//return new Greeting(counter.incrementAndGet(), String.format(template, name));
    }

    @GetMapping("/estado_partidas")
    public EstadoPartida consultarEstado(@RequestParam String usuario) throws IOException {
    	Utils util = socket.getUtils();
        JugadorApi jugadorApi = new JugadorApi(util);
        JugadorApi jugadorApiAux = new JugadorApi(jugadorApi.agregarUsuario(usuario), util);
        jugadorApiAux.buscarUsuario(usuario);
//        System.out.println(jugadorApiAux.validarPartida(new Usuario()));
        jugadorApiAux.getEstadoPartida().getPosicionesTablero();
        String cadena = "";
        boolean primerValor = true;
        for (char[] x: jugadorApiAux.getEstadoPartida().getPosicionesTablero()) {
            for (char y: x ) {
                if (!primerValor) {
                    cadena += ",";
                }
                if (y == 0) {
                    y = '-';
                }
                cadena += y;
                primerValor = false;
            }
        }
        jugadorApiAux.getEstadoPartida().setTableApi(cadena);
        
        return jugadorApiAux.getEstadoPartida();
	//return new Greeting(counter.incrementAndGet(), String.format(template, name));
    }

    @GetMapping("/ranking")
    public List<Usuario> cerrar() throws IOException {
    	Utils util = socket.getUtils();
        System.out.println(util.calcularRanking());
        return util.calcularRanking();
    }

    @PutMapping("/cerrar")
    public EstadoPartida cerrar(@RequestParam String usuario) throws IOException {
    	Utils util = socket.getUtils();
        JugadorApi jugadorApi = new JugadorApi(util);
        JugadorApi jugadorApiAux = new JugadorApi(jugadorApi.agregarUsuario(usuario), util);
        jugadorApiAux.buscarUsuario(usuario);
//        System.out.println(jugadorApiAux.validarPartida(new Usuario()));
        return jugadorApiAux.getEstadoPartida();
	//return new Greeting(counter.incrementAndGet(), String.format(template, name));
    }
}
