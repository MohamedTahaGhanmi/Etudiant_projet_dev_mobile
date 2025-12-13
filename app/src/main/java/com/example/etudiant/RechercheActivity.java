package com.example.etudiant;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RechercheActivity extends AppCompatActivity {

    // Déclaration des éléments de l’interface
    private EditText rechercheCIN;   // Champ où l’utilisateur entre le CIN
    private Button boutonRecherche;  // Bouton pour lancer la recherche

    private BaseDeDonnees db;        // Objet pour interagir avec la base SQLite

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_recherche_eleve);
        // Lie cette activité avec le fichier XML contenant l'interface

        rechercheCIN = findViewById(R.id.rechercheCIN);
        // On lie la variable au champ texte du layout

        boutonRecherche = findViewById(R.id.boutonRecherche);

        // Création d'une instance de la base de données
        db = new BaseDeDonnees(this);
        // "this" = contexte actuel, nécessaire pour ouvrir la base

        // Listener = action lorsque l'on clique sur le bouton de recherche
        boutonRecherche.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Récupération du CIN tapé par l'utilisateur
                String cin = rechercheCIN.getText().toString().trim();

                // Vérification : si le champ est vide → message d'erreur
                if (cin.isEmpty()) {
                    Toast.makeText(RechercheActivity.this,
                            "Veuillez entrer un CIN",
                            Toast.LENGTH_SHORT).show();
                    return;
                    // On arrête l’exécution ici
                }

                // Recherche de l'élève dans la base via CIN
                Eleve eleve = db.chercherEleveParCin(cin);

                // Si l'élève existe dans la base
                if (eleve != null) {

                    // Création d'une intention pour aller vers StudentDetailsActivity
                    Intent intent = new Intent(RechercheActivity.this,
                            StudentDetailsActivity.class);

                    // Passage des informations de l'élève à la prochaine activité
                    intent.putExtra("cin", eleve.getCin());
                    intent.putExtra("nom", eleve.getNom());
                    intent.putExtra("prenom", eleve.getPrenom());
                    intent.putExtra("sexe", eleve.getSexe());
                    intent.putExtra("classe", eleve.getClasse());
                    intent.putExtra("moyenne",eleve.getMoyenne());

                    // Lancement de l'activité d'affichage
                    startActivity(intent);
                } else {
                    // Si aucun élève trouvé → message
                    Toast.makeText(RechercheActivity.this,
                            "Aucun élève trouvé",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Bouton retour vers la page principale
        Button btnRetour = findViewById(R.id.boutonRetour);
        btnRetour.setOnClickListener(v -> {
            Intent intent = new Intent(RechercheActivity.this, MainActivity.class);
            startActivity(intent);  // On retourne à la page principale
        });
    }
}
