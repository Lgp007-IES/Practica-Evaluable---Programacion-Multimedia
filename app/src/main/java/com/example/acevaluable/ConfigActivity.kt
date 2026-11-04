package com.example.acevaluable

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class ConfigActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_config)

        val etNombre = findViewById<EditText>(R.id.etNombreConfig)
        val etTelefono = findViewById<EditText>(R.id.etTelefonoConfig)
        val etUrl = findViewById<EditText>(R.id.etUrlConfig)
        val btnGuardar = findViewById<Button>(R.id.btnGuardarConfig)

        // Cargar Preferencias Compartidas (SharedPreferences)
        val prefs = getSharedPreferences("PreferenciasOneShot", Context.MODE_PRIVATE)
        etNombre.setText(prefs.getString("CLAVE_NOMBRE", "Niko"))
        etTelefono.setText(prefs.getString("CLAVE_TELEFONO", "5555054455"))
        etUrl.setText(prefs.getString("CLAVE_URL", "https://oneshot.fandom.com"))

        // Guardar en SharedPreferences y pasar datos con putExtra
        btnGuardar.setOnClickListener {
            val nombre = etNombre.text.toString().trim()
            val telefono = etTelefono.text.toString().trim()
            val url = etUrl.text.toString().trim()

            if (nombre.isEmpty() || telefono.isEmpty() || url.isEmpty()) {
                Toast.makeText(this, "Completa todos los parámetros del sistema", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Guardado en SharedPreferences
            val editor = prefs.edit()
            editor.putString("CLAVE_NOMBRE", nombre)
            editor.putString("CLAVE_TELEFONO", telefono)
            editor.putString("CLAVE_URL", url)
            editor.apply()

            // Paso de datos al MainActivity usando Intent Explícito
            val intent = Intent(this, MainActivity::class.java).apply {
                putExtra("EXTRA_NOMBRE", nombre)
                putExtra("EXTRA_TELEFONO", telefono)
                putExtra("EXTRA_URL", url)
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            }
            startActivity(intent)
        }
    }
}