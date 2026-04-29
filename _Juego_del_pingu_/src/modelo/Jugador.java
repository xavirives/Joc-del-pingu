package modelo;

/** Clase base de un jugador del tablero. */
public class Jugador {
    private int posicion;
    private String nombre;
    private String color;
    private boolean pierdeTurno;

    public Jugador(String nombre, String color, int posicion) {
        this.nombre = nombre;
        this.color = color;
        this.posicion = posicion;
        this.pierdeTurno = false;
    }

    public int getPosicion() { return posicion; }
    public void setPosicion(int posicion) { this.posicion = Math.max(0, posicion); }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
    public boolean isPierdeTurno() { return pierdeTurno; }
    public void setPierdeTurno(boolean pierdeTurno) { this.pierdeTurno = pierdeTurno; }
}
