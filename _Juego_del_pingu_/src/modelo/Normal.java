package modelo;

public class Normal extends Casilla {
    public Normal(int posicion) { super(posicion); }
    @Override public void realizarAccion(Partida partida, Pinguino jugador) {
        partida.setUltimoEvento(jugador.getNombre() + " cae en una casilla normal.");
    }
}
