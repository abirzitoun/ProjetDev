package com.example.formPatient;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.formPatient.Entity.User;
import com.example.formPatient.Service.UserService;

public class ResetPasswordActivity extends AppCompatActivity {

    private EditText editTextResetCode, editTextNewPassword, editTextConfirmPassword;
    private Button buttonResetPassword;
    private int expectedResetCode;
    private String email;
    private UserService userService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

         editTextResetCode = findViewById(R.id.editTextResetCode);
        editTextNewPassword = findViewById(R.id.editTextNewPassword);
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);
        buttonResetPassword = findViewById(R.id.buttonResetPassword);

         userService = new UserService(this);

         Intent intent = getIntent();
        email = intent.getStringExtra("email");
        expectedResetCode = intent.getIntExtra("resetCode", -1);

         buttonResetPassword.setOnClickListener(v -> resetPassword());
    }

    private void resetPassword() {
        String resetCodeInput = editTextResetCode.getText().toString().trim();
        String newPassword = editTextNewPassword.getText().toString().trim();
        String confirmPassword = editTextConfirmPassword.getText().toString().trim();

         if (TextUtils.isEmpty(resetCodeInput)) {
            Toast.makeText(this, "Please enter the reset code", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(newPassword) || TextUtils.isEmpty(confirmPassword)) {
            Toast.makeText(this, "Please enter and confirm your new password", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            int resetCode = Integer.parseInt(resetCodeInput);
            if (resetCode != expectedResetCode) {
                Toast.makeText(this, "Invalid reset code", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Reset code must be a number", Toast.LENGTH_SHORT).show();
            return;
        }


        int rowsUpdated = userService.updatePassword(email, newPassword);

        if (rowsUpdated > 0) {
            Toast.makeText(this, "Password updated successfully for " + email, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Error updating password!", Toast.LENGTH_SHORT).show();
        }

    }
}
