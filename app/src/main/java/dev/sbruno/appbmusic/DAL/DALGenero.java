package dev.sbruno.appbmusic.DAL;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.widget.Toast;

import java.util.ArrayList;

import dev.sbruno.appbmusic.Class.Genero;
import dev.sbruno.appbmusic.Util.Conexao;

/**
 * Created by bruhh on 30/10/2017.
 */

public class DALGenero {
    private Conexao con;
    private final String TABLE="genero";

    public DALGenero(Context context) {
        con = new Conexao(context);
        try {
            con.conectar();
        }
        catch(Exception e)
        {
            Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

    public boolean salvar(Genero o)
    {
        ContentValues dados=new ContentValues();
        dados.put("g_nome",o.getNome());

        return con.inserir(TABLE,dados)>0;
    }
    public boolean alterar(Genero o)
    {
        ContentValues dados=new ContentValues();
        dados.put("g_codigo",o.getCodigo());
        dados.put("g_nome",o.getNome());
        return con.alterar(TABLE,dados,"g_codigo="+o.getCodigo())>0;
    }
    public boolean apagar(long chave)
    {
        return con.apagar(TABLE,"g_codigo="+chave)>0;
    }

    public Genero get(int id)
    {   Genero o = null;
        Cursor cursor=con.consultar("select * from "+TABLE+" where g_codigo="+id);
        if(cursor.moveToFirst())
            o=new Genero(cursor.getInt(0),cursor.getString(1));
        cursor.close();;
        return o;
    }

    public ArrayList<Genero> get(String filtro)
    {   ArrayList <Genero> objs = new ArrayList();
        String sql="select * from "+TABLE;
        if (!filtro.equals(""))
            sql+=" where "+filtro;

        Cursor cursor=con.consultar(sql);
        if(cursor.moveToFirst())
            while (!cursor.isAfterLast()) {
                objs.add(new Genero(cursor.getInt(0), cursor.getString(1)));
                cursor.moveToNext();
            }
        cursor.close();;
        return objs;
    }
}

