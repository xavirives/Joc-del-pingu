package modelo;

public class Agujero extends Casilla {
    public Agujero(int posicion) { super(posicion); }
    @Override public void realizarAccion(Partida partida, Pinguino jugador) {
        int anterior = partida.getTablero().buscarAgujeroAnterior(posicion);
        jugador.setPosicion(anterior);
        if (anterior == 0) {
            partida.setUltimoEvento(jugador.getNombre() + " cae en el primer agujero y vuelve al inicio.");
        } else {
            partida.setUltimoEvento(jugador.getNombre() + " cae en un agujero y vuelve al agujero anterior.");
        }
    }
}
