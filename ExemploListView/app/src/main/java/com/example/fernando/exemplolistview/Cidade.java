package com.example.fernando.exemplolistview;

public class Cidade {

    private String nome;
    private String descricao;
    private String uf;
    private int resIdImagem;

    public Cidade(String nome, String descricao, String uf, int resIdImagem){
        this.nome = nome;
        this.descricao = descricao;
        this.uf = uf;
        this.resIdImagem = resIdImagem;
    }

    public String getNome(){ return this.nome; }
    public String getDescricao(){ return this.descricao; }
    public String getUf(){ return this.uf; }
    public int getIdImagem(){ return this.resIdImagem; }
}
