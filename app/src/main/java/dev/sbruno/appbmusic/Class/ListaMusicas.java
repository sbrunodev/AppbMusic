package dev.sbruno.appbmusic.Class;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by bruhh on 29/10/2017.
 */

public class ListaMusicas implements Serializable
{
    private ArrayList<Musica> listamusica;
    private boolean ordem = true;

    public ListaMusicas() {
        listamusica = new ArrayList();
    }

    public ArrayList<Musica> getLista()
    {
        return listamusica;
    }

    public void add(Musica m)
    {
        listamusica.add(m);
    }

    public Musica get(int indice)
    {
        return listamusica.get(indice);
    }

    public void Excluir (int indice)
    {
        listamusica.remove(indice);
    }

    public void ordenar()
    {
        Collections.sort(listamusica, new Comparator<Musica>() {
            @Override
            public int compare(Musica o1, Musica o2) {
                if(!ordem)
                    return o1.getNomemusica().compareToIgnoreCase(o2.getNomemusica());
                else
                    return o2.getNomemusica().compareToIgnoreCase(o1.getNomemusica());
            }
        });
        ordem=!ordem;
    }


    public ArrayList<Musica> filtrar(String palchave)
    {
        if(palchave.length()==0)
            return  listamusica;

        ArrayList<Musica> aux = new ArrayList();
        for(Musica m : listamusica)
            if(m.getNomemusica().toUpperCase().contains(palchave.toUpperCase()))
                aux.add(m);
        return aux;
    }

    public void AtualizaContato(Musica c, int Pos){
        listamusica.set(Pos,c);
    }

    public void carregarMusicas()
    {
        listamusica=new ArrayList();

        Genero g = new Genero(0,"Rock");

        listamusica.add(new Musica(0,"Música 0","Cantor",g,2000,3));
        listamusica.add(new Musica(1,"Música 1","Cantor",g,2000,3));
        listamusica.add(new Musica(2,"Música 2","Cantor",g,2000,3));
    }

    public void gravar(Context context)
    {
        FileOutputStream fout  = null;
        ObjectOutputStream out;
        try
        {
            fout = context.openFileOutput("objeto.dad",Context.MODE_PRIVATE);
            out = new ObjectOutputStream(fout);
            out.writeObject(this);
            out.close();
        }
        catch(Exception e){   e.printStackTrace();}
    }


    public static ListaMusicas carregar(Context context)
    {
        ListaMusicas tmp=null;
        FileInputStream fin  = null;
        ObjectInputStream in;
        try
        {
            fin = context.openFileInput("objeto.dad");
            in = new ObjectInputStream(fin);
            tmp = (ListaMusicas) in.readObject();
            in.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return tmp;
    }

}

