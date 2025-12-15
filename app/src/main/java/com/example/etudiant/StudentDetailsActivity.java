package com.example.etudiant;
// Le package où se trouve cette activité

import android.content.Intent;
import android.os.Bundle;           // Pour gérer l'état de l'activité
import android.widget.Button;
import android.widget.TextView;     // Pour afficher du texte
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
// Activité de base compatible Android

public class StudentDetailsActivity extends AppCompatActivity {

    // Déclaration des TextViews qui afficheront les infos de l'élève
    private TextView tvCin, tvNom, tvPrenom, tvSexe, tvClasse, tvMoyenne;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Appelle la fonction parent pour initialiser l'activité

        setContentView(R.layout.activity_student_details);
        // Lie l'activité au layout XML qui contient l'interface

        // Récupération des TextViews depuis le layout
        tvCin = findViewById(R.id.tvCin);
        tvNom = findViewById(R.id.tvNom);
        tvPrenom = findViewById(R.id.tvPrenom);
        tvSexe = findViewById(R.id.tvSexe);
        tvClasse = findViewById(R.id.tvClasse);
        tvMoyenne = findViewById(R.id.tvMoyenne);

        Button btnRetour = findViewById(R.id.boutonRetour);
        btnRetour.setOnClickListener(v -> {
            Intent intent = new Intent(StudentDetailsActivity.this, RechercheActivity.class);
            startActivity(intent);  // On retourne à la page principale
        });

        // Récupération des données envoyées via l'Intent
        Bundle extras = getIntent().getExtras();

        // Vérifie que les données existent
        if (extras != null) {

            // Affichage des informations récupérées
            tvCin.setText("CIN: " + extras.getString("cin"));
            tvNom.setText("Nom: " + extras.getString("nom"));
            tvPrenom.setText("Prénom: " + extras.getString("prenom"));
            tvSexe.setText("Sexe: " + extras.getString("sexe"));
            tvClasse.setText("Classe: " + extras.getString("classe"));
            tvMoyenne.setText("Moyenne: " + extras.getDouble("moyenne"));
        }

        Button btnModifier = findViewById(R.id.boutonModifier);

        btnModifier.setOnClickListener(v -> {
            Intent intent = new Intent(StudentDetailsActivity.this, ModifierEleveActivity.class);

            intent.putExtra("cin", extras.getString("cin"));
            intent.putExtra("nom", extras.getString("nom"));
            intent.putExtra("prenom", extras.getString("prenom"));
            intent.putExtra("sexe", extras.getString("sexe"));
            intent.putExtra("classe", extras.getString("classe"));
            intent.putExtra("moyenne", extras.getDouble("moyenne"));

            startActivity(intent);
        });


        Button btnSupprimer = findViewById(R.id.boutonSupprimer);
        BaseDeDonnees db = new BaseDeDonnees(this);

        btnSupprimer.setOnClickListener(v -> {

            if (extras != null) {

                String cin = extras.getString("cin");
                boolean supprime = db.supprimerEleve(cin);

                if (supprime) {
                    Toast.makeText(this, "Étudiant supprimé", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(StudentDetailsActivity.this, MainActivity.class);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(this, "Erreur de suppression", Toast.LENGTH_SHORT).show();
                }
            }
        });


        Button btncalculMoyenne = findViewById(R.id.boutonCalculMoyenne);
        btncalculMoyenne.setOnClickListener(v -> {
            Intent intent = new Intent(StudentDetailsActivity.this, CalculMoyenne.class);
            intent.putExtra("cin", extras.getString("cin"));
            startActivity(intent);
        });

    }
}