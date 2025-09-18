package com.example.proyecto15_calculadorabasica;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import java.util.Stack;

public class MainActivity extends AppCompatActivity {

    EditText editText;
    StringBuilder expression = new StringBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.txtPantalla);
    }

    public void onDigitClick(View view) {
        Button button = (Button) view;
        expression.append(button.getText().toString());
        editText.setText(expression.toString());
    }

    public void onOperatorClick(View view) {
        Button button = (Button) view;
        String op = button.getText().toString();

        //convertir simbolos visuales a operadores matematicos
        if (op.equals("x")) op = "*";
        if (op.equals("÷")) op = "/";

        //evita operadores duplicados al final
        if (expression.length() > 0 && !isOperator(expression.charAt(expression.length() - 1))) {
            expression.append(op);
            editText.setText(expression.toString());
        }
    }

    public void onEqualsClick(View view) {
        try {
            double result = evaluateExpression(expression.toString());
            //mostrar resultado sin decimales si es entero
            if (result == (int) result) {
                editText.setText(String.valueOf((int) result));
            } else {
                editText.setText(String.valueOf(result));
            }
            expression.setLength(0);
            expression.append(editText.getText().toString());

        } catch (Exception e) {
            editText.setText("Error");
            expression.setLength(0);
        }
    }

    public void onClearClick(View view) {
        expression.setLength(0);
        editText.setText("");
    }

    private boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/';
    }

    //metodo para evaluar expresiones matemáticas con multiples operaciones
    private double evaluateExpression(String expression) {
        //Eliminar espacios y verificar si está vacía
        String expr = expression.replaceAll("\\s+", "");
        if (expr.isEmpty()) return 0;

        Stack<Double> numbers = new Stack<>();
        Stack<Character> operators = new Stack<>();

        for (int i = 0; i < expr.length(); i++) {
            char c = expr.charAt(i);

            //si es un digito, leer el numero completo
            if (Character.isDigit(c) || c == '.') {
                StringBuilder numStr = new StringBuilder();
                while (i < expr.length() && (Character.isDigit(expr.charAt(i)) || expr.charAt(i) == '.')) {
                    numStr.append(expr.charAt(i));
                    i++;
                }
                i--; //retroceder porque el for también incrementa
                numbers.push(Double.parseDouble(numStr.toString()));
            }
            //Si es un operador
            else if (isOperator(c)) {
                while (!operators.isEmpty() && hasPrecedence(c, operators.peek())) {
                    numbers.push(applyOperation(operators.pop(), numbers.pop(), numbers.pop()));
                }
                operators.push(c);
            }
        }

        //aplicar las operaciones restantes
        while (!operators.isEmpty()) {
            numbers.push(applyOperation(operators.pop(), numbers.pop(), numbers.pop()));
        }

        return numbers.pop();
    }

    //verificar precedencia de operadores
    private boolean hasPrecedence(char op1, char op2) {
        if ((op1 == '*' || op1 == '/') && (op2 == '+' || op2 == '-')) {
            return false;
        }
        return true;
    }

    //aplicar operacion a dos operandos
    private double applyOperation(char op, double b, double a) {
        switch (op) {
            case '+':
                return a + b;
            case '-':
                return a - b;
            case '*':
                return a * b;
            case '/':
                if (b == 0) throw new ArithmeticException("División por cero");
                return a / b;
        }
        return 0;
    }

    public void onDeleteClick(View view) {
        if (expression.length() > 0) {
            expression.deleteCharAt(expression.length() - 1);
            editText.setText(expression.toString());
        }
    }

}
