package com.example.proyecto07_notificacionestoast;

//imports del proyecto
import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
/*Imports del ejercicio*/
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/*Clase principal*/
public class MainActivity extends AppCompatActivity {


    //variables de clase
    private EditText et1;
    private int num;

    /*Constructor de la clase*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        /*enlazamos el layout con la clase*/
        setContentView(R.layout.activity_main);

        //traspaso de variablews
        et1 = (EditText) findViewById(R.id.et1);
        num = (int) (Math.random()*100001);
        String cadena = String.valueOf(num);
        Toast notificacion = Toast.makeText(getApplicationContext(), cadena, Toast.LENGTH_LONG);
        notificacion.show();


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void controlar (View view) {
        String valorIngresado = et1.getText().toString();
        int valor = Integer.parseInt(valorIngresado);
        if (valor == num) {
            Toast notificacion = Toast.makeText(getApplicationContext(), "Muy bien recordaste el número mostrado", Toast.LENGTH_LONG);
            notificacion.show();
        } else {
            Toast notificacion = Toast.makeText(getApplicationContext(), "Lo siento pero no es el número que se mostró", Toast.LENGTH_LONG);
            notificacion.show();
        }
    }
}