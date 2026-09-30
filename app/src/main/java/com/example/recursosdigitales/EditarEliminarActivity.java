package com.example.recursosdigitales;

import android.app.AlertDialog;
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

public class EditarEliminarActivity extends AppCompatActivity {

    EditText edtIdRecurso, edtNombreEdit, edtCategoriaEdit, edtDescripcionEdit, edtEnlaceEdit, edtNivelEdit, edtAutorEdit;
    Button btnEditar, btnEliminar;
    TextView btnRegresar;

    String URL_EDITAR = "http://10.0.2.2/recursos_api/editar_recurso.php";
    String URL_ELIMINAR = "http://10.0.2.2/recursos_api/eliminar_recurso.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_eliminar);

        edtIdRecurso = findViewById(R.id.edtIdRecurso);
        edtNombreEdit = findViewById(R.id.edtNombreEdit);
        edtCategoriaEdit = findViewById(R.id.edtCategoriaEdit);
        edtDescripcionEdit = findViewById(R.id.edtDescripcionEdit);
        edtEnlaceEdit = findViewById(R.id.edtEnlaceEdit);
        edtNivelEdit = findViewById(R.id.edtNivelEdit);
        edtAutorEdit = findViewById(R.id.edtAutorEdit);

        btnEditar = findViewById(R.id.btnEditar);
        btnEliminar = findViewById(R.id.btnEliminar);
        btnRegresar = findViewById(R.id.btnRegresar);

        btnRegresar.setOnClickListener(v -> finish());

        btnEditar.setOnClickListener(v -> editarRecurso());

        btnEliminar.setOnClickListener(v -> confirmarEliminacion());
    }

    private void editarRecurso() {
        String id = edtIdRecurso.getText().toString().trim();
        String nombre = edtNombreEdit.getText().toString().trim();
        String categoria = edtCategoriaEdit.getText().toString().trim();
        String descripcion = edtDescripcionEdit.getText().toString().trim();
        String enlace = edtEnlaceEdit.getText().toString().trim();
        String nivel = edtNivelEdit.getText().toString().trim();
        String autor = edtAutorEdit.getText().toString().trim();

        if (id.isEmpty() || nombre.isEmpty() || categoria.isEmpty() || descripcion.isEmpty()) {
            Toast.makeText(this, "ID, nombre, categoría y descripción son obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest request = new StringRequest(
                Request.Method.POST,
                URL_EDITAR,
                response -> {
                    try {
                        JSONObject json = new JSONObject(response);
                        String message = json.getString("message");
                        Toast.makeText(this, message, Toast.LENGTH_LONG).show();

                        boolean success = json.getBoolean("success");

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
                params.put("id_recurso", id);
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

    private void confirmarEliminacion() {
        String id = edtIdRecurso.getText().toString().trim();

        if (id.isEmpty()) {
            Toast.makeText(this, "Escribe el ID del recurso que deseas eliminar", Toast.LENGTH_SHORT).show();
            return;
        }

        new AlertDialog.Builder(this)
                .setTitle("Confirmar eliminación")
                .setMessage("¿Seguro que deseas eliminar el recurso con ID " + id + "?")
                .setPositiveButton("Eliminar", (dialog, which) -> eliminarRecurso())
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void eliminarRecurso() {
        String id = edtIdRecurso.getText().toString().trim();

        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest request = new StringRequest(
                Request.Method.POST,
                URL_ELIMINAR,
                response -> {
                    try {
                        JSONObject json = new JSONObject(response);
                        String message = json.getString("message");
                        Toast.makeText(this, message, Toast.LENGTH_LONG).show();

                        boolean success = json.getBoolean("success");

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
                params.put("id_recurso", id);
                return params;
            }
        };

        queue.add(request);
    }

    private void limpiarCampos() {
        edtIdRecurso.setText("");
        edtNombreEdit.setText("");
        edtCategoriaEdit.setText("");
        edtDescripcionEdit.setText("");
        edtEnlaceEdit.setText("");
        edtNivelEdit.setText("");
        edtAutorEdit.setText("");
    }
}