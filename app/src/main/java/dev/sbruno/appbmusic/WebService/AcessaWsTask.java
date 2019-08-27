package dev.sbruno.appbmusic.WebService;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import dev.sbruno.appbmusic.PrincipalActivity;

/**
 * Created by bruhh on 30/10/2017.
 */

public class AcessaWsTask extends AsyncTask <String,Integer,String>
{


    @Override
    protected String doInBackground(String... params)
    {
        String dados="";
        dados=AcessaWs.consumir(params[0]);
        return dados;
    }




}
