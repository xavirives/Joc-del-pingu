package modelo;

import java.io.Serializable;

/**
 * Clase base para los objetos del juego.
 * Sirve para representar elementos del inventario.
 */
public class Item implements Serializable {

    // Nombre del objeto, por ejemplo: pez, bola o dado.
    private String nombre;

    // Cantidad disponible de ese objeto.
    private int cantidad;

    /**
     * Crea un objeto con un nombre y una cantidad inicial.
     */
    public Item(String nombre, int cantidad) {
        this.nombre = nombre;
        this.cantidad = cantidad;
    }

    // Devuelve el nombre del objeto.
    public String getNombre() { 
        return nombre; 
    }

    // Cambia el nombre del objeto.
    public void setNombre(String nombre) { 
        this.nombre = nombre; 
    }

    // Devuelve la cantidad del objeto.
    public int getCantidad() { 
        return cantidad; 
    }

    // Cambia la cantidad, evitando que sea menor que 0.
    public void setCantidad(int cantidad) { 
        this.cantidad = Math.max(0, cantidad); 
    }
}