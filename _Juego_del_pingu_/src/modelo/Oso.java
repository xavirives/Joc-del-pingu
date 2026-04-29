package modelo;

public class Oso extends Casilla {
    public Oso(int posicion) { super(posicion); }
    @Override public void realizarAccion(Partida partida, Pinguino jugador) {
        if (jugador.getInv().removeCantidad("pez", 1)) {
            partida.setUltimoEvento("Un oso aparece, pero " + jugador.getNombre() + " usa un pez y se salva.");
        } else {
            jugador.setPosicion(0);
            partida.setUltimoEvento("Un oso ataca a " + jugador.getNombre() + " y vuelve al inicio.");
        }
    }
}
