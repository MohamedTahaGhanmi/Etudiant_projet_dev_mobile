package com.example.etudiant;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import java.util.List;

public class ListStudentActivity extends AppCompatActivity {

    LinearLayout linearLayout;
    BaseDeDonnees db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_student);

        linearLayout = findViewById(R.id.linearLayoutStudents);
        db = new BaseDeDonnees(this);

        showStudents();

        Button btnRetour = findViewById(R.id.boutonRetour);
        btnRetour.setOnClickListener(v -> finish());
    }

    private void showStudents() {
        linearLayout.removeAllViews(); // clear existing views
        List<Eleve> students = db.getAllStudents();

        for (Eleve e : students) {
            LinearLayout row = new LinearLayout(this);
            row.setOrientation(LinearLayout.HORIZONTAL);
            row.setPadding(0, 10, 0, 10);

            TextView tv = new TextView(this);
            tv.setText(e.getCin() + " - " + e.getNom() + " " + e.getPrenom() + " | Moyenne: " + e.getMoyenne());
            tv.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));

            Button btnDelete = new Button(this);
            btnDelete.setText("Supprimer");
            btnDelete.setOnClickListener(v -> {
                boolean deleted = db.supprimerEleve(e.getCin());
                if (deleted) {
                    Toast.makeText(this, "Étudiant supprimé", Toast.LENGTH_SHORT).show();
                    showStudents(); // refresh the list
                } else {
                    Toast.makeText(this, "Erreur de suppression", Toast.LENGTH_SHORT).show();
                }
            });

            row.addView(tv);
            row.addView(btnDelete);

            linearLayout.addView(row);
        }
    }
}
