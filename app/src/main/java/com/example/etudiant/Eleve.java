package com.example.etudiant;

// Classe représentant un élève
public class Eleve {

    // Attributs privés de l'élève
    private String cin;       // CIN (identifiant unique)
    private String nom;       // Nom de l'élève
    private String prenom;    // Prénom de l'élève
    private String sexe;      // Sexe de l'élève ("Homme" ou "Femme")
    private String classe;    // Classe de l'élève (ex : "GLSI 1")
    private Double moyenne;   // Moyenne de l'élève

    // Constructeur pour créer un objet Eleve avec toutes ses informations
    public Eleve(String cin, String nom, String prenom, String sexe, String classe, Double moyenne) {
        this.cin = cin;
        this.nom = nom;
        this.prenom = prenom;
        this.sexe = sexe;
        this.classe = classe;
        this.moyenne = moyenne;
    }

    // Méthodes "getter" pour accéder aux informations de l'élève

    public String getCin() {
        return cin;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getSexe() {
        return sexe;
    }

    public String getClasse() {
        return classe;
    }

    public Double getMoyenne() {
        return moyenne;
    }
}