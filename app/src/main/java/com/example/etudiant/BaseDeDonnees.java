package com.example.etudiant;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;

import java.util.ArrayList;
import java.util.List;

// Classe pour gérer la base de données SQLite
public class BaseDeDonnees extends SQLiteOpenHelper {

    // Nom de la base de données
    public static final String NOM_BD = "eleves.db";
    // Version de la base de données
    public static final int VERSION_BD = 1;
    // Nom de la table qui contiendra les élèves
    public static final String TABLE_ELEVES = "eleves";

    // Constructeur : appelle le constructeur parent SQLiteOpenHelper
    public BaseDeDonnees(Context context) {
        super(context, NOM_BD, null, VERSION_BD);
    }

    // Méthode appelée lors de la création de la base de données
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Création de la table "eleves" avec les colonnes cin, nom, prenom, sexe, classe et moyenne
        db.execSQL(
                "CREATE TABLE " + TABLE_ELEVES + " (" +
                        "cin TEXT PRIMARY KEY," +   // CIN comme clé primaire
                        "nom TEXT," +
                        "prenom TEXT," +
                        "sexe TEXT," +
                        "classe TEXT," +
                        "moyenne REAL" +           // moyenne de type réel
                        ")"
        );
    }

    // Méthode appelée lors de la mise à jour de la version de la base
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
        // Supprime l'ancienne table si elle existe
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ELEVES);
        // Crée une nouvelle table
        onCreate(db);
    }

    // Méthode pour insérer un élève dans la base
    public boolean insererEleve(Eleve e) {
        SQLiteDatabase db = this.getWritableDatabase(); // Ouvre la base en écriture
        ContentValues cv = new ContentValues(); // Crée un objet pour stocker les valeurs

        // Ajout des valeurs dans l'objet ContentValues
        cv.put("cin", e.getCin());
        cv.put("nom", e.getNom());
        cv.put("prenom", e.getPrenom());
        cv.put("sexe", e.getSexe());
        cv.put("classe", e.getClasse());
        cv.put("moyenne", e.getMoyenne());

        // Insère l'élève dans la table, retourne -1 si échec (ex: CIN déjà existant)
        Long resultat = db.insert(TABLE_ELEVES, null, cv);
        return resultat != -1;
    }

    // Méthode pour rechercher un élève via son CIN
    public Eleve chercherEleveParCin(String cin) {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = db.rawQuery(
                "SELECT cin, nom, prenom, sexe, classe, moyenne FROM " + TABLE_ELEVES + " WHERE cin = ?",
                new String[]{cin}
        );

        if (c != null && c.moveToFirst()) {

            String _cin = c.getString(0);
            String nom = c.getString(1);
            String prenom = c.getString(2);
            String sexe = c.getString(3);
            String classe = c.getString(4);
            Double moyenne = c.getDouble(5);

            c.close();
            return new Eleve(_cin, nom, prenom, sexe, classe, moyenne);
        }
        else{
            return null;
        }
    }

    // Méthode pour modifier un élève existant (par CIN)
    public boolean modifierEleve(Eleve e) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        // Nouvelles valeurs
        cv.put("nom", e.getNom());
        cv.put("prenom", e.getPrenom());
        cv.put("sexe", e.getSexe());
        cv.put("classe", e.getClasse());
        cv.put("moyenne", e.getMoyenne());

        // Mise à jour de l'élève dont le CIN correspond
        int resultat = db.update(
                TABLE_ELEVES,
                cv,
                "cin = ?",
                new String[]{ e.getCin() }
        );

        // retourne true si au moins une ligne a été modifiée
        return resultat !=-1;
    }

    public boolean supprimerEleve(String cin) {

        SQLiteDatabase db = this.getWritableDatabase();

        int resultat = db.delete(
                TABLE_ELEVES,
                "cin = ?",
                new String[]{cin}
        );

        // retourne true si au moins une ligne a été supprimée
        return resultat != -1;
    }

    public List<Eleve> getAllStudents() {
        List<Eleve> students = new ArrayList<>();
        SQLiteDatabase dbRead = this.getReadableDatabase();
        Cursor c = dbRead.rawQuery("SELECT * FROM " + TABLE_ELEVES, null);

        if (c.moveToFirst()) {
            do {
                String cin = c.getString(c.getColumnIndexOrThrow("cin"));
                String nom = c.getString(c.getColumnIndexOrThrow("nom"));
                String prenom = c.getString(c.getColumnIndexOrThrow("prenom"));
                String sexe = c.getString(c.getColumnIndexOrThrow("sexe"));
                String classe = c.getString(c.getColumnIndexOrThrow("classe"));
                double moyenne = c.getDouble(c.getColumnIndexOrThrow("moyenne"));

                students.add(new Eleve(cin, nom, prenom, sexe, classe, moyenne));
            } while (c.moveToNext());
        }
        c.close();
        return students;
    }



}
