package com.example.actividad_2;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

//librerias
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.RadioGroup;

public class MainActivity extends AppCompatActivity {

    private EditText et1, et2;
    private TextView tv3;
    private RadioButton r1, r2, r3, r4;

    //Creamos la variable con la que se evaluara la seleccion del switch
    private int opcion;

    //constructor de la clase
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        //relacion de la vista del xml con la clase
        setContentView(R.layout.activity_main);

        //traspaso de variables
        et1 = (EditText)findViewById(R.id.et1);
        et2 = (EditText)findViewById(R.id.et2);
        tv3 = (TextView)findViewById(R.id.tv3);
        r1 = (RadioButton)findViewById(R.id.radioButton);
        r2 = (RadioButton)findViewById(R.id.radioButton2);
        //importante agregar el id del radiobutton que faltaban
        r3 = (RadioButton)findViewById(R.id.radioButton3);
        r4 = (RadioButton)findViewById(R.id.radioButton4);



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    /*Metodo que se ejecuta para realizar las operaciones*/

    /*Se cambio la estructura de la funcion para que se pueda manejar
    * un switch que evalue el estado de los radioButtons, para ello
    * primero creamos una variable opcion hicimos un if, para que,
    * al estar seleccionado un radiobutton tomara distintos valores.
    *
    * Luego esos valores de opción, en función de la selección, se evaluan
    * en un switch, dentro del cual esta la logica de las operaciones
    * */
    public void operar(View v){

        /*se asigna el valor introducido a la variable y se convierte
         * a String, haciendo uso de los metodos:
         * - GEThText() para obtener el valor
         * - toString() para convertirlo a String*/
        String valor1 = et1.getText().toString();
        String valor2 = et2.getText().toString();
       /*se parsean los valores a int para poder
        realizar las operaciones, haciendo uso de la clase
        Integer y accediendo al metodo parseInt para poder
        convertir*/
        int nro1 = Integer.parseInt(valor1);
        int nro2 = Integer.parseInt(valor2);

        /*Primero evaluamos el estado de los radioButtons
        * y de acuerdo al estado, se asgina el valor de
        * la variable oopcion que vamos a evaluar en el switch*/
        if (r1.isChecked()){
            opcion = 1;
        } else if (r2.isChecked()){
            opcion = 2;
        } else if (r3.isChecked()){
            opcion = 3;
        } else if (r4.isChecked()) {
            opcion = 4;
        } else {
            System.out.println("Seleccione una opcion valida");
        }

        switch (opcion){
            /*Para todos los casos, primero se crea una variable. Dicha variable
            * tiene como valor la operación que se hace entre los dos numeros,
            * la cual se determina por el signo. Luego creamos una variable String
            * para poder obtener el valor obtenido de la suma, y lo convertimos a un
            * String haciendo uso del metodo valueOf, y le pasamos el valor de
            * la variable de la operación. Y finalmente, seteamos el valor ya convertido
            * a String a tv3 para que se muestre el resultado.*/
            case 1:
                int suma = nro1 + nro2;
                String resu = String.valueOf(suma);
                tv3.setText(resu);
                break;
            case 2:
                int resta = nro1 - nro2;
                String resu2 = String.valueOf(resta);
                tv3.setText(resu2);
                break;
            case 3:
                int multiplicacion = nro1 * nro2;
                String resu3 = String.valueOf(multiplicacion);
                tv3.setText(resu3);
                break;
            case 4:
                /*Se hace el casting de la variable a double*/
                double division = (double) nro1 / nro2;
                String resu4 = String.valueOf(division);
                tv3.setText(resu4);
                break;
            default:
                tv3.setText("Error");
                break;
        }

    }

    /*

    int opcion;
    * if (r1.isChecked()){
            opcion = 1;
        } else if (r2.isChecked()){
            opcion = 2;
        } else if (r3.isChecked()){
            opcion = 3;
        } else {
            opcion = 4;
        }
    * }

    switch (opcion){
        case 1:
        int suma = nro1 + nro2;
        String resu = String.valueOf(suma);
        tv3.setText(resu);
        break;
        case 2:
        int resta = nro1 - nro2;
        String resu = String.valueOf(resta);
        tv3.setText(resu);
        break;
        case 3:
        int multiplicacion = nro1 * nro2;
        String resu = String.valueOf(multiplicacion);
        tv3.setText(resu);
        break;
        case 4:
        int division = nro1 / nro2;
        String resu = String.valueOf(division);
        tv3.setText(resu);
        break;
        default:
        tv3.setText("Error");
    }

    * */
}