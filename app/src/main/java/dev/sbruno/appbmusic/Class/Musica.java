package dev.sbruno.appbmusic.Class;

import java.security.cert.CertificateExpiredException;

/**
 * Created by bruhh on 29/10/2017.
 */

public class Musica {

    private int codigo;
    private String nomemusica;
    private String interprete;
    private Genero genero;
    private int ano;
    private double duracao;

    public Musica(){

    }

    public Musica(int _codigo, String _nomemusica, String _interprete, Genero _genero, int _ano, double _duracao) {
        this.codigo = _codigo;
        this.nomemusica = _nomemusica;
        this.interprete = _interprete;
        this.genero = _genero;
        this.ano = _ano;
        this.duracao = _duracao;
    }

    public Musica(int _codigo, String _nomemusica, String _interprete) {
        this.codigo = _codigo;
        this.nomemusica = _nomemusica;
        this.interprete = _interprete;
        this.genero = null;
        this.ano = 0;
        this.duracao = 0;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getNomemusica() {
        return nomemusica;
    }

    public void setNomemusica(String nomemusica) {
        this.nomemusica = nomemusica;
    }

    public String getInterprete() {
        return interprete;
    }

    public void setInterprete(String interprete) {
        this.interprete = interprete;
    }

    public Genero getGenero() {
        return genero;
    }

    public void setGenero(Genero genero) {
        this.genero = genero;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public double getDuracao() {
        return duracao;
    }

    public void setDuracao(double duracao) {
        this.duracao = duracao;
    }

    private String ConvertMinutos()
    {
        int minutos = (int) duracao/60;
        int segundos = (int) ( duracao - (minutos*60) );
        String m="",s="";
        if( (minutos+"").toString().length() == 1 ) m = "0"+minutos; else m = minutos+"";
        if( (segundos+"").toString().length() == 1 ) s = "0"+segundos; else s = segundos+"";
        return m+":"+s;
    }

    @Override
    public String toString() {
        String mensagem="";
        try{
            mensagem =  "Música: " + nomemusica +
                    ", Interprete: " + interprete +
                    ", Genero: " + genero.getNome() +
                    ", Ano: " + (1900+ano) +
                    ", Duração: " + ConvertMinutos() ;

        }
        catch (Exception e)
        {
            mensagem = nomemusica;
        }


        return mensagem;
    }
}
