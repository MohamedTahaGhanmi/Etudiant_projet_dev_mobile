package com.example.etudiant;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.content.Intent;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CalculMoyenne extends AppCompatActivity {

    EditText nomMatiere, coef, tp, ds, exam;
    TextView tvMoyenne;
    Button boutonAjout, boutonAnnuler ;
    Button boutonConfirmer;

    // Variables to store global calculation
    float sommeNotes = 0;
    float sommeCoef = 0;
    String cin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calcul_moyenne);

        // Linking views
        nomMatiere = findViewById(R.id.nomMatiere);
        coef = findViewById(R.id.coef);
        tp = findViewById(R.id.tp);
        ds = findViewById(R.id.ds);
        exam = findViewById(R.id.exam);
        tvMoyenne = findViewById(R.id.tvMoyenne);
        cin = getIntent().getStringExtra("cin");

        boutonAjout = findViewById(R.id.boutonAjout);
        boutonAnnuler = findViewById(R.id.boutonAnnuler);
        boutonConfirmer = findViewById(R.id.boutonConfirmer);

        // Ajouter Matiere
        boutonAjout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Check empty fields
                if (tp.getText().toString().isEmpty() ||
                        ds.getText().toString().isEmpty() ||
                        exam.getText().toString().isEmpty() ||
                        coef.getText().toString().isEmpty()) {

                    Toast.makeText(CalculMoyenne.this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                    return;
                }

                float tpVal = Float.parseFloat(tp.getText().toString());
                float dsVal = Float.parseFloat(ds.getText().toString());
                float examVal = Float.parseFloat(exam.getText().toString());
                float coefVal = Float.parseFloat(coef.getText().toString());

                if (coefVal < 1 || coefVal > 2) {
                    Toast.makeText(CalculMoyenne.this, "Le coefficient doit être entre 1 et 2", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (tpVal < 0 || tpVal > 20 ||
                        dsVal < 0 || dsVal > 20 ||
                        examVal < 0 || examVal > 20) {

                    Toast.makeText(CalculMoyenne.this, "Les notes doivent être entre 0 et 20", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Score of one subject
                float scoreMatiere = (tpVal * 0.20f) + (dsVal * 0.10f) + (examVal * 0.70f);

                // Update totals
                sommeNotes += scoreMatiere * coefVal;
                sommeCoef += coefVal;

                float moyenne = sommeNotes / sommeCoef;

                tvMoyenne.setText("Moyenne : " + String.format("%.2f", moyenne));

                clearFields();
            }
        });

        // Annuler
        boutonAnnuler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearFields();
            }
        });

        boutonConfirmer.setOnClickListener(v -> {

            if (sommeCoef == 0) {
                Toast.makeText(this, "Aucune matière ajoutée", Toast.LENGTH_SHORT).show();
                return;
            }

            float moyenneFinale = sommeNotes / sommeCoef;

            BaseDeDonnees db = new BaseDeDonnees(this);
            Eleve eleve = db.chercherEleveParCin(cin);

            if (eleve != null) {
                eleve.setMoyenne(moyenneFinale);
                db.modifierEleve(eleve);

                Toast.makeText(this, "Moyenne mise à jour", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(CalculMoyenne.this, StudentDetailsActivity.class);
                intent.putExtra("cin", eleve.getCin());
                intent.putExtra("nom", eleve.getNom());
                intent.putExtra("prenom", eleve.getPrenom());
                intent.putExtra("sexe", eleve.getSexe());
                intent.putExtra("classe", eleve.getClasse());
                intent.putExtra("moyenne", moyenneFinale);

                startActivity(intent);
                finish();
            }
        });

        Button btnRetour = findViewById(R.id.boutonRetour);
        btnRetour.setOnClickListener(v -> finish());
    }

    private void clearFields() {
        nomMatiere.setText("");
        coef.setText("");
        tp.setText("");
        ds.setText("");
        exam.setText("");
    }

}
