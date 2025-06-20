package br.com.domain.literalura.model;

import br.com.domain.literalura.repository.LivroRepository;
import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.persistence.*;

@Entity
@Table(name = "autores")
public class Autor {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    @JsonAlias("name")
    private String nome;

    @JsonAlias("birth_year")
    private Integer anoNascimento;

    @JsonAlias("death_year")
    private Integer anoFalecimento;

    @Transient
    private LivroRepository livroRepository;

    public Autor() { }

    //Convertendo String (ano de nascimento e falecimento) para Integer
    private Integer parseYear(String anoString) {
        try {
            return anoString != null && !anoString.isEmpty() ? Integer.valueOf(anoString) : null;
        } catch (NumberFormatException e) {
            System.err.println("Não foi possível converter o ano '" + anoString + "' para número. Foi definido como nulo.");
            return null; // Retorna null se a conversão falhar
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getAnoNascimento() {
        return anoNascimento;
    }

    public void setAnoNascimento(Integer anoNascimento) {
        this.anoNascimento = anoNascimento;
    }

    public Integer getAnoFalecimento() {
        return anoFalecimento;
    }

    public void setAnoFalecimento(Integer anoFalecimento) {
        this.anoFalecimento = anoFalecimento;
    }
}
