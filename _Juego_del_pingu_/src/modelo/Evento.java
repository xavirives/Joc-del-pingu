package modelo;

import java.util.Random;

/**
 * Casilla especial de evento.
 * Cuando un jugador cae aquí, ocurre un evento aleatorio.
 */
public class Evento extends Casilla {

    // Se usa para generar eventos al azar.
    private Random random = new Random();

    /**
     * Crea una casilla de evento en una posición concreta del tablero.
     */
    public Evento(int posicion) { 
        super(posicion); 
    }

    /**
     * Aplica un evento aleatorio al jugador que cae en esta casilla.
     */
    @Override 
    public void realizarAccion(Partida partida, Pinguino jugador) {

        // Genera un número del 0 al 5 para elegir el evento.
        int evento = random.nextInt(6);

        // Obtiene el inventario del jugador.
        Inventario inv = jugador.getInv();

        if (evento == 0) {

            // El jugador obtiene un pez.
            inv.addCantidad("pez", 1);
            partida.setUltimoEvento(jugador.getNombre() + " obtiene un pez.");

        } else if (evento == 1) {

            // El jugador obtiene entre 1 y 3 bolas de nieve.
            int bolas = random.nextInt(3) + 1;
            inv.addCantidad("bola", bolas);
            partida.setUltimoEvento(jugador.getNombre() + " obtiene " + bolas + " bola(s) de nieve.");

        } else if (evento == 2) {

            // El jugador obtiene un dado rápido si no supera el límite.
            if (inv.totalDadosEspeciales() < 3) {
                inv.addCantidad("rapido", 1);
                partida.setUltimoEvento(jugador.getNombre() + " obtiene un dado rápido.");
            } else {
                partida.setUltimoEvento(jugador.getNombre() + " no puede llevar más dados especiales.");
            }

        } else if (evento == 3) {

            // El jugador obtiene un dado lento si no supera el límite.
            if (inv.totalDadosEspeciales() < 3) {
                inv.addCantidad("lento", 1);
                partida.setUltimoEvento(jugador.getNombre() + " obtiene un dado lento.");
            } else {
                partida.setUltimoEvento(jugador.getNombre() + " no puede llevar más dados especiales.");
            }

        } else if (evento == 4) {

            // El jugador pierde el siguiente turno.
            jugador.setPierdeTurno(true);
            partida.setUltimoEvento(jugador.getNombre() + " pierde el próximo turno.");

        } else {

            // El jugador pierde un objeto aleatorio del inventario.
            inv.perderObjetoAleatorio();
            partida.setUltimoEvento(jugador.getNombre() + " pierde un objeto aleatorio.");
        }
    }
}
