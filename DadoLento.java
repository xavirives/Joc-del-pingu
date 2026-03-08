package model.dados;

public class DadoLento extends Dado {

    public DadoLento() {
        super("Dado lento");
    }

    @Override
    public int tirar() {
        return 1 + (int) (Math.random() * 3);
    }
}
