package modelo;

import java.util.Random;

/**
 * Representa un dado del juego.
 * Hereda de Item porque es un objeto que puede estar en el inventario.
 */
public class Dado extends Item {

    // Valor mínimo que puede salir al tirar el dado.
    private int min;

    // Valor máximo que puede salir al tirar el dado.
    private int max;

    /**
     * Crea un dado indicando su nombre, cantidad y rango de valores.
     */
    public Dado(String nombre, int cantidad, int min, int max) {
        super(nombre, cantidad);
        this.min = min;
        this.max = max;
    }

    /**
     * Devuelve un número aleatorio entre el mínimo y el máximo.
     */
    public int tirar(Random r) { 
        return r.nextInt((max - min) + 1) + min; 
    }

    // Devuelve el valor máximo del dado.
    public int getMax() { 
        return max; 
    }

    // Cambia el valor máximo del dado.
    public void setMax(int max) { 
        this.max = max; 
    }

    // Devuelve el valor mínimo del dado.
    public int getMin() { 
        return min; 
    }

    // Cambia el valor mínimo del dado.
    public void setMin(int min) { 
        this.min = min; 
    }
}