package com.example.medioambiente


import android.database.Cursor
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class MainActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var tempSensor: Sensor? = null
    private var humSensor: Sensor? = null
    private var pressSensor: Sensor? = null
    private var lightSensor: Sensor? = null

    private lateinit var tvTemp: TextView
    private lateinit var tvHum: TextView
    private lateinit var tvPres: TextView
    private lateinit var tvLight: TextView
    private lateinit var tvStatus: TextView

    private lateinit var db: DatabaseHelper
    private lateinit var adapter: ReadingAdapter

    private var recording = false
    private var lastTemp = Double.NaN
    private var lastHum = Double.NaN
    private var lastPres = Double.NaN
    private var lastLight = Double.NaN

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Vistas
        tvTemp = findViewById(R.id.tvTemp)
        tvHum = findViewById(R.id.tvHum)
        tvPres = findViewById(R.id.tvPres)
        tvLight = findViewById(R.id.tvLight)
        tvStatus = findViewById(R.id.tvStatus)

        val btnStart: Button = findViewById(R.id.btnStart)
        val btnStop: Button = findViewById(R.id.btnStop)
        val btnExport: Button = findViewById(R.id.btnExportDB)

        // BD y RecyclerView
        db = DatabaseHelper(this)
        val rv: RecyclerView = findViewById(R.id.rvHistory)
        rv.layoutManager = LinearLayoutManager(this)
        adapter = ReadingAdapter()
        rv.adapter = adapter

        // Sensores
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        tempSensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE)
        humSensor = sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY)
        pressSensor = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE)
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)

        tvStatus.text = "Sensores: " +
                (if (tempSensor != null) "Temp " else "") +
                (if (humSensor != null) "Hum " else "") +
                (if (pressSensor != null) "Pres " else "") +
                (if (lightSensor != null) "Luz " else "")

        btnStart.setOnClickListener { startRecording() }
        btnStop.setOnClickListener { stopRecording() }
        btnExport.setOnClickListener { exportDB() }

        loadHistory()
    }

    private fun startRecording() {
        if (recording) return
        recording = true

        tempSensor?.let { sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL) }
        humSensor?.let { sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL) }
        pressSensor?.let { sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL) }
        lightSensor?.let { sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL) }

    }

    private fun stopRecording() {
        recording = false
        sensorManager.unregisterListener(this)
        tvStatus.text = "Estado: detenido"
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (!recording) return

        val now = System.currentTimeMillis()

        when (event.sensor.type) {
            Sensor.TYPE_AMBIENT_TEMPERATURE -> {
                lastTemp = event.values[0].toDouble()
                tvTemp.text = String.format("%.2f °C", lastTemp)
            }
            Sensor.TYPE_RELATIVE_HUMIDITY -> {
                lastHum = event.values[0].toDouble()
                tvHum.text = String.format("%.2f %%", lastHum)
            }
            Sensor.TYPE_PRESSURE -> {
                lastPres = event.values[0].toDouble()
                tvPres.text = String.format("%.2f hPa", lastPres)
            }
            Sensor.TYPE_LIGHT -> {
                lastLight = event.values[0].toDouble()
                tvLight.text = String.format("%.2f lx", lastLight)
            }
        }

        db.insertReading(
            now,
            if (lastTemp.isNaN()) null else lastTemp,
            if (lastHum.isNaN()) null else lastHum,
            if (lastPres.isNaN()) null else lastPres,
            if (lastLight.isNaN()) null else lastLight
        )

        loadHistory()
        println("Sensor detectado: ${event.sensor.name} -> ${event.values[0]}")

    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) { }

    private fun loadHistory() {
        val cursor = db.getLatestReadings(10)
        val list = ArrayList<Reading>()

        cursor.use { c ->
            while (c.moveToNext()) {
                val id = c.getLong(c.getColumnIndexOrThrow("_id"))
                val ts = c.getLong(c.getColumnIndexOrThrow("timestamp"))
                val temp = c.getDoubleOrNull("temperature")
                val hum = c.getDoubleOrNull("humidity")
                val pres = c.getDoubleOrNull("pressure")
                val light = c.getDoubleOrNull("light")

                list.add(
                    Reading(
                        id = id,
                        timestamp = ts,
                        temperature = temp,
                        humidity = hum,
                        pressure = pres,
                        light = light
                    )
                )
            }
        }

        adapter.setItems(list)
    }

    // Helper para Double nullable
    private fun Cursor.getDoubleOrNull(columnName: String): Double? {
        val idx = getColumnIndex(columnName)
        return if (idx != -1 && !isNull(idx)) getDouble(idx) else null
    }

    private fun exportDB() {
        try {
            val dbFile = getDatabasePath("env_unit.db")

            val values = android.content.ContentValues().apply {
                put(android.provider.MediaStore.Downloads.DISPLAY_NAME, "env_unit.db")
                put(android.provider.MediaStore.Downloads.MIME_TYPE, "application/octet-stream")
                put(android.provider.MediaStore.Downloads.IS_PENDING, 1)
            }

            val resolver = contentResolver
            val collection = android.provider.MediaStore.Downloads.getContentUri(android.provider.MediaStore.VOLUME_EXTERNAL_PRIMARY)
            val itemUri = resolver.insert(collection, values)

            if (itemUri != null) {
                resolver.openOutputStream(itemUri).use { output ->
                    dbFile.inputStream().use { input ->
                        input.copyTo(output!!)
                    }
                }

                values.put(android.provider.MediaStore.Downloads.IS_PENDING, 0)
                resolver.update(itemUri, values, null, null)

                Toast.makeText(this, "Base exportada en Descargas", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Error al crear archivo", Toast.LENGTH_LONG).show()
            }

        } catch (e: Exception) {
            Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

}
