package com.example.etudiant;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

public class ModifierEleveActivity extends AppCompatActivity {

    private EditText etNom, etPrenom, etMoyenne;
    private RadioGroup rgSexe;
    private RadioButton rbHomme, rbFemme;
    private Spinner spClasse;
    private Button btnModifier, btnAnnuler, btnRetour;

    private String cin; // CIN stays unchanged
    private BaseDeDonnees db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifier_eleve);

        // Liaison avec XML
        etNom = findViewById(R.id.saisieNom);
        etPrenom = findViewById(R.id.saisiePrenom);
        etMoyenne = findViewById(R.id.saisieMoyenne);
        rgSexe = findViewById(R.id.groupeSexe);
        rbHomme = findViewById(R.id.sexeHomme);
        rbFemme = findViewById(R.id.sexeFemme);
        spClasse = findViewById(R.id.listeClasse);

        btnModifier = findViewById(R.id.boutonModifier);
        btnAnnuler = findViewById(R.id.boutonAnnuler);
        btnRetour = findViewById(R.id.boutonRetour);

        db = new BaseDeDonnees(this);

        // Remplir le Spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                new String[]{"GLSI 1", "GLSI 2", "GLSI 3"}
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spClasse.setAdapter(adapter);

        // Récupérer les données envoyées
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            cin = extras.getString("cin");
            etNom.setText(extras.getString("nom"));
            etPrenom.setText(extras.getString("prenom"));
            etMoyenne.setText(String.valueOf(extras.getFloat("moyenne")));

            if ("Homme".equals(extras.getString("sexe")))
                rbHomme.setChecked(true);
            else
                rbFemme.setChecked(true);

            int pos = adapter.getPosition(extras.getString("classe"));
            spClasse.setSelection(pos);
        }

        // Bouton Modifier avec vérifications
        btnModifier.setOnClickListener(v -> {

            String nom = etNom.getText().toString().trim();
            String prenom = etPrenom.getText().toString().trim();
            String classe = spClasse.getSelectedItem().toString();

            // Vérification champs vides
            if (nom.isEmpty() || prenom.isEmpty() || etMoyenne.getText().toString().isEmpty()) {
                Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                return;
            }

            // Vérification sexe sélectionné
            int idSexe = rgSexe.getCheckedRadioButtonId();
            if (idSexe == -1) {
                Toast.makeText(this, "Sélectionnez le sexe", Toast.LENGTH_SHORT).show();
                return;
            }
            RadioButton rbSexe = findViewById(idSexe);
            String sexe = rbSexe.getText().toString();

            // Vérification nom
            if (!nom.matches("[a-zA-Z][a-zA-Z ]*")) {
                Toast.makeText(this, "Le nom doit contenir uniquement des lettres et espaces", Toast.LENGTH_SHORT).show();
                return;
            }

            // Vérification prénom
            if (!prenom.matches("[a-zA-Z][a-zA-Z ]*")) {
                Toast.makeText(this, "Le prénom doit contenir uniquement des lettres et espaces", Toast.LENGTH_SHORT).show();
                return;
            }

            // Vérification moyenne (nombre)
            float moyenne;
            try {
                moyenne = Float.parseFloat(etMoyenne.getText().toString());
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Moyenne invalide", Toast.LENGTH_SHORT).show();
                return;
            }

            // Vérification moyenne entre 0 et 20
            if (moyenne < 0 || moyenne > 20) {
                Toast.makeText(this, "La moyenne doit être entre 0 et 20", Toast.LENGTH_SHORT).show();
                return;
            }

            // Création objet et modification
            Eleve e = new Eleve(cin, nom, prenom, sexe, classe, moyenne);

            if (db.modifierEleve(e)) {
                Toast.makeText(this, "Élève modifié avec succès", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ModifierEleveActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Erreur de modification", Toast.LENGTH_SHORT).show();
            }
        });

        // Annuler = réinitialiser
        btnAnnuler.setOnClickListener(v -> {
            etNom.setText("");
            etPrenom.setText("");
            etMoyenne.setText("");
            rgSexe.clearCheck();
            spClasse.setSelection(0);
        });

        // Retour
        btnRetour.setOnClickListener(v -> finish());
    }
}