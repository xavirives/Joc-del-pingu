package model;

import model.eventos.Evento;

// Clase base de los jugadores del juego.
public abstract class Jugador {

    protected int idJugador;
    protected String nombre;
    protected String color;
    protected int posicion;
    protected boolean turnoPerdido;
    protected Inventario inventario;

    public Jugador(int idJugador, String nombre, String color) {
        this.idJugador = idJugador;
        this.nombre = nombre;
        this.color = color;
        this.posicion = 0;
        this.turnoPerdido = false;
        this.inventario = new Inventario();
    }

    public abstract boolean usarDadoDelInventario(int indice);

    public void mover(int pasos, int maxCasilla) {
        this.posicion += pasos;
        if (this.posicion > maxCasilla) {
            this.posicion = maxCasilla;
        }
        if (this.posicion < 0) {
            this.posicion = 0;
        }
    }

    public void aplicarEvento(Evento evento, Juego juego) {
        evento.aplicar(this, juego);
    }

    public int getIdJugador() {
        return idJugador;
    }

    public String getNombre() {
        return nombre;
    }

    public String getColor() {
        return color;
    }

    public int getPosicion() {
        return posicion;
    }

    public void setPosicion(int posicion) {
        this.posicion = posicion;
    }

    public boolean isTurnoPerdido() {
        return turnoPerdido;
    }

    public void setTurnoPerdido(boolean turnoPerdido) {
        this.turnoPerdido = turnoPerdido;
    }

    public Inventario getInventario() {
        return inventario;
    }

    @Override
    public String toString() {
        return nombre + " [color=" + color + ", posicion=" + posicion + ", " + inventario + "]";
    }
}
