package com.example.projetws;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.projetws.beans.Etudiant;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    EtudiantAdapter adapter;
    List<Etudiant> etudiants = new ArrayList<>();
    String URL = "http://10.0.2.2/projet/ws/loadEtudiant.php"; // ou un fichier spécial getAll.php

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new EtudiantAdapter(etudiants, this);
        recyclerView.setAdapter(adapter);

        loadEtudiants();
    }
    private void loadEtudiants() {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest req = new StringRequest(Request.Method.GET, URL, response -> {
            Type type = new TypeToken<List<Etudiant>>(){}.getType();
            etudiants.clear();
            etudiants.addAll(new Gson().fromJson(response, type));
            adapter.notifyDataSetChanged();
        }, error -> {
            Toast.makeText(this, "Erreur de chargement", Toast.LENGTH_SHORT).show();
        });

        queue.add(req);
    }
}