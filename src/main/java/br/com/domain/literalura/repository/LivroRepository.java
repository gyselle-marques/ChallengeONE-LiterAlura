package br.com.domain.literalura.repository;

import br.com.domain.literalura.model.Livro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LivroRepository extends JpaRepository<Livro, Long> {


    Optional<Livro> findByTituloAndAutor(String titulo, String autor);

    // Método para listar livros por idioma
    List<Livro> findByIdioma(String idioma);

    // Método para listar os 10 livros mais baixados
    List<Livro> findTop10ByOrderByDownloadsDesc();

    // Método para contar o número total de livros
    long count();

    // Método para buscar livros por autor
    List<Livro> findByAutor(String autor);
}
