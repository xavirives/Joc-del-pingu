package modelo;
/**
 * Representa el objeto bola de nieve.
 * Este objeto se usa para atacar a otros jugadores y hacerlos retroceder.
 */
public class BolaDeNieve extends Item {
    /**
     * Crea una bola de nieve con un nombre y una cantidad.
     */
    public BolaDeNieve(String nombre, int cantidad) { 
        super(nombre, cantidad); 
    }
}