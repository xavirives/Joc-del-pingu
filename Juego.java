package model;

import java.util.ArrayList;
import java.util.List;

import model.casillas.Casilla;
import model.eventos.GestorEventos;

// Clase principal del modelo del juego
public class Juego {

    private String idPartida;
    private int maxCasillas;
    private boolean enMarcha;
    private Tablero tablero;
    private List<Jugador> jugadores;
    private GestorTurnos gestorTurnos;
    private GestorEventos gestorEventos;

    public Juego() {
        this.maxCasillas = 50;
        this.enMarcha = false;
        this.tablero = new Tablero();
        this.jugadores = new ArrayList<>();
        this.gestorTurnos = new GestorTurnos();
        this.gestorEventos = new GestorEventos();
    }

    public void iniciar() {
        tablero.generarAleatorio(maxCasillas);
        enMarcha = true;
    }

    public void agregarJugador(Jugador jugador) {
        jugadores.add(jugador);
    }

    public void jugarTurno(int pasos) {
        Jugador actual = gestorTurnos.jugadorActual(jugadores);

        if (actual.isTurnoPerdido()) {
            actual.setTurnoPerdido(false);
            System.out.println(actual.getNombre() + " pierde el turno.");
            gestorTurnos.siguienteTurno(jugadores);
            return;
        }

        actual.mover(pasos, tablero.getUltimaPosicion());
        System.out.println(actual.getNombre() + " avanza a la casilla " + actual.getPosicion());

        Casilla casilla = tablero.getCasilla(actual.getPosicion());
        casilla.activar(actual, this);
        resolverEncuentros(actual);

        if (actual.getPosicion() >= tablero.getUltimaPosicion()) {
            enMarcha = false;
            System.out.println("Ha ganado " + actual.getNombre());
        } else {
            gestorTurnos.siguienteTurno(jugadores);
        }
    }

    // Guerra entre jugadores cuando coinciden en una misma casilla
    private void resolverEncuentros(Jugador actual) {
        for (Jugador otro : jugadores) {
            if (otro != actual && otro.getPosicion() == actual.getPosicion()) {
                int bolasActual = actual.getInventario().getBolasNieve();
                int bolasOtro = otro.getInventario().getBolasNieve();

                actual.getInventario().setBolasNieve(0);
                otro.getInventario().setBolasNieve(0);

                if (bolasActual > bolasOtro) {
                    actual.mover(bolasActual - bolasOtro, tablero.getUltimaPosicion());
                    System.out.println(actual.getNombre() + " gana la guerra de bolas de nieve.");
                } else if (bolasOtro > bolasActual) {
                    otro.mover(bolasOtro - bolasActual, tablero.getUltimaPosicion());
                    System.out.println(otro.getNombre() + " gana la guerra de bolas de nieve.");
                } else {
                    System.out.println("Empate en la guerra de bolas de nieve.");
                }
            }
        }
    }

    public void usarBolaNieveContra(int idObjetivo) {
        Jugador atacante = gestorTurnos.jugadorActual(jugadores);
        if (!atacante.getInventario().usarBolas(1)) {
            System.out.println("No tiene bolas de nieve.");
            return;
        }

        for (Jugador objetivo : jugadores) {
            if (objetivo.getIdJugador() == idObjetivo) {
                objetivo.mover(-2, tablero.getUltimaPosicion());
                System.out.println(atacante.getNombre() + " lanza una bola de nieve a " + objetivo.getNombre());
                return;
            }
        }
    }

    public void moverAlAnteriorAgujero(Jugador jugador) {
        int destino = tablero.buscarAnteriorAgujero(jugador.getPosicion());
        jugador.setPosicion(destino);
    }

    public void moverAlSiguienteTrineo(Jugador jugador) {
        int destino = tablero.buscarSiguienteTrineo(jugador.getPosicion());
        jugador.setPosicion(destino);
    }

    public String getIdPartida() {
        return idPartida;
    }

    public void setIdPartida(String idPartida) {
        this.idPartida = idPartida;
    }

    public int getMaxCasillas() {
        return maxCasillas;
    }

    public void setMaxCasillas(int maxCasillas) {
        this.maxCasillas = Math.max(50, maxCasillas);
    }

    public boolean isEnMarcha() {
        return enMarcha;
    }

    public Tablero getTablero() {
        return tablero;
    }

    public List<Jugador> getJugadores() {
        return jugadores;
    }

    public GestorTurnos getGestorTurnos() {
        return gestorTurnos;
    }

    public GestorEventos getGestorEventos() {
        return gestorEventos;
    }
}
