package modelo;

import java.io.Serializable;

/** Casilla base del tablero. */
public abstract class Casilla implements Serializable{
    protected int posicion;

    public Casilla(int posicion) { this.posicion = posicion; }
    public int getPosicion() { return posicion; }
    public void setPosicion(int posicion) { this.posicion = posicion; }

    /** Ejecuta el efecto de la casilla sobre el jugador actual. */
    public abstract void realizarAccion(Partida partida, Pinguino jugador);

    /** Método vacío mantenido por compatibilidad con la plantilla inicial. */
    public void realizarAccion() { }
}
