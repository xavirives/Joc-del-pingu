package modelo;

/** Jugador principal del juego. */
public class Pinguino extends Jugador {
    public Pinguino(String nombre, String color, int posicion, Inventario inventario) {
        super(nombre, color, posicion);
        this.setInv(inventario);
    }
}
