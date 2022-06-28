package com.example.controleacoes;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private EditText nomeAcao, precoAcao, quantidade;
    private Button btnSalvar;
    private Acao acao;
    private String acoes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nomeAcao = findViewById(R.id.nomeAcao);
        precoAcao = findViewById(R.id.precoAcao);
        quantidade = findViewById(R.id.quantidadeAcao);
        btnSalvar = findViewById(R.id.btnSalvar);

        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salvar();
            }
        });

        acoes = getIntent().getExtras().getString("acoes");
        if (acoes.equals("editar")){
            acao = new Acao();
            acao.setId(getIntent().getExtras().getString("id"));
            acao.setNome(getIntent().getExtras().getString("nome"));
            acao.setPreco(getIntent().getExtras().getString("preco"));
            acao.setQuantidade(getIntent().getExtras().getString("quantidade"));
        }

    }

    public void salvar(){
        if (acoes.equals("adicionar")){
            acao = new Acao();
        }

        String nome = nomeAcao.getText().toString();
        String precoAcaoString = precoAcao.getText().toString();
        String quantidadeAcoesString = quantidade.getText().toString();
        if (!nome.isEmpty() && !precoAcaoString.isEmpty() && !quantidadeAcoesString.isEmpty()){
            acao.setNome(nome);
            acao.setPreco(precoAcaoString);
            acao.setQuantidade(quantidadeAcoesString);

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference = database.getReference();

            if (acoes.equals("adicionar")){
                databaseReference.child("acoes").push().setValue(acao);
            }else{
                databaseReference.child("acoes").child(acao.getId()).setValue(acao);
            }

            finish();

        }
    }

}