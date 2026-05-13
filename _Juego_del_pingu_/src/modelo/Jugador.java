package modelo;

import java.io.Serializable;

/**
 * Clase base que representa a un jugador del juego.
 * Guarda su nombre, color, posición, inventario y si pierde turno.
 */
public class Jugador implements Serializable {

    // Posición actual del jugador en el tablero.
    protected int posicion;

    // Nombre del jugador.
    protected String nombre;

    // Color que identifica al jugador en el tablero.
    protected String color;

    // Indica si el jugador debe saltarse su próximo turno.
    protected boolean pierdeTurno;

    // Inventario del jugador con sus objetos.
    protected Inventario inv; 

    /**
     * Crea un jugador con nombre, color y posición inicial.
     */
    public Jugador(String nombre, String color, int posicion) {
        this.nombre = nombre;
        this.color = color;
        this.posicion = posicion;
        this.pierdeTurno = false;
        this.inv = new Inventario();
    }

    // Devuelve la posición actual del jugador.
    public int getPosicion() { 
        return posicion; 
    }

    // Cambia la posición del jugador, evitando posiciones negativas.
    public void setPosicion(int posicion) { 
        this.posicion = Math.max(0, posicion); 
    }

    // Devuelve el nombre del jugador.
    public String getNombre() { 
        return nombre; 
    }

    // Cambia el nombre del jugador.
    public void setNombre(String nombre) { 
        this.nombre = nombre; 
    }

    // Devuelve el color del jugador.
    public String getColor() { 
        return color; 
    }
    // Cambia el color del jugador.
    public void setColor(String color) { 
        this.color = color; 
    }
    // Indica si el jugador pierde el turno.
    public boolean isPierdeTurno() { 
        return pierdeTurno; 
    }
    // Cambia el estado de pérdida de turno.
    public void setPierdeTurno(boolean pierdeTurno) { 
        this.pierdeTurno = pierdeTurno; 
    }
    // Devuelve el inventario del jugador.
    public Inventario getInv() { 
        return inv; 
    }
    // Cambia el inventario del jugador.
    public void setInv(Inventario inv) { 
        this.inv = inv; 
    }
}
