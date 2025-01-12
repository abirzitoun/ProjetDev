package com.example.pharmacie;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.EditText;
import android.widget.Toast;
import android.app.AlertDialog;
import android.os.AsyncTask;

public class MainActivity extends AppCompatActivity {
    private String generatedCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton btnGoToHome = findViewById(R.id.pharmacyButton);
        btnGoToHome.setOnClickListener(v -> {
            // 1. Générer un code
            generatedCode = CodeGenerator.generateCode();

            // 2. Envoyer un e-mail avec le code en arrière-plan
            new SendEmailTask().execute("Souissi.Achref@esprit.tn", generatedCode);

            // 3. Afficher une boîte de dialogue pour entrer le code
            showValidationDialog();
        });
    }

    private void showValidationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Validation par e-mail");

        final EditText input = new EditText(this);
        builder.setView(input);

        builder.setPositiveButton("Valider", (dialog, which) -> {
            String enteredCode = input.getText().toString();
            if (enteredCode.equals(generatedCode)) {
                // Code correct, passer à l'activité suivante
                Intent intent = new Intent(MainActivity.this, PharmacyHomeActivity.class);
                startActivity(intent);
            } else {
                // Code incorrect
                Toast.makeText(MainActivity.this, "Code incorrect, réessayez.", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Annuler", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    // AsyncTask pour envoyer l'email en arrière-plan
    private static class SendEmailTask extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            String email = params[0];
            String code = params[1];
            try {
                // Envoyer l'e-mail en arrière-plan
                MailSender.sendValidationEmail(email, code);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
