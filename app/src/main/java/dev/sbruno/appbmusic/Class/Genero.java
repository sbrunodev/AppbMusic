package dev.sbruno.appbmusic.Class;

/**
 * Created by bruhh on 29/10/2017.
 */

public class Genero {

    private int codigo;
    private String nome;

    public Genero(){}

    public Genero(int _codigo, String _nome) {
        this.codigo = _codigo;
        this.nome = _nome;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public String toString() {
        return nome;
    }
}
