package modelo;

public class SueloQuebradizo extends Casilla {
    public SueloQuebradizo(int posicion) { super(posicion); }
    @Override public void realizarAccion(Partida partida, Pinguino jugador) {
        int objetos = jugador.getInv().totalObjetos();
        if (objetos > 5) {
            jugador.setPosicion(0);
            partida.setUltimoEvento("El suelo se rompe por llevar más de 5 objetos. " + jugador.getNombre() + " vuelve al inicio.");
        } else if (objetos > 0) {
            jugador.setPierdeTurno(true);
            partida.setUltimoEvento("El suelo quebradizo hace que " + jugador.getNombre() + " pierda el próximo turno.");
        } else {
            partida.setUltimoEvento(jugador.getNombre() + " pasa por suelo quebradizo sin penalización.");
        }
    }
}
