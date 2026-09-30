package com.example.recursosdigitales;

import android.os.Bundle;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.Color;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class BuscarRecursoActivity extends AppCompatActivity {

    EditText edtBusqueda;
    Button btnBuscar;
    TextView btnRegresar, txtTituloResultados;
    LinearLayout contenedorResultados;

    String URL_BUSCAR = "http://10.0.2.2/recursos_api/buscar_recurso.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_recurso);

        edtBusqueda = findViewById(R.id.edtBusqueda);
        btnBuscar = findViewById(R.id.btnBuscar);
        btnRegresar = findViewById(R.id.btnRegresar);
        txtTituloResultados = findViewById(R.id.txtTituloResultados);
        contenedorResultados = findViewById(R.id.contenedorResultados);

        btnRegresar.setOnClickListener(v -> finish());

        txtTituloResultados.setVisibility(View.GONE);

        btnBuscar.setOnClickListener(v -> buscarRecurso());
    }

    private void buscarRecurso() {
        String busqueda = edtBusqueda.getText().toString().trim();

        if (busqueda.isEmpty()) {
            Toast.makeText(this, "Escribe algo para buscar", Toast.LENGTH_SHORT).show();
            return;
        }

        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest request = new StringRequest(
                Request.Method.POST,
                URL_BUSCAR,
                response -> {
                    try {
                        JSONObject json = new JSONObject(response);
                        boolean success = json.getBoolean("success");

                        contenedorResultados.removeAllViews();
                        txtTituloResultados.setVisibility(View.VISIBLE);

                        if (success) {
                            JSONArray recursos = json.getJSONArray("recursos");

                            if (recursos.length() == 0) {
                                TextView vacio = new TextView(this);
                                vacio.setText("No se encontraron recursos con esa búsqueda.");
                                vacio.setTextColor(Color.parseColor("#3A1F12"));
                                vacio.setTextSize(16);
                                vacio.setGravity(Gravity.CENTER);
                                vacio.setBackgroundResource(R.drawable.bg_card);
                                vacio.setPadding(dp(18), dp(18), dp(18), dp(18));

                                LinearLayout.LayoutParams paramsVacio = new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT
                                );
                                paramsVacio.setMargins(0, 0, 0, dp(18));
                                vacio.setLayoutParams(paramsVacio);

                                contenedorResultados.addView(vacio);
                                return;
                            }

                            txtTituloResultados.setText("Resultados encontrados: " + recursos.length());

                            for (int i = 0; i < recursos.length(); i++) {
                                JSONObject recurso = recursos.getJSONObject(i);

                                String id = recurso.getString("id_recurso");
                                String nombre = recurso.getString("nombre");
                                String categoria = recurso.getString("categoria");
                                String descripcion = recurso.getString("descripcion");
                                String nivel = recurso.getString("nivel");
                                String autor = recurso.getString("autor");

                                crearTarjetaResultado(id, nombre, categoria, descripcion, nivel, autor);
                            }

                        } else {
                            Toast.makeText(this, "No se pudo realizar la búsqueda", Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        Toast.makeText(this, "Error al procesar JSON: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                },
                error -> Toast.makeText(this, "Error de conexión: " + error.toString(), Toast.LENGTH_LONG).show()
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("busqueda", busqueda);
                return params;
            }
        };

        queue.add(request);
    }

    private void crearTarjetaResultado(String id, String nombre, String categoria, String descripcion, String nivel, String autor) {

        LinearLayout tarjeta = new LinearLayout(this);
        tarjeta.setOrientation(LinearLayout.HORIZONTAL);
        tarjeta.setGravity(Gravity.CENTER_VERTICAL);
        tarjeta.setBackgroundResource(R.drawable.bg_card);
        tarjeta.setPadding(dp(14), dp(14), dp(14), dp(14));
        tarjeta.setElevation(dp(5));

        LinearLayout.LayoutParams paramsTarjeta = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        paramsTarjeta.setMargins(0, 0, 0, dp(18));
        tarjeta.setLayoutParams(paramsTarjeta);

        ImageView imagen = new ImageView(this);
        imagen.setImageResource(obtenerImagenPorCategoria(categoria));
        imagen.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

        LinearLayout.LayoutParams paramsImagen = new LinearLayout.LayoutParams(dp(105), dp(105));
        paramsImagen.setMargins(0, 0, dp(14), 0);
        imagen.setLayoutParams(paramsImagen);

        tarjeta.addView(imagen);

        LinearLayout contenido = new LinearLayout(this);
        contenido.setOrientation(LinearLayout.VERTICAL);
        contenido.setGravity(Gravity.CENTER_VERTICAL);

        LinearLayout.LayoutParams paramsContenido = new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1
        );
        contenido.setLayoutParams(paramsContenido);

        TextView txtNombre = new TextView(this);
        txtNombre.setText(nombre);
        txtNombre.setTextColor(Color.parseColor("#3A1F12"));
        txtNombre.setTextSize(20);
        txtNombre.setTypeface(null, Typeface.BOLD);
        txtNombre.setMaxLines(2);
        contenido.addView(txtNombre);

        TextView txtCategoria = new TextView(this);
        txtCategoria.setText(categoria + " • " + nivel);
        txtCategoria.setTextColor(Color.parseColor("#A2693D"));
        txtCategoria.setTextSize(15);
        txtCategoria.setPadding(0, dp(4), 0, dp(8));
        contenido.addView(txtCategoria);

        TextView txtDescripcion = new TextView(this);
        txtDescripcion.setText(descripcion);
        txtDescripcion.setTextColor(Color.parseColor("#4C3B32"));
        txtDescripcion.setTextSize(14);
        txtDescripcion.setMaxLines(3);
        contenido.addView(txtDescripcion);

        View linea = new View(this);
        linea.setBackgroundColor(Color.parseColor("#EAD9C5"));

        LinearLayout.LayoutParams paramsLinea = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                dp(1)
        );
        paramsLinea.setMargins(0, dp(10), 0, dp(8));
        linea.setLayoutParams(paramsLinea);
        contenido.addView(linea);

        LinearLayout filaInferior = new LinearLayout(this);
        filaInferior.setOrientation(LinearLayout.HORIZONTAL);
        filaInferior.setGravity(Gravity.CENTER_VERTICAL);

        TextView txtId = new TextView(this);
        txtId.setText("ID: " + id);
        txtId.setTextColor(Color.parseColor("#3A1F12"));
        txtId.setTextSize(14);

        LinearLayout.LayoutParams paramsId = new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1
        );
        txtId.setLayoutParams(paramsId);

        TextView txtActivo = new TextView(this);
        txtActivo.setText("● Activo");
        txtActivo.setTextColor(Color.parseColor("#2E7D32"));
        txtActivo.setTextSize(14);
        txtActivo.setBackgroundResource(R.drawable.bg_status_active);
        txtActivo.setPadding(dp(10), dp(4), dp(10), dp(4));

        filaInferior.addView(txtId);
        filaInferior.addView(txtActivo);

        contenido.addView(filaInferior);

        tarjeta.addView(contenido);

        contenedorResultados.addView(tarjeta);
    }

    private int obtenerImagenPorCategoria(String categoria) {
        String cat = categoria.toLowerCase();

        if (cat.contains("android") || cat.contains("móvil") || cat.contains("movil")) {
            return R.drawable.btn_agregar_recursos;
        } else if (cat.contains("php") || cat.contains("backend")) {
            return R.drawable.btn_buscar;
        } else if (cat.contains("mysql") || cat.contains("base")) {
            return R.drawable.btn_reportes;
        } else if (cat.contains("program")) {
            return R.drawable.btn_ver_recursos;
        } else {
            return R.drawable.btn_ver_recursos;
        }
    }

    private int dp(int valor) {
        return (int) (valor * getResources().getDisplayMetrics().density);
    }
}