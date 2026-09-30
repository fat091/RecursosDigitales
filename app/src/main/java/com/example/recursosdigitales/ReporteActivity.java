package com.example.recursosdigitales;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

public class ReporteActivity extends AppCompatActivity {

    Button btnCargarReporte;
    TextView btnRegresar, txtTotalRecursos, txtTotalCategorias, txtEstado;
    LinearLayout contenedorCategorias;

    String URL_REPORTE = "http://10.0.2.2/recursos_api/reporte.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reporte);

        btnRegresar = findViewById(R.id.btnRegresar);
        btnCargarReporte = findViewById(R.id.btnCargarReporte);
        txtTotalRecursos = findViewById(R.id.txtTotalRecursos);
        txtTotalCategorias = findViewById(R.id.txtTotalCategorias);
        txtEstado = findViewById(R.id.txtEstado);
        contenedorCategorias = findViewById(R.id.contenedorCategorias);

        btnRegresar.setOnClickListener(v -> finish());

        cargarReporte();

        btnCargarReporte.setOnClickListener(v -> cargarReporte());
    }

    private void cargarReporte() {
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                URL_REPORTE,
                null,
                response -> {
                    try {
                        boolean success = response.getBoolean("success");

                        contenedorCategorias.removeAllViews();

                        if (success) {
                            int totalRecursos = response.getInt("total_recursos");
                            JSONArray categorias = response.getJSONArray("categorias");

                            txtTotalRecursos.setText(String.valueOf(totalRecursos));
                            txtTotalCategorias.setText(String.valueOf(categorias.length()));
                            txtEstado.setText("OK");

                            if (categorias.length() == 0) {
                                TextView vacio = new TextView(this);
                                vacio.setText("No hay categorías para mostrar.");
                                vacio.setTextColor(Color.parseColor("#3A1F12"));
                                vacio.setTextSize(16);
                                vacio.setGravity(Gravity.CENTER);
                                contenedorCategorias.addView(vacio);
                                return;
                            }

                            for (int i = 0; i < categorias.length(); i++) {
                                JSONObject categoria = categorias.getJSONObject(i);

                                String nombreCategoria = categoria.getString("categoria");
                                int cantidad = categoria.getInt("cantidad");

                                crearFilaCategoria(nombreCategoria, cantidad, totalRecursos);
                            }

                        } else {
                            Toast.makeText(this, "No se pudo cargar el reporte", Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        Toast.makeText(this, "Error al procesar JSON: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                },
                error -> Toast.makeText(this, "Error de conexión: " + error.toString(), Toast.LENGTH_LONG).show()
        );

        queue.add(request);
    }

    private void crearFilaCategoria(String categoria, int cantidad, int totalRecursos) {
        int porcentaje = 0;

        if (totalRecursos > 0) {
            porcentaje = (cantidad * 100) / totalRecursos;
        }

        LinearLayout contenedorFila = new LinearLayout(this);
        contenedorFila.setOrientation(LinearLayout.VERTICAL);
        contenedorFila.setPadding(0, 0, 0, dp(18));

        LinearLayout.LayoutParams paramsFila = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        contenedorFila.setLayoutParams(paramsFila);

        LinearLayout filaTexto = new LinearLayout(this);
        filaTexto.setOrientation(LinearLayout.HORIZONTAL);
        filaTexto.setGravity(Gravity.CENTER_VERTICAL);

        TextView txtCategoria = new TextView(this);
        txtCategoria.setText(categoria);
        txtCategoria.setTextColor(Color.parseColor("#3A1F12"));
        txtCategoria.setTextSize(16);
        txtCategoria.setTypeface(null, Typeface.BOLD);

        LinearLayout.LayoutParams paramsCategoria = new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1
        );
        txtCategoria.setLayoutParams(paramsCategoria);

        TextView txtCantidad = new TextView(this);
        txtCantidad.setText(cantidad + " (" + porcentaje + "%)");
        txtCantidad.setTextColor(Color.parseColor("#8A6547"));
        txtCantidad.setTextSize(15);
        txtCantidad.setTypeface(null, Typeface.BOLD);

        filaTexto.addView(txtCategoria);
        filaTexto.addView(txtCantidad);

        ProgressBar barra = new ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal);
        barra.setMax(100);
        barra.setProgress(porcentaje);

        LinearLayout.LayoutParams paramsBarra = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                dp(12)
        );
        paramsBarra.setMargins(0, dp(8), 0, 0);
        barra.setLayoutParams(paramsBarra);

        contenedorFila.addView(filaTexto);
        contenedorFila.addView(barra);

        contenedorCategorias.addView(contenedorFila);
    }

    private int dp(int valor) {
        return (int) (valor * getResources().getDisplayMetrics().density);
    }
}