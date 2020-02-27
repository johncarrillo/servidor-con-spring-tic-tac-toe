package co.edu.ufps.tictactoe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import co.edu.ufps.tictactoe.socket.ServidorSocket;

@SpringBootApplication
public class TicTocTacServeApplication extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure (SpringApplicationBuilder builder) {
		return builder.sources(TicTocTacServeApplication.class);
	}
	public static void main(String[] args) {
		SpringApplication.run(TicTocTacServeApplication.class, args);
		(new ServidorSocket()).escuchar();
	}

}
