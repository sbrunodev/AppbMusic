package dev.sbruno.appbmusic;

import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
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
import android.widget.TabHost;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import dev.sbruno.appbmusic.Class.Genero;
import dev.sbruno.appbmusic.Class.Musica;
import dev.sbruno.appbmusic.DAL.DALGenero;
import dev.sbruno.appbmusic.DAL.DALMusica;

public class CadastroGeneroActivity extends AppCompatActivity   {

    private EditText ttbNomeGenero;
    private EditText ttbPesquisarGenero;
    private Button btnSalvar;
    private Button btnCancelarGenero;
    private ListView ListVGenero;
    private TabHost Abas;
    private int Codigo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_genero);

        // Habilita o button para voltar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Codigo = 0;

        ttbNomeGenero = (EditText) findViewById(R.id.ttbNomeGenero);
        btnSalvar = (Button) findViewById(R.id.btnSalvar);
        btnCancelarGenero = (Button) findViewById(R.id.btnCancelarGenero);
        ListVGenero = (ListView) findViewById(R.id.ListVGenero);

        ttbPesquisarGenero = (EditText) findViewById(R.id.ttbPesquisarGenero);

        // Configurando Abas
        Abas = (TabHost) findViewById(R.id.Tab);
        Abas.setup();
        TabHost.TabSpec descritor = Abas.newTabSpec("TabGenero");
            descritor.setContent(R.id.tab1);
            descritor.setIndicator("Gênero");
        Abas.addTab(descritor);

        descritor = Abas.newTabSpec("TabLista");
            descritor.setContent(R.id.tab2);
            descritor.setIndicator("Lista");
        Abas.addTab(descritor);


        Abas.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                if(tabId.equals("TabLista"))
                    ExibeGeneros("");
            }
        });


        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Salvar();
            }
        });

        btnCancelarGenero.setVisibility(View.INVISIBLE);
        btnCancelarGenero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Inicia();
            }
        });


        ListVGenero.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Genero g = (Genero) ListVGenero.getAdapter().getItem(position);
                Codigo = g.getCodigo();
                Excluir(g.getNome());
                return true;
            }
        });


        ListVGenero.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Genero g = (Genero) ListVGenero.getAdapter().getItem(position);
                Toast.makeText(CadastroGeneroActivity.this, "Gênero: "+g.getCodigo()+", "+g.getNome(), Toast.LENGTH_SHORT).show();

                //Atualiza
                Codigo = g.getCodigo();
                ttbNomeGenero.setText(g.getNome());
                //Volta para a Tab Gênero
                btnCancelarGenero.setVisibility(View.VISIBLE);
                Abas.setCurrentTab(0);
            }
        });


        ttbPesquisarGenero.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                ExibeGeneros(" g_nome like '%"+ttbPesquisarGenero.getText().toString()+"%' ");
            }
        });

    }

    private void ExcluirBd(String Genero){

        DALMusica DALM = new DALMusica(this);

        ArrayList<Musica> MusicasporGenero = DALM.get(" m_genero="+Codigo);
        if(MusicasporGenero.size()==0) {
            DALGenero DALG = new DALGenero(this);
            if (DALG.apagar(Codigo)) {
                Toast.makeText(CadastroGeneroActivity.this, "Gênero: " + Codigo + ", " + Genero + " Apagado com sucesso!", Toast.LENGTH_SHORT).show();
                ExibeGeneros("");
            } else
                Toast.makeText(CadastroGeneroActivity.this, "Não foi possível apagar esse Gênero", Toast.LENGTH_SHORT).show();
        }
        else
            Toast.makeText(CadastroGeneroActivity.this, "Existe músicas que utilizam esse Gênero", Toast.LENGTH_SHORT).show();

        Codigo=0;
    }

    private void Excluir(final String Genero)
    {
        AlertDialog alerta;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("bMusic"); //define o titulo
        builder.setMessage("Deseja Realmente Excluir o Gênero "+Genero+" ?"); //define a mensagem

        //define um botão como positivo
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                ExcluirBd(Genero);
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

    private void ExibeGeneros(String filtro){
        Codigo=0;
        DALGenero DALG = new DALGenero(this);
        ArrayList<Genero> ArrayG = DALG.get(filtro);
        ArrayAdapter<Genero> adapter = new ArrayAdapter<Genero>(this, android.R.layout.simple_list_item_1,ArrayG);
        ListVGenero.setAdapter(adapter);
    }

    private String Valida(){

        String e = "";
        if(ttbNomeGenero.getText().toString().equals(""))
            e = "Informe um nome para o Gênero!";
        else
        {
            DALGenero DALG = new DALGenero(this);
            ArrayList<Genero> ArrayG = DALG.get(" g_nome='"+ttbNomeGenero.getText().toString()+"' ");

            if(ArrayG.size()==1) {
                if(Codigo==0)
                    e = "Esse gênero já está cadastrado!";
                else
                    if(ArrayG.get(0).getCodigo()!=Codigo)
                        e = "Esse gênero já está cadastrado!";
            }
        }

        return e;
    }

    private void Inicia()
    {
        Codigo=0;
        ttbNomeGenero.setText("");

        btnCancelarGenero.setVisibility(View.INVISIBLE);
    }

    private void Salvar(){
        String sValida = Valida();
        if(sValida.equals(""))
        {
            DALGenero DALG = new DALGenero(this);

            if(Codigo==0)
            {
                if(DALG.salvar(new Genero(0,ttbNomeGenero.getText().toString()))) {
                    Toast.makeText(CadastroGeneroActivity.this, "Genero salvo com sucesso!", Toast.LENGTH_SHORT).show();
                    Inicia();
                }
                else
                    Toast.makeText(CadastroGeneroActivity.this, "Erro ao Salvar Genero!", Toast.LENGTH_SHORT).show();
            }
            else
            {
                if(DALG.alterar(new Genero(Codigo,ttbNomeGenero.getText().toString()))) {
                    Toast.makeText(CadastroGeneroActivity.this, "Genero Alterado com sucesso!", Toast.LENGTH_SHORT).show();
                    Inicia();
                }
                else
                    Toast.makeText(CadastroGeneroActivity.this, "Erro ao Alterar Genero!", Toast.LENGTH_SHORT).show();
            }
        }
        else
            Toast.makeText(CadastroGeneroActivity.this, sValida, Toast.LENGTH_SHORT).show();
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
