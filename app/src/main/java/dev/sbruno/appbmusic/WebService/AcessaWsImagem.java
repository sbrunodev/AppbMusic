package dev.sbruno.appbmusic.WebService;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.InputStream;
import java.net.URL;

/**
 * Created by bruhh on 02/11/2017.
 */

public class AcessaWsImagem extends AsyncTask<String,Integer,Bitmap>
{
    @Override
    protected Bitmap doInBackground(String... params)
    {
        Bitmap bmp=null;
        try {
            URL url = new URL(params[0]);
            InputStream is=url.openStream();
            bmp= BitmapFactory.decodeStream(is);
            is.close();
        }
        catch(Exception e)
        {
            bmp=null;
        }
        return bmp;
    }
}
