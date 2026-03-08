package model;

public class JugadorHumano extends Jugador {

    public JugadorHumano(int idJugador, String nombre, String color) {
        super(idJugador, nombre, color);
    }

    @Override
    public boolean usarDadoDelInventario(int indice) {
        return inventario.removeDado(indice);
    }
}
