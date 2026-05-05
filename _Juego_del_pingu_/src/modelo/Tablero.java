package modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class Tablero implements Serializable {
    private ArrayList<Casilla> casillas;

    public Tablero() {
        casillas = new ArrayList<Casilla>();
        generarCasillasAleatorias();
    }

    public ArrayList<Casilla> getCasillas() { return casillas; }
    public void setCasillas(ArrayList<Casilla> casillas) { this.casillas = casillas; }

    public void generarCasillasAleatorias() {
        casillas.clear();
        Random random = new Random();
        casillas.add(new Normal(0));
        for (int i = 1; i < 49; i++) {
            int tipo = random.nextInt(6);
            if (tipo == 0) casillas.add(new Normal(i));
            else if (tipo == 1) casillas.add(new Oso(i));
            else if (tipo == 2) casillas.add(new Agujero(i));
            else if (tipo == 3) casillas.add(new Trineo(i));
            else if (tipo == 4) casillas.add(new Evento(i));
            else casillas.add(new SueloQuebradizo(i));
        }
        casillas.add(new Normal(49));

        casillas.set(5, new Evento(5));
        casillas.set(8, new Oso(8));
        casillas.set(12, new Agujero(12));
        casillas.set(18, new Agujero(18));
        casillas.set(20, new Trineo(20));
        casillas.set(30, new Trineo(30));
        casillas.set(36, new SueloQuebradizo(36));
    }

    public int buscarAgujeroAnterior(int posicionActual) {
        for (int i = posicionActual - 1; i >= 0; i--) {
            if (casillas.get(i) instanceof Agujero) return i;
        }
        return 0;
    }

    public int buscarTrineoSiguiente(int posicionActual) {
        for (int i = posicionActual + 1; i < casillas.size(); i++) {
            if (casillas.get(i) instanceof Trineo) return i;
        }
        return posicionActual;
    }
}
