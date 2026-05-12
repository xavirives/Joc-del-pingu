package modelo;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

/**
 * Clase que gestiona el inventario de items de cada Pinguino.
 * Controla cuántos items de cada tipo tiene el jugador (peces, bolas, dados especiales).
 * Implementa Serializable para poder guardarse en la base de datos.
 */
public class Inventario implements Serializable {
    // Lista que almacena todos los items del inventario
    private ArrayList<Item> lista;

    /**
     * Constructor del Inventario.
     * Inicializa los 4 tipos de items con cantidad 0.
     */
    public Inventario() {
        lista = new ArrayList<Item>();
        // Crea los 4 tipos de items que existen en el juego con cantidad inicial 0
        lista.add(new Item("rapido", 0));    // Dado rápido (avanza 5-10)
        lista.add(new Item("lento", 0));     // Dado lento (avanza 1-3)
        lista.add(new Item("pez", 0));       // Pez (distrae a la Foca)
        lista.add(new Item("bola", 0));      // Bola de nieve (ataca otros pinguinos)
    }

    /**
     * Devuelve la lista completa de items del inventario.
     */
    public ArrayList<Item> getLista() { 
        return lista; 
    }

    /**
     * Establece una nueva lista de items (usado al cargar partida).
     */
    public void setLista(ArrayList<Item> lista) { 
        this.lista = lista; 
    }

    /**
     * Devuelve la cantidad de un item específico por su nombre.
     * Parámetro: nombre = nombre del item ("rapido", "lento", "pez", "bola")
     * Devuelve: cantidad de ese item, o 0 si no existe
     */
    public int getCantidad(String nombre) {
        // Busca el item en la lista
        Item item = buscar(nombre);
        // Si no existe, devuelve 0
        if (item == null) return 0;
        // Si existe, devuelve su cantidad
        return item.getCantidad();
    }

    /**
     * Añade una cantidad de un item al inventario.
     * No puede exceder el máximo permitido de ese item.
     * Parámetro: nombre = nombre del item, cantidad = cuántos añadir
     */
    public void addCantidad(String nombre, int cantidad) {
        // Obtiene el máximo permitido para este tipo de item
        int max = getMaximo(nombre);
        // Busca el item en la lista
        Item item = buscar(nombre);
        // Si el item no existe en la lista, crea uno nuevo
        if (item == null) {
            item = new Item(nombre, 0);
            lista.add(item);
        }
        // Suma la cantidad, pero limitada al máximo permitido
        // Math.min() asegura que no supere el máximo
        item.setCantidad(Math.min(max, item.getCantidad() + cantidad));
    }

    /**
     * Elimina una cantidad de un item del inventario.
     * Parámetro: nombre = nombre del item, cantidad = cuántos eliminar
     * Devuelve: true si se pudo eliminar, false si no hay suficientes items
     */
    public boolean removeCantidad(String nombre, int cantidad) {
        // Busca el item en la lista
        Item item = buscar(nombre);
        // Si el item no existe o no hay suficiente cantidad, devuelve false
        if (item == null || item.getCantidad() < cantidad) return false;
        // Resta la cantidad
        item.setCantidad(item.getCantidad() - cantidad);
        // Devuelve true indicando que se eliminó exitosamente
        return true;
    }

    /**
     * Calcula el total de objetos en el inventario.
     * Devuelve: suma de todas las cantidades de todos los items
     */
    public int totalObjetos() {
        int total = 0;
        // Recorre todos los items y suma sus cantidades
        for (Item i : lista) total += i.getCantidad();
        return total;
    }

    /**
     * Calcula cuántos dados especiales tiene el pinguino.
     * Devuelve: cantidad de dados rápidos + cantidad de dados lentos
     */
    public int totalDadosEspeciales() {
        // Suma los dos tipos de dados especiales
        return getCantidad("rapido") + getCantidad("lento");
    }

    /**
     * El pinguino pierde un item al azar cuando es atacado por la Foca.
     * Elige aleatoriamente entre los items que tiene, y elimina uno.
     */
    public void perderObjetoAleatorio() {
        // Lista que guardará los nombres de items disponibles (con cantidad > 0)
        ArrayList<String> disponibles = new ArrayList<String>();
        // Recorre todos los items y añade a la lista los que existen (cantidad > 0)
        for (Item i : lista) {
            if (i.getCantidad() > 0) disponibles.add(i.getNombre());
        }
        // Si no tiene items, sale sin hacer nada
        if (disponibles.isEmpty()) return;
        // Elige un item aleatorio de los disponibles
        String elegido = disponibles.get(new Random().nextInt(disponibles.size()));
        // Elimina una unidad del item elegido
        removeCantidad(elegido, 1);
    }

    /**
     * Elimina todas las bolas de nieve del inventario.
     * Se usa cuando el pinguino cae en una casilla especial que quita bolas.
     */
    public void vaciarBolas() {
        // Busca el item "bola" en la lista
        Item bola = buscar("bola");
        // Si existe, establece su cantidad a 0
        if (bola != null) bola.setCantidad(0);
    }

    /**
     * Busca un item en la lista por su nombre.
     * Parámetro: nombre = nombre del item a buscar
     * Devuelve: el Item encontrado, o null si no existe
     */
    private Item buscar(String nombre) {
        // Recorre la lista de items
        for (Item i : lista) {
            // Si encuentra uno con el nombre indicado, lo devuelve
            if (i.getNombre().equals(nombre)) return i;
        }
        // Si no lo encuentra, devuelve null
        return null;
    }

    /**
     * Devuelve la cantidad máxima permitida de un item específico.
     * Cada tipo de item tiene un máximo diferente.
     * Parámetro: nombre = nombre del item
     * Devuelve: cantidad máxima permitida de ese item
     */
    private int getMaximo(String nombre) {
        // Peces: máximo 2 unidades
        if (nombre.equals("pez")) return 2;
        // Bolas de nieve: máximo 6 unidades
        if (nombre.equals("bola")) return 6;
        // Dados especiales (rápido y lento): máximo 3 unidades cada uno
        if (nombre.equals("rapido") || nombre.equals("lento")) return 3;
        // Para otros items (si los hay), máximo 99 unidades
        return 99;
    }
}
