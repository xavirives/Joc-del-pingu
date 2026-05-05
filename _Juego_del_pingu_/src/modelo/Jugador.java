package modelo;

/** Clase base de un jugador del tablero. */
public class Jugador {
    protected int posicion;
    protected String nombre;
    protected String color;
    protected boolean pierdeTurno;
    protected Inventario inv; // Ahora todos los jugadores tienen inventario

    public Jugador(String nombre, String color, int posicion) {
        this.nombre = nombre;
        this.color = color;
        this.posicion = posicion;
        this.pierdeTurno = false;
        this.inv = new Inventario();
    }

    public int getPosicion() { return posicion; }
    public void setPosicion(int posicion) { this.posicion = Math.max(0, posicion); }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
    public boolean isPierdeTurno() { return pierdeTurno; }
    public void setPierdeTurno(boolean pierdeTurno) { this.pierdeTurno = pierdeTurno; }
    public Inventario getInv() { return inv; }
    public void setInv(Inventario inv) { this.inv = inv; }
}
