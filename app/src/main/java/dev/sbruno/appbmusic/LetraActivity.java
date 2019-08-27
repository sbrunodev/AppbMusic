package dev.sbruno.appbmusic;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import dev.sbruno.appbmusic.Class.Musica;

public class LetraActivity extends AppCompatActivity {

    private TextView ttbMusica;
    private TextView ttbLetra;
    private TextView ttbInterprete;

    private TextView ttbMusicaT;
    private TextView ttbLetraT;
    private TextView ttbInterpreteT;

    private TabHost Abas;
    private Button btnCompartilharLetra;
    private Button btnCompartilharTraducao;
    private Button btnReproduzirLetra;
    private Button btnReproduzirLetraT;
    private ScrollView ScrollT;
    private LinearLayout panelLetra;

    private GestureDetectorCompat gDetector; // global in fragment

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_letra);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ttbMusica = (TextView) findViewById(R.id.ttbMusica);
        ttbLetra = (TextView) findViewById(R.id.ttbLetra);
        ttbInterprete = (TextView) findViewById(R.id.ttbInterprete);

        ttbMusicaT = (TextView) findViewById(R.id.ttbMusicaT);
        ttbLetraT = (TextView) findViewById(R.id.ttbLetraT);
        ttbInterpreteT = (TextView) findViewById(R.id.ttbInterpreteT);

        btnCompartilharLetra = (Button) findViewById(R.id.btnCompartilharLetra);
        btnCompartilharTraducao = (Button) findViewById(R.id.btnCompartilharTraducao);
        btnReproduzirLetra = (Button) findViewById(R.id.btnReproduzirLetra);
        btnReproduzirLetraT = (Button) findViewById(R.id.btnReproduzirLetraT);
        panelLetra = (LinearLayout) findViewById(R.id.ScrollLetra);
        ScrollT = (ScrollView) findViewById(R.id.ScrollT);
        //btnCompartilharLetra.requestFocus();



        Intent intent = getIntent();
        final String sLetra = intent.getStringExtra("letra");
        String sMusica = intent.getStringExtra("musica");
        String sInterprete = intent.getStringExtra("interprete");
        final String sTraducao = intent.getStringExtra("traducao");

        ttbMusica.setText(sMusica);
        ttbLetra.setText(sLetra);
        ttbInterprete.setText(sInterprete);

        if(!sTraducao.equals("")) // Quer dizer que tem tradução
        {
            Abas = (TabHost) findViewById(R.id.TabLetras);

            // Configurando Abas
            Abas.setup();
            TabHost.TabSpec descritor = Abas.newTabSpec("TabLetra");
            descritor.setContent(R.id.tab1Letras);
            descritor.setIndicator("Letra");
            Abas.addTab(descritor);

            descritor = Abas.newTabSpec("TabTraducao");
            descritor.setContent(R.id.tab2Letras);
            descritor.setIndicator("Tradução");
            Abas.addTab(descritor);

            ttbMusicaT.setText(sMusica);
            ttbLetraT.setText(sTraducao);
            ttbInterpreteT.setText(sInterprete);

            gDetector = new GestureDetectorCompat( this, new GestureDetector.OnGestureListener() {

                @Override
                public boolean onDown(MotionEvent e) {
                    return true;
                }

                @Override
                public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                                       float velocityY) {
                    Log.i("motion", "onFling has been called!");
                    final int SWIPE_MIN_DISTANCE = 120;
                    final int SWIPE_MAX_OFF_PATH = 250;
                    final int SWIPE_THRESHOLD_VELOCITY = 200;
                    try {
                        if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
                            return false;
                        if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
                                && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                            Log.i("motion", "Right to Left");
                            switchTabs(false);
                        } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
                                && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {

                            Log.i("motion", "Left to Right");
                            switchTabs(true);

                        }
                    } catch (Exception e) {
                        // nothing
                    }
                    return false;
                }

                @Override
                public void onLongPress(MotionEvent e) {

                }

                @Override
                public boolean onScroll(MotionEvent e1, MotionEvent e2,
                                        float distanceX, float distanceY) {
                    return false;
                }

                @Override
                public void onShowPress(MotionEvent e) {

                }

                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return false;
                }
            });

            Abas.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return gDetector.onTouchEvent(event);
                }
            });

        }
        else
        {
            ttbMusicaT.setText("");
            ttbLetraT.setText("");
            ttbInterpreteT.setText("");
            btnCompartilharTraducao.setVisibility(View.INVISIBLE);
            btnReproduzirLetraT.setVisibility(View.INVISIBLE);
            ScrollT.setVisibility(View.INVISIBLE);


        }

        btnCompartilharTraducao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EnviarWhatsApp(ttbLetraT.getText().toString());
            }
        });

        btnCompartilharLetra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EnviarWhatsApp(ttbLetra.getText().toString());
            }
        });


        btnReproduzirLetra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Reproduzir();
            }
        });

        //ttbMusica.setFocusable(false);
        //panelLetra.setFocusable(true);

        /*final ScrollView scrollview = ((ScrollView) findViewById(R.id.ScrollLetra));
        scrollview.post(new Runnable() {
            @Override
            public void run() {
                scrollview.fullScroll(ScrollView.FOCUS_UP);
            }
        });*/



    }

    private void Reproduzir(){
        String Site = "https://www.youtube.com/results?search_query=";
        String Search =  ttbInterprete.getText().toString().replace(" ","+") +" "+ ttbMusica.getText().toString().replace(" ","+");

        Uri uri = Uri.parse(Site+Search);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    public void EnviarWhatsApp(String mensagem) {
        PackageManager pm=getPackageManager();
        try {

            Intent waIntent = new Intent(Intent.ACTION_SEND);
            waIntent.setType("text/plain");
            String text = mensagem;

            PackageInfo info=pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);
            waIntent.setPackage("com.whatsapp");

            waIntent.putExtra(Intent.EXTRA_TEXT, text);
            startActivity(waIntent);

        } catch (PackageManager.NameNotFoundException e) {
            Toast.makeText(this, "WhatsApp não instalado", Toast.LENGTH_SHORT).show();
        }
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


    public void switchTabs(boolean direction) {

        if (direction) // true = Move Left
        {
            if (Abas.getCurrentTab() == 0)
                Abas.setCurrentTab(Abas.getTabWidget().getTabCount() - 1);
            else
                Abas.setCurrentTab(Abas.getCurrentTab() - 1);
        }
        else  // Move Right
        {
            if (Abas.getCurrentTab() != (Abas.getTabWidget()
                    .getTabCount() - 1))
                Abas.setCurrentTab(Abas.getCurrentTab() + 1);
            else
                Abas.setCurrentTab(0);
        }
    }

}
