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

        // Remplir le Spinner (simple example)
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
            etMoyenne.setText(String.valueOf(extras.getDouble("moyenne")));

            if (extras.getString("sexe").equals("Homme"))
                rbHomme.setChecked(true);
            else
                rbFemme.setChecked(true);

            int pos = adapter.getPosition(extras.getString("classe"));
            spClasse.setSelection(pos);
        }

        // Bouton Modifier
        btnModifier.setOnClickListener(v -> {

            String nom = etNom.getText().toString();
            String prenom = etPrenom.getText().toString();
            String sexe = rbHomme.isChecked() ? "Homme" : "Femme";
            String classe = spClasse.getSelectedItem().toString();
            double moyenne = Double.parseDouble(etMoyenne.getText().toString());

            Eleve e = new Eleve(cin, nom, prenom, sexe, classe, moyenne);

            if (db.modifierEleve(e)) {
                Toast.makeText(this, "Élève modifié avec succès", Toast.LENGTH_SHORT).show();
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
        });

        // Retour
        btnRetour.setOnClickListener(v -> finish());
    }
}