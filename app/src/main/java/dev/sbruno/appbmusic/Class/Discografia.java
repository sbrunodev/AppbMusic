package dev.sbruno.appbmusic.Class;

import android.graphics.Bitmap;
import android.graphics.Interpolator;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by bruhh on 02/11/2017.
 */

public class Discografia implements Serializable{

    private int Posicao;
    private String Descricao;
    private String Interprete;
    private Bitmap Imagem;
    private ArrayList<Musica> Musicas;

    public Discografia() {
        Musicas = new ArrayList<Musica>();
    }

    public Discografia(int posicao, String descricao, String interprete) {
        Posicao = posicao;
        Descricao = descricao;
        Interprete = interprete;
        Musicas = new ArrayList<Musica>();
    }

    public int getPosicao() {
        return Posicao;
    }

    public void setPosicao(int posicao) {
        Posicao = posicao;
    }

    public String getDescricao() {
        return Descricao;
    }

    public void setDescricao(String descricao) {
        Descricao = descricao;
    }

    public String getInterprete() {
        return Interprete;
    }

    public void setInterprete(String interprete) {
        Interprete = interprete;
    }

    public void AddMusicas(int Posicao, String Titulo)
    {
        Musicas.add(new Musica(Posicao,Titulo, Interprete));
    }

    public int TotalMusicas()
    {
        return Musicas.size();
    }

    public Bitmap getImagem() {
        return Imagem;
    }

    public void setImagem(Bitmap imagem) {
        Imagem = imagem;
    }

    public ArrayList<Musica> getMusicas() {
        return Musicas;
    }

    public void setMusicas(ArrayList<Musica> musicas) {
        Musicas = musicas;
    }

    @Override
    public String toString() {
        return "Discografia: "+Descricao;
    }
}
