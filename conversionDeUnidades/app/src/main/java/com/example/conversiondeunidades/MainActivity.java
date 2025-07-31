package com.example.conversiondeunidades;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
//imports necesarios
import android.widget.EditText;
import android.widget.TextView;
import android.widget.RadioButton;
import android.view.View;

//clase main
public class MainActivity extends AppCompatActivity {

    //parametros globales a utilizar en la clase
    private EditText et1;
    private TextView tv2;
    private RadioButton rb1, rb2, rb3, rb4;

    private int opc;

    //constructor de la clase
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        //relación entre los elementos de la interfaz y la clase
        setContentView(R.layout.activity_main);

        /*Traspaso de variables*/
        et1 =(EditText)findViewById(R.id.et1);
        tv2 =(TextView)findViewById(R.id.tv2);
        rb1 =(RadioButton)findViewById(R.id.rb1);
        rb2 =(RadioButton)findViewById(R.id.rb2);
        rb3 =(RadioButton)findViewById(R.id.rb3);
        rb4 =(RadioButton)findViewById(R.id.rb4);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void conversion (View view){

        String valor = et1.getText().toString();
        /*Control de campo vacio*/
        if (valor.isEmpty()) {
            tv2.setText("Ingrese un valor en el campo");
            return;
        }
        /*Para evitar entradas de Strings
        * //d indica los digitos del 0 al 9
        * + indica uno o mas digitos
        *
        * si el valor es distinto a numeros del cero al nueve
        * se muestra este mensaje
        *
        * es una expresión regular para comparar cadenas
        * de texto*/
        if (!valor.matches("\\d+")) {
            tv2.setText("Ingrese un numero");
            return;
        }
        //una vez validado el campo parseo
        double numeroAConvertir = Double.parseDouble(valor);

        /*Control de estado de los radioButtons */
        if (rb1.isChecked()) {
            opc = 1;
        } else if (rb2.isChecked()) {
            opc = 2;
        } else if (rb3.isChecked()) {
            opc = 3;
        } else if (rb4.isChecked()) {
            opc = 4;
        }

        /*Ahora evaluo el numero asignado al estado
        * de los radioButtons en un switch*/
        switch (opc) {
            case 1:
                double resultado = numeroAConvertir / 1000;
                tv2.setText("Resultado: " + resultado + " km");
                break;
            case 2:
                resultado = numeroAConvertir * 0.621371;
                tv2.setText("Resultado: " + resultado + " millas");
                break;
            case 3:
                resultado = numeroAConvertir / 2.2;
                tv2.setText("Resultado: " + resultado + " kg");
                break;
            case 4:
                resultado = (numeroAConvertir * 9 / 5) + 32;
                tv2.setText("Resultado: " + resultado + " °F");
                break;
        }
    }
}