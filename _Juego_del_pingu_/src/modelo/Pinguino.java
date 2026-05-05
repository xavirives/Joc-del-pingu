package modelo;

import java.io.Serializable; 

public class Pinguino extends Jugador implements Serializable {
    private static final long serialVersionUID = 1L;

    public Pinguino(String nombre, String color, int posicion, Inventario inventario) {
        super(nombre, color, posicion);
        this.setInv(inventario);
    }
}
