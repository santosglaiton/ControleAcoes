package com.example.controleacoes;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Acao {

    public String id;
    public String nome;
    public String preco;
    public String quantidade;
    public String precoTotal;

    public Acao() {
    }

    public Acao(String nome, String preco, String quantidade, String precoTotal) {
        this.nome = nome;
        this.preco = preco;
        this.quantidade = quantidade;
        this.precoTotal = precoTotal;
    }

    public Acao(String id, String nome, String preco, String quantidade, String precoTotal) {
        this.id = id;
        this.nome = nome;
        this.preco = preco;
        this.quantidade = quantidade;
        this.precoTotal = precoTotal;
    }

    public String getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(String quantidade) {
        this.quantidade = quantidade;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getPreco() {
        return preco;
    }

    public void setPreco(String preco) {
        this.preco = preco;
    }

    public String getPrecoTotal() {
        return precoTotal;
    }

    public void setPrecoTotal(String precoTotal) {
        this.precoTotal = precoTotal;
    }

    @Override
    public String toString() {
        return "Acao: " + nome + '\n' + "Preco medio: " + preco + '\n' + "Quantidade: " + quantidade + '\n' + "Preco Total: " + precoTotal;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("nome", nome);
        result.put("preco", preco);
        result.put("quantidade", quantidade);
        result.put("precoTotal", precoTotal);

        return result;
    }


}
