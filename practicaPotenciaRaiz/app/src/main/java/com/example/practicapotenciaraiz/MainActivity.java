package com.example.practicapotenciaraiz;

import android.app.assist.AssistStructure;
import android.os.Bundle;
import android.view.TextureView;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

//imports nuevos
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;


/*Clase main activity*/
public class MainActivity extends AppCompatActivity {

    //parametros globales que voy a ocupar: et1, et2, tv3
    private EditText et1, et2;
    private TextView tv3;

    /*Constructor de la clase MainActivity*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        /*Esto es lo que conecta el layout con la activity*/
        setContentView(R.layout.activity_main);

        /*Hago el traspaso de variables... es decir, a las variables que he
        * creado acá en la clase yo le voy a ir asignando los valores que se
        * vayan insertando en el layout a través del metodo findViewById y el
        * respectivo id que tengan los objetos a utilizar del layout*/
        et1 = (EditText)findViewById(R.id.et1);
        et2 = (EditText)findViewById(R.id.et2);
        tv3 = (TextView)findViewById(R.id.tv3);


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    /*METODOS PARA HACER LAS OPERACIONES*/
    public void potencia(View view) {
        /*Los valores inregsados en los campos de texto los
        * paso a String, así:
        * - Primero y correspondiendo con el id del campo de texto
        *  obtengo los valores con getText()
        * - Luego con el metodo ToString yo lo convieto a un String*/
        String num1 = et1.getText().toString();
        String num2 = et2.getText().toString();
        /*Antes de parsear tengo que validar que los campos
        * no estén vacíos porque cuando se ingresan los valores
        * se le asignan a la variable donde parseo, pero el metodo
        * parseDouble me va a devolver un error de NumericFormatException
        * porque espera una variable con contenido, no acepta vacios*/
        if (num1.isEmpty() || num2.isEmpty()) {
            tv3.setText("Por favor, ingrese rellene todos los campos");
            //retorno
            return;
        }
        /*Ahora si pareseo los valores*/
        //parseo la base, el numero que vamos a elevar a la potencia
        double base = Double.parseDouble(num1);
        //parseo el exponente
        double exponente = Double.parseDouble(num2);
        //ahora creo una vairable resultado
        //cuyo valor es el resultado de la operacion
        //para la operación, yo ocupo la clase Math y
        //el metoto pow y le paso los dos valores que
        //necesita para hacer la operación
        double resultado = Math.pow(base, exponente);
        tv3.setText("El numero " + base + " elevado a la " + exponente + " es: " + resultado);
    }

    /*Este metodo es para deshabilitar el segundo editText
    * ya que para la raiz cuadrada solo necesito un campo habilitado
    * y es lo que se me ocurrió en este momento*/
    public void deshabilitar(View view) {
        et2.setEnabled(false);
    }

    public void raizCuadrada (View view) {
        String num1 = et1.getText().toString();
        /*Valido los campos vacios*/
        if (num1.isEmpty()) {
            tv3.setText("Por favor, ingrese rellene todos los campos");
            //retorno
            return;
        }
        /*Parseo a double porque algunas raices cuadradas no son exactas*/
        Double numero = Double.parseDouble(num1);
        /*Validacion para numeros negativo, valido despues de parsear
        * porque numero existe hasta que se ingrese el numero jaja */
        if(numero < 0) {
            tv3.setText("No puede ingresar numeros negativos");
            //retorno
            return;
        }
        double resultado = Math.sqrt(numero);
        tv3.setText("La raiz cuadrada de " + numero + " es: " + resultado);
    }
}