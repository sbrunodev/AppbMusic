package dev.sbruno.appbmusic.Adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import dev.sbruno.appbmusic.Class.Musica;
import dev.sbruno.appbmusic.R;

/**
 * Created by bruhh on 29/10/2017.
 */

public class MusicaAdapter extends ArrayAdapter<Musica>
{
    private int layout;

    public MusicaAdapter(Context context, int resource, List<Musica> musicas)
    {
        super(context, resource, musicas);
        layout=resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if(convertView==null)
        {
            LayoutInflater inflater = (LayoutInflater) getContext().
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(this.layout,parent,false);
        }
        TextView ilTitulo = (TextView)convertView.findViewById(R.id.ilTitulo);
        TextView ilGenero = (TextView)convertView.findViewById(R.id.ilGenero);
        TextView ilInterprete = (TextView)convertView.findViewById(R.id.ilInterprete);

        Musica m = this.getItem(position);
        ilTitulo.setText(m.getNomemusica());


        ilGenero.setText(m.getGenero().getNome());


        ilInterprete.setText(m.getInterprete());

        return convertView;
    }
}


