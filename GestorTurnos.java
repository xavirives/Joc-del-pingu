package model;

import java.util.List;

public class GestorTurnos {

    private int idTurno;

    public GestorTurnos() {
        this.idTurno = 0;
    }

    public Jugador jugadorActual(List<Jugador> jugadores) {
        return jugadores.get(idTurno);
    }

    public void siguienteTurno(List<Jugador> jugadores) {
        idTurno = (idTurno + 1) % jugadores.size();
    }

    public int getIdTurno() {
        return idTurno;
    }

    public void setIdTurno(int idTurno) {
        this.idTurno = idTurno;
    }
}
