package dev.sbruno.appbmusic.Util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import dev.sbruno.appbmusic.DB.DBMusic;

/**
 * Created by bruhh on 30/10/2017.
 */

public class Conexao {

    private SQLiteDatabase database ;
    private DBMusic sqliteOpenHelper ;

    public Conexao(Context context )
    {
        sqliteOpenHelper = new DBMusic(context) ;
    }

    public void conectar() throws SQLException {
        database = sqliteOpenHelper.getWritableDatabase();
    }

    public void desconectar()
    {
        sqliteOpenHelper.close();
    }

    public int getMaxPK(String tabela, String chave)
    {
        Cursor cursor = database.rawQuery("select max("+chave+") from "+tabela,null);
        cursor.moveToFirst();
        int id=cursor.getInt(0);
        cursor.close();
        return id;
    }
    public long inserir(String tabela, ContentValues dados )
    {
        long insertId = database.insert(tabela,null,dados);
        return insertId;
    }
    public long alterar(String tabela, ContentValues dados, String restricao)
    {
        long insertId = database.update(tabela,dados,restricao,null);
        return insertId;
    }

    public int apagar(String tabela, String query)
    {
        return database.delete(tabela , query, null);
    }

    public Cursor consultar(String query)
    {   Cursor cursor=null;
        cursor = database.rawQuery(query, null);
        return cursor;
    }

}