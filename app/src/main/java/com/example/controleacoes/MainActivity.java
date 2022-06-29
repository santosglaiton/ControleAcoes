package com.example.controleacoes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private EditText nomeAcao, precoAcao, quantidade;
    private Button btnSalvar;
    private Acao acao;
    private String acoes;
    private String nomeDaAcao;
    private ArrayList<Acao> arrayAcao;
    boolean novaAcao = false;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = database.getReference();

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
            nomeAcao.setText(getIntent().getExtras().getString("nome"));
            precoAcao.setText(getIntent().getExtras().getString("preco"));
            quantidade.setText(getIntent().getExtras().getString("quantidade"));
        }

    }

    public void salvar() {
        if (acoes.equals("adicionar")) {
            acao = new Acao();
        }

        DatabaseReference ds = databaseReference.child("acoes");

        String nome = nomeAcao.getText().toString();

        Query verificaSeNomeExiste = ds.orderByChild("nome");

        System.out.println("CVCCCCCCCCCCCCCCCCCCCCCCCC" + verificaSeNomeExiste);

        databaseReference.child("acoes").orderByChild("nome").equalTo(nome).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                String precoAcaoString = precoAcao.getText().toString();
                String quantidadeAcoesString = quantidade.getText().toString();

                Iterable<DataSnapshot> snapshotAcao = task.getResult().getChildren();

                String key = null;

                Acao acao2 = null;

                for (DataSnapshot snapshot : snapshotAcao) {
                    acao2 = snapshot.getValue(Acao.class);
                    key = snapshot.getKey();
                    System.out.println(snapshot.getValue(Acao.class));
                }

                //Acao acao2 = task.getResult().getValue(Acao.class);

                System.out.println("ASIHDFIUAGHFAYSGDFUAYSGDASJDFSA" + task.getResult().getValue());

                if (acao2 != null) {

                    System.out.println("CAIU AQUIAAAAAAAAAAAA" + acao2);

                    //acao.setId(acao2.getId());

                    acao2.setNome(nome);

                    acao2.setQuantidade(somaQuantidadeAcoes(acao.getQuantidade(), acao2.getQuantidade()));

                    acao2.setPreco(somaPrecoMedio(acao.getPreco(), acao2.getPreco(), acao2.getQuantidade()));

                    acao2.setPreco(precoAcaoString);

                    Map<String, Object> postValues = acao2.toMap();

                    Map<String, Object> childUpdates = new HashMap<>();
                    childUpdates.put("/acoes/" + key, postValues);

                    //databaseReference.child("acoes").push().setValue(acao2);

                    databaseReference.updateChildren(childUpdates);

                    finish();

                } else {
                    novaAcao = true;
                    acao.setNome(nome);
                    acao.setPreco(precoAcaoString);
                    acao.setQuantidade(quantidadeAcoesString);
                    if (!nome.isEmpty() && !precoAcaoString.isEmpty() && !quantidadeAcoesString.isEmpty()) {
                        if (acoes.equals("adicionar")) {
                            if (databaseReference.child("acoes").child("nome").equals(acao.getNome())) {
                                databaseReference.child("acoes").push().setValue(acao);
                            } else {
                                databaseReference.child("acoes").push().setValue(acao);
                            }
                        } else {
                            databaseReference.child("acoes").child(acao.getId()).setValue(acao);
                        }

                        finish();

                    }
                }
            }
        });

    }

    public String somaPrecoMedio(String valorAtual, String valorAntigo, String quantidadeAcoes){
        String precoFinal;
        Double quantidadeFinal;
        Double precoFinalTransition;
        Double precoAntigo;
        Double precoAtual;

        quantidadeFinal = Double.parseDouble(quantidadeAcoes);

        precoAntigo = Double.parseDouble(valorAntigo);
        precoAtual = Double.parseDouble(valorAtual);

        precoFinalTransition = (precoAntigo + precoAtual) / quantidadeFinal;

        precoFinal = String.valueOf(precoFinalTransition);

        return precoFinal;
    }

    public String somaQuantidadeAcoes(String quantidadeAtual, String quantidadeAAdicionar){
        String quantidadeFinal;
        Integer quantidadeExistente;
        Integer quantidadeParaAdicionar;
        Integer quantidadeFinalTransition;

        quantidadeExistente = Integer.parseInt(quantidadeAtual);
        quantidadeParaAdicionar = Integer.parseInt(quantidadeAAdicionar);

        quantidadeFinalTransition = quantidadeExistente + quantidadeParaAdicionar;

        quantidadeFinal = String.valueOf(quantidadeFinalTransition);

        return quantidadeFinal;
    }

}