package controlador;

import java.util.ArrayList;
import java.util.Random;

import modelo.*;

/** Controla la lógica principal de la partida. */
public class GestorPartida {
    private Partida partida;
    private GestorTablero gestorTablero;
    private GestorJugador gestorJugador;
    private GestorBBDD gestorBBDD;
    private Random random;

    public GestorPartida() {
        gestorTablero = new GestorTablero();
        gestorJugador = new GestorJugador();
        gestorBBDD = new GestorBBDD();
        random = new Random();
    }

    public void nuevaPartida() {
        nuevaPartida("Jugador 1", "Jugador 2");
    }

    public void nuevaPartida(String nombre1, String nombre2) {
        partida = new Partida();
        ArrayList<Jugador> jugadores = new ArrayList<Jugador>();
        jugadores.add(new Pinguino(nombre1, "Azul", 0, new Inventario()));
        jugadores.add(new Pinguino(nombre2, "Rojo", 0, new Inventario()));
        partida.setJugadores(jugadores);
        partida.setUltimoEvento("Nueva partida creada. Turno de " + nombre1 + ".");
    }

    public int usarDadoNormal() {
        return usarDado("normal");
    }

    public int usarDado(String tipo) {
        if (partida == null || partida.isFinalizada()) return 0;
        Pinguino actual = (Pinguino) partida.getJugadorActual();

        if (actual.isPierdeTurno()) {
            actual.setPierdeTurno(false);
            partida.setUltimoEvento(actual.getNombre() + " pierde este turno.");
            siguienteTurno();
            return 0;
        }

        int resultado = 0;
        if (tipo.equals("normal")) {
            resultado = random.nextInt(6) + 1;
        } else if (tipo.equals("rapido")) {
            if (!actual.getInv().removeCantidad("rapido", 1)) {
                partida.setUltimoEvento("No tienes dado rápido.");
                return 0;
            }
            resultado = random.nextInt(6) + 5;
        } else if (tipo.equals("lento")) {
            if (!actual.getInv().removeCantidad("lento", 1)) {
                partida.setUltimoEvento("No tienes dado lento.");
                return 0;
            }
            resultado = random.nextInt(3) + 1;
        }

        moverJugador(actual, resultado);
        comprobarGuerra();
        comprobarVictoria(actual);
        if (!partida.isFinalizada()) siguienteTurno();
        return resultado;
    }

    private void moverJugador(Pinguino jugador, int pasos) {
        gestorJugador.jugadorSeMueve(jugador, pasos, partida.getTablero());
        Casilla casilla = partida.getTablero().getCasillas().get(jugador.getPosicion());
        gestorTablero.ejecutarCasilla(partida, jugador, casilla);
        limitarPosicion(jugador);
    }

    public void usarBolaDeNieve() {
        if (partida == null || partida.isFinalizada()) return;
        Pinguino actual = (Pinguino) partida.getJugadorActual();
        Pinguino otro = (Pinguino) partida.getOtroJugador();

        if (!actual.getInv().removeCantidad("bola", 1)) {
            partida.setUltimoEvento("No tienes bolas de nieve.");
            return;
        }

        otro.setPosicion(otro.getPosicion() - 3);
        limitarPosicion(otro);
        partida.setUltimoEvento(actual.getNombre() + " lanza una bola de nieve y " + otro.getNombre() + " retrocede 3 casillas.");
        siguienteTurno();
    }

    private void comprobarGuerra() {
        Pinguino j1 = (Pinguino) partida.getJugadores().get(0);
        Pinguino j2 = (Pinguino) partida.getJugadores().get(1);
        if (j1.getPosicion() == 0 || j1.getPosicion() != j2.getPosicion()) return;

        int bolas1 = j1.getInv().getCantidad("bola");
        int bolas2 = j2.getInv().getCantidad("bola");
        j1.getInv().vaciarBolas();
        j2.getInv().vaciarBolas();

        if (bolas1 > bolas2) {
            int diferencia = bolas1 - bolas2;
            j2.setPosicion(j2.getPosicion() - diferencia);
            limitarPosicion(j2);
            partida.setUltimoEvento("Guerra de nieve: gana " + j1.getNombre() + ". " + j2.getNombre() + " retrocede " + diferencia + ".");
        } else if (bolas2 > bolas1) {
            int diferencia = bolas2 - bolas1;
            j1.setPosicion(j1.getPosicion() - diferencia);
            limitarPosicion(j1);
            partida.setUltimoEvento("Guerra de nieve: gana " + j2.getNombre() + ". " + j1.getNombre() + " retrocede " + diferencia + ".");
        } else {
            partida.setUltimoEvento("Guerra de nieve empatada. Ambos pierden sus bolas de nieve.");
        }
    }

    private void comprobarVictoria(Pinguino jugador) {
        int ultima = partida.getTablero().getCasillas().size() - 1;
        if (jugador.getPosicion() >= ultima) {
            jugador.setPosicion(ultima);
            partida.setFinalizada(true);
            partida.setGanador(jugador);
            partida.setUltimoEvento(jugador.getNombre() + " ha ganado la partida.");
        }
    }

    private void limitarPosicion(Pinguino jugador) {
        int ultima = partida.getTablero().getCasillas().size() - 1;
        if (jugador.getPosicion() < 0) jugador.setPosicion(0);
        if (jugador.getPosicion() > ultima) jugador.setPosicion(ultima);
    }

    public void siguienteTurno() {
        if (partida == null || partida.isFinalizada()) return;
        int siguiente = partida.getJugadorActualIndice() + 1;
        if (siguiente >= partida.getJugadores().size()) siguiente = 0;
        partida.setJugadorActualIndice(siguiente);
        partida.setTurnos(partida.getTurnos() + 1);
    }

    public Partida getPartida() { return partida; }

    public void guardarPartida() {
        gestorBBDD.guardarBBDD(partida);
        if (partida != null) partida.setUltimoEvento("Plantilla de guardado BBDD llamada. La BBDD se implementará más adelante.");
    }

    public void cargarPartida(int id) {
        Partida cargada = gestorBBDD.cargarBBDD(id);
        if (cargada != null) partida = cargada;
        else if (partida != null) partida.setUltimoEvento("No hay carga real todavía: GestorBBDD queda como plantilla.");
    }
}
