package modelo;

import java.io.Serializable;
/*
 * Clase base para todas las casillas del tablero.
 * Cada tipo de casilla tendrá su propio efecto dentro del juego.
 */
public abstract class Casilla implements Serializable {

    // Posición que ocupa la casilla dentro del tablero.
    protected int posicion;

    /*
     * Crea una casilla en una posición concreta.
     */
    public Casilla(int posicion) { 
        this.posicion = posicion; 
    }

    // Devuelve la posición de la casilla.
    public int getPosicion() { 
        return posicion; 
    }

    // Cambia la posición de la casilla.
    public void setPosicion(int posicion) { 
        this.posicion = posicion; 
    }

    /*
     * Método que debe implementar cada casilla especial.
     * Define qué ocurre cuando un jugador cae en esta casilla.
     */
    public abstract void realizarAccion(Partida partida, Pinguino jugador);

    /*
     * Método vacío para evitar errores si se llama sin parámetros.
     * No aplica ningún efecto por defecto.
     */
    public void realizarAccion() { 
    }
}