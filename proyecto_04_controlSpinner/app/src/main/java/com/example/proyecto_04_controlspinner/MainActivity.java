package com.example.proyecto_04_controlspinner;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
//imports de la actividad
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    /*Declaración de variables*/
    private Spinner spinner1;
    private EditText et1, et2;
    private TextView tv3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        //conectamos el layout con la actividad
        //para poder crear las variables
        setContentView(R.layout.activity_main);

        /*Traspaso de variables*/
        et1 = (EditText) findViewById(R.id.et1);
        et2 = (EditText) findViewById(R.id.et2);
        tv3 = (TextView) findViewById(R.id.tv3);

        spinner1 = (Spinner) findViewById(R.id.spinner);
        /*Creamos el arreglo de tipo string con las opciones que vamos
        * a seleccionar en el spinner*/
        String[] opciones = {"Sumar", "Restar", "Multiplicar", "Dividir"};
        /*Investigando encontré que el ArrayAdapter sirve como un puente entre la fuente de datos
        * que creo que sería el arreglo y el componente de la interfaz que sería
        * el spinner. Es para poder convertir este arreglo a algo que se pueda mostrar
        * en el spinner.
        *
        * Así, instanciamos un objeto de la clase ArrayAdapter, este objeto lo que nos retorna
        * según entiendo es una vista para el objeto spinner y los elementos del array.
        *
        * Lo que hace internamente es crear la vista de los objetos y los convierte a un String mostrable
        * para ponerlo en los textview internos del spinner
        * */
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout
                .simple_spinner_item, opciones);
        //le seteamos el contenido del adaotador de arreglo al spinner
        spinner1.setAdapter(adapter);


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    /*Metodo que se va a ejecutar cuando se presione el boton*/
    public void operar (View view) {
        //pasamos los valores de los edittext a variables String
        String valor1 = et1.getText().toString();
        String valor2= et2.getText().toString();

        /*Validar que los campos no estén vacíos antes
        * de hacer el parseo y no de error... porque pareseInt
        * no acepta vacío, si hay campos vacíos ahí, al hacer
        * el parseo se rompe por eso*/
        if (et1.getText().toString().isEmpty() || et2.getText().toString().isEmpty()){
            tv3.setText("Ingrese los valores");
            return;
        }



        //pasamos los valores de los edittext a variables int
        int nro1 = Integer.parseInt(valor1);
        int nro2 = Integer.parseInt(valor2);

        //Creamos una variable cuyo valor va a ser la opción
        //seleccionada del spinner convertida a un String
        String selec = spinner1.getSelectedItem().toString();

        /*Evaluamos el valor de la opción seleccionada y en función
        * de eso se evalua en los if anidados que condición es y lo
        * que debe hacer internamente*/
        if (selec.equals("Sumar")) {
            int suma = nro1 + nro2;
            //a la variable resu le damos el valor de la suma
            //pero como un string
            String resu = String.valueOf(suma);
            //seteamos el valor de el text view 3 con el valor
            //de la variable resu
            tv3.setText(String.valueOf(resu));
        } else if (selec.equals("Restar")) {
            int resta = nro1 - nro2;
            //a la variable resu le damos el valor de la resta
            //pero como un string
            String resu = String.valueOf(resta);
            //seteamos el valor de el text view 3 con el valor
            //de la variable resu
            tv3.setText(String.valueOf(resu));
        } else if (selec.equals("Multiplicar")) {
            int mult = nro1 * nro2;
            //a la variable resu le damos
            // el valor de la multiplicación
            String resu = String.valueOf(mult);
            //seteamos el valor de el text view 3 con el valor
            //de la variable resu
            tv3.setText(String.valueOf(resu));
        } else if (selec.equals("Dividir")) {
            //validamos denominador cero antes de hacer el casting
            //si porque primero entran las variables y luego se asignan
            //a la variable de la operacion division
            if (nro2 == 0){
                tv3.setText("No se puede dividir por cero");
                return;
            }
            //parseamos los valores a double para poder
            // hacer la división
            double div = (double) nro1 / (double) nro2;
            //a la variable resu le damos
            // el valor de la división
            String resu = String.valueOf(div);
            //seteamos el valor de el text view 3 con el valor
            //de la variable resu
            tv3.setText(String.valueOf(resu));
        }
    }
}