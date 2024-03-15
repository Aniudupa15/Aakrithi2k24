package com.example.aakrithi2k24;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class scanner extends AppCompatActivity {
    private CodeScanner mCodeScanner;
    private TextView textView;
    FirebaseFirestore db;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scanner);
        CodeScannerView scannerView = findViewById(R.id.scannerView);
        textView = findViewById(R.id.textview);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        mCodeScanner = new CodeScanner(this, scannerView);
        mCodeScanner.setDecodeCallback(result -> runOnUiThread(() -> {
            String scannedText = result.getText();
            Toast.makeText(scanner.this, scannedText, Toast.LENGTH_SHORT).show();
            DocumentReference document = db.collection("user").document(scannedText);
            document.get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    // Assuming "reg" is a field in your Firestore document
                    String registration = documentSnapshot.getString("reg");
                    textView.setText(registration);
                    Toast.makeText(scanner.this, registration, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(scanner.this, "Data not found", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(scanner.this, "Failed to fetch data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }));
        scannerView.setOnClickListener(view -> mCodeScanner.startPreview());
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
    }

    @Override
    protected void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }
}