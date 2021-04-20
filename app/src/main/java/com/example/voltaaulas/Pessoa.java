package com.example.voltaaulas;

public class Pessoa {
    private String nome;
    private String sobrenome;
    private int idade;
    private double peso;
    private double altura;

    //Atalho para montar o construtor: Alt + Insert
    public Pessoa(String nome, String sobrenome, int idade, double peso, double altura) {
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.idade = idade;
        this.peso = peso;
        this.altura = altura;
    }

    //Atalho para criar os métodos Get: Alt + Insert
    public String getNome() {
        return nome;
    }

    public String getSobrenome() {
        return sobrenome;
    }

    public int getIdade() {
        return idade;
    }

    public double getPeso() {
        return peso;
    }

    public double getAltura() {
        return altura;
    }
}
