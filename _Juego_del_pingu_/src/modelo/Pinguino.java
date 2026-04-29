package modelo;

/** Jugador principal del juego. */
public class Pinguino extends Jugador {
    private Inventario inv;

    public Pinguino(String nombre, String color, int posicion, Inventario inventario) {
        super(nombre, color, posicion);
        this.inv = inventario;
    }

    public Inventario getInv() { return inv; }
    public void setInv(Inventario inv) { this.inv = inv; }
}
