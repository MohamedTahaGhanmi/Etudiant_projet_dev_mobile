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
    double sommeNotes = 0;
    double sommeCoef = 0;
    String cin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calcul_moyenne); // change if XML name is different

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

                double tpVal = Double.parseDouble(tp.getText().toString());
                double dsVal = Double.parseDouble(ds.getText().toString());
                double examVal = Double.parseDouble(exam.getText().toString());
                double coefVal = Double.parseDouble(coef.getText().toString());

                // Score of one subject
                double scoreMatiere = (tpVal * 0.20) + (dsVal * 0.10) + (examVal * 0.70);

                // Update totals
                sommeNotes += scoreMatiere * coefVal;
                sommeCoef += coefVal;

                double moyenne = sommeNotes / sommeCoef;

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

            double moyenneFinale = sommeNotes / sommeCoef;

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



        Button btnRetour=findViewById(R.id.boutonRetour);
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