package model.eventos;

public class GestorEventos {

    public Evento generarAleatorio() {
        int numero = 1 + (int) (Math.random() * 7);
        switch (numero) {
        case 1:
            return new EventoObtenerPez();
        case 2:
            return new EventoObtenerBolas();
        case 3:
            return new EventoDadoRapido();
        case 4:
            return new EventoDadoLento();
        case 5:
            return new EventoPerderTurno();
        case 6:
            return new EventoPerderObjeto();
        default:
            return new EventoMotoNieve();
        }
    }
}
