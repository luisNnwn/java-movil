package com.example.aplicardescuentos;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

//imports necesarios
import android.widget.EditText;
import android.widget.TextView;
import android.widget.CheckBox;
import android.view.View;

//clase main donde se ejecutara el programa
public class MainActivity extends AppCompatActivity {

    /*Parametros globales a ocupar en la clase*/
    private EditText et1, et2;
    //Entiendase cBx como CheckBox
    private CheckBox cBx1, cBx2, cBx3, cBx4;
    private TextView tv3;

    //constructor de la clase main
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        //Enlace entre la clase y el xml
        setContentView(R.layout.activity_main);

        /*Traspaso de variables*/
        et1 = (EditText) findViewById(R.id.et1);
        et2 = (EditText) findViewById(R.id.et2);
        cBx1 = (CheckBox) findViewById(R.id.cBx1);
        cBx2 = (CheckBox) findViewById(R.id.cBx2);
        cBx3 = (CheckBox) findViewById(R.id.cBx3);
        cBx4 = (CheckBox) findViewById(R.id.cBx4);
        tv3 = (TextView) findViewById(R.id.tv3);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void calcularDescuento (View view){

        String precioBase = et1.getText().toString();
        String cantidad = et2.getText().toString();
        //validacion de campos vacios
        if (precioBase.isEmpty() || cantidad.isEmpty()){
            tv3.setText("Por favor complete los campos");
            return;
        }
        //validacion de que los campos sean numeros
        if (!precioBase.matches("\\d+") || !cantidad.matches("\\d+")){
            tv3.setText("Por favor ingrese solo numeros");
            return;
        }
        /*Asegurandonos de seleccionar al menos un checkbox*/
        if (!cBx1.isChecked() && !cBx2.isChecked() && !cBx3.isChecked() && !cBx4.isChecked()){
            tv3.setText("Por favor seleccione al menos una opción");
            return;
        }

        //parsear los campos a enteros
        double precio = Double.parseDouble(precioBase);
        double cant = Double.parseDouble(cantidad);
        double total = precio * cant;
        String precioTotal = "";
        
        /*Validacion y calculo de descuentos*/
        if (cBx1.isChecked()){
            double descuento = total *0.10;
            total = total - descuento;
            precioTotal = "Descuento 10%: $" + total;
        }
        if (cBx2.isChecked()){
            double descuento = total *0.20;
            total = total - descuento;
            precioTotal = precioTotal + "\nDescuento 20%: $" + total;
        }
        if (cBx3.isChecked()) {
            double descuento = total * 0.13;
            total = total - descuento;
            precioTotal = precioTotal + "\nDescuento 13%: $" + total;
        }
        if (cBx4.isChecked()) {
            if (total <= 50) {
                precioTotal = precioTotal + "\nEnvío gratis";
            } else {
                total = total + 5;
                precioTotal = precioTotal + "\nCosto de envío $5: " + "$"+total;
            }
        }
        tv3.setText(precioTotal);
    }

}