package modelo;

import java.io.Serializable; 

/**
 * Representa a un pingüino jugador.
 * Es el personaje que controla cada usuario durante la partida.
 */
public class Pinguino extends Jugador implements Serializable {

    // Identificador necesario para poder guardar y cargar el objeto correctamente.
    private static final long serialVersionUID = 1L;

    /**
     * Crea un pingüino con nombre, color, posición inicial e inventario.
     */
    public Pinguino(String nombre, String color, int posicion, Inventario inventario) {

        // Llama al constructor de Jugador para guardar nombre, color y posición.
        super(nombre, color, posicion);

        // Asigna el inventario propio del pingüino.
        this.setInv(inventario);
    }
}
