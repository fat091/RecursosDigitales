package com.example.recursosdigitales;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    LinearLayout cardVerRecursos, cardAgregarRecurso, cardBuscarRecurso;
    LinearLayout cardEditarEliminar, cardReportes, cardCerrarSesion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cardVerRecursos = findViewById(R.id.cardVerRecursos);
        cardAgregarRecurso = findViewById(R.id.cardAgregarRecurso);
        cardBuscarRecurso = findViewById(R.id.cardBuscarRecurso);
        cardEditarEliminar = findViewById(R.id.cardEditarEliminar);
        cardReportes = findViewById(R.id.cardReportes);
        cardCerrarSesion = findViewById(R.id.cardCerrarSesion);

        cardVerRecursos.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ListaRecursosActivity.class);
            startActivity(intent);
        });

        cardAgregarRecurso.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AgregarRecursoActivity.class);
            startActivity(intent);
        });

        cardBuscarRecurso.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, BuscarRecursoActivity.class);
            startActivity(intent);
        });

        cardEditarEliminar.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, EditarEliminarActivity.class);
            startActivity(intent);
        });

        cardReportes.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ReporteActivity.class);
            startActivity(intent);
        });

        cardCerrarSesion.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }
}