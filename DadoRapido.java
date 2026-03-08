package model.dados;

public class DadoRapido extends Dado {

    public DadoRapido() {
        super("Dado rápido");
    }

    @Override
    public int tirar() {
        return 5 + (int) (Math.random() * 6);
    }
}
