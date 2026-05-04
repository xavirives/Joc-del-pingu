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
        java.util.List<String> nombres = new ArrayList<>();
        nombres.add("Jugador 1");
        nombres.add("Jugador 2");
        nuevaPartida(nombres);
    }

    public void nuevaPartida(String nombre1, String nombre2) {
        java.util.List<String> nombres = new ArrayList<>();
        nombres.add(nombre1);
        nombres.add(nombre2);
        nuevaPartida(nombres);
    }

    public void nuevaPartida(java.util.List<String> nombres) {
        partida = new Partida();
        ArrayList<Jugador> jugadores = new ArrayList<Jugador>();
        String[] colores = {"Azul", "Rojo", "Verde", "Amarillo"};
        
        for (int i = 0; i < nombres.size(); i++) {
            jugadores.add(new Pinguino(nombres.get(i), colores[i % colores.length], 0, new Inventario()));
        }
        
        partida.setJugadores(jugadores);
        partida.setUltimoEvento("Nueva partida creada con " + nombres.size() + " jugadores. Turno de " + nombres.get(0) + ".");
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

        if (!actual.getInv().removeCantidad("bola", 1)) {
            partida.setUltimoEvento("No tienes bolas de nieve.");
            return;
        }

        // Buscar el objetivo: el que está más cerca por delante, o si no hay nadie, el que va primero.
        Pinguino objetivo = null;
        int minDistanciaAdelante = Integer.MAX_VALUE;
        Pinguino primero = null;
        int maxPosicion = -1;

        for (Jugador j : partida.getJugadores()) {
            Pinguino p = (Pinguino) j;
            if (p == actual) continue;
            
            if (p.getPosicion() > maxPosicion) {
                maxPosicion = p.getPosicion();
                primero = p;
            }
            
            if (p.getPosicion() > actual.getPosicion()) {
                int dist = p.getPosicion() - actual.getPosicion();
                if (dist < minDistanciaAdelante) {
                    minDistanciaAdelante = dist;
                    objetivo = p;
                }
            }
        }

        if (objetivo == null) objetivo = primero; // Si va ganando, le da al que va más avanzado
        if (objetivo == null) objetivo = (Pinguino) partida.getJugadores().get(0 == partida.getJugadorActualIndice() ? 1 : 0); // Fallback

        objetivo.setPosicion(objetivo.getPosicion() - 3);
        limitarPosicion(objetivo);
        partida.setUltimoEvento(actual.getNombre() + " lanza una bola de nieve y " + objetivo.getNombre() + " retrocede 3 casillas.");
        siguienteTurno();
    }

    private void comprobarGuerra() {
        // Encontrar a todos los jugadores en la misma casilla
        ArrayList<Pinguino> implicados = new ArrayList<>();
        int maxPos = -1;
        
        for (Jugador j : partida.getJugadores()) {
            Pinguino p = (Pinguino) j;
            if (p.getPosicion() == 0) continue; // No hay guerra en la salida
            
            // Agrupar por posición
            boolean encontrado = false;
            for (Pinguino yaImplicado : implicados) {
                if (yaImplicado.getPosicion() == p.getPosicion()) {
                    implicados.add(p);
                    encontrado = true;
                    break;
                }
            }
            if (!encontrado && implicados.isEmpty()) {
                // Buscamos si hay otro en la misma posicion
                for (Jugador j2 : partida.getJugadores()) {
                    Pinguino p2 = (Pinguino) j2;
                    if (p != p2 && p.getPosicion() == p2.getPosicion()) {
                        implicados.add(p);
                        break;
                    }
                }
            } else if (!encontrado) {
                 // Otra pelea en otro lado? Solo procesamos una pelea por turno (la del jugador actual)
                 Pinguino actual = (Pinguino) partida.getJugadorActual();
                 if (p.getPosicion() == actual.getPosicion()) {
                     implicados.clear();
                     implicados.add(p);
                 }
            }
        }
        
        // Si hay una pelea donde está el jugador actual
        Pinguino actual = (Pinguino) partida.getJugadorActual();
        implicados.clear();
        for (Jugador j : partida.getJugadores()) {
             Pinguino p = (Pinguino) j;
             if (p.getPosicion() > 0 && p.getPosicion() == actual.getPosicion()) {
                 implicados.add(p);
             }
        }

        if (implicados.size() < 2) return;

        Pinguino ganador = null;
        int maxBolas = -1;
        boolean empate = false;

        for (Pinguino p : implicados) {
            int bolas = p.getInv().getCantidad("bola");
            if (bolas > maxBolas) {
                maxBolas = bolas;
                ganador = p;
                empate = false;
            } else if (bolas == maxBolas) {
                empate = true;
            }
        }

        // Vaciar bolas de todos los implicados
        for (Pinguino p : implicados) {
            p.getInv().vaciarBolas();
        }

        if (empate) {
            partida.setUltimoEvento("Guerra de nieve empatada. Todos en la casilla " + actual.getPosicion() + " pierden sus bolas de nieve.");
        } else {
            String perdedores = "";
            for (Pinguino p : implicados) {
                if (p != ganador) {
                    p.setPosicion(p.getPosicion() - maxBolas);
                    limitarPosicion(p);
                    perdedores += p.getNombre() + " ";
                }
            }
            partida.setUltimoEvento("Guerra de nieve: gana " + ganador.getNombre() + ". " + perdedores.trim() + " retroceden " + maxBolas + ".");
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
