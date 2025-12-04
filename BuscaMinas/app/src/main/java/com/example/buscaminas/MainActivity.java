package com.example.buscaminas;

// Reemplaza tus importaciones con estas si migras a AndroidX
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color; // Importar Color
import android.graphics.Paint;
import android.graphics.RectF; // Importar RectF
import android.graphics.Typeface;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

// Usa AppCompatActivity en lugar de la versión de soporte obsoleta
public class MainActivity extends AppCompatActivity implements View.OnTouchListener {

    private Tablero fondo;
    private Casilla[][] casillas;
    private boolean activo = true;

    // Para la gestión del sonido
    private SoundPool soundPool;
    private int sonidoExplosionId;

    // La clase Casilla debe estar definida, preferiblemente como una clase interna.
    class Casilla {
        int x, y, ancho;
        int contenido = 0; // 0:vacío, 1-8:pista, 80:bomba
        boolean destapado = false;
        // RectF para evitar crear objetos nuevos en onDraw
        RectF rect = new RectF();

        public void fijarxy(int x, int y, int ancho) {
            this.x = x;
            this.y = y;
            this.ancho = ancho;
            rect.set(x, y, x + ancho, y + ancho);
        }

        public boolean dentro(int clickX, int clickY) {
            return clickX >= this.x && clickX <= this.x + this.ancho &&
                    clickY >= this.y && clickY <= this.y + this.ancho;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Configuraciones de la ventana
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Ocultar la barra de acción si existe
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        LinearLayout layout = findViewById(R.id.layout1); // No es necesario castear
        fondo = new Tablero(this);
        fondo.setOnTouchListener(this);
        layout.addView(fondo);

        // --- INICIO: CAMBIOS PARA SONIDO ---
        // Inicializar SoundPool para la reproducción de audio
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();
        soundPool = new SoundPool.Builder()
                .setMaxStreams(2) // Podemos reproducir hasta 2 sonidos a la vez
                .setAudioAttributes(audioAttributes)
                .build();

        // Cargar el sonido desde la carpeta res/raw
        sonidoExplosionId = soundPool.load(this, R.raw.sonidoperder, 1);
        // --- FIN: CAMBIOS PARA SONIDO ---

        inicializarJuego();
    }

    private void inicializarJuego() {
        casillas = new Casilla[8][8];
        for (int f = 0; f < 8; f++) {
            for (int c = 0; c < 8; c++) {
                casillas[f][c] = new Casilla();
            }
        }
        this.disponerBombas();
        this.contarBombasPerimetro();
        activo = true;
    }

    public void reiniciar(View v) {
        inicializarJuego();
        // Invalida la vista para forzar el redibujado
        fondo.invalidate();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (activo && event.getAction() == MotionEvent.ACTION_DOWN) {
            for (int f = 0; f < 8; f++) {
                for (int c = 0; c < 8; c++) {
                    if (casillas[f][c].dentro((int) event.getX(), (int) event.getY())) {
                        if (!casillas[f][c].destapado) {
                            casillas[f][c].destapado = true;
                            if (casillas[f][c].contenido == 80) {
                                // --- INICIO: CAMBIO PARA REPRODUCIR SONIDO ---
                                // Reproducir el sonido de explosión si está cargado
                                if (soundPool != null) {
                                    soundPool.play(sonidoExplosionId, 1, 1, 1, 0, 1);
                                }
                                // --- FIN: CAMBIO PARA REPRODUCIR SONIDO ---
                                Toast.makeText(this, "Boooooom!", Toast.LENGTH_SHORT).show();
                                activo = false;
                            } else if (casillas[f][c].contenido == 0) {
                                recorrer(f, c);
                            }
                            fondo.invalidate();

                            if (gano() && activo) {
                                Toast.makeText(this, "¡Ganaste!", Toast.LENGTH_LONG).show();
                                activo = false;
                            }
                        }
                        return true; // Salimos del bucle una vez encontrada la casilla
                    }
                }
            }
        }
        return true;
    }

    private void disponerBombas() {
        int cantidad = 8;
        while (cantidad > 0) {
            int fila = (int) (Math.random() * 8);
            int columna = (int) (Math.random() * 8);
            if (casillas[fila][columna].contenido == 0) {
                casillas[fila][columna].contenido = 80;
                cantidad--;
            }
        }
    }

    private boolean gano() {
        int cant = 0;
        for (int f = 0; f < 8; f++) {
            for (int c = 0; c < 8; c++) {
                if (casillas[f][c].destapado) {
                    cant++;
                }
            }
        }
        // Ganas si has destapado todas las casillas que no son bombas
        return cant == 56;
    }

    private void contarBombasPerimetro() {
        for (int f = 0; f < 8; f++) {
            for (int c = 0; c < 8; c++) {
                if (casillas[f][c].contenido == 0) {
                    int cant = contarCoordenada(f, c);
                    casillas[f][c].contenido = cant;
                }
            }
        }
    }

    int contarCoordenada(int fila, int columna) {
        int total = 0;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0) continue; // No contar la casilla misma
                int fVecino = fila + i;
                int cVecino = columna + j;
                if (fVecino >= 0 && fVecino < 8 && cVecino >= 0 && cVecino < 8) {
                    if (casillas[fVecino][cVecino].contenido == 80) {
                        total++;
                    }
                }
            }
        }
        return total;
    }

    private void recorrer(int fil, int col) {
        // Condición para evitar salir de los límites y recursión infinita
        if (fil < 0 || fil >= 8 || col < 0 || col >= 8 || casillas[fil][col].destapado) {
            return;
        }

        casillas[fil][col].destapado = true;

        // Si la casilla está vacía (0 bombas alrededor), sigue explorando
        if (casillas[fil][col].contenido == 0) {
            recorrer(fil - 1, col - 1);
            recorrer(fil - 1, col);
            recorrer(fil - 1, col + 1);
            recorrer(fil, col - 1);
            recorrer(fil, col + 1);
            recorrer(fil + 1, col - 1);
            recorrer(fil + 1, col);
            recorrer(fil + 1, col + 1);
        }
    }

    // --- INICIO: NUEVO MÉTODO onDestroy ---
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Liberar los recursos de SoundPool para evitar fugas de memoria
        if (soundPool != null) {
            soundPool.release();
            soundPool = null;
        }
    }
    // --- FIN: NUEVO MÉTODO onDestroy ---

    // La clase Tablero debe ser una clase interna de MainActivity
    class Tablero extends View {
        private Paint paintCasillaTapada = new Paint();
        private Paint paintCasillaDestapada = new Paint();
        private Paint paintBordeClaro = new Paint();
        private Paint paintBordeOscuro = new Paint();
        private Paint paintBordeHundido = new Paint();
        private Paint paintNumero = new Paint();
        private Paint paintBomba = new Paint();
        private Paint paintBombaNucleo = new Paint();

        public Tablero(Context context) {
            super(context);
            // Colores de las casillas
            paintCasillaTapada.setColor(Color.rgb(192, 192, 192));
            paintCasillaDestapada.setColor(Color.rgb(220, 220, 220));

            // Colores para el efecto de biselado (3D)
            paintBordeClaro.setColor(Color.WHITE);
            paintBordeClaro.setStyle(Paint.Style.STROKE);
            paintBordeOscuro.setColor(Color.rgb(128, 128, 128));
            paintBordeOscuro.setStyle(Paint.Style.STROKE);
            paintBordeHundido.setColor(Color.rgb(128, 128, 128));
            paintBordeHundido.setStyle(Paint.Style.STROKE);

            // Paint para el número
            paintNumero.setTextSize(60);
            paintNumero.setTypeface(Typeface.DEFAULT_BOLD);
            paintNumero.setColor(Color.BLUE);
            paintNumero.setAntiAlias(true);

            // Paint para la bomba
            paintBomba.setColor(Color.BLACK);
            paintBomba.setAntiAlias(true);
            paintBombaNucleo.setColor(Color.RED);
            paintBombaNucleo.setAntiAlias(true);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawColor(Color.rgb(192, 192, 192)); // Fondo general
            int dimensionCelda = getWidth() / 8;
            int grosorBorde = Math.max(2, dimensionCelda / 16);
            paintBordeClaro.setStrokeWidth(grosorBorde);
            paintBordeOscuro.setStrokeWidth(grosorBorde);
            paintBordeHundido.setStrokeWidth(grosorBorde/2f);
            paintNumero.setTextSize(dimensionCelda * 0.6f);

            for (int f = 0; f < 8; f++) {
                for (int c = 0; c < 8; c++) {
                    casillas[f][c].fijarxy(c * dimensionCelda, f * dimensionCelda, dimensionCelda);
                    RectF rect = casillas[f][c].rect;

                    if (!casillas[f][c].destapado) {
                        // --- ESTILO CASILLA SIN DESTAPAR (Efecto elevado) ---
                        canvas.drawRect(rect, paintCasillaTapada);
                        // Borde claro (arriba e izquierda)
                        canvas.drawLine(rect.left, rect.top, rect.right, rect.top, paintBordeClaro);
                        canvas.drawLine(rect.left, rect.top, rect.left, rect.bottom, paintBordeClaro);
                        // Borde oscuro (abajo y derecha)
                        canvas.drawLine(rect.right, rect.top, rect.right, rect.bottom, paintBordeOscuro);
                        canvas.drawLine(rect.left, rect.bottom, rect.right, rect.bottom, paintBordeOscuro);

                    } else {
                        // --- ESTILO CASILLA DESTAPADA ---
                        canvas.drawRect(rect, paintCasillaDestapada);

                        if (casillas[f][c].contenido >= 1 && casillas[f][c].contenido <= 8) {
                            // Cambiar color del número según el riesgo
                            establecerColorNumero(casillas[f][c].contenido);
                            // Centrar texto
                            float textX = rect.centerX() - paintNumero.measureText(String.valueOf(casillas[f][c].contenido)) / 2;
                            float textY = rect.centerY() - ((paintNumero.descent() + paintNumero.ascent()) / 2);
                            canvas.drawText(String.valueOf(casillas[f][c].contenido), textX, textY, paintNumero);
                        } else if (casillas[f][c].contenido == 80) {
                            // Dibujar una bomba más bonita
                            canvas.drawCircle(rect.centerX(), rect.centerY(), dimensionCelda * 0.35f, paintBomba);
                            canvas.drawCircle(rect.centerX(), rect.centerY(), dimensionCelda * 0.1f, paintBombaNucleo);
                        } else {
                            // Casilla vacía destapada, efecto hundido
                            canvas.drawLine(rect.left, rect.top, rect.right, rect.top, paintBordeHundido);
                        }
                    }
                }
            }
        }

        private void establecerColorNumero(int numero) {
            switch (numero) {
                case 1: paintNumero.setColor(Color.BLUE); break;
                case 2: paintNumero.setColor(Color.rgb(0, 100, 0)); break; // Verde oscuro
                case 3: paintNumero.setColor(Color.RED); break;
                case 4: paintNumero.setColor(Color.rgb(0, 0, 139)); break; // Azul oscuro
                case 5: paintNumero.setColor(Color.rgb(139, 0, 0)); break; // Marrón
                default: paintNumero.setColor(Color.rgb(255, 120, 0)); break; // Naranja
            }
        }
    }
}
