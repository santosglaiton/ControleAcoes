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

                    acao2.setPrecoTotal(precoTotal(acao.getPreco(), acao.getQuantidade(), acao2.getPrecoTotal() ));

                    acao2.setQuantidade(somaQuantidadeAcoes(quantidadeAcoesString, acao2.getQuantidade()));

                    acao2.setPreco(somaPrecoMedio( acao2.getPrecoTotal(), acao2.getQuantidade()));

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
                    acao.setPrecoTotal(precoTotal(acao.getPreco(), acao.getQuantidade(),  "0"));
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

    public String precoTotal(String valorAtual, String quantidadeAcoes, String precoTotal){

        Double valorDeAgora;
        Double quantidade;
        valorDeAgora = Double.parseDouble(valorAtual);
        quantidade = Double.parseDouble(quantidadeAcoes);
        Double precoTotal1;

        precoTotal1 = Double.parseDouble(precoTotal);

        Double resultadoDouble;

        resultadoDouble = (valorDeAgora * quantidade) + precoTotal1;

        return resultadoDouble.toString();
    }


    public String somaPrecoMedio(String valorAtual, String quantidadeAcoes){
        String precoFinal;
        Double quantidadeFinal;
        Double precoFinalTransition;

        Double precoAtual;


        quantidadeFinal = Double.parseDouble(quantidadeAcoes);


        System.out.println("PRECO atual" + valorAtual);



        precoAtual = Double.parseDouble(valorAtual);

        precoFinalTransition = precoAtual / quantidadeFinal;


        precoFinal = String.valueOf(precoFinalTransition);

        System.out.println("PRECO FINAL " + precoFinal);

        return precoFinal;
    }

    public String somaQuantidadeAcoes(String quantidadeAtual, String quantidadeAAdicionar){
        String quantidadeFinal;
        Integer quantidadeExistente;
        Integer quantidadeParaAdicionar;
        Integer quantidadeFinalTransition;

        System.out.println("QUANTIDADE ATUAL " + quantidadeAtual);

        quantidadeExistente = Integer.parseInt(quantidadeAtual);

        quantidadeParaAdicionar = Integer.parseInt(quantidadeAAdicionar);

        quantidadeFinalTransition = quantidadeExistente + quantidadeParaAdicionar;

        quantidadeFinal = String.valueOf(quantidadeFinalTransition);

        return quantidadeFinal;
    }

}