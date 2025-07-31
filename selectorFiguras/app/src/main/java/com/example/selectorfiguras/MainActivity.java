package com.example.selectorfiguras;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

//imports necesarios
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Spinner;
import android.view.View;
import android.widget.SpinnerAdapter;

//clase principal donde se ejecuta la app
public class MainActivity extends AppCompatActivity {

    //variables que se van a usar en la app
    private EditText et1, et2;
    private TextView tv1, tv3;
    private Spinner spinner;

    private int opc;


    //constructor de la clase
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        //relacion entre el layout y la clase
        setContentView(R.layout.activity_main);

        //traspaso de variables
        et1 = findViewById(R.id.et1);
        et2 = findViewById(R.id.et2);
        tv1 = findViewById(R.id.tv1);
        tv3 = findViewById(R.id.tv3);
        spinner = findViewById(R.id.spinner);

        String [] figuras = {"Cuadrado", "Rectángulo", "Círculo", "Triángulo"};
        //declaración del adaptador
        ArrayAdapter <String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, figuras);
        //le seteamos el contenido del objeto adapter al spinner, el cual tendrá el arreglo
        spinner.setAdapter(adapter);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void calcular (View view) {

        String base = et1.getText().toString();
        String altura = et2.getText().toString();
        String figura = spinner.getSelectedItem().toString();

        /*Validacion de campos vacios*/
        if (figura.equals("Círculo")) {
            // SOLO SE VALIDA EL RADIO (base)
            if (base.isEmpty()) {
                tv3.setText("Error: Ingrese el radio");
                return;
            }
            if (!base.matches("\\d+")) {
                tv3.setText("Error: El radio debe ser un número");
                return;
            }
        } else {
            // PARA TODAS LAS DEMÁS FIGURAS (SE OCUPA BASE Y ALTURA)
            if (base.isEmpty() || altura.isEmpty()) {
                tv3.setText("Error: Campos vacíos");
                return;
            }
            if (!base.matches("\\d+") || !altura.matches("\\d+")) {
                tv3.setText("Error: Ingrese solo números");
                return;
            }
        }

        //parseo de las variables
        double b = Double.parseDouble(base);
        double a = altura.isEmpty() ? 0 : Double.parseDouble(altura);

        //validacion de la seleccion del spinner
        if (spinner.getSelectedItem().toString().equals("Cuadrado")) {
            opc = 1;
        } else if (spinner.getSelectedItem().toString().equals("Rectángulo")) {
            opc = 2;
        } else if (spinner.getSelectedItem().toString().equals("Círculo")) {
            opc = 3;
            //cambio del texto del textView para indicar que ya no
            //se trata de ingresar una base, sino el radio del círculo
            tv1.setText("Radio");
        } else if (spinner.getSelectedItem().toString().equals("Triángulo")) {
            opc = 4;
        }

        /*Ahora en el switch se realizan las operaciones según el valor
        * tomado por la variable opción, de acuerdo con el item del spiner
        * seleccionado*/
        switch (opc) {
            case 1:
                double area = b * a;
                tv3.setText("El área del cuadrado es: " + area);
                break;
            case 2:
                double area2 = b * a;
                tv3.setText("El área del rectángulo es: " + area2);
                break;
            case 3:
                /*Usamos la constante Math.PI para obtener el valor de PI
                * y lo multiplicamos por el radio al cuadrado*/
                double area3 = (Math.PI * Math.pow(b, 2));
                tv3.setText("El área del círculo es: " + area3);
                break;
            case 4:
                double area4 = (b * a) / 2;
                tv3.setText("El área del triángulo es: " + area4);
                break;
        }
    }
}