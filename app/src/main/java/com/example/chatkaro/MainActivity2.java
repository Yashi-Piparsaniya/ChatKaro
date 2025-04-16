package com.example.chatkaro;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class MainActivity2 extends AppCompatActivity {
    private EditText username;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main2);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        button = findViewById(R.id.button);
        username = findViewById(R.id.username);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Username = username.getText().toString().trim();
                if(TextUtils.isEmpty(Username))
                {
                    Toast.makeText(MainActivity2.this,"Please enter an username", Toast.LENGTH_SHORT).show();
                }
                else{
                    FirebaseUser user = mAuth.getCurrentUser();
                    if (user == null) {
                        Toast.makeText(MainActivity2.this, "User not logged in. Please sign in again.", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(MainActivity2.this, MainActivity3.class)); // redirect to login
                        finish();
                        return;
                    }
                    if (user != null) {
                        user.reload().addOnCompleteListener(task -> {
                            if (user.isEmailVerified()) {
                                Map<String, Object> userMap = new HashMap<>();
                                userMap.put("username", Username);
                                db.collection("users").document(user.getUid())
                                        .set(userMap)
                                        .addOnSuccessListener(aVoid -> {
                                            Toast.makeText(MainActivity2.this, "Username created!!", Toast.LENGTH_LONG).show();
                                            Intent intent1 = new Intent(MainActivity2.this ,MainActivity4.class);
                                            startActivity(intent1);
                                        })
                                        .addOnFailureListener(e -> {
                                            Toast.makeText(MainActivity2.this, "Firestore Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                        });
                            } else {
                                Toast.makeText(MainActivity2.this, "Please verify you email first!", Toast.LENGTH_LONG).show();
                            }
                        });
                    }


                }
            }
        });
    }
}