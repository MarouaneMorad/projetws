package com.example.projetws;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.projetws.beans.Etudiant;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class addEtudiant extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextNom, editTextPrenom;
    private Spinner spinnerVille;
    private RadioButton radioHomme, radioFemme;
    private Button buttonAdd;
    private RequestQueue requestQueue;
    private static final String INSERT_URL = "http://10.0.2.2/projet/ws/createEtudiant.php";
    private static final String TAG = "AddEtudiant";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_etudiant);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialisation des vues
        editTextNom = findViewById(R.id.nom);
        editTextPrenom = findViewById(R.id.prenom);
        spinnerVille = findViewById(R.id.ville);
        radioHomme = findViewById(R.id.m);
        radioFemme = findViewById(R.id.f);
        buttonAdd = findViewById(R.id.add);

        buttonAdd.setOnClickListener(this);
        requestQueue = Volley.newRequestQueue(getApplicationContext());
    }

    //Version 1 :
     /*@Override
       public void onClick(View v) {
        if (v.getId() == R.id.add) {
            String nom = editTextNom.getText().toString().trim();
            String prenom = editTextPrenom.getText().toString().trim();
            String ville = spinnerVille.getSelectedItem().toString();
            String sexe = radioHomme.isChecked() ? "homme" : "femme";

            if (nom.isEmpty() || prenom.isEmpty()) {
                Toast.makeText(this, "Veuillez remplir tous les champs.", Toast.LENGTH_SHORT).show();
                return;
            }

            StringRequest request = new StringRequest(Request.Method.POST, INSERT_URL,
                    response -> {
                        Log.d(TAG, "Réponse : " + response);
                        Toast.makeText(addEtudiant.this, "Étudiant ajouté avec succès", Toast.LENGTH_SHORT).show();
                    },
                    error -> {
                        Log.e(TAG, "Erreur Volley : " + error.toString());
                        Toast.makeText(addEtudiant.this, "Erreur lors de l'ajout", Toast.LENGTH_SHORT).show();
                    }
            ) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("nom", nom);
                    params.put("prenom", prenom);
                    params.put("ville", ville);
                    params.put("sexe", sexe);
                    return params;
                }
            };

            requestQueue.add(request);
        }
    }*/
    //version 2 :
    @Override
    public void onClick(View v) {
        Log.d("ok", "ok");
        if (v.getId() == R.id.add) {
            requestQueue = Volley.newRequestQueue(getApplicationContext());
            StringRequest request = new StringRequest(Request.Method.POST,
                    INSERT_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d(TAG, response);
                    Type type = new TypeToken<Collection<Etudiant>>() {
                    }.getType();
                    Collection<Etudiant> etudiants = new Gson().fromJson(response, type);
                    for (Etudiant e : etudiants) {
                        Log.d(TAG, e.toString());
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    String sexe = "";
                    if (radioHomme.isChecked())
                        sexe = "homme";
                    else
                        sexe = "femme";
                    HashMap<String, String> params = new HashMap<String, String>();
                    params.put("nom", editTextNom.getText().toString());
                    params.put("prenom", editTextPrenom.getText().toString());
                    params.put("ville", spinnerVille.getSelectedItem().toString());
                    params.put("sexe", sexe);
                    return params;
                }
            };
            requestQueue.add(request);
        }

    }
}