package br.com.catalogo.model;

import java.time.LocalDateTime;

/**
 * Entidade que representa um item do catálogo (livro, série ou filme).
 */
public class ItemCatalogo {

    private Long id;
    private String titulo;
    private Tipo tipo;
    private String genero;
    private Integer anoLancamento;
    private String descricao;
    private Double avaliacao;
    private LocalDateTime dataCadastro;

    public ItemCatalogo() {
    }

    public ItemCatalogo(Long id, String titulo, Tipo tipo, String genero, Integer anoLancamento,
                        String descricao, Double avaliacao, LocalDateTime dataCadastro) {
        this.id = id;
        this.titulo = titulo;
        this.tipo = tipo;
        this.genero = genero;
        this.anoLancamento = anoLancamento;
        this.descricao = descricao;
        this.avaliacao = avaliacao;
        this.dataCadastro = dataCadastro;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public Integer getAnoLancamento() {
        return anoLancamento;
    }

    public void setAnoLancamento(Integer anoLancamento) {
        this.anoLancamento = anoLancamento;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Double getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(Double avaliacao) {
        this.avaliacao = avaliacao;
    }

    public LocalDateTime getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDateTime dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    @Override
    public String toString() {
        return "ItemCatalogo{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", tipo=" + tipo +
                ", genero='" + genero + '\'' +
                ", anoLancamento=" + anoLancamento +
                ", avaliacao=" + avaliacao +
                '}';
    }
}
