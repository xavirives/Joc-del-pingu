package modelo;

import java.util.Random;

/**
 * Clase que representa la Foca, un enemigo controlado por IA en el juego.
 * Extiende Jugador para heredar propiedades básicas (nombre, color, posición).
 * Implementa Serializable para poder guardarse en la base de datos.
 */
public class Foca extends Jugador implements java.io.Serializable {
    // Generador de números aleatorios para que la Foca tome decisiones "al azar"
    private Random random = new Random();

    /**
     * Constructor de la Foca.
     * Parámetros: nombre = nombre de la Foca, color = color del marcador, posicion = casilla inicial
     */
    public Foca(String nombre, String color, int posicion) {
        // Llama al constructor de la clase padre (Jugador) con los parámetros
        super(nombre, color, posicion);
    }

    /**
     * Ejecuta la inteligencia artificial (IA) de la Foca en su turno.
     * La Foca se mueve, puede ganar, atacar pinguinos o simplemente avanzar.
     * Parámetro: partida = la partida actual
     */
    public void ejecutarIA(Partida partida) {
        // Genera un número aleatorio entre 1 y 4 (la Foca se mueve menos que los pinguinos)
        int pasos = random.nextInt(4) + 1;
        // Calcula la nueva posición de la Foca
        int nuevaPos = this.getPosicion() + pasos;
        // La casilla meta/final del tablero es la 49
        int casillaMeta = 49;

        // CASO 1: Si la Foca llega o supera la casilla 49, la Foca gana
        if (nuevaPos >= casillaMeta) {
            // Coloca la Foca exactamente en la meta
            this.setPosicion(casillaMeta);
            // Marca la partida como finalizada
            partida.setFinalizada(true);
            // Establece a la Foca como ganadora
            partida.setGanador(this);
            // Registra el evento de victoria de la Foca
            partida.setUltimoEvento("¡La " + this.getNombre() + " ha llegado a la meta y ha ganado!");
            // Sale del método, no continúa con el ataque
            return;
        } 
        // CASO 2: Si la Foca no gana, se mueve a la nueva posición
        else {
            this.setPosicion(nuevaPos);
        }

        // ATAQUE: Busca si hay algún Pinguino en la misma casilla que la Foca
        Pinguino objetivo = null;
        // Recorre todos los jugadores de la partida
        for (Jugador j : partida.getJugadores()) {
            // Si el jugador es un Pinguino (no otra Foca) y está en la misma casilla...
            if (j instanceof Pinguino && j.getPosicion() == this.getPosicion()) {
                // Guarda este Pinguino como objetivo del ataque
                objetivo = (Pinguino) j;
                break; // Sale del bucle porque ya encontró el objetivo
            }
        }

        // Si la Foca encontró un Pinguino en su casilla, lo ataca
        if (objetivo != null) {
            // ATAQUE 1: Intenta comerse un pez del Pinguino (el Foca puede distraerse con comida)
            if (objetivo.getInv().removeCantidad("pez", 1)) {
                // Si el Pinguino tenía un pez, la Foca se lo come y lo deja en paz
                partida.setUltimoEvento("¡La Foca te ha pillado! Pero se ha comido tu pez y te deja en paz.");
            } 
            // ATAQUE 2: Si no hay pez, la Foca ataca más agresivamente
            else {
                // El Pinguino pierde un objeto aleatorio del inventario
                objetivo.getInv().perderObjetoAleatorio();
                // El Pinguino retrocede 4 casillas (pero no puede ir más atrás de la casilla 0)
                objetivo.setPosicion(Math.max(0, objetivo.getPosicion() - 4));
                // Registra el evento del ataque
                partida.setUltimoEvento("¡ATAQUE DE FOCA! " + objetivo.getNombre() + " retrocede 4 casillas y pierde un objeto.");
            }
        } 
        // Si no hay Pinguino en la casilla, la Foca simplemente avanza
        else {
            partida.setUltimoEvento("La foca se ha movido a la casilla " + this.getPosicion());
        }
    }
}