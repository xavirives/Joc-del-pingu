package modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

/** Inventario simple con límites del enunciado. */
public class Inventario implements Serializable {
    private ArrayList<Item> lista;

    public Inventario() {
        lista = new ArrayList<Item>();
        lista.add(new Item("rapido", 0));
        lista.add(new Item("lento", 0));
        lista.add(new Item("pez", 0));
        lista.add(new Item("bola", 0));
    }

    public ArrayList<Item> getLista() { return lista; }
    public void setLista(ArrayList<Item> lista) { this.lista = lista; }

    public int getCantidad(String nombre) {
        Item item = buscar(nombre);
        if (item == null) return 0;
        return item.getCantidad();
    }

    public void addCantidad(String nombre, int cantidad) {
        int max = getMaximo(nombre);
        Item item = buscar(nombre);
        if (item == null) {
            item = new Item(nombre, 0);
            lista.add(item);
        }
        item.setCantidad(Math.min(max, item.getCantidad() + cantidad));
    }

    public boolean removeCantidad(String nombre, int cantidad) {
        Item item = buscar(nombre);
        if (item == null || item.getCantidad() < cantidad) return false;
        item.setCantidad(item.getCantidad() - cantidad);
        return true;
    }

    public int totalObjetos() {
        int total = 0;
        for (Item i : lista) total += i.getCantidad();
        return total;
    }

    public int totalDadosEspeciales() {
        return getCantidad("rapido") + getCantidad("lento");
    }

    public void perderObjetoAleatorio() {
        ArrayList<String> disponibles = new ArrayList<String>();
        for (Item i : lista) {
            if (i.getCantidad() > 0) disponibles.add(i.getNombre());
        }
        if (disponibles.isEmpty()) return;
        String elegido = disponibles.get(new Random().nextInt(disponibles.size()));
        removeCantidad(elegido, 1);
    }

    public void vaciarBolas() {
        Item bola = buscar("bola");
        if (bola != null) bola.setCantidad(0);
    }

    private Item buscar(String nombre) {
        for (Item i : lista) {
            if (i.getNombre().equals(nombre)) return i;
        }
        return null;
    }

    private int getMaximo(String nombre) {
        if (nombre.equals("pez")) return 2;
        if (nombre.equals("bola")) return 6;
        if (nombre.equals("rapido") || nombre.equals("lento")) return 3;
        return 99;
    }
}
