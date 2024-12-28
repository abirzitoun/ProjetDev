    package com.example.pharmacie;

    import android.content.ContentValues;
    import android.content.Context;
    import android.database.Cursor;
    import android.database.sqlite.SQLiteDatabase;
    import android.database.sqlite.SQLiteOpenHelper;

    import androidx.annotation.Nullable;

    import java.util.ArrayList;

    // Classe pour la gestion de la base de données
    public class DbHelper extends SQLiteOpenHelper {

        public DbHelper(@Nullable Context context) {
            super(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            // Créer la table
            db.execSQL(Constants.CREATE_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // Mettre à jour la structure de la base de données si nécessaire
            db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_NAME);
            onCreate(db);
        }

        // Fonction pour insérer des données dans la base de données
        public long insertData(String image, String nom, String dosage, String prix, String validite, String addedDate, String updatedTime) {
            // Obtenir une instance de la base de données en écriture
            SQLiteDatabase db = this.getWritableDatabase();

            // Créer un ContentValues pour stocker les données
            ContentValues contentValues = new ContentValues();
            contentValues.put(Constants.C_IMAGE, image);
            contentValues.put(Constants.C_NOM, nom);
            contentValues.put(Constants.C_DOSAGE, dosage);
            contentValues.put(Constants.C_PRIX, prix);
            contentValues.put(Constants.C_VALIDITE, validite);
            contentValues.put(Constants.C_ADDED_DATE, addedDate);
            contentValues.put(Constants.C_UPDATED_TIME, updatedTime);

            // Insérer les données
            long id = db.insert(Constants.TABLE_NAME, null, contentValues);

            // Fermer la connexion à la base de données
            db.close();

            // Retourner l'ID de la nouvelle ligne insérée
            return id;
        }

        //update function to update data in db
        public void updateData(String id, String image, String nom, String dosage, String prix, String validite, String addedDate, String updatedTime) {
            // Obtenir une instance de la base de données en écriture
            SQLiteDatabase db = this.getWritableDatabase();

            // Créer un ContentValues pour stocker les données
            ContentValues contentValues = new ContentValues();
            contentValues.put(Constants.C_IMAGE, image);
            contentValues.put(Constants.C_NOM, nom);
            contentValues.put(Constants.C_DOSAGE, dosage);
            contentValues.put(Constants.C_PRIX, prix);
            contentValues.put(Constants.C_VALIDITE, validite);
            contentValues.put(Constants.C_ADDED_DATE, addedDate);
            contentValues.put(Constants.C_UPDATED_TIME, updatedTime);

            // update les données
            db.update(Constants.TABLE_NAME, contentValues, Constants.C_ID + " = ?", new String[]{id});

            // Fermer la connexion à la base de données
            db.close();

            // Retourner l'ID de la nouvelle ligne insérée

        }

        // Fonction pour récupérer toutes les données de la base de données
        //delete data by id
        public void deleteData(String id) {
            //get writable data base
            SQLiteDatabase db = this.getWritableDatabase();
            //delete data
            db.delete(Constants.TABLE_NAME, Constants.C_ID + " = ?", new String[]{id});
            //close db
            db.close();

        }

        // delete all data
        public void deleteAllData() {
            //get writable data base
            SQLiteDatabase db = this.getWritableDatabase();
            //delete all data
            db.execSQL("DELETE FROM " + Constants.TABLE_NAME);
            //close db
            db.close();
        }

        //get data
        public ArrayList<ModelPharmacie> getAllData(String orderBy) {
            // Créer une liste pour stocker les objets ModelPharmacie
            ArrayList<ModelPharmacie> list = new ArrayList<>();

            // Requête SQL pour sélectionner toutes les données
            String selectQuery = "SELECT * FROM " + Constants.TABLE_NAME + " ORDER BY " + orderBy;

            // Obtenir une instance de la base de données en lecture
            SQLiteDatabase db = getReadableDatabase();

            // Exécuter la requête et obtenir un curseur
            Cursor cursor = db.rawQuery(selectQuery, null);

            // Boucler sur les résultats du curseur
            if (cursor.moveToFirst()) {
                do {
                    // Récupérer les valeurs des colonnes
                    String id = cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_ID));
                    String nom = cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_NOM));
                    String image = cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_IMAGE));
                    String dosage = cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_DOSAGE));
                    String prix = cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_PRIX));
                    String validite = cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_VALIDITE));
                    String date = cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_ADDED_DATE));
                    String time = cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_UPDATED_TIME));

                    // Créer un objet ModelPharmacie et l'ajouter à la liste
                    ModelPharmacie modelPharmacie = new ModelPharmacie(id, nom, image, dosage, prix, validite, date, time);
                    list.add(modelPharmacie);

                } while (cursor.moveToNext());
            }

            // Fermer le curseur et la base de données
            cursor.close();
            db.close();

            // Retourner la liste des données
            return list;
        }

        //search data in SQL db
        public ArrayList<ModelPharmacie> getSearchPharmacie(String query) {
            // Créer une liste pour stocker les objets ModelPharmacie
            ArrayList<ModelPharmacie> list = new ArrayList<>();
            // get readble data base
            SQLiteDatabase db = getReadableDatabase();
            //query for search
            String queryToSearch = "SELECT * FROM " + Constants.TABLE_NAME + " WHERE " + Constants.C_NOM + " LIKE '%" + query + "%'";
            //excute query
            Cursor cursor = db.rawQuery(queryToSearch, null);
            if (cursor.moveToFirst()) {
                do {
                    // Récupérer les valeurs des colonnes
                    String id = cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_ID));
                    String nom = cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_NOM));
                    String image = cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_IMAGE));
                    String dosage = cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_DOSAGE));
                    String prix = cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_PRIX));
                    String validite = cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_VALIDITE));
                    String date = cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_ADDED_DATE));
                    String time = cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_UPDATED_TIME));

                    // Créer un objet ModelPharmacie et l'ajouter à la liste
                    ModelPharmacie modelPharmacie = new ModelPharmacie(id, nom, image, dosage, prix, validite, date, time);
                    list.add(modelPharmacie);

                } while (cursor.moveToNext());
            }
            db.close();
            return list ;

            }
    }