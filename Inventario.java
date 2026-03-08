package model;

import java.util.ArrayList;
import java.util.List;

import model.dados.Dado;

//Gestiona el inventario del jugador
public class Inventario {

    public static final int MAX_DADOS = 3;
    public static final int MAX_PECES = 2;
    public static final int MAX_BOLAS = 6;

    private List<Dado> dados;
    private int peces;
    private int bolasNieve;

    // Crea un inventario vacío.
    public Inventario() {
        this.dados = new ArrayList<>();
        this.peces = 0;
        this.bolasNieve = 0;
    }

    public boolean addDado(Dado dado) {
        if (dados.size() >= MAX_DADOS || dado == null) {
            return false;
        }
        return dados.add(dado);
    }

    public boolean removeDado(int indice) {
        if (indice < 0 || indice >= dados.size()) {
            return false;
        }
        dados.remove(indice);
        return true;
    }

    public boolean addPez() {
        if (peces >= MAX_PECES) {
            return false;
        }
        peces++;
        return true;
    }

    public boolean usarPez() {
        if (peces <= 0) {
            return false;
        }
        peces--;
        return true;
    }

    public boolean addBolas(int cantidad) {
        if (cantidad <= 0) {
            return false;
        }
        bolasNieve += cantidad;
        if (bolasNieve > MAX_BOLAS) {
            bolasNieve = MAX_BOLAS;
        }
        return true;
    }

    public boolean usarBolas(int cantidad) {
        if (cantidad <= 0 || bolasNieve < cantidad) {
            return false;
        }
        bolasNieve -= cantidad;
        return true;
    }

    // Elimina un objeto aleatorio del inventario con prioridad sobre dados, peces y bolas
    public void perderObjetoAleatorio() {
        if (!dados.isEmpty()) {
            dados.remove(0);
        } else if (peces > 0) {
            peces--;
        } else if (bolasNieve > 0) {
            bolasNieve--;
        }
    }

    public int getNumeroObjetos() {
        return dados.size() + peces + bolasNieve;
    }

    public List<Dado> getDados() {
        return dados;
    }

    public int getPeces() {
        return peces;
    }

    public void setPeces(int peces) {
        this.peces = Math.max(0, Math.min(MAX_PECES, peces));
    }

    public int getBolasNieve() {
        return bolasNieve;
    }

    public void setBolasNieve(int bolasNieve) {
        this.bolasNieve = Math.max(0, Math.min(MAX_BOLAS, bolasNieve));
    }

    public void setDados(List<Dado> dados) {
        this.dados = dados;
    }

    @Override
    public String toString() {
        return "Inventario [dados=" + dados.size() + ", peces=" + peces + ", bolasNieve=" + bolasNieve + "]";
    }
}
