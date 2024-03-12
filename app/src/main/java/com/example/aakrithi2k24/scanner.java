package com.example.aakrithi2k24;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;

public class scanner extends AppCompatActivity {
    private CodeScanner mCodeScanner;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scanner);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) CodeScannerView scannerView = findViewById(R.id.scannerView);
        mCodeScanner = new CodeScanner(this, scannerView);
        mCodeScanner.setDecodeCallback(result -> {
            runOnUiThread(() -> {
                mDatabase = FirebaseDatabase.getInstance().getReference();
                Task<DataSnapshot> dataSnapshotTask = mDatabase.child("users").child("result").get().addOnCompleteListener(task -> {
                    if ("registeredEvents" == null) {
                        Toast.makeText(scanner.this, "registered", Toast.LENGTH_SHORT).show();
                        Toast.makeText(scanner.this, "good", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(scanner.this, "bad", Toast.LENGTH_SHORT).show();
                    }
                });
            });
        });
        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCodeScanner.startPreview();
            }
        });
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