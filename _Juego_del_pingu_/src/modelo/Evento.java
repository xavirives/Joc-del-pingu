package modelo;

import java.util.Random;

public class Evento extends Casilla {
    private Random random = new Random();

    public Evento(int posicion) { super(posicion); }

    @Override public void realizarAccion(Partida partida, Pinguino jugador) {
        int evento = random.nextInt(6);
        Inventario inv = jugador.getInv();

        if (evento == 0) {
            inv.addCantidad("pez", 1);
            partida.setUltimoEvento(jugador.getNombre() + " obtiene un pez.");
        } else if (evento == 1) {
            int bolas = random.nextInt(3) + 1;
            inv.addCantidad("bola", bolas);
            partida.setUltimoEvento(jugador.getNombre() + " obtiene " + bolas + " bola(s) de nieve.");
        } else if (evento == 2) {
            if (inv.totalDadosEspeciales() < 3) {
                inv.addCantidad("rapido", 1);
                partida.setUltimoEvento(jugador.getNombre() + " obtiene un dado rápido.");
            } else {
                partida.setUltimoEvento(jugador.getNombre() + " no puede llevar más dados especiales.");
            }
        } else if (evento == 3) {
            if (inv.totalDadosEspeciales() < 3) {
                inv.addCantidad("lento", 1);
                partida.setUltimoEvento(jugador.getNombre() + " obtiene un dado lento.");
            } else {
                partida.setUltimoEvento(jugador.getNombre() + " no puede llevar más dados especiales.");
            }
        } else if (evento == 4) {
            jugador.setPierdeTurno(true);
            partida.setUltimoEvento(jugador.getNombre() + " pierde el próximo turno.");
        } else {
            inv.perderObjetoAleatorio();
            partida.setUltimoEvento(jugador.getNombre() + " pierde un objeto aleatorio.");
        }
    }
}
