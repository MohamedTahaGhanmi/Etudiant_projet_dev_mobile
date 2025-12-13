package com.example.etudiant;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Ajuste le padding pour Edge-to-Edge
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Bouton Ajouter un Élève
        Button btnAddStudent = findViewById(R.id.btnAddStudent);
        btnAddStudent.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AjoutEleveActivity.class);
            startActivity(intent);
        });

        // Bouton Rechercher un Élève
        Button btnSearchStudent = findViewById(R.id.btnSearchStudent);
        btnSearchStudent.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RechercheActivity.class);
            startActivity(intent);
        });
    }
}