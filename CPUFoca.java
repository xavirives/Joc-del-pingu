package model;

public class CPUFoca extends Jugador {

    private int turnosBloqueada;

    public CPUFoca(int idJugador, String nombre, String color) {
        super(idJugador, nombre, color);
        this.turnosBloqueada = 0;
    }

    public int decidirAccion(Juego juego) {
        if (turnosBloqueada > 0) {
            turnosBloqueada--;
            return 0;
        }
        return 1 + (int) (Math.random() * 6);
    }

    public void bloquear(int turnos) {
        this.turnosBloqueada = turnos;
    }

    public int getTurnosBloqueada() {
        return turnosBloqueada;
    }

    @Override
    public boolean usarDadoDelInventario(int indice) {
        return inventario.removeDado(indice);
    }
}
