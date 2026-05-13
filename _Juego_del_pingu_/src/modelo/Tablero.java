package modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

/**
 * Representa el tablero del juego.
 * Contiene la lista de casillas y métodos para buscar casillas especiales.
 */
public class Tablero implements Serializable {

    // Lista con todas las casillas del tablero.
    private ArrayList<Casilla> casillas;

    /**
     * Crea un tablero nuevo y genera sus casillas.
     */
    public Tablero() {
        casillas = new ArrayList<Casilla>();
        generarCasillasAleatorias();
    }

    // Devuelve la lista de casillas del tablero.
    public ArrayList<Casilla> getCasillas() { 
        return casillas; 
    }

    // Cambia la lista de casillas del tablero.
    public void setCasillas(ArrayList<Casilla> casillas) { 
        this.casillas = casillas; 
    }

    /**
     * Genera las 50 casillas del tablero.
     * La primera y la última son normales para evitar problemas al empezar o ganar.
     */
    public void generarCasillasAleatorias() {

        // Limpia el tablero antes de volver a generarlo.
        casillas.clear();

        // Objeto para generar tipos de casillas al azar.
        Random random = new Random();

        // La casilla inicial siempre es normal.
        casillas.add(new Normal(0));

        // Genera las casillas intermedias de forma aleatoria.
        for (int i = 1; i < 49; i++) {
            int tipo = random.nextInt(6);

            if (tipo == 0) casillas.add(new Normal(i));
            else if (tipo == 1) casillas.add(new Oso(i));
            else if (tipo == 2) casillas.add(new Agujero(i));
            else if (tipo == 3) casillas.add(new Trineo(i));
            else if (tipo == 4) casillas.add(new Evento(i));
            else casillas.add(new SueloQuebradizo(i));
        }

        // La última casilla también es normal porque representa la meta.
        casillas.add(new Normal(49));

        /*
         * Se fijan algunas casillas especiales en posiciones concretas.
         * Así se asegura que siempre existan eventos, osos, agujeros,
         * trineos y suelo quebradizo en el tablero.
         */
        casillas.set(5, new Evento(5));
        casillas.set(8, new Oso(8));
        casillas.set(12, new Agujero(12));
        casillas.set(18, new Agujero(18));
        casillas.set(20, new Trineo(20));
        casillas.set(30, new Trineo(30));
        casillas.set(36, new SueloQuebradizo(36));
    }

    /**
     * Busca el agujero anterior a la posición actual.
     * Si no encuentra ninguno, devuelve la casilla 0.
     */
    public int buscarAgujeroAnterior(int posicionActual) {
        for (int i = posicionActual - 1; i >= 0; i--) {
            if (casillas.get(i) instanceof Agujero) return i;
        }

        return 0;
    }

    /**
     * Busca el siguiente trineo después de la posición actual.
     * Si no encuentra ninguno, el jugador se queda en la misma posición.
     */
    public int buscarTrineoSiguiente(int posicionActual) {
        for (int i = posicionActual + 1; i < casillas.size(); i++) {
            if (casillas.get(i) instanceof Trineo) return i;
        }

        return posicionActual;
    }
}