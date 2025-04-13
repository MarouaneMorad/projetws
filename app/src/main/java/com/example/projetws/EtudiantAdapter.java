package com.example.projetws;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.projetws.beans.Etudiant;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EtudiantAdapter extends RecyclerView.Adapter<EtudiantAdapter.EtudiantViewHolder> {
    private List<Etudiant> etudiants;
    private Context context;

    public EtudiantAdapter(List<Etudiant> etudiants, Context context) {
        this.etudiants = etudiants;
        this.context = context;
    }

    @NonNull
    @Override
    public EtudiantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.etudiant_item, parent, false);
        return new EtudiantViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull EtudiantViewHolder holder, int position) {
        Etudiant e = etudiants.get(position);
        holder.nomPrenom.setText(e.getNom() + " " + e.getPrenom());
        holder.ville.setText(e.getVille());

        holder.itemView.setOnClickListener(v -> showPopupOptions(e, position));
    }

    private void showPopupOptions(Etudiant e, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choisir une action")
                .setItems(new String[]{"Modifier", "Supprimer"}, (dialog, which) -> {
                    if (which == 0) showUpdateDialog(e, position);
                    else showDeleteConfirmation(e.getId(), position);
                }).show();
    }

    private void showDeleteConfirmation(int id, int position) {
        new AlertDialog.Builder(context)
                .setTitle("Confirmer")
                .setMessage("Supprimer cet étudiant ?")
                .setPositiveButton("Oui", (d, i) -> {
                    deleteEtudiant(id);
                    etudiants.remove(position);
                    notifyItemRemoved(position);
                })
                .setNegativeButton("Non", null)
                .show();
    }

    private void deleteEtudiant(int id) {
        String url = "http://10.0.2.2/projet/ws/deleteEtudiant.php";
        RequestQueue queue = Volley.newRequestQueue(context);

        StringRequest req = new StringRequest(Request.Method.POST, url, null, null) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> p = new HashMap<>();
                p.put("id", String.valueOf(id));
                return p;
            }
        };
        queue.add(req);
    }

    private void showUpdateDialog(Etudiant etudiant, int position) {
        View dialogView = LayoutInflater.from(context).inflate(R.layout.update_dialog, null);
        EditText editNom = dialogView.findViewById(R.id.editNomUpdate);
        EditText editPrenom = dialogView.findViewById(R.id.editPrenomUpdate);

        editNom.setText(etudiant.getNom());
        editPrenom.setText(etudiant.getPrenom());

        new AlertDialog.Builder(context)
                .setTitle("Modifier Étudiant")
                .setView(dialogView)
                .setPositiveButton("Modifier", (d, i) -> {
                    etudiant.setNom(editNom.getText().toString());
                    etudiant.setPrenom(editPrenom.getText().toString());
                    updateEtudiant(etudiant);
                    notifyItemChanged(position);
                })
                .setNegativeButton("Annuler", null)
                .show();
    }

    private void updateEtudiant(Etudiant e) {
        String url = "http://10.0.2.2/projet/ws/updateEtudiant.php";
        RequestQueue queue = Volley.newRequestQueue(context);

        StringRequest req = new StringRequest(Request.Method.POST, url, null, null) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> p = new HashMap<>();
                p.put("id", String.valueOf(e.getId()));
                p.put("nom", e.getNom());
                p.put("prenom", e.getPrenom());
                return p;
            }
        };
        queue.add(req);
    }

    @Override
    public int getItemCount() {
        return etudiants.size();
    }

    public static class EtudiantViewHolder extends RecyclerView.ViewHolder {
        TextView nomPrenom, ville;

        public EtudiantViewHolder(@NonNull View itemView) {
            super(itemView);
            nomPrenom = itemView.findViewById(R.id.txtNomPrenom);
            ville = itemView.findViewById(R.id.txtVille);
        }
    }
}