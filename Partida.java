package model;

public class Partida {

    private String id;
    private String estado;
    private String datosJugadores;
    private String tableroActual;
    private int turnoActual;

    public Partida(String id, String estado, String datosJugadores, String tableroActual, int turnoActual) {
        this.id = id;
        this.estado = estado;
        this.datosJugadores = datosJugadores;
        this.tableroActual = tableroActual;
        this.turnoActual = turnoActual;
    }

    public String getId() {
        return id;
    }

    public String getEstado() {
        return estado;
    }

    public String getDatosJugadores() {
        return datosJugadores;
    }

    public String getTableroActual() {
        return tableroActual;
    }

    public int getTurnoActual() {
        return turnoActual;
    }
}
