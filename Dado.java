package model.dados;

public abstract class Dado {

    protected String nombre;

    public Dado(String nombre) {
        this.nombre = nombre;
    }

    public abstract int tirar();

    public String getNombre() {
        return nombre;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
