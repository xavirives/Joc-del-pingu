package modelo;
/**
 * Representa el objeto pez.
 * El pez sirve para proteger al jugador de algunos peligros.
 */
public class Pez extends Item {

    /**
     * Crea un pez con un nombre y una cantidad.
     */
    public Pez(String nombre, int cantidad) { 
        super(nombre, cantidad); 
    }
}