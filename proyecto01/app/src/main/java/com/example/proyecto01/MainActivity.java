package com.example.proyecto01;

//librerias
import android.os.Bundle;

//nuevas librerias
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import java.lang.Integer;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

/*Estructura:
 * import
 * clase
 * constructor
 * metodos*/

//clase principal
public class MainActivity extends AppCompatActivity {

    //1- declaracion de variables
    private EditText et1, et2;
    private TextView tv3;



    //constructor de la clase: direcciona a la activity main para tener los objetos disponibles en el diseño
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        //por eso apunta al xml donde están los objetos que usaremos
        setContentView(R.layout.activity_main);

        //aca se pueden hacer los traspasos de variables
        //el metodo a utilizar para hacer el traspaso se llama
        //findViewById(R.id.nombre del objeto)
        /*
        * El metodo findViewById es para el traspaso de variables
        * (R.id.et1) quiere decir a traves de fbid se le ha pasado
        * el dato a traves del mismo a las variables et1, et2 y tv3
        * */
        et1 = (EditText)findViewById(R.id.et1);
        et2 = (EditText)findViewById(R.id.et2);
        tv3 = (TextView)findViewById(R.id.tv3);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    //aca se construyen los métodos que se van a utilizar
    //metodo que se ejecuta al presionar el boton
    public void sumar(View view) {
        //definimos las variables que tomamos como texto
        //usamos los metodos getText y toString
        String valor1 = et1.getText().toString();
        String valor2 = et2.getText().toString();
        //ahora como vamos a usarlas para una operacion
        //numerica las vamos a parsear
        /*La estructura para parsear es simple, creamos una
        * nueva variable, hacemos uso de la clase Integer
        * y el metodo parseInt y le pasamos el valor
        * que queremos convertir a int*/
        int nro1 = Integer.parseInt(valor1);
        int nro2 = Integer.parseInt(valor2);
        //ahora creamos la variable en donde se va a
        //realizar la operacion de la suma
        int suma = nro1 + nro2;
        //ahora parseamos el valor numerico a un string
        //para mostrarlo como texto
        /*La estrucutura es la misma, creamos la variable
        * a la que vamos a asignar el valor convertido
        * a texto, usamos la clase String y el metodo
        * valueOf y le pasamos el valor*/
        String resu = String.valueOf(suma);
        tv3.setText(resu);
      }

    public void restar(View view) {
        //definimos las variables que tomamos como texto
        //usamos los metodos getText y toString
        String valor1 = et1.getText().toString();
        String valor2 = et2.getText().toString();
        //ahora como vamos a usarlas para una operacion
        //numerica las vamos a parsear
        /*La estructura para parsear es simple, creamos una
         * nueva variable, hacemos uso de la clase Integer
         * y el metodo parseInt y le pasamos el valor
         * que queremos convertir a int*/
        int nro1 = Integer.parseInt(valor1);
        int nro2 = Integer.parseInt(valor2);
        //ahora creamos la variable en donde se va a
        //realizar la operacion de la suma
        int resta = nro1 - nro2;
        //ahora parseamos el valor numerico a un string
        //para mostrarlo como texto
        /*La estrucutura es la misma, creamos la variable
         * a la que vamos a asignar el valor convertido
         * a texto, usamos la clase String y el metodo
         * valueOf y le pasamos el valor*/
        String resu = String.valueOf(resta);
        tv3.setText(resu);
    }

    public void multiplicacion(View view) {
        //definimos las variables que tomamos como texto
        //usamos los metodos getText y toString
        String valor1 = et1.getText().toString();
        String valor2 = et2.getText().toString();
        //ahora como vamos a usarlas para una operacion
        //numerica las vamos a parsear
        /*La estructura para parsear es simple, creamos una
         * nueva variable, hacemos uso de la clase Integer
         * y el metodo parseInt y le pasamos el valor
         * que queremos convertir a int*/
        int nro1 = Integer.parseInt(valor1);
        int nro2 = Integer.parseInt(valor2);
        //ahora creamos la variable en donde se va a
        //realizar la operacion de la suma
        int multiplicacion = nro1 * nro2;
        //ahora parseamos el valor numerico a un string
        //para mostrarlo como texto
        /*La estrucutura es la misma, creamos la variable
         * a la que vamos a asignar el valor convertido
         * a texto, usamos la clase String y el metodo
         * valueOf y le pasamos el valor*/
        String resu = String.valueOf(multiplicacion);
        tv3.setText(resu);
    }

    public void division(View view) {
        //definimos las variables que tomamos como texto
        //usamos los metodos getText y toString
        String valor1 = et1.getText().toString();
        String valor2 = et2.getText().toString();
        //ahora como vamos a usarlas para una operacion
        //numerica las vamos a parsear
        /*La estructura para parsear es simple, creamos una
         * nueva variable, hacemos uso de la clase Integer
         * y el metodo parseInt y le pasamos el valor
         * que queremos convertir a int*/
        double nro1 = Double.parseDouble(valor1);
        double nro2 = Double.parseDouble(valor2);
        //ahora creamos la variable en donde se va a
        //realizar la operacion de la suma
        double  division = nro1 / nro2;
        //ahora parseamos el valor numerico a un string
        //para mostrarlo como texto
        /*La estrucutura es la misma, creamos la variable
         * a la que vamos a asignar el valor convertido
         * a texto, usamos la clase String y el metodo
         * valueOf y le pasamos el valor*/
        String resu = String.valueOf(division);
        tv3.setText(resu);
    }
}
