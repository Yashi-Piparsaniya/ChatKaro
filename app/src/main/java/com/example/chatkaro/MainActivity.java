package com.example.chatkaro;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    private Button button1;
    EditText emailid;
    EditText password;
    private CheckBox tnc;
    private Button button2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        emailid = findViewById(R.id.emailid);
        password = findViewById(R.id.password);
        tnc = findViewById(R.id.tnc);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email =emailid.getText().toString().trim();
                String password1 = password.getText().toString().trim();
                if(TextUtils.isEmpty(email))
                {
                    Toast.makeText(MainActivity.this,"Please enter your email id", Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(password1))
                {
                    Toast.makeText(MainActivity.this,"Please enter your password", Toast.LENGTH_SHORT).show();
                }
                else if (password1.length() < 6)
                {
                    Toast.makeText(MainActivity.this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
                }

                else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())
                {
                    Toast.makeText(MainActivity.this, "Invalid email format", Toast.LENGTH_SHORT).show();
                }
                else if (!tnc.isChecked()) {
                    Toast.makeText(MainActivity.this,"Please agree to our Terms and Conditions!", Toast.LENGTH_SHORT).show();
                }
                else{
                        mAuth.createUserWithEmailAndPassword(email, password1)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            FirebaseUser user = mAuth.getCurrentUser();
                                            if (user != null) {
                                                user.sendEmailVerification()
                                                        .addOnCompleteListener(task1 -> {
                                                            if (task1.isSuccessful()) {
                                                                Map<String, Object> userMap = new HashMap<>();
                                                                userMap.put("emailid", email);
                                                                db.collection("users").document(user.getUid())
                                                                        .set(userMap)
                                                                        .addOnSuccessListener(aVoid -> {
                                                                            Toast.makeText(MainActivity.this, "Registration successful! Please verify your email.", Toast.LENGTH_LONG).show();
                                                                            Intent intent1 = new Intent(MainActivity.this ,MainActivity2.class);
                                                                            startActivity(intent1);
                                                                        })
                                                                        .addOnFailureListener(e -> {
                                                                            Toast.makeText(MainActivity.this, "Firestore Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                                                        });
                                                            } else {
                                                                Exception e = task1.getException();
                                                                Toast.makeText(MainActivity.this, "Verification email failed: " + (e != null ? e.getMessage() : "Unknown error"), Toast.LENGTH_LONG).show();
                                                            }
                                                        });
                                            }

                                        } else {
                                            Toast.makeText(MainActivity.this, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });

                }
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(MainActivity.this ,MainActivity3.class);
                startActivity(intent2);
            }
        });
    }
}