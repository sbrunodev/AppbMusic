package dev.sbruno.appbmusic;

import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.support.v4.media.MediaBrowserServiceCompat;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import dev.sbruno.appbmusic.Class.Genero;
import dev.sbruno.appbmusic.Class.Musica;
import dev.sbruno.appbmusic.DAL.DALGenero;
import dev.sbruno.appbmusic.DAL.DALMusica;
import dev.sbruno.appbmusic.WebService.AcessaWsTask;

public class CadastroMusicaActivity extends AppCompatActivity {

    private Button btnSalvarMusica;
    private Button btnCancelarMusica;
    private EditText ttbNomeMusica;
    private EditText ttbPesquisarMusica;
    private EditText ttbInterprete;
    private TextView ttbAno;
    private TextView ttbDuracao;
    private SeekBar SeekBarAno;
    private SeekBar SeekBarDuracao;
    private Spinner SpinnerGenero;
    private TabHost Abas;
    private int Codigo;
    private ListView ListVMusica;
    private Genero g;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_musica);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Habilita o button para voltar
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); // Não deixar alterar a orientação

        Codigo=0;
        g=null;
        btnSalvarMusica = (Button) findViewById(R.id.btnSalvarMusica);
        ttbNomeMusica = (EditText) findViewById(R.id.ttbNomeMusica);
        ttbPesquisarMusica = (EditText) findViewById(R.id.ttbPesquisarMusica);
        ttbInterprete = (EditText) findViewById(R.id.ttbInterprete);
        btnCancelarMusica = (Button) findViewById(R.id.btnCancelarMusica);
        ListVMusica = (ListView) findViewById(R.id.ListVMusica);
        ttbAno = (TextView) findViewById(R.id.ttbAno);
        ttbDuracao = (TextView) findViewById(R.id.ttbDuracao);

        SeekBarAno = (SeekBar) findViewById(R.id.seekBarAno);
        SeekBarDuracao = (SeekBar) findViewById(R.id.seekBarDuracao);

        SpinnerGenero = (Spinner) findViewById(R.id.SpinnerGenero);


        SeekBarAno.setMax(117);
        //SeekBarAno.setProgress(2000);
        //ttbAno.setText("Ano: 2000");

        SeekBarDuracao.setProgress(0);
        SeekBarDuracao.setMax(900);

        SeekBarAno.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                ttbAno.setText("Ano: "+(1900+SeekBarAno.getProgress()) );
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        SeekBarDuracao.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int Valor = SeekBarDuracao.getProgress();

                int minutos = Valor/60;
                int segundos = ( Valor - (minutos*60) );
                String m="",s="";
                if( (minutos+"").toString().length() == 1 ) m = "0"+minutos; else m = minutos+"";
                if( (segundos+"").toString().length() == 1 ) s = "0"+segundos; else s = segundos+"";
                String tempo = m+":"+s;
                ttbDuracao.setText("Duração: "+tempo );
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        // Carrega Lista de Genero
        DALGenero DAL = new DALGenero(this);
        ArrayList<Genero> GeneroList = DAL.get("");

        ArrayAdapter<Genero> adapter = new ArrayAdapter<Genero>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, GeneroList);
        SpinnerGenero.setAdapter(adapter);


        SpinnerGenero.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                g = (Genero) parent.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        Abas = (TabHost) findViewById(R.id.TabM);

        // Configurando Abas
        Abas.setup();
        TabHost.TabSpec descritor = Abas.newTabSpec("TabMusica");
        descritor.setContent(R.id.tab1M);
        descritor.setIndicator("Música");
        Abas.addTab(descritor);

        descritor = Abas.newTabSpec("TabLista");
        descritor.setContent(R.id.tab2M);
        descritor.setIndicator("Lista");
        Abas.addTab(descritor);


        Abas.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                if(tabId.equals("TabLista"))
                    ExibeMusicas();
            }
        });

        ListVMusica.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Musica m = (Musica) ListVMusica.getAdapter().getItem(position);
                Codigo = m.getCodigo();
                //ConsumindoWs(m.getInterprete(),m.getNomemusica());
                Excluir(m.getNomemusica());
                return true;
            }
        });


        ListVMusica.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Musica m = (Musica) ListVMusica.getAdapter().getItem(position);
                Toast.makeText(CadastroMusicaActivity.this, "Música: "+m.getCodigo()+", "+m.getNomemusica(), Toast.LENGTH_SHORT).show();

                //Atualiza
                Codigo = m.getCodigo();
                ttbNomeMusica.setText(m.getNomemusica());
                ttbInterprete.setText(m.getInterprete());
                SeekBarAno.setProgress(m.getAno());
                SeekBarDuracao.setProgress((int) m.getDuracao());

                int Posicao = getIndex(SpinnerGenero,m.getGenero().getNome());
                SpinnerGenero.setSelection(Posicao);

                //Volta para a Tab Gênero
                btnCancelarMusica.setVisibility(View.VISIBLE);
                Abas.setCurrentTab(0);
            }
        });

        ttbPesquisarMusica.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                ExibeMusicas();
            }
        });


        //Estilo Snipper



        btnCancelarMusica.setVisibility(View.INVISIBLE);
        btnCancelarMusica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Inicia();
            }
        });

        btnSalvarMusica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Salvar();
            }
        });
    }

    private int getIndex(Spinner spinner, String myString){

        int index = 0;
        int Tamanho = spinner.getCount();
        for (int i=0;i<Tamanho;i++){
            String Valor = spinner.getItemAtPosition(i).toString();
            if (Valor.equals(myString))
                index = i;
        }
        return index;
    }


    private void ExcluirBd(String Musica){
        DALMusica DALM = new DALMusica(this);
        if(DALM.apagar(Codigo)) {
            Toast.makeText(CadastroMusicaActivity.this, "Música: " + Codigo + ", " + Musica + " Apagado com sucesso!", Toast.LENGTH_SHORT).show();
            ExibeMusicas();
        }
        else
            Toast.makeText(CadastroMusicaActivity.this, "Não foi possível apagar esse Gênero", Toast.LENGTH_SHORT).show();

        Codigo=0;
    }

    private void Excluir(final String Musica)
    {
        AlertDialog alerta;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("bMusic"); //define o titulo
        builder.setMessage("Deseja Realmente Excluir a Música "+Musica+" ?"); //define a mensagem

        //define um botão como positivo
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                ExcluirBd(Musica);
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



    private void ExibeMusicas(){
        Codigo=0;

        String filtro = "";
        if(ttbPesquisarMusica.getText().toString().length()!=0)
            filtro = " m_nomemusica like '%"+ttbPesquisarMusica.getText().toString()+"%' ";

        DALMusica DALM = new DALMusica(this);
        ArrayList<Musica> ArrayM = DALM.get(filtro);
        ArrayAdapter<Musica> adapter = new ArrayAdapter<Musica>(this, android.R.layout.simple_list_item_1,ArrayM);
        ListVMusica.setAdapter(adapter);
    }

    private String Valida(){

        String e = "";
        if(ttbNomeMusica.getText().toString().equals(""))
            e = "Informe um nome para a Música!\n";

        if(ttbInterprete.getText().toString().equals(""))
            e += "Informe um nome para o Interprete!\n";

        if(SeekBarAno.getProgress()==0)
            e += "Informe um ano!\n";

        if(SeekBarDuracao.getProgress()==0)
            e += "Informe uma Duração!\n";

        if(g == null)
            e+="Informe um Gênero!\n";

        return e;
    }

    private void Inicia()
    {
        Codigo=0;
        ttbNomeMusica.setText("");

        SeekBarAno.setMax(117);
        SeekBarDuracao.setProgress(0);
        SeekBarDuracao.setMax(900);



        ttbInterprete.setText("");
        btnCancelarMusica.setVisibility(View.INVISIBLE);
    }

    private void Salvar(){
        String sValida = Valida();
        if(sValida.equals(""))
        {
            DALMusica DALM = new DALMusica(this);
//  public Musica(int _codigo, String _nomemusica, String _interprete, Genero _genero, int _ano, double _duracao)
            if(Codigo==0)
            {
                if(DALM.salvar(new Musica(0,ttbNomeMusica.getText().toString(), ttbInterprete.getText().toString(), g, SeekBarAno.getProgress(), SeekBarDuracao.getProgress() )))
                {
                    Toast.makeText(CadastroMusicaActivity.this, "Música salva com sucesso!", Toast.LENGTH_SHORT).show();
                    Inicia();
                }
                else
                    Toast.makeText(CadastroMusicaActivity.this, "Erro ao Salvar Música!", Toast.LENGTH_SHORT).show();
            }
            else
            {
                if(DALM.alterar(new Musica(Codigo,ttbNomeMusica.getText().toString(), ttbInterprete.getText().toString(), g, SeekBarAno.getProgress(), SeekBarDuracao.getProgress()  ))) {
                    Toast.makeText(CadastroMusicaActivity.this, "Música Alterado com sucesso!", Toast.LENGTH_SHORT).show();
                    Inicia();
                }
                else
                    Toast.makeText(CadastroMusicaActivity.this, "Erro ao Alterar Música!", Toast.LENGTH_SHORT).show();
            }
        }
        else
            Toast.makeText(CadastroMusicaActivity.this, sValida, Toast.LENGTH_SHORT).show();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
