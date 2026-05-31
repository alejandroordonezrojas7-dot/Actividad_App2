package com.example.app2

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 1. Vinculación directa con tu XML actual
        val etCorreo = findViewById<EditText>(R.id.et_correo)
        val etContrasena = findViewById<EditText>(R.id.et_contrasena)
        val btnIngresar = findViewById<Button>(R.id.btn_ingresar)
        val spRoles = findViewById<Spinner>(R.id.sp_roles)

        var rolSeleccionado = ""

        // 2. Configuración de la lista desplegable (Spinner)
        val opcionesRoles = arrayOf("Seleccionar rol...", "Estudiante UAEM", "Profesor", "Administrativo")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, opcionesRoles)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spRoles.adapter = adapter

        spRoles.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                rolSeleccionado = opcionesRoles[position]
                if (position > 0) {
                    Toast.makeText(applicationContext, "Perfil: $rolSeleccionado", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        // 3. Escuchador en tiempo real (Limpia los errores rojos mientras el usuario escribe)
        val validacionEnTiempoReal = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!etCorreo.text.isNullOrBlank()) etCorreo.error = null
                if (!etContrasena.text.isNullOrBlank()) etContrasena.error = null
            }
            override fun afterTextChanged(s: Editable?) {}
        }
        etCorreo.addTextChangedListener(validacionEnTiempoReal)

        // 4. Acción al pulsar el botón Ingresar
        btnIngresar.setOnClickListener {
            val correo = etCorreo.text.toString().trim()
            val contrasena = etContrasena.text.toString().trim()

            // Validación A: Campos Vacíos
            if (correo.isEmpty()) {
                etCorreo.error = "El correo no puede quedar vacío"
                Toast.makeText(this, "Por favor llena todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (contrasena.isEmpty()) {
                etContrasena.error = "La contraseña es obligatoria"
                Toast.makeText(this, "Por favor llena todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Validación B: Formato de correo (Debe incluir una @)
            if (!correo.contains("@")) {
                etCorreo.error = "Formato de correo electrónico inválido"
                Toast.makeText(this, "Correo inválido", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Validación C: Selección de rol de la lista
            if (rolSeleccionado == "Seleccionar rol...") {
                Toast.makeText(this, "Por favor selecciona tu perfil universitario", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            // Éxito: Navegación hacia la segunda pantalla
            Toast.makeText(this, "¡Bienvenido! Iniciando sesión...", Toast.LENGTH_SHORT).show()

            val intento = Intent(this, HomeActivity::class.java)
            intento.putExtra("ROL_USUARIO", rolSeleccionado)
            startActivity(intento)
        }
    }
}