package modelo;

import java.util.Random;

/** CPU/Foca con IA para el Nivel Avanzado. */
public class Foca extends Jugador implements java.io.Serializable {
    private Random random = new Random();

    public Foca(String nombre, String color, int posicion) {
        super(nombre, color, posicion);
    }

    /** Lógica de la IA: Ataca a jugadores o interactúa con inventarios. */
    public void ejecutarIA(Partida partida) {
        // 1. Movimiento básico de la CPU (entre 1 y 4 casillas)
        int pasos = random.nextInt(4) + 1;
        this.setPosicion(this.getPosicion() + pasos);

        // 2. Buscar objetivo en la misma casilla
        Pinguino objetivo = null;
        for (Jugador j : partida.getJugadores()) {
            if (j instanceof Pinguino && j.getPosicion() == this.getPosicion()) {
                objetivo = (Pinguino) j;
                break;
            }
        }

        // 3. Interacción
        if (objetivo != null) {
            if (objetivo.getInv().removeCantidad("pez", 1)) {
                partida.setUltimoEvento("¡La Foca te ha pillado! Pero se ha comido tu pez y te deja en paz.");
            } else {
                objetivo.getInv().perderObjetoAleatorio();
                objetivo.setPosicion(Math.max(0, objetivo.getPosicion() - 4));
                partida.setUltimoEvento("¡ATAQUE DE FOCA! " + objetivo.getNombre() + " retrocede 4 casillas y pierde un objeto.");
            }
        } else {
            partida.setUltimoEvento("La foca se ha movido a la casilla " + this.getPosicion());
        }
    }
}

