package com.example.chatkaro;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class MainActivity4 extends AppCompatActivity {
    private Button logout;
    private Button send;
    EditText msg ;
    RecyclerView rv;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main4);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        logout = findViewById(R.id.logout);
        rv = findViewById(R.id.rv);
        send = findViewById(R.id.send);
        msg = findViewById(R.id.msg);
        mAuth = FirebaseAuth.getInstance();
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent intent2 = new Intent(MainActivity4.this ,MainActivity3.class);
                startActivity(intent2);
            }
        });
        ArrayList<Messages> messagesList = new ArrayList<>();
        Adapter adapter = new Adapter(messagesList);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Msg = msg.getText().toString().trim();
                if (!Msg.isEmpty()) {
                    messagesList.add(new Messages(Msg));
                    adapter.notifyItemInserted(messagesList.size() - 1);
                    rv.scrollToPosition(messagesList.size() - 1); 
                    msg.setText("");
                }
            }
        });
    }
}