package dev.sbruno.appbmusic.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by bruhh on 30/10/2017.
 */

public class DBMusic extends SQLiteOpenHelper {

    public DBMusic(Context context) {
        super(context, "bmusic.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //public Genero(int _codigo, String _nome) {
        db.execSQL("CREATE TABLE genero( g_codigo INTEGER PRIMARY KEY AUTOINCREMENT, g_nome VARCHAR (25));");

        //  public Musica(int _codigo, String _nomemusica, String _interprete, Genero _genero, int _ano, double _duracao)
        db.execSQL("CREATE TABLE musica " +
                "(m_codigo  INTEGER PRIMARY KEY AUTOINCREMENT, " +
                " m_nomemusica VARCHAR(50), " +
                " m_interprete VARCHAR (50), " +
                " m_genero INTEGER REFERENCES genero (g_codigo)," +
                " m_ano INTEGER, " +
                " mus_duracao NUMERIC (4, 1) );");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL ("DROP TABLE IF EXISTS musica");
        db.execSQL ("DROP TABLE IF EXISTS genero");
        onCreate(db);
    }
}
