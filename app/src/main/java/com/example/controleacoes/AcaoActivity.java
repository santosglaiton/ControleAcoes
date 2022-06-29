package com.example.controleacoes;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;

public class AcaoActivity extends AppCompatActivity {

    private ListView listViewAcoes;
    private Button btnAdicionar;

    private List<Acao> lista = new ArrayList<>();
    private ArrayAdapter adapter;

    FirebaseDatabase database;
    DatabaseReference databaseReference;

    ChildEventListener childEventListener;
    Query query;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_page);

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();

        listViewAcoes = findViewById(R.id.listViewAcoes);
        btnAdicionar = findViewById(R.id.btnAdicionar);

        btnAdicionar.setOnClickListener(v -> {
            Intent intent = new Intent(AcaoActivity.this, MainActivity.class);
            intent.putExtra("acoes", "adicionar");
            startActivity(intent);

        });

        listViewAcoes.setOnItemClickListener((parent, view, position, id) -> {
            Acao acaoSelecionada = lista.get( position );

            Intent intent = new Intent(AcaoActivity.this, MainActivity.class);
            intent.putExtra("acoes", "editar");
            intent.putExtra("id", acaoSelecionada.getId());
            intent.putExtra("nome", acaoSelecionada.getNome());
            intent.putExtra("preco", acaoSelecionada.getPreco());
            intent.putExtra("quantidade", acaoSelecionada.getQuantidade());
            startActivity(intent);
        });
    }

    @Override
    protected void onStart(){
        super.onStart();
        carregarInvestimentos();

        lista.clear();

        query = databaseReference.child("acoes").orderByChild("nome");

        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Acao acao1 = new Acao();
                acao1.setId(snapshot.getKey());
                acao1.setNome(snapshot.child("nome").getValue(String.class));
                acao1.setPreco(snapshot.child("preco").getValue(String.class));
                acao1.setQuantidade(snapshot.child("quantidade").getValue(String.class));
                lista.add(acao1);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                Acao acaoAtualizada = snapshot.getValue(Acao.class);

                String idAcao = snapshot.getKey();



                for (Acao acao : lista){
                    if (acao.getId().equals(idAcao)){
                        acao.setNome(snapshot.child("nome").getValue(String.class));
                        acao.setPreco(snapshot.child("preco").getValue(String.class));
                        acao.setQuantidade(snapshot.child("preco").getValue(String.class));
                        break;
                    }
                }
            adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        query.addChildEventListener(childEventListener);

    }

    @Override
    protected void onStop(){
        super.onStop();
        query.removeEventListener(childEventListener);
    }

    private void carregarInvestimentos(){

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, lista);
        listViewAcoes.setAdapter(adapter);
    }


}
