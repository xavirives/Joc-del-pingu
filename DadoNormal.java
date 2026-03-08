package model.dados;

public class DadoNormal extends Dado {

    public DadoNormal() {
        super("Dado normal");
    }

    @Override
    public int tirar() {
        return 1 + (int) (Math.random() * 6);
    }
}
