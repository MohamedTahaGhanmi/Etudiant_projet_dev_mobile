package com.example.etudiant;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

public class AjoutEleveActivity extends AppCompatActivity {

    // Déclaration des champs de saisie
    EditText saisieCIN, saisieNom, saisiePrenom, saisieMoyenne;
    RadioGroup groupeSexe; // Groupe de boutons radio pour le sexe
    Spinner listeClasse;    // Liste déroulante pour choisir la classe
    Button boutonEnvoyer, boutonAnnuler;
    BaseDeDonnees base;     // Objet pour gérer la base de données

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajout_eleve); // Liaison avec le layout XML

        base = new BaseDeDonnees(this); // Initialisation de la base de données

        // Liaison des champs du layout avec les variables Java
        saisieCIN = findViewById(R.id.saisieCIN);
        saisieNom = findViewById(R.id.saisieNom);
        saisiePrenom = findViewById(R.id.saisiePrenom);
        saisieMoyenne = findViewById(R.id.saisieMoyenne);
        groupeSexe = findViewById(R.id.groupeSexe);
        listeClasse = findViewById(R.id.listeClasse);
        boutonEnvoyer = findViewById(R.id.boutonEnvoyer);
        boutonAnnuler = findViewById(R.id.boutonAnnuler);

        // Remplissage du Spinner (liste déroulante) avec les classes
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item,
                new String[]{"GLSI 1", "GLSI 2", "GLSI 3"} // Valeurs disponibles
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        listeClasse.setAdapter(adapter); // Liaison de l'adapter avec le Spinner

        // Actions des boutons
        boutonEnvoyer.setOnClickListener(v -> enregistrerEleve()); // Enregistrer un élève
        boutonAnnuler.setOnClickListener(v -> effacerChamps());// Effacer les champs

        Button btnRetour = findViewById(R.id.boutonRetour);
        btnRetour.setOnClickListener(v -> {
            Intent intent = new Intent(AjoutEleveActivity.this, MainActivity.class);
            startActivity(intent);
        });
    }

    // Méthode pour enregistrer un élève dans la base de données
    private void enregistrerEleve() {
        // Récupération des valeurs saisies
        String cin = saisieCIN.getText().toString().trim();
        String nom = saisieNom.getText().toString().trim();
        String prenom = saisiePrenom.getText().toString().trim();
        String classe = listeClasse.getSelectedItem().toString();

        // Vérification qu'un sexe est sélectionné
        int idSexe = groupeSexe.getCheckedRadioButtonId();
        if (idSexe == -1) {
            Toast.makeText(this, "Sélectionnez le sexe", Toast.LENGTH_SHORT).show();
            return;
        }
        RadioButton boutonSexe = findViewById(idSexe);
        String sexe = boutonSexe.getText().toString();

        // Vérification que la moyenne est saisie
        if (saisieMoyenne.getText().toString().isEmpty()) {
            Toast.makeText(this, "Veuillez saisir la moyenne", Toast.LENGTH_SHORT).show();
            return;
        }

        // Conversion de la moyenne en nombre et vérification
        double moyenne;
        try {
            moyenne = Float.parseFloat(saisieMoyenne.getText().toString());
        } catch (NumberFormatException e) {
            Toast.makeText(this, "La moyenne doit être un nombre valide", Toast.LENGTH_SHORT).show();
            return;
        }

        // Vérification que la moyenne est entre 0 et 20
        if (moyenne < 0 || moyenne > 20) {
            Toast.makeText(this, "La moyenne doit être entre 0 et 20", Toast.LENGTH_SHORT).show();
            return;
        }

        // Vérification que les champs obligatoires ne sont pas vides
        if (cin.isEmpty() || nom.isEmpty() || prenom.isEmpty()) {
            Toast.makeText(this, "Veuillez remplir les champs obligatoires", Toast.LENGTH_SHORT).show();
            return;
        }

        // **Vérification que le CIN contient exactement 8 chiffres**
        if (cin.length() != 8 || !cin.matches("\\d{8}")) {
            Toast.makeText(this, "Le CIN doit contenir exactement 8 chiffres", Toast.LENGTH_SHORT).show();
            return;
        }

        // Vérification que le nom commence par une lettre et contient uniquement des lettres et espaces
        if (!nom.matches("[a-zA-Z][a-zA-Z ]*")) {
            Toast.makeText(this, "Le nom doit commencer par une lettre et ne contenir que des lettres et espaces", Toast.LENGTH_SHORT).show();
            return;
        }

        // Vérification que le prénom commence par une lettre et contient uniquement des lettres et espaces
        if (!prenom.matches("[a-zA-Z][a-zA-Z ]*")) {
            Toast.makeText(this, "Le prénom doit commencer par une lettre et ne contenir que des lettres et espaces", Toast.LENGTH_SHORT).show();
            return;
        }


        // Création de l'objet Eleve avec les valeurs saisies
        Eleve e = new Eleve(cin, nom, prenom, sexe, classe, moyenne);

        // Insertion dans la base de données et message de confirmation ou d'erreur
        if (base.insererEleve(e)) {
            Toast.makeText(this, "Élève ajouté avec succès", Toast.LENGTH_SHORT).show();
            effacerChamps(); // Effacer les champs après insertion
        } else {
            Toast.makeText(this, "Erreur : CIN déjà existant !", Toast.LENGTH_SHORT).show();
        }
    }

    // Méthode pour réinitialiser tous les champs du formulaire
    private void effacerChamps() {
        saisieCIN.setText("");
        saisieNom.setText("");
        saisiePrenom.setText("");
        saisieMoyenne.setText("");
        groupeSexe.clearCheck(); // Déselectionne tous les boutons radio
        listeClasse.setSelection(0); // Remet le spinner sur la première valeur
    }

}
