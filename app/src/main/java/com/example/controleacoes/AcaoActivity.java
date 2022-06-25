package com.example.controleacoes;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AcaoActivity extends AppCompatActivity {

    private ListView listViewAcoes;
    private Button btnAdicionar;

    FirebaseDatabase database;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_page);


    }
}
