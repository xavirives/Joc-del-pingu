package modelo;

public class Trineo extends Casilla {
    public Trineo(int posicion) { super(posicion); }
    @Override public void realizarAccion(Partida partida, Pinguino jugador) {
        int siguiente = partida.getTablero().buscarTrineoSiguiente(posicion);
        if (siguiente > posicion) {
            jugador.setPosicion(siguiente);
            partida.setUltimoEvento(jugador.getNombre() + " usa un trineo y avanza hasta el siguiente trineo.");
        } else {
            partida.setUltimoEvento(jugador.getNombre() + " cae en el último trineo y no avanza más.");
        }
    }
}
