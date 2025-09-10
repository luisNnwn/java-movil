package com.example.proyecto12_framelayout;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

//imports de la actividad
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.view.Menu;
import android.view.MenuItem;

//clase principal
public class MainActivity extends AppCompatActivity {

    //declaración de variables de clase
    private ImageView iv1;
    private Button b1, b2;

    //constructor de la clase
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        //relacion del layout con la actividad
        setContentView(R.layout.activity_main);

        /*traspaso de variables relacionando las variables de clase
        * con los elementos del layout*/
        iv1 = findViewById(R.id.imageView);
        b1 = findViewById(R.id.b1);
        b2 = findViewById(R.id.b2);


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }


    //metodos para determinar la visibilidad de la imagen en el image
    //view
    public void ver (View v) {
        b1.setVisibility(View.VISIBLE);
        iv1.setVisibility(View.VISIBLE);
    }

    public void ocultar (View v) {
        b2.setVisibility(View.VISIBLE);
        iv1.setVisibility(View.INVISIBLE);

    }
}