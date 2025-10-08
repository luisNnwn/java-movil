package com.example.proyecto20_almacenamientodedatosarchivodetexto;

import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
//imports del ejercicio
import android.widget.EditText;
import android.view.View;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;


//clase principal de la actividad
public class MainActivity extends AppCompatActivity {

    //declaración de variables de clase
    private EditText et1, et2;

    //constructor de la clase
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        //traspaso de variables
        et1 = (EditText) findViewById(R.id.et1);
        et2 = (EditText) findViewById(R.id.et2);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void grabar (View v) {
        String nomarchivo = et1.getText().toString();
        nomarchivo = nomarchivo.replace('/','-');
        try {
            OutputStreamWriter archivo = new OutputStreamWriter(openFileOutput(nomarchivo, MODE_PRIVATE));
            archivo.write(et2.getText().toString());
            archivo.flush();
            archivo.close();
        } catch (IOException e) {
        }
        Toast t = Toast.makeText(getApplicationContext(), "Los datos fueron grabados", Toast.LENGTH_SHORT);
        t.show();
        et1.setText("");
        et2.setText("");
    }

    public void recuperar (View v) {
        String nomarchivo = et1.getText().toString();
        nomarchivo = nomarchivo.replace('/','-');
        boolean enco = false;
        String [] archivos = fileList();
        for (int i = 0; i < archivos.length; i++) {
            if (nomarchivo.equals(archivos[i])) {
                enco = true;
            }
            if (enco == true) {
                try {
                    InputStreamReader archivo = new InputStreamReader(openFileInput(nomarchivo));
                    BufferedReader br = new BufferedReader(archivo);
                    String linea = br.readLine();
                    String todo = "";
                    while (linea != null) {
                        todo = todo + linea + "\n";
                        linea = br.readLine();
                    }
                    br.close();
                    archivo.close();
                    et2.setText(todo);
                } catch (IOException e) {
                }
            } else {
                Toast.makeText(getApplicationContext(), "No hay datos grabados para dicha fecha", Toast.LENGTH_LONG).show();
                et2.setText("");
            }
        }
    }

}