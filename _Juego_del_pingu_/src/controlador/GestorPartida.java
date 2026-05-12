package controlador;
import java.util.ArrayList;
import java.util.Random;
import modelo.*;

/**
 * Gestor principal de la partida. Controla el flujo del juego, turnos, movimientos y acciones.
 */
public class GestorPartida {
    // Objeto que almacena toda la información de la partida actual (jugadores, tablero, estado)
    private Partida partida;
    // Gestor para guardar y cargar partidas en la base de datos Oracle
    private GestorBBDD gestorBBDD = new GestorBBDD();
    // Generador de números aleatorios para los dados
    private Random random = new Random();

    /**
     * Crea una nueva partida con los jugadores especificados.
     * Parámetros: nombres = lista de nombres de jugadores, incluyeFoca = si hay IA (Foca)
     */
    public void nuevaPartida(java.util.List<String> nombres, boolean incluyeFoca) {
        // Crea un objeto Partida vacío
        partida = new Partida();
        // Lista donde se guardarán todos los Pinguinos de la partida
        ArrayList<Jugador> jugadores = new ArrayList<>();
        // Array de colores (azul, rojo, verde, naranja) que se asignan a los jugadores
        String[] colores = {"#3b82f6", "#ef4444", "#10b981", "#f59e0b"}; 
        
        // Crea un Pinguino por cada nombre en la lista
        // Si hay más de 4 jugadores, recicla colores: jugador 5 = color jugador 1, etc.
        for (int i = 0; i < nombres.size(); i++) {
            jugadores.add(new Pinguino(nombres.get(i), colores[i % colores.length], 0, new Inventario()));
        }
        
        // Si el checkbox "Incluye Foca" está marcado, añade una Foca (enemigo controlado por IA)
        if (incluyeFoca) {
            jugadores.add(new Foca("Foca Ártica", "#6b7280", 0));
        }
        
        // Asigna la lista de jugadores a la partida
        partida.setJugadores(jugadores);
        // Establece el primer evento/mensaje que verá el jugador
        partida.setUltimoEvento("¡Partida iniciada! Que gane el mejor.");
    }

    /**
     * Lanza un dado normal (1-6). Es el método simplificado sin parámetros.
     */
    public int usarDadoNormal() {
        // Llama al método usarDado pasando "normal" como tipo
        return usarDado("normal");
    }

    /**
     * Lanza un dado según el tipo especificado: normal, rápido o lento.
     * El tipo determina el rango de números y consume items del inventario.
     */
    public int usarDado(String tipo) {
        // Valida que la partida exista y no haya finalizado
        if (partida == null || partida.isFinalizada()) return 0;
        // Obtiene el jugador actual (debe ser un Pinguino, no la Foca)
        Pinguino actual = (Pinguino) partida.getJugadorActual();
        
        // Variable que almacenará el resultado del dado
        int resultado = 0;
        // TIPO 1: Dado normal = resultado entre 1 y 6
        if (tipo.equals("normal")) {
            resultado = random.nextInt(6) + 1;
        } 
        // TIPO 2: Dado rápido = resultado entre 5 y 10 (avanza más), cuesta 1 item "rapido"
        else if (tipo.equals("rapido")) {
            // Intenta eliminar 1 item "rapido" del inventario
            if (actual.getInv().removeCantidad("rapido", 1)) {
                resultado = random.nextInt(6) + 5; // Números del 5 al 10
            }
        } 
        // TIPO 3: Dado lento = resultado entre 1 y 3 (avanza menos), cuesta 1 item "lento"
        else if (tipo.equals("lento")) {
            // Intenta eliminar 1 item "lento" del inventario
            if (actual.getInv().removeCantidad("lento", 1)) {
                resultado = random.nextInt(3) + 1; // Números del 1 al 3
            }
        }
        
        // Si el resultado es válido (mayor que 0), mueve el jugador y cambia de turno
        if (resultado > 0) {
            moverJugador(actual, resultado);
            // Si la partida aún no ha terminado (no hay ganador), pasa al siguiente turno
            if (!partida.isFinalizada()) siguienteTurno();
        }
        return resultado;
    }

    /**
     * El jugador actual lanza una bola de nieve contra el jugador más adelantado.
     * Retrocede 3 casillas al objetivo. Cuesta 1 item "bola" del inventario.
     */
    public void usarBolaDeNieve() {
        // Valida que la partida exista y no haya finalizado
        if (partida == null || partida.isFinalizada()) return;
        // Obtiene el jugador actual
        Pinguino actual = (Pinguino) partida.getJugadorActual();
        
        // Intenta eliminar 1 bola de nieve del inventario
        if (actual.getInv().removeCantidad("bola", 1)) {
            // Variable para guardar al jugador que recibe el golpe (el más adelantado)
            Jugador objetivo = null;
            // Variable para saber cuál es la posición más alta encontrada
            int maxPos = -1;
            
            // Recorre todos los jugadores buscando al que está más adelantado (excepto el actual)
            for (Jugador j : partida.getJugadores()) {
                // Si el jugador no es el actual y está más adelantado que el máximo encontrado...
                if (j != actual && j.getPosicion() > maxPos) {
                    // Actualiza el máximo y guarda este jugador como objetivo
                    maxPos = j.getPosicion();
                    objetivo = j;
                }
            }
            
            // Si encontró un objetivo, lo retrocede 3 casillas
            if (objetivo != null) {
                // Retrocede 3 casillas, pero sin pasar de la casilla 0
                objetivo.setPosicion(Math.max(0, objetivo.getPosicion() - 3));
                // Registra el evento en la partida
                partida.setUltimoEvento(actual.getNombre() + " lanzó nieve a " + objetivo.getNombre());
            }
            // Pasa al siguiente turno después de usar la bola
            siguienteTurno();
        }
    }

    /**
     * Mueve un jugador un número de casillas y ejecuta la acción de la casilla.
     * Detecta si el jugador ha llegado a la meta (casilla 49).
     */
    private void moverJugador(Jugador j, int pasos) {
        // Si la partida ya terminó, no hacer nada
        if (partida.isFinalizada()) return; 
        // Calcula la nueva posición sumando los pasos
        int nuevaPos = j.getPosicion() + pasos;
        // La última casilla del tablero es la 49 (el tablero tiene 50 casillas: 0-49)
        int ultima = 49; 
        
        // Si el jugador llega o supera la casilla 49, gana la partida
        if (nuevaPos >= ultima) {
            // Lo deja exactamente en la casilla 49
            j.setPosicion(ultima);
            // Marca la partida como finalizada
            partida.setFinalizada(true);
            // Guarda quién es el ganador
            partida.setGanador(j);
            // Registra el evento de victoria
            partida.setUltimoEvento("¡" + j.getNombre() + " ha ganado la partida!");
        } 
        // Si no ha ganado, lo mueve a la nueva posición
        else {
            j.setPosicion(nuevaPos);
            // Obtiene la casilla en la que ha caído el jugador
            Casilla c = partida.getTablero().getCasillas().get(j.getPosicion());
            // Si el jugador es un Pinguino (no la Foca), ejecuta la acción de la casilla
            // (puede ser trampa, bonificación, etc.)
            if (j instanceof Pinguino) {
                c.realizarAccion(partida, (Pinguino)j);
            }
        }
    }

    /**
     * Pasa el turno al siguiente jugador en la lista.
     * Si el siguiente es una Foca (IA), la Foca juega automáticamente.
     */
    public void siguienteTurno() {
        // Calcula el índice del siguiente jugador: si es el último, vuelve al primero (índice 0)
        // Ejemplo: si hay 3 jugadores (0,1,2) y el actual es el 2, el siguiente es (2+1)%3 = 0
        int sig = (partida.getJugadorActualIndice() + 1) % partida.getJugadores().size();
        // Actualiza el índice del jugador actual
        partida.setJugadorActualIndice(sig);
        
        // Si el nuevo jugador actual es una Foca (enemigo IA) y la partida no ha terminado...
        if (partida.getJugadorActual() instanceof Foca && !partida.isFinalizada()) {
            // La Foca ejecuta su IA (toma decisiones automáticamente)
            ((Foca) partida.getJugadorActual()).ejecutarIA(partida);
            // Después que la Foca juega, pasa al siguiente turno (para que juegue un Pinguino)
            siguienteTurno();
        }
    }

    /**
     * Guarda la partida actual en la base de datos Oracle.
     */
    public void guardarPartida() { 
        gestorBBDD.guardarBBDD(partida); 
    }

    /**
     * Carga una partida guardada desde la base de datos Oracle (ID = 1).
     */
    public void cargarPartida() { 
        // Intenta cargar la partida con ID 1 de la base de datos
        Partida p = gestorBBDD.cargarBBDD(1); 
        // Si la carga fue exitosa (p no es null), reemplaza la partida actual
        if (p != null) this.partida = p; 
    }

    /**
     * Devuelve el objeto Partida actual.
     */
    public Partida getPartida() { 
        return partida; 
    }
}
