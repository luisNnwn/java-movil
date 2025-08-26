package com.example.proyecto_08_segundoactivityyparametros;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

//imports del ejercicio
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

//clase del segundo activity
public class Actividad2 extends AppCompatActivity {

    //variable de clase
    private WebView webView1;

    //constructor de la clase
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //enlace del layout con el activity
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_actividad2);

        //traspaso de variables
        webView1= (WebView) findViewById(R.id.webView1);

        //bundle sirve para pasar datos entre activities,
        Bundle bundle = getIntent().getExtras();
        //se crea una variable de tipo string para recibir el dato
        //que se adjunto al intent
        String dato = bundle.getString("direccion");

        //para habilitar el javaScript en el webView
        WebSettings webSettings = webView1.getSettings();
        webView1.getSettings().setJavaScriptEnabled(true);
        webView1.setWebViewClient(new WebViewClient());

        //se carga la url en el webView con el dato recibido
        webView1.loadUrl("https://" + dato);


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void finalizar(View v){
        //se cierra la activity
        finish();
    }
}