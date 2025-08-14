package com.example.proyecto05_controllistview;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

//imports del ejercicio
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView;
import android.widget.TextView;
import android.view.View;
//Libreria para el Toast
import android.widget.Toast;

/*Clase principal del programa*/
public class MainActivity extends AppCompatActivity {

    /*Declaracion de los arreglos y variables*/
    private String [] paises = {"Argentina","Bahamas","Barbados","Bélice","Bolivia","Brasil","Cánada","Chile","Colombia","Cost Rica", "Cuba",
    "Dominica", "Ecuador", "El Salvador", "Estados Unidos", "Guatemala", "Guyana", "Haití", "Honduras", "Jamaica", "México", "Nicaraua", "Panama",
        "Paraguay", "Perú", "República dominicana", "San Cristóbal y Nieves", "Santa Lucía", "Surinam", "Trinidad y Tobago", "Uruguay", "Venezuela",
        "Alaska (estado de EE.UU)", "Hawai (estado de EE.UU)", "Puerto Rico (territorio EE.UU)", "Guam (territorio EE.UU)",
        "Islas Marianas del Norte (territorio EE.UU)", "Samoa Americana (territorio EE.UU)"};
    private String[] habitantes = {
            "45851378", "403033", "282623", "422924", "12581843", "212000000", "40126723", "19859921", "53425635", "5152950", "11000000", "75000",
            "18000000", "6500000", "339979847", "18800000", "800000", "11000000", "11000000", "3000000", "130000000", "7000000", "4500000", "7000000",
            "34000000", "11000000", "50000", "200000", "600000", "1400000", "3470000", "28516896", "733391", "1446000", "3300000", "170000", "51000",
            "44000"};

    private TextView tv1;
    private ListView lv1;

    //constructor de la clase
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        /*Relación entre el layout y la clase*/
        setContentView(R.layout.activity_main);

        /*Traspaso de variables*/
        /*A las variables tv1 y lv1 creadas en la clase se les relaciona
         * con los elementos del layout*/
        tv1 = (TextView) findViewById(R.id.tv1);
        lv1 = (ListView) findViewById(R.id.lv1);

        /*Acá instanciamos un objeto adaptador, y le pasamos como parámetros el contexto, el layout
         * , el tipo de objeto y también el arreglo de paises*/
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, paises);
        //Asignamos el adaptador a la lista, a través del metodo setAdapter
        lv1.setAdapter(adapter);

        /*aca hacemos uso del metodo setOnItemClickListener de la lista, el cual se dispara
        * cuando seleccionamos uno de los elementos de la lista
        * le pasamos varios parametros, el parent, view, position y id*/
        lv1.setOnItemClickListener((parent, view, position, id) -> {
            /*Aca le asignamos el contenido que va a ir teniendo el tv1 a través del metodo textView
            *, dentro de la asignacion concatenamos lv1 y tomamos la posicion del elemento seleccionado
            * y al final volvemos a concatenar la posicion con el arreglo de habitantes*/
            tv1.setText("Poblacion de " + lv1.getItemAtPosition(position) + " es " + habitantes[position]);
            /*Linea para mostrar un Toast
            * 1. Hacer primero el import de la clase Toast
            * 2. Instanciamos un objeto Toast
            * 3. Usamos el metodo makeText(contexto, texto, duracion), teniendo en cuenta
            * los parámetros que espera el metodo de acuerdo a la documentación
            * 4. Usamos el metodo show para poder mostrar el toast.
            *
            * NOTA: podemos configurar la duración del toast con el parametro duration,
            * pero usando solamente a través de hacer uso de las constantes de la clase que son
            * LENGTH_SHORT y LENGTH_LONG
            * */
            Toast.makeText(this, "Poblacion de " + lv1.getItemAtPosition(position) + " es " + habitantes[position], Toast.LENGTH_SHORT).show();
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}
