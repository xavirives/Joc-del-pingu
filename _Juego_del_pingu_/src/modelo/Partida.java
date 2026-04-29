package modelo;

import java.util.ArrayList;

/** Guarda el estado completo de la partida. */
public class Partida {
    private Tablero tablero;
    private ArrayList<Jugador> jugadores;
    private int turnos;
    private int jugadorActual;
    private boolean finalizada;
    private Jugador ganador;
    private String ultimoEvento;

    public Partida() {
        tablero = new Tablero();
        jugadores = new ArrayList<Jugador>();
        turnos = 0;
        jugadorActual = 0;
        finalizada = false;
        ganador = null;
        ultimoEvento = "Partida creada.";
    }

    public Tablero getTablero() { return tablero; }
    public void setTablero(Tablero tablero) { this.tablero = tablero; }
    public ArrayList<Jugador> getJugadores() { return jugadores; }
    public void setJugadores(ArrayList<Jugador> jugadores) { this.jugadores = jugadores; }
    public int getTurnos() { return turnos; }
    public void setTurnos(int turnos) { this.turnos = turnos; }
    public int getJugadorActualIndice() { return jugadorActual; }
    public void setJugadorActualIndice(int jugadorActual) { this.jugadorActual = jugadorActual; }
    public boolean isFinalizada() { return finalizada; }
    public void setFinalizada(boolean finalizada) { this.finalizada = finalizada; }
    public Jugador getGanador() { return ganador; }
    public void setGanador(Jugador ganador) { this.ganador = ganador; }
    public String getUltimoEvento() { return ultimoEvento; }
    public void setUltimoEvento(String ultimoEvento) { this.ultimoEvento = ultimoEvento; }

    public Jugador getJugadorActual() {
        if (jugadores == null || jugadores.isEmpty()) return null;
        return jugadores.get(jugadorActual);
    }

    public Jugador getOtroJugador() {
        if (jugadores == null || jugadores.size() < 2) return null;
        if (jugadorActual == 0) return jugadores.get(1);
        return jugadores.get(0);
    }
}
