package com.example.recursosdigitales;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AgregarRecursoActivity extends AppCompatActivity {

    EditText edtNombre, edtCategoria, edtDescripcion, edtEnlace, edtNivel, edtAutor;
    Button btnGuardar;
    TextView btnRegresar;

    String URL_AGREGAR = "http://10.0.2.2/recursos_api/agregar_recurso.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_recurso);

        edtNombre = findViewById(R.id.edtNombre);
        edtCategoria = findViewById(R.id.edtCategoria);
        edtDescripcion = findViewById(R.id.edtDescripcion);
        edtEnlace = findViewById(R.id.edtEnlace);
        edtNivel = findViewById(R.id.edtNivel);
        edtAutor = findViewById(R.id.edtAutor);

        btnGuardar = findViewById(R.id.btnGuardar);
        btnRegresar = findViewById(R.id.btnRegresar);

        btnRegresar.setOnClickListener(v -> finish());

        btnGuardar.setOnClickListener(v -> guardarRecurso());
    }

    private void guardarRecurso() {
        String nombre = edtNombre.getText().toString().trim();
        String categoria = edtCategoria.getText().toString().trim();
        String descripcion = edtDescripcion.getText().toString().trim();
        String enlace = edtEnlace.getText().toString().trim();
        String nivel = edtNivel.getText().toString().trim();
        String autor = edtAutor.getText().toString().trim();

        if (nombre.isEmpty() || categoria.isEmpty() || descripcion.isEmpty()) {
            Toast.makeText(this, "Nombre, categoría y descripción son obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest request = new StringRequest(
                Request.Method.POST,
                URL_AGREGAR,
                response -> {
                    try {
                        JSONObject json = new JSONObject(response);
                        boolean success = json.getBoolean("success");
                        String message = json.getString("message");

                        Toast.makeText(this, message, Toast.LENGTH_LONG).show();

                        if (success) {
                            limpiarCampos();
                        }

                    } catch (Exception e) {
                        Toast.makeText(this, "Error al procesar respuesta: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                },
                error -> Toast.makeText(this, "Error de conexión: " + error.toString(), Toast.LENGTH_LONG).show()
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("nombre", nombre);
                params.put("categoria", categoria);
                params.put("descripcion", descripcion);
                params.put("enlace", enlace);
                params.put("nivel", nivel);
                params.put("autor", autor);
                return params;
            }
        };

        queue.add(request);
    }

    private void limpiarCampos() {
        edtNombre.setText("");
        edtCategoria.setText("");
        edtDescripcion.setText("");
        edtEnlace.setText("");
        edtNivel.setText("");
        edtAutor.setText("");
    }
}