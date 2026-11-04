package com.example.acevaluable

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class CallActivity : AppCompatActivity() {

    private var numeroTelefono: String = ""

    companion object {
        private const val REQUEST_CODE_CALL_PHONE = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_call)

        val tvTitulo = findViewById<TextView>(R.id.tvTituloLlamada)
        val tvNumero = findViewById<TextView>(R.id.tvNumeroLlamada)
        val btnEjecutarLlamada = findViewById<ImageButton>(R.id.btnEjecutarLlamada)
        val btnVolverMain = findViewById<Button>(R.id.btnVolverMain)

        // Recibir los datos pasados por putExtra desde MainActivity
        val nombre = intent.getStringExtra("EXTRA_NOMBRE") ?: "Niko"
        numeroTelefono = intent.getStringExtra("EXTRA_TELEFONO") ?: "600123456"

        tvTitulo.text = "LÍNEA DIRECTA DE ${nombre.uppercase()}"
        tvNumero.text = "Tel: $numeroTelefono"

        // Al pulsar el botón de llamada, verificamos el nivel de API
        btnEjecutarLlamada.setOnClickListener {
            verificarPermisosYLlamar()
        }

        btnVolverMain.setOnClickListener {
            finish() // Regresa al MainActivity
        }
    }

    private fun verificarPermisosYLlamar() {
        // Punto 5: Diferenciar entre API inferior a 23 y API >= 23 (Marshmallow)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            // API < 23: El permiso ya se concedió al instalar la app desde el Manifest
            Toast.makeText(this, "API < 23 detectada: Llamando sin pedir permiso en ejecución", Toast.LENGTH_SHORT).show()
            realizarLlamadaDirecta()
        } else {
            // API >= 23: Es obligatorio verificar y solicitar el permiso en tiempo de ejecución
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                realizarLlamadaDirecta()
            } else {
                // Solicitamos el permiso al usuario
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.CALL_PHONE),
                    REQUEST_CODE_CALL_PHONE
                )
            }
        }
    }

    private fun realizarLlamadaDirecta() {
        val intentLlamada = Intent(Intent.ACTION_CALL).apply {
            data = Uri.parse("tel:$numeroTelefono")
        }
        startActivity(intentLlamada)
    }

    // Respuesta del usuario al cuadro de diálogo de permisos (en API >= 23)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_CALL_PHONE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permiso concedido. Iniciando llamada...", Toast.LENGTH_SHORT).show()
                realizarLlamadaDirecta()
            } else {
                Toast.makeText(this, "Permiso denegado: No se puede realizar la llamada.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}