package model;

import java.util.ArrayList;
import java.util.List;

import model.casillas.Casilla;
import model.casillas.CasillaAgujeroHielo;
import model.casillas.CasillaInterrogante;
import model.casillas.CasillaNormal;
import model.casillas.CasillaOso;
import model.casillas.CasillaTierraTrizada;
import model.casillas.CasillaTrineo;

public class Tablero {

    private List<Casilla> casillas;

    public Tablero() {
        this.casillas = new ArrayList<>();
    }

    public void generarAleatorio(int totalCasillas) {
        casillas.clear();
        casillas.add(new CasillaNormal(0));
        for (int i = 1; i < totalCasillas; i++) {
            double r = Math.random();
            if (r < 0.15) {
                casillas.add(new CasillaOso(i));
            } else if (r < 0.30) {
                casillas.add(new CasillaAgujeroHielo(i));
            } else if (r < 0.45) {
                casillas.add(new CasillaTrineo(i));
            } else if (r < 0.60) {
                casillas.add(new CasillaInterrogante(i));
            } else if (r < 0.72) {
                casillas.add(new CasillaTierraTrizada(i));
            } else {
                casillas.add(new CasillaNormal(i));
            }
        }
        casillas.set(totalCasillas - 1, new CasillaNormal(totalCasillas - 1));
    }

    public Casilla getCasilla(int posicion) {
        return casillas.get(posicion);
    }

    public int buscarAnteriorAgujero(int posicion) {
        for (int i = posicion - 1; i >= 0; i--) {
            if (casillas.get(i).getTipoCasilla() == TipoCasilla.AGUJERO_HIELO) {
                return i;
            }
        }
        return 0;
    }

    public int buscarSiguienteTrineo(int posicion) {
        for (int i = posicion + 1; i < casillas.size(); i++) {
            if (casillas.get(i).getTipoCasilla() == TipoCasilla.TRINEO) {
                return i;
            }
        }
        return casillas.size() - 1;
    }

    public List<Casilla> getCasillas() {
        return casillas;
    }

    public int getUltimaPosicion() {
        return casillas.size() - 1;
    }
}
