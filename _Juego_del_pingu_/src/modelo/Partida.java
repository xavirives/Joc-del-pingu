package modelo;

import java.util.ArrayList;
import java.io.Serializable;

/**
 * Representa una partida completa del juego.
 * Guarda el tablero, los jugadores, el turno actual,
 * el ganador y el último evento ocurrido.
 */
public class Partida implements Serializable {
	
    // Identificador necesario para poder guardar y cargar la partida correctamente.
    private static final long serialVersionUID = 1L;
	
    // Tablero donde se desarrolla la partida.
    private Tablero tablero;

    // Lista de jugadores que participan en la partida.
    private ArrayList<Jugador> jugadores;

    // Contador de turnos jugados.
    private int turnos;

    // Índice del jugador al que le toca jugar.
    private int jugadorActual;

    // Indica si la partida ha terminado o no.
    private boolean finalizada;

    // Guarda el jugador que ha ganado la partida.
    private Jugador ganador;

    // Guarda el último mensaje o evento ocurrido en la partida.
    private String ultimoEvento;

    /**
     * Constructor de la partida.
     * Inicializa el tablero, la lista de jugadores y los valores iniciales.
     */
    public Partida() {
        tablero = new Tablero();
        jugadores = new ArrayList<Jugador>();
        turnos = 0;
        jugadorActual = 0;
        finalizada = false;
        ganador = null;
        ultimoEvento = "Partida creada.";
    }

    // Devuelve el tablero de la partida.
    public Tablero getTablero() { 
        return tablero; 
    }

    // Cambia el tablero de la partida.
    public void setTablero(Tablero tablero) { 
        this.tablero = tablero; 
    }

    // Devuelve la lista de jugadores.
    public ArrayList<Jugador> getJugadores() { 
        return jugadores; 
    }

    // Cambia la lista de jugadores.
    public void setJugadores(ArrayList<Jugador> jugadores) { 
        this.jugadores = jugadores; 
    }

    // Devuelve el número de turnos jugados.
    public int getTurnos() { 
        return turnos; 
    }

    // Cambia el número de turnos jugados.
    public void setTurnos(int turnos) { 
        this.turnos = turnos; 
    }

    // Devuelve el índice del jugador actual.
    public int getJugadorActualIndice() { 
        return jugadorActual; 
    }

    // Cambia el índice del jugador actual.
    public void setJugadorActualIndice(int jugadorActual) { 
        this.jugadorActual = jugadorActual; 
    }

    // Indica si la partida está finalizada.
    public boolean isFinalizada() { 
        return finalizada; 
    }

    // Cambia el estado de finalización de la partida.
    public void setFinalizada(boolean finalizada) { 
        this.finalizada = finalizada; 
    }

    // Devuelve el jugador ganador.
    public Jugador getGanador() { 
        return ganador; 
    }

    // Guarda el jugador ganador.
    public void setGanador(Jugador ganador) { 
        this.ganador = ganador; 
    }

    // Devuelve el último evento ocurrido.
    public String getUltimoEvento() { 
        return ultimoEvento; 
    }

    // Cambia el último evento ocurrido.
    public void setUltimoEvento(String ultimoEvento) { 
        this.ultimoEvento = ultimoEvento; 
    }

    /**
     * Devuelve el jugador al que le toca jugar actualmente.
     * Si no hay jugadores, devuelve null para evitar errores.
     */
    public Jugador getJugadorActual() {
        if (jugadores == null || jugadores.isEmpty()) return null;
        return jugadores.get(jugadorActual);
    }

    /**
     * Devuelve el otro jugador de la partida.
     * Está pensado para partidas de 2 jugadores.
     */
    public Jugador getOtroJugador() {
        if (jugadores == null || jugadores.size() < 2) return null;

        // Si juega el jugador 0, devuelve el jugador 1.
        if (jugadorActual == 0) return jugadores.get(1);

        // Si juega el jugador 1, devuelve el jugador 0.
        return jugadores.get(0);
    }
}
