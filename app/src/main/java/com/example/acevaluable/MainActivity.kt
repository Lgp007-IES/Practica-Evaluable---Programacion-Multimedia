package com.example.acevaluable

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.AlarmClock
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.Calendar

class MainActivity : AppCompatActivity() {

    private var telefonoGuardado: String = ""
    private var nombreUsuario: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tvBienvenida = findViewById<TextView>(R.id.tvBienvenida)
        val etUrlMain = findViewById<EditText>(R.id.etUrlMain)
        val btnIrLlamada = findViewById<ImageButton>(R.id.btnIrLlamada)
        val btnAbrirUrl = findViewById<ImageButton>(R.id.btnAbrirUrl)
        val btnCrearAlarma = findViewById<ImageButton>(R.id.btnCrearAlarma)
        val btnAbrirMapa = findViewById<ImageButton>(R.id.btnAbrirMapa)
        val btnVolverConfig = findViewById<Button>(R.id.btnVolverConfig)


        actualizarDatosDesdeIntent(intent, tvBienvenida, etUrlMain)

        // Llamada Telefónica -> Abre CallActivity
        btnIrLlamada.setOnClickListener {
            val intentLlamada = Intent(this, CallActivity::class.java).apply {
                putExtra("EXTRA_TELEFONO", telefonoGuardado)
                putExtra("EXTRA_NOMBRE", nombreUsuario)
            }
            startActivity(intentLlamada)
        }

        // Abrir URL -> Abre el navegador
        btnAbrirUrl.setOnClickListener {
            var urlTexto = etUrlMain.text.toString().trim()
            if (urlTexto.isEmpty()) {
                Toast.makeText(this, "Introduce una URL válida", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (!urlTexto.startsWith("http://") && !urlTexto.startsWith("https://")) {
                urlTexto = "https://$urlTexto"
            }
            val intentWeb = Intent(Intent.ACTION_VIEW, Uri.parse(urlTexto))
            startActivity(intentWeb)
        }

        // Crear Alarma para que suene en 1 minutos
        btnCrearAlarma.setOnClickListener {
            val calendario = Calendar.getInstance()
            calendario.add(Calendar.MINUTE, 1) // Sumamos 2 minutos a la hora actual
            val hora = calendario.get(Calendar.HOUR_OF_DAY)
            val minutos = calendario.get(Calendar.MINUTE)

            val intentAlarma = Intent(AlarmClock.ACTION_SET_ALARM).apply {
                putExtra(AlarmClock.EXTRA_MESSAGE, "¡Despierta $nombreUsuario, debes restaurar el Sol!")
                putExtra(AlarmClock.EXTRA_HOUR, hora)
                putExtra(AlarmClock.EXTRA_MINUTES, minutos)
                putExtra(AlarmClock.EXTRA_SKIP_UI, false) // Muestra la app de reloj confirmando la alarma
            }
            startActivity(intentAlarma)
        }

        // Abrir ubicación en Google Maps
        btnAbrirMapa.setOnClickListener {
            val geoUri = Uri.parse("geo:0,0?q=37.76922,-3.79028(Jaén)")
            val intentMapa = Intent(Intent.ACTION_VIEW, geoUri)
            startActivity(intentMapa)
        }

        // Volver al primer Activity de configuración
        btnVolverConfig.setOnClickListener {
            val intentConfig = Intent(this, ConfigActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            }
            startActivity(intentConfig)
            finish()
        }
    }
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        val tvBienvenida = findViewById<TextView>(R.id.tvBienvenida)
        val etUrlMain = findViewById<EditText>(R.id.etUrlMain)
        actualizarDatosDesdeIntent(intent, tvBienvenida, etUrlMain)
    }

    private fun actualizarDatosDesdeIntent(intentRecibido: Intent?, tvBienvenida: TextView, etUrlMain: EditText) {
        nombreUsuario = intentRecibido?.getStringExtra("EXTRA_NOMBRE") ?: "Niko"
        telefonoGuardado = intentRecibido?.getStringExtra("EXTRA_TELEFONO") ?: "600123456"
        val urlRecibida = intentRecibido?.getStringExtra("EXTRA_URL") ?: "https://oneshot.fandom.com"

        tvBienvenida.text = "CONEXIÓN: ${nombreUsuario.uppercase()}"
        etUrlMain.setText(urlRecibida)
    }
}