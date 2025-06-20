package br.com.domain.literalura.repository;

import br.com.domain.literalura.model.DadosAutor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AutorRepository extends JpaRepository<DadosAutor, Long> {

    // Método para listar autores vivos
    List<DadosAutor> findByAnoNascimentoLessThanEqualAndAnoFalecimentoIsNullOrAnoFalecimentoGreaterThanEqual(int ano, int ano1);

    // Método para contar o número total de autores
    long count();

    Optional<DadosAutor> findByNome(String nome);
}
