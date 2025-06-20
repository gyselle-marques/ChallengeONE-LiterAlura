package br.com.domain.literalura.service;

import br.com.domain.literalura.model.Autor;
import br.com.domain.literalura.model.DadosAutor;
import br.com.domain.literalura.model.Livro;
import br.com.domain.literalura.repository.AutorRepository;
import br.com.domain.literalura.repository.LivroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LivroService {
    private final LivroRepository livroRepository;

    @Autowired
    private AutorRepository autorRepository;

    public LivroService(LivroRepository livroRepository) {
        this.livroRepository = livroRepository;
    }

    public List<Livro> listarLivros() {
        return livroRepository.findAll();
    }

    public void salvarLivro(Livro livro) {
        Optional<Livro> livroExistente = livroRepository.findByTituloAndAutor(livro.getTitulo(), livro.getAutor());
        if (livroExistente.isEmpty()) {
            livroRepository.save(livro);
        } else {
            System.out.println("Livro já existe com o título: " + livro.getTitulo());
        }
    }

    public List<Livro> buscarLivrosPorAutor(String nomeAutor) {
        return livroRepository.findByAutor(nomeAutor);
    }

    public DadosAutor salvarAutor(DadosAutor autor) {
        Optional<DadosAutor> autorExistente = autorRepository.findByNome(autor.getNome());
        return autorExistente.orElseGet(() -> autorRepository.save(autor));
        }

    public List<Livro> buscarLivrosPorIdioma(String idioma) {
        return livroRepository.findByIdioma(idioma);
    }

    public List<Livro> buscarTop10LivrosMaisBaixados() {
        return livroRepository.findTop10ByOrderByDownloadsDesc();
    }

    public List<Livro> findAll() {
        return livroRepository.findAll();
    }
}
