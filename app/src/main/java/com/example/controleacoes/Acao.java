package com.example.controleacoes;

public class Acao {

    public String id;
    public String nome;
    public Double preco;

    public Acao() {
    }

    public Acao(String nome, Double preco) {
        this.nome = nome;
        this.preco = preco;
    }

    public Acao(String id, String nome, Double preco) {
        this.id = id;
        this.nome = nome;
        this.preco = preco;
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

    public Double getPreco() {
        return preco;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }
}
