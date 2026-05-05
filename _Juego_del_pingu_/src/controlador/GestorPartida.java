package controlador;

import java.util.ArrayList;
import java.util.Random;
import modelo.*;

/** 
 * Controlador central del juego.
 * Gestiona dados, inventario, turnos de la foca y persistencia en Oracle.
 */
public class GestorPartida {
    private Partida partida;
    private GestorBBDD gestorBBDD = new GestorBBDD();
    private Random random = new Random();

    public void nuevaPartida(java.util.List<String> nombres, boolean incluyeFoca) {
        partida = new Partida();
        ArrayList<Jugador> jugadores = new ArrayList<>();
        String[] colores = {"#3b82f6", "#ef4444", "#10b981", "#f59e0b"}; 
        
        for (int i = 0; i < nombres.size(); i++) {
            jugadores.add(new Pinguino(nombres.get(i), colores[i % colores.length], 0, new Inventario()));
        }
        
        if (incluyeFoca) {
            jugadores.add(new Foca("Foca Ártica", "#6b7280", 0));
        }
        
        partida.setJugadores(jugadores);
        partida.setUltimoEvento("¡Partida iniciada! Que gane el mejor.");
    }

    // Método para el dado normal
    public int usarDadoNormal() {
        return usarDado("normal");
    }

    // Método genérico para todos los dados (Soluciona el error en PantallaJuego)
    public int usarDado(String tipo) {
        if (partida == null || partida.isFinalizada()) return 0;
        Pinguino actual = (Pinguino) partida.getJugadorActual();
        
        int resultado = 0;
        if (tipo.equals("normal")) {
            resultado = random.nextInt(6) + 1;
        } else if (tipo.equals("rapido")) {
            if (actual.getInv().removeCantidad("rapido", 1)) {
                resultado = random.nextInt(6) + 5; 
            }
        } else if (tipo.equals("lento")) {
            if (actual.getInv().removeCantidad("lento", 1)) {
                resultado = random.nextInt(3) + 1; 
            }
        }

        if (resultado > 0) {
            moverJugador(actual, resultado);
            if (!partida.isFinalizada()) siguienteTurno();
        }
        return resultado;
    }

    // Método para lanzar bolas de nieve (Soluciona el error en PantallaJuego)
    public void usarBolaDeNieve() {
        if (partida == null || partida.isFinalizada()) return;
        Pinguino actual = (Pinguino) partida.getJugadorActual();
        
        if (actual.getInv().removeCantidad("bola", 1)) {
            Jugador objetivo = null;
            int maxPos = -1;
            for (Jugador j : partida.getJugadores()) {
                if (j != actual && j.getPosicion() > maxPos) {
                    maxPos = j.getPosicion();
                    objetivo = j;
                }
            }
            if (objetivo != null) {
                objetivo.setPosicion(Math.max(0, objetivo.getPosicion() - 3));
                partida.setUltimoEvento(actual.getNombre() + " lanzó nieve a " + objetivo.getNombre());
            }
            siguienteTurno();
        }
    }

    private void moverJugador(Jugador j, int pasos) {
        int nuevaPos = j.getPosicion() + pasos;
        int ultima = 49;
        
        if (nuevaPos >= ultima) {
            j.setPosicion(ultima);
            partida.setFinalizada(true);
            partida.setGanador(j);
        } else {
            j.setPosicion(nuevaPos);
            Casilla c = partida.getTablero().getCasillas().get(j.getPosicion());
            if (j instanceof Pinguino) {
                c.realizarAccion(partida, (Pinguino)j);
            }
        }
    }

    public void siguienteTurno() {
        int sig = (partida.getJugadorActualIndice() + 1) % partida.getJugadores().size();
        partida.setJugadorActualIndice(sig);
        
        if (partida.getJugadorActual() instanceof Foca && !partida.isFinalizada()) {
            ((Foca) partida.getJugadorActual()).ejecutarIA(partida);
            siguienteTurno();
        }
    }

    public void guardarPartida() { gestorBBDD.guardarBBDD(partida); }
    public void cargarPartida() { 
        Partida p = gestorBBDD.cargarBBDD(1); 
        if (p != null) this.partida = p; 
    }
    public Partida getPartida() { return partida; }
}
