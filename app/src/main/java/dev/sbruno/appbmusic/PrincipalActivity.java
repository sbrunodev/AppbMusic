package dev.sbruno.appbmusic;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import dev.sbruno.appbmusic.Adapter.DiscografiaAdapter;
import dev.sbruno.appbmusic.Adapter.MusicaAdapter;
import dev.sbruno.appbmusic.Class.Discografia;
import dev.sbruno.appbmusic.Class.Genero;
import dev.sbruno.appbmusic.Class.Musica;
import dev.sbruno.appbmusic.Class.Singleton;
import dev.sbruno.appbmusic.DAL.DALGenero;
import dev.sbruno.appbmusic.DAL.DALMusica;
import dev.sbruno.appbmusic.WebService.AcessaWsImagem;
import dev.sbruno.appbmusic.WebService.AcessaWsTask;

public class PrincipalActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private ListView ListV;
    private TabHost Abas;
    private EditText ttbPesquisarDiscografia;
    private Button btnPesquisarDiscografia;
    private ListView ListDiscografia;
    JSONArray ArrayDiscografia;
    private Spinner SpinnerPesquisa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);


        ListV = (ListView) findViewById(R.id.ListV);
        ListDiscografia = (ListView) findViewById(R.id.ListDiscografia);
        btnPesquisarDiscografia = (Button) findViewById(R.id.btnPesquisarDiscografia);
        ttbPesquisarDiscografia = (EditText) findViewById(R.id.ttbPesquisarDiscografia);
        //SpinnerPesquisa = (Spinner) findViewById(R.id.SpinnerPesquisa);

        ListV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Musica m = (Musica) ListV.getAdapter().getItem(position);
                ConsumindoWsPesquisarLetra(m.getInterprete(),m.getNomemusica());
                //ConsumindoWs(m.getInterprete(),m.getNomemusica());
            }
        });

        InicializaDados("");
        IniciaAplicacao();

        // --


        Abas = (TabHost) findViewById(R.id.TabPrincipal);
        Abas.setup();
        TabHost.TabSpec descritor = Abas.newTabSpec("TabMusicas");
        descritor.setContent(R.id.tab1Principal);
        descritor.setIndicator("Músicas");
        Abas.addTab(descritor);

        descritor = Abas.newTabSpec("TabDiscografia");
        descritor.setContent(R.id.tab2Principal);
        descritor.setIndicator("Discografia");
        Abas.addTab(descritor);


        Abas.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                 if(tabId.equals("TabMusicas"))
                    InicializaDados("");
            }
        });


        btnPesquisarDiscografia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                try {
                    PesquisarDiscografia();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });


        ListDiscografia.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Discografia d = (Discografia) ListDiscografia.getAdapter().getItem(position);
                AbreCd(d);
            }
        });


        ListDiscografia.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Discografia d = (Discografia) ListDiscografia.getAdapter().getItem(position);
                SalvaDiscografia(d);
                return true;
            }
        });






    }

    private void AbreCd(Discografia d)
    {
        Intent newActivity = new Intent(this,CdActivity.class);
        Singleton.singletonDiscografia = d;
        startActivity(newActivity);
        Toast.makeText(this,d.getInterprete(), Toast.LENGTH_SHORT).show();
    }

    private void IniciaAplicacao()
    {
        DALGenero DAL = new DALGenero(this);
        ArrayList<Genero> ArrayG = DAL.get("");
        if(ArrayG.size()==0)
            DAL.salvar(new Genero(1,"Outros"));
    }

    private void PesquisarDiscografia() throws JSONException {
        String Pesquisar = ttbPesquisarDiscografia.getText().toString().trim().toLowerCase();

        if(Pesquisar.equals(""))
            Toast.makeText(this, "Informe o nome da banda ou cantor para ser pesquisado!", Toast.LENGTH_SHORT).show();
        else
        {
            String Artista = ConsumindoWsPesquisarDiscografia(Pesquisar);
            if(ArrayDiscografia!=null)
            {
                ArrayList<Discografia> ArrayD = new ArrayList<>();

                for (int i = 0; i < ArrayDiscografia.length(); i++) {

                    JSONObject row = ArrayDiscografia.getJSONObject(i);
                    Discografia d = new Discografia(i,row.getString("desc"),Artista);
                    d.setImagem(RecuperaImagem(row.getString("cover")));

                    //Recupera Músicas
                    d.setMusicas(RecuperaMusicas(i));
                    ArrayD.add(d);
                }

                InicializaDadosDiscografia(ArrayD);
            }

        }
    }

    private ArrayList<Musica> RecuperaMusicas(int Discografia)
    {
        ArrayList <Musica> Musicas = new ArrayList<Musica>();
        try {
            JSONObject Disco = ArrayDiscografia.getJSONObject(Discografia);
            JSONArray ArrayMusicas = (JSONArray)Disco.getJSONArray("discs").get(0);
            for (int i = 0; i < ArrayMusicas.length(); i++) {
                JSONObject row = ArrayMusicas.getJSONObject(i);
                String Musica = row.getString("desc");

                Musicas.add(new Musica(i,Musica,""));
            }

        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return Musicas;
    }

    private Bitmap RecuperaImagem(String Caminho)
    {
        Caminho = "https://s2.vagalume.com"+Caminho;
        Bitmap bmp;
        AcessaWsImagem task = new AcessaWsImagem();
        try {
            bmp = task.execute(Caminho).get();
        }
        catch(Exception e){
            bmp = null;
        }
        return bmp;

    }

    private String ConsumindoWsPesquisarDiscografia(String Interprete){
        Toast.makeText(PrincipalActivity.this, "Pesquisando", Toast.LENGTH_SHORT).show();
        AcessaWsTask task = new AcessaWsTask();
        String Artista="";
        try {

            String Url = "https://www.vagalume.com.br/"+Interprete.replace(" ","-")+"/discografia/index.js";
            String Resultado = "";

            Resultado = task.execute(Url).get();

            JSONObject json = new JSONObject(Resultado);
            JSONObject jsondiscografia = (JSONObject)json.getJSONObject("discography");
            JSONObject jsonArtista = (JSONObject)jsondiscografia.getJSONObject("artist");

            Artista =jsonArtista.get("desc").toString();


            ArrayDiscografia = (JSONArray)jsondiscografia.getJSONArray("item");

        }catch(Exception e){
            ArrayDiscografia = null;
            Toast.makeText(PrincipalActivity.this, "Não foi possivel encontrar essa Discografia", Toast.LENGTH_SHORT).show();
        }
        return Artista;
    }


    private void ConsumindoWsPesquisarLetra(String Interprete, String Musica){
        Toast.makeText(PrincipalActivity.this, "Pesquisando", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(PrincipalActivity.this, "Não foi possivel encontrar essa música", Toast.LENGTH_SHORT).show();
            //Toast.makeText(CadastroMusicaActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
            //Excluir(e.toString());
        }
    }

    private void AbreLetra(String Letra, String Musica, String Interprete, String Traducao){
        Intent newActivity = new Intent(this,LetraActivity.class);
        newActivity.putExtra("letra", Letra);
        newActivity.putExtra("musica", Musica);
        newActivity.putExtra("interprete", Interprete);
        newActivity.putExtra("traducao", Traducao);
        startActivity(newActivity);

    }

    private void ExibirLetra(String Letra, String Musica, String Interprete)
    {
        AlertDialog alerta;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("bMusic"); //define o titulo
        builder.setMessage(Musica+", "+Interprete+"\n"+Letra); //define a mensagem

        //define um botão como positivo
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {

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


    private void InicializaDadosDiscografia(List<Discografia> ArrayD){
        DiscografiaAdapter adapter = new DiscografiaAdapter(this, R.layout.item_listadiscografia, ArrayD);
        ListDiscografia.setAdapter(adapter);
    }

    private void InicializaDados(String filtro){

        DALMusica DALM = new DALMusica(this);
        MusicaAdapter adapter =new MusicaAdapter(this,R.layout.item_lista,DALM.get(filtro));
        ListV.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menuprincipal,menu);

        MenuItem searchItem = menu.findItem(R.id.pesquisar);

        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        searchView.setOnQueryTextListener(this);

        return true;
    }

    private void Pesquisar(MenuItem item){

        SearchView searchView = (SearchView) item.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {


                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Toast.makeText(PrincipalActivity.this, newText, Toast.LENGTH_SHORT).show();
                return false;

            }
        });
    }

    private AlertDialog eAlerta;
    // Itens Menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.itaddmusica:
                Intent newActivityMusica = new Intent(this,CadastroMusicaActivity.class);
                startActivity(newActivityMusica);
                break;

            case R.id.itaddgenero:

                Intent newActivityGenero = new Intent(this,CadastroGeneroActivity.class);
                startActivity(newActivityGenero);

                break;

            case R.id.itsair:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("bMusic"); //define o titulo
                builder.setMessage("Deseja Realmente Sair?"); //define a mensagem
                //define um botão como positivo
                builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        finish();
                    }
                });
                //define um botão como negativo.
                builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });
                eAlerta = builder.create();
                eAlerta.show();
                break;

            case R.id.pesquisar: Pesquisar(item);break;
        }
        return super.onOptionsItemSelected(item);
    }










    private void SalvaDiscografia(final Discografia d)
    {
        AlertDialog alerta;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("bMusic"); //define o titulo
        builder.setMessage("Deseja salvar as músicas referentes a Discográfia "+d.getDescricao()+" ?"); //define a mensagem

        //define um botão como positivo
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                SalvaMusicas(d.getMusicas(), d.getInterprete());
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

    private void SalvaMusicas(ArrayList<Musica> ArrayM, String Interprete)
    {
        DALMusica DALM = new DALMusica(this);
        int MusicasSalvas=0;
        for(int i=0; i<ArrayM.size(); i++)
        {
            String nome = ArrayM.get(i).getNomemusica();
            ArrayList<Musica> Valida = DALM.get(" m_nomemusica like '%"+nome+"%' and m_interprete like '%"+Interprete+"%' ");

            if(Valida.size()==0) {
                if( DALM.salvar(new Musica(0, nome, Interprete, new Genero(1, "Outro"), 0, 0)) )
                    MusicasSalvas++;
            }

        }
        Toast.makeText(this,"Músicas salvas: "+MusicasSalvas+", Músicas que já estavam salvas: "+(ArrayM.size()-MusicasSalvas), Toast.LENGTH_SHORT).show();
    }











    // Itens Menu


    @Override
    protected void onResume() {
        super.onResume();
        InicializaDados("");

    }

    // Pesquisar Menu
    @Override
    public boolean onQueryTextSubmit(String query) {


        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText)
    {
        InicializaDados(" m_nomemusica like'%"+newText+"%' or m_interprete like'%"+newText+"%' " );
        return false;
    }
    // Fim Pesquisar Menu
}


