package dev.sbruno.appbmusic;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;

import dev.sbruno.appbmusic.Class.Discografia;
import dev.sbruno.appbmusic.Class.Genero;
import dev.sbruno.appbmusic.Class.Musica;
import dev.sbruno.appbmusic.Class.Singleton;
import dev.sbruno.appbmusic.DAL.DALGenero;
import dev.sbruno.appbmusic.DAL.DALMusica;
import dev.sbruno.appbmusic.WebService.AcessaWsTask;

public class CdActivity extends AppCompatActivity {

    private TextView ttbDiscografiaCD;
    private TextView ttbInterpreteCD;
    private ListView ListViewCD;
    private TextView ttbNumeroMusicasCD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cd);

        Discografia d = Singleton.singletonDiscografia;

        ttbDiscografiaCD = (TextView) findViewById(R.id.ttbDiscografiaCD);
        ttbInterpreteCD  = (TextView) findViewById(R.id.ttbInterpreteCD);
        ttbNumeroMusicasCD = (TextView) findViewById(R.id.ttbNumeroMusicasCD);
        ListViewCD = (ListView) findViewById(R.id.ListViewCD);

        ttbDiscografiaCD.setText(d.getDescricao());
        ttbInterpreteCD.setText(d.getInterprete());
        ttbNumeroMusicasCD.setText("  "+d.TotalMusicas()+" Músicas");

        ArrayList list = new ArrayList();
        for (int i=0; i<d.getMusicas().size();i++)
            list.add(d.getMusicas().get(i).getNomemusica());



        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,list);
        ListViewCD.setAdapter(adapter);

        ListViewCD.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String strMusica = (String) ListViewCD.getAdapter().getItem(position);
                ConsumindoWsPesquisarLetra(ttbInterpreteCD.getText().toString(),strMusica);
            }
        });


        ListViewCD.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String d = (String) ListViewCD.getAdapter().getItem(position);
                SalvaDiscografia(d);
                return true;
            }
        });
    }

    private void SalvaDiscografia(final String d)
    {
        AlertDialog alerta;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("bMusic"); //define o titulo
        builder.setMessage("Deseja salvar a música "+d+" ?"); //define a mensagem

        //define um botão como positivo
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                SalvaMusicas(d, ttbInterpreteCD.getText().toString());
            }
        });
        //define um botao como negativo.
        builder.setNegativeButton("Nao", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {

            }
        });


        alerta = builder.create();
        alerta.show();
    }

    private void ConsumindoWsPesquisarLetra(String Interprete, String Musica){
        Toast.makeText(CdActivity.this, "Pesquisando", Toast.LENGTH_SHORT).show();
        AcessaWsTask task = new AcessaWsTask();
        try {

            String Url = "https://api.vagalume.com.br/search.php?art="+Interprete+"&mus="+Musica+"&apikey={key}";
            String Resultado = "";
            //Toast.makeText(CadastroMusicaActivity.this, task.execute(Url).get(), Toast.LENGTH_SHORT).show();
            Resultado = task.execute(Url).get();

            JSONObject json = new JSONObject(Resultado);
            JSONObject jsonmusica = (JSONObject)json.getJSONArray("mus").get(0);
            String Texto = jsonmusica.get("text").toString();
            String Traducao="";
            String sTraducao="";
            try
            {
                JSONObject jsonT = (JSONObject)jsonmusica.getJSONArray("translate").get(0);
                Traducao= jsonT.get("text").toString();
                int i=0;
            }
            catch (Exception e)
            {
                Traducao="";
            }

            AbreLetra(Texto,Musica,Interprete, Traducao);

        }catch(Exception e){
            Toast.makeText(CdActivity.this, "Não foi possivel encontrar essa música", Toast.LENGTH_SHORT).show();
            //Toast.makeText(CadastroMusicaActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
            //Excluir(e.toString());
        }
    }

    private void SalvaMusicas(String m, String Interprete)
    {
        DALMusica DALM = new DALMusica(this);

        ArrayList<Musica> Valida = DALM.get(" m_nomemusica like '%"+m+"%' and m_interprete like '%"+Interprete+"%' ");

        if(Valida.size()==0) {
           if( DALM.salvar(new Musica(0, m, Interprete, new Genero(1, "Outro"), 0, 0)) )
               Toast.makeText(this,"Música Salva :)", Toast.LENGTH_SHORT).show();
        }
        else
            Toast.makeText(this,"Essa música já está salva!", Toast.LENGTH_SHORT).show();
    }

    private void AbreLetra(String Letra, String Musica, String Interprete, String Traducao){
        Intent newActivity = new Intent(this,LetraActivity.class);
        newActivity.putExtra("letra", Letra);
        newActivity.putExtra("musica", Musica);
        newActivity.putExtra("interprete", Interprete);
        newActivity.putExtra("traducao", Traducao);
        startActivity(newActivity);

    }
}
