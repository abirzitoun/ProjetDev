package com.example.formPatient;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText editTextEmail;
    private Button buttonSendResetLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        editTextEmail = findViewById(R.id.editTextEmail);
        buttonSendResetLink = findViewById(R.id.buttonSendResetLink);

        buttonSendResetLink.setOnClickListener(v -> sendResetCode());
    }

    private void sendResetCode() {
        String email = editTextEmail.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please enter an email address", Toast.LENGTH_SHORT).show();
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
        } else {
            int resetCode = new Random().nextInt(9000) + 1000;

            String subject = "Password Reset Code";
            String messageBody = "Hello,\n\nYour password reset code is: " + resetCode +
                    "\n\nPlease use this code to reset your password.\n" +
                    "If you did not request this, please ignore this email.";

            JavaMailAPI javaMailAPI = new JavaMailAPI(email, subject, messageBody);
            try {
                javaMailAPI.sendEmail();
                Toast.makeText(this, "Reset code sent successfully. Please check your email.", Toast.LENGTH_SHORT).show();

                 Intent intent = new Intent(ForgotPasswordActivity.this, ResetPasswordActivity.class);
                intent.putExtra("email", email);
                intent.putExtra("resetCode", resetCode);
                startActivity(intent);

            } catch (Exception e) {
                Toast.makeText(this, "Failed to send email. Please try again later.", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }
}
