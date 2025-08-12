package com.example.laboratorio1_luisnelsonhernandezblanco;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

/*Imports necesarios*/
import android.widget.TextView;
import android.widget.RadioButton;
import android.widget.EditText;
import android.view.View;

import com.google.android.material.internal.EdgeToEdgeUtils;


/*Clase principal donde se ejecuta la aplicacion*/
public class MainActivity extends AppCompatActivity {

    /*Parametros necesarios para la aplicacion*/
    private TextView tv4;
    private EditText et1, et2;

    //entiéndase rb por RadioButton en todos los usos
    private RadioButton rb1, rb2, rb3, rb4, rb5, rb6;

    //esta opcion es para el switch
    private int opc;


    /*Constructor de la clase*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        /*Esto enlaza el layout con la clase, es importante porque abajo vamos
        * a hacer el traspaso de variables y sin este contexto, no se podría
        * relacionar las variables creadas aca en la clase con el id de los objetos
        * del layout*/
        setContentView(R.layout.activity_main);

        /*En el traspaso de variables lo que hacemos es que a las variables creadas
        * en Java se les relacione con los id respectivos de cada objeto del layout*/
        //Traspaso de variables
        tv4 = (TextView) findViewById(R.id.tv4);
        et1 = (EditText) findViewById(R.id.et1);
        et2 = (EditText) findViewById(R.id.et2);
        rb1 = (RadioButton) findViewById(R.id.rb1);
        rb2 = (RadioButton) findViewById(R.id.rb2);
        rb3 = (RadioButton) findViewById(R.id.rb3);
        rb4 = (RadioButton) findViewById(R.id.rb4);
        rb5 = (RadioButton) findViewById(R.id.rb5);
        rb6 = (RadioButton) findViewById(R.id.rb6);


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void hacerCalculo (View view) {

        //Primero traemos las variables ingresadas
        //y las pasamos a String
        String valor1 = et1.getText().toString();
        String valor2 = et2.getText().toString();

        //ahora validamos que los campos no estén vacios
        if (valor1.isEmpty() || valor2.isEmpty()){
            tv4.setText("Rellene los campos");
            return;
        }
        //validacion para que los campos no tengan string
        if (!valor1.matches("\\d+") ||  !valor2.matches("\\d+")){
            tv4.setText("Ingrese solo valores numéricos");
        }

        //parsear los valores a int
        int nro1 = Integer.parseInt(valor1);
        int nro2 = Integer.parseInt(valor2);

        //asignacion de valor de estado de radioButtons
        if (rb1.isChecked()){
            opc = 1;
        }
        if (rb2.isChecked()){
            opc = 2;
        }
        if (rb3.isChecked()){
            opc = 3;
        }
        if (rb4.isChecked()){
            opc = 4;
        }
        if (rb5.isChecked()){
            opc = 5;
        }
        if (rb6.isChecked()){
            opc = 6;
        }

        /*Ahora con el valor que toma cada radioButoon
        * según se seleccione voy a evaluar cada caso
        * en el switch y hacer segun cada cual la logica que corresponda*/
        switch (opc){
            case 1:
                int suma = nro1 + nro2;
                String resu = String.valueOf(suma);
                tv4.setText("La suma de ambos números es: " + resu);
                break;
            case 2:
                int resta = nro1 - nro2;
                resu = String.valueOf(resta);
                tv4.setText("La resta de ambos números es: " + resu);
                break;
            case 3:
                int multi = nro1 * nro2;
                resu = String.valueOf(multi);
                tv4.setText("La multiplicación de ambos números es: " + resu);
                break;
            case 4:
                if (nro2 == 0){
                    tv4.setText("No se puede dividir por cero");
                    return;
                }
                double div = (double)nro1 / (double) nro2;
                resu = String.valueOf(div);
                tv4.setText("La multiplicación de ambos números es: " + resu);
                break;
            case 5:
                int parImpar = nro1 + nro2;
                resu = String.valueOf(parImpar);
                //evaluo que el modulo sea distindo de cero
                if (parImpar%2 != 0){
                    tv4.setText("Numero Impar " + resu);
                } else {
                    tv4.setText("Número Par" + resu);
                }
                break;
            case 6:
                int areaTriangulo = (nro1*nro2)/2;
                resu = String.valueOf(areaTriangulo);
                tv4.setText("El área del triángulo es: " + resu);
        }

    }
}