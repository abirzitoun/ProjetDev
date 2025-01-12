package com.example.pharmacie;
import java.util.Random;

public class CodeGenerator {
    public static String generateCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000); // Génère un nombre entre 100000 et 999999
        return String.valueOf(code);
    }
}
