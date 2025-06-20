package br.com.domain.literalura.model;

import jakarta.persistence.*;

@Entity
@Table(name = "livros")
public class Livro {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;
    private String titulo;
    private String autor;
    private String idioma;
    private Integer downloads;

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

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    public Integer getDownloads() {
        return downloads;
    }

    public void setDownloads(Integer downloads) {
        this.downloads = downloads;
    }

    @Override
    public String toString() {
        //String atorNome = (autor != null) ? autor(). : "Desconhecido";
        String numeroDownloads = (downloads != null) ? downloads.toString() : "N/A";
        String listaIdiomas = (idioma != null && !idioma.isEmpty()) ? String.join(", ", idioma) : "N/A";

        return "\n================================================" +
                "\nTítulo: " + titulo +
                "\nAutor: " + autor +
                "\nIdioma: " + idioma +
                "\n Número de downloads: " + downloads +
                "\n================================================";
    }
}
