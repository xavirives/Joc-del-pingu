package modelo;

import java.util.Random;

public class Foca extends Jugador implements java.io.Serializable {
    private Random random = new Random();

    public Foca(String nombre, String color, int posicion) {
        super(nombre, color, posicion);
    }

    public void ejecutarIA(Partida partida) {
        int pasos = random.nextInt(4) + 1;
        int nuevaPos = this.getPosicion() + pasos;
        int casillaMeta = 49; 

        if (nuevaPos >= casillaMeta) {
            this.setPosicion(casillaMeta);
            partida.setFinalizada(true);
            partida.setGanador(this);
            partida.setUltimoEvento("¡La " + this.getNombre() + " ha llegado a la meta y ha ganado!");
            return;
        } else {
            this.setPosicion(nuevaPos);
        }

        Pinguino objetivo = null;
        for (Jugador j : partida.getJugadores()) {
            if (j instanceof Pinguino && j.getPosicion() == this.getPosicion()) {
                objetivo = (Pinguino) j;
                break;
            }
        }

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

