package co.edu.ufps.tictactoe.controllers;

import co.edu.ufps.tictactoe.JugadorApi.JugadorApi;
import co.edu.ufps.tictactoe.models.EstadoPartida;
import co.edu.ufps.tictactoe.models.Usuario;
import co.edu.ufps.tictactoe.socket.ServidorSocket;
import co.edu.ufps.tictactoe.util.Utils;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class IndexController {
	@Autowired
	private ServidorSocket socket;
	
	@GetMapping({"/index", "/"})
	public String index(Model model) throws IOException {
		model.addAttribute("titulo", "Proyecto Tic Tac Toe!");
		Utils utils = socket.getUtils();
		model.addAttribute("ranking", utils.calcularRanking());
		return "index";
	}

	@GetMapping("/estado_partida")
    public String consultarEstado(Model model, @RequestParam String usuario) throws IOException {
    	Utils util = socket.getUtils();
        JugadorApi jugadorApi = new JugadorApi(util);
        JugadorApi jugadorApiAux = new JugadorApi(jugadorApi.agregarUsuario(usuario), util);
        jugadorApiAux.buscarUsuario(usuario);
//        System.out.println(jugadorApiAux.validarPartida(new Usuario()));
        jugadorApiAux.getEstadoPartida().getPosicionesTablero();
        String cadena = "";
        boolean primerValor = true;
        int i = 0;
        for (char[] x: jugadorApiAux.getEstadoPartida().getPosicionesTablero()) {
        	int j = 0;
            for (char y: x ) {
                if (y == 0) {
                	y = ' ';
                }System.out.println(y);
                model.addAttribute("c" + i + "" + j, y);
                cadena += y;
                primerValor = false;
                j++;
            }
            i++;
        }
        jugadorApiAux.getEstadoPartida().setTableApi(cadena);
        
        model.addAttribute("nombre", usuario);
        model.addAttribute("mensaje", jugadorApiAux.getEstadoPartida().getMensaje());
        model.addAttribute("victoria", jugadorApiAux.getEstadoPartida().getVictoria());
        model.addAttribute("movimientoValido", jugadorApiAux.getEstadoPartida().isMovimientoValido());
        model.addAttribute("turno", jugadorApiAux.getEstadoPartida().isTurno());
        model.addAttribute("bandera", true);
        
        return "partida";
	//return new Greeting(counter.incrementAndGet(), String.format(template, name));
    }

	@GetMapping("/movimiento")
    public String jugador(Model model, @RequestParam String movimiento, @RequestParam String usuario ) throws IOException {
        System.out.println("111111111111");
		System.out.println(usuario + "  " + movimiento.substring(0, 1) + " - "+  movimiento.substring(1, 2));
        Utils util = socket.getUtils();
        JugadorApi jugadorApi = new JugadorApi(util);
        JugadorApi jugadorApiAux = new JugadorApi(jugadorApi.agregarUsuario(usuario), util);
        jugadorApiAux.buscarUsuario(usuario);
//        System.out.println(jugadorApiAux.validarPartida(new Usuario()));
        jugadorApiAux.agregarMovimiento(movimiento.substring(1, 2), movimiento.substring(0, 1) );
        model.addAttribute("nombre", usuario);
        model.addAttribute("mensaje", jugadorApiAux.getEstadoPartida().getMensaje());
        model.addAttribute("victoria", jugadorApiAux.getEstadoPartida().getVictoria());
        model.addAttribute("movimientoValido", jugadorApiAux.getEstadoPartida().isMovimientoValido());
        model.addAttribute("turno", jugadorApiAux.getEstadoPartida().isTurno());
        model.addAttribute("bandera", true);
        String cadena = "";
        boolean primerValor = true;
        int i = 0;
        for (char[] x: jugadorApiAux.getEstadoPartida().getPosicionesTablero()) {
        	int j = 0;
            for (char y: x ) {
                if (y == 0) {
                	y = ' ';
                }System.out.println(y);
                model.addAttribute("c" + i + "" + j, y);
                cadena += y;
                primerValor = false;
                j++;
            }
            i++;
        }
        return "partida";
	//return new Greeting(counter.incrementAndGet(), String.format(template, name));
    }
}
