package com.example.etudiant;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AdminLoginActivity extends AppCompatActivity {

    private static final String ADMIN_PASSWORD = "1234";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        EditText etPassword = findViewById(R.id.etPassword);
        Button btnLogin = findViewById(R.id.btnLoginAdmin);

        btnLogin.setOnClickListener(v -> {
            String pw = etPassword.getText().toString().trim();
            if (pw.equals(ADMIN_PASSWORD)) {

                Intent intent = new Intent(AdminLoginActivity.this, ListStudentActivity.class);
                startActivity(intent);

            } else {
                Toast.makeText(this, "Mot de passe incorrect", Toast.LENGTH_SHORT).show();
            }
        });

        Button btnRetour = findViewById(R.id.boutonRetour);
        btnRetour.setOnClickListener(v -> finish());
    }
}
