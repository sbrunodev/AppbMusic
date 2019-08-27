package dev.sbruno.appbmusic.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import dev.sbruno.appbmusic.Class.Discografia;
import dev.sbruno.appbmusic.R;

/**
 * Created by bruhh on 02/11/2017.
 */

public class DiscografiaAdapter extends ArrayAdapter<Discografia> {

    private int layout;

    public DiscografiaAdapter(Context context, int resource, List<Discografia> discografias)
    {
        super(context, resource, discografias);
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
        TextView ilDiscografia = (TextView)convertView.findViewById(R.id.ilDiscografia);
        ImageView ImageDiscografia = (ImageView) convertView.findViewById(R.id.imageDiscografia);
        TextView ilTotalMusicas = (TextView)convertView.findViewById(R.id.ilTotalMusicas);

        Discografia d = this.getItem(position);
        ilDiscografia.setText(d.getDescricao());
        ilTotalMusicas.setText(d.TotalMusicas()+" Músicas");

        if(d.getImagem()!=null)
            ImageDiscografia.setImageBitmap(d.getImagem());



        return convertView;
    }
}
