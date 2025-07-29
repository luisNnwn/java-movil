package com.example.proyecto_03_controlcheckbox;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.CheckBox;

public class MainActivity extends AppCompatActivity {

    //declaración de variables
    private EditText et1, et2;
    private TextView tv3;
    private CheckBox checkBox1, checkBox2, checkBox3, checkBox4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        //traspaso de variables
        et1 = (EditText) findViewById(R.id.et1);
        et2 = (EditText) findViewById(R.id.et2);
        tv3 = (TextView) findViewById(R.id.tv3);
        checkBox1 = (CheckBox) findViewById(R.id.checkBox);
        checkBox2 = (CheckBox) findViewById(R.id.checkBox2);
        checkBox3 = (CheckBox) findViewById(R.id.checkBox3);
        checkBox4 = (CheckBox) findViewById(R.id.checkBox4);


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void operar (View view){
        String valor1 = et1.getText().toString();
        String valor2 = et2.getText().toString();
        /*Aca manejo también el caso en que el usuario no ingrese
         * algun valor para evitar que se cierre por no tener
         * definido este caso
         *
         * Lo había puesto debajo pero de String resu, pero no iba a funcionar la validacion
         * tenía que tomar en cuenta el flujo del programa.
         *
         * Primero tomo los valores, si están vacíos van al if, si no, se pasan
         * a int porque para hacer las operaciones necesito que estén como
         * números*/
        if (et1.getText().toString().isEmpty() || et2.getText().toString().isEmpty()){
            tv3.setText("Ingrese los valores");
            return;
        }
        /*Antes de hacer algo tambien me tengo que
         * asegurar que esté selecionado al menos una opción
         * así que evaluo la negación de cada checkbox*/
        if (!checkBox1.isChecked() && !checkBox2.isChecked()
                && !checkBox3.isChecked() && !checkBox4.isChecked()){
            tv3.setText("Seleccione al menos una opción");
            return;
        }
        //pd: esto no acepta vacio, por eso lo de arriba
        int nro1 = Integer.parseInt(valor1);
        int nro2 = Integer.parseInt(valor2);
        String resu = "";

        /*Nota:
        * 1. Tomar los string de los edittext
        * 2. Validar campos vacios
        * 3. Parsear los string a int*/

        if (checkBox1.isChecked()){
            int suma = nro1 + nro2;
            resu = "La suma es: " + suma;
        }
        if (checkBox2.isChecked()){
            int resta = nro1 - nro2;
            //agregue los saltos de linea para que se vea mejor
            resu = resu + "\nLa resta es: " + resta;
        }
        if (checkBox3.isChecked()){
            int mult = nro1 * nro2;
            resu = resu + "\nLa multiplicación es: " + mult;
        }
        if (checkBox4.isChecked()){
            //nos aseguramos de tener una condicion que evalue
            //la division por cero para evitar que se cierre la app
            //por no tener definida la situación
            if (nro2 == 0){
                //aca se va a seguir mostrando concatenadas las
                //operaciones seleccionadas
                tv3.setText(resu + "\nNo se puede dividir por cero");
                return;
            }
            //casting a double por si no es exacta la division
            double div = (double) nro1 / (double) nro2;
            resu = resu + "\nLa división es: " + div;
        }
        /*Esta linea fuera  nos ahorra repetirla, dado que al finalizar el switch, resu ya va a tener
         * un valor, entonces solo necesitamos setearlo una vez aca afuera
         * porque ya va a tener el valor que corresponde*/
        tv3.setText(resu);
    }
}