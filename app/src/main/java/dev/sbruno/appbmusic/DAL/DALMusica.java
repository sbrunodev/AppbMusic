package dev.sbruno.appbmusic.DAL;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.widget.Toast;

import java.util.ArrayList;

import dev.sbruno.appbmusic.Class.Musica;
import dev.sbruno.appbmusic.Util.Conexao;

/**
 * Created by bruhh on 30/10/2017.
 */

public class DALMusica {

    private Conexao con;
    private Context context;
    private final String TABLE="musica";

    public DALMusica(Context context) {
        this.context=context;
        con = new Conexao(context);
        try {
            con.conectar();
        }
        catch(Exception e)
        {
            Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

    // Construtor música
    //public Musica(int _codigo, String _nomemusica, String _interprete, Genero _genero, int _ano, double _duracao)
    public boolean salvar(Musica o)
    {
        ContentValues dados=new ContentValues();

        dados.put("m_nomemusica",o.getNomemusica());
        dados.put("m_interprete",o.getInterprete());
        dados.put("m_genero",o.getGenero().getCodigo());
        dados.put("m_ano",o.getAno());
        dados.put("mus_duracao",o.getDuracao());

        return con.inserir(TABLE,dados)>0;
    }

    public boolean alterar(Musica o)
    {
        ContentValues dados=new ContentValues();
        dados.put("m_codigo",o.getCodigo());
        dados.put("m_nomemusica",o.getNomemusica());
        dados.put("m_interprete",o.getInterprete());
        dados.put("m_genero",o.getGenero().getCodigo());
        dados.put("m_ano",o.getAno());
        dados.put("mus_duracao",o.getDuracao());
        return con.alterar(TABLE,dados,"m_codigo="+o.getCodigo())>0;
    }

    public boolean apagar(long chave)
    {
        return con.apagar(TABLE,"m_codigo="+chave)>0;
    }

    public Musica get(int id)
    {   DALGenero gdal=new DALGenero(context);
        Musica o = null;
        Cursor cursor=con.consultar("select * from "+TABLE+" where m_codigo="+id);
        if(cursor.moveToFirst())
            o=new Musica(cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    gdal.get(cursor.getInt(3)),
                    cursor.getInt(4),
                    cursor.getDouble(5));
        cursor.close();
        return o;
    }

    public ArrayList<Musica> get(String filtro)
    {   DALGenero gdal=new DALGenero(context);
        ArrayList <Musica> objs = new ArrayList();
        String sql="select * from "+TABLE;
        if (!filtro.equals(""))
            sql+=" where "+filtro;
        sql+=" order by m_nomemusica";
        Cursor cursor=con.consultar(sql);
        if(cursor.moveToFirst())
            while (!cursor.isAfterLast()) {
                objs.add(new Musica(cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        gdal.get(cursor.getInt(3)),
                        cursor.getInt(4),
                        cursor.getDouble(5)));
                cursor.moveToNext();
            }
        cursor.close();;
        return objs;
    }
}


