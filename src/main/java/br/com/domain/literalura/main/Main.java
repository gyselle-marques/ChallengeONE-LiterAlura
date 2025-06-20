package br.com.domain.literalura.main;

import br.com.domain.literalura.model.*;
import br.com.domain.literalura.repository.AutorRepository;
import br.com.domain.literalura.service.ConsultaApi;
import br.com.domain.literalura.service.ConverteDados;
import br.com.domain.literalura.service.LivroService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

@Component
public class Main {
    private final Scanner leitura = new Scanner(System.in);
    private final ConsultaApi consultaApi = new ConsultaApi();
    private final ConverteDados converteDados = new ConverteDados();

    @Autowired
    private LivroService livroService;
    @Autowired
    private AutorRepository autorRepository;

    private final String BASE_URL = "https://gutendex.com/books/?search=";

    public void start() {
        var opcao = -1;

        while (opcao != 0) {
            var menu = """
                \n
                ================================================
                          Bem-vindo(a) ao LiterAlura!
                ================================================
                \n
                > Selecione uma opção:
                
                1 - Buscar livro por título
                2 - Listar livros registrados
                3 - Listar autores registrados
                4 - Listar autores vivos em determinado ano
                5 - Listar livros por idioma
                6 - Top 10 livros mais baixados
                7 - Estatísticas do banco de dados
                0 - Sair
                """;

            System.out.println(menu);

            if(leitura.hasNextInt()) {
                opcao = leitura.nextInt();
                leitura.nextLine(); // Limpa o buffer do scanner

                switch (opcao) {
                    case 1:
                        buscarLivroPorTitulo();
                        break;
                    case 2:
                        listarLivrosRegistrados();
                        break;
                    case 3:
                        listarAutoresRegistrados();
                        break;
                    case 4:
                        listarAutoresVivos();
                        break;
                    case 5:
                        listarLivrosPorIdioma();
                        break;
                    case 6:
                        listarTop10LivrosMaisBaixados();
                        break;
                    case 7:
                        mostrarEstatisticasDoBancoDeDados();
                        break;
                    case 0:
                        System.out.println("\nFinalizando a aplicação...");
                        break;
                    default:
                        System.out.println("\nOpção inválida. Tente novamente.");
                }
            } else {
                System.out.println("\nEntrada inválida. Por favor, digite um número inteiro.");
                leitura.nextLine(); // Limpa a entrada inválida
            }
        }
    }

    @Transactional
    private void buscarLivroPorTitulo() {
        System.out.println("\nDigite o título do livro para busca: ");
        var titulo = leitura.nextLine();

        if (titulo.isBlank() || isANumber(titulo)) {
            System.out.println("\nEntrada inválida. Por favor, digite um título válido.");
            return;
        }

        var json = consultaApi.obterDados(BASE_URL + titulo.replace(" ", "%20"));

        if (json == null || json.isEmpty()) {
            System.out.println("\nNão foi possível obter dados da API ou a busca retornou vazia.");
            return;
        }

        ResultadosLivro resultado = converteDados.obterDados(json, ResultadosLivro.class);
        List<DadosLivro> livros = resultado.getLivros();

        if (!livros.isEmpty()) {
            DadosLivro dadosLivro = livros.get(0);

            DadosAutor autor = new DadosAutor();
            autor.setNome(dadosLivro.getAutores().get(0).getNome());
            autor.setAnoNascimento(Integer.valueOf(dadosLivro.getAutores().get(0).getAnoNascimento()));
            autor.setAnoFalecimento(Integer.valueOf(dadosLivro.getAutores().get(0).getAnoFalecimento()));

            autor = livroService.salvarAutor(autor);

            Livro livro = new Livro();
            livro.setTitulo(dadosLivro.getTitulo());
            livro.setAutor(autor.getNome());
            livro.setIdioma(dadosLivro.getIdiomas().get(0));
            livro.setDownloads(dadosLivro.getDownloads());

            livroService.salvarLivro(livro);

            System.out.println("\n================================================");
            System.out.println("\n                LIVRO ENCONTRADO                ");
            System.out.println("\n================================================");
            System.out.println("\nTitulo: " + dadosLivro.getTitulo());
            System.out.println("\nAutor: " + dadosLivro.getAutores().get(0).getNome());
            System.out.println("\nIdioma: " + dadosLivro.getIdiomas().get(0));
            System.out.println("\nNumero de downloads: " + dadosLivro.getDownloads());
            System.out.println("\n================================================");
        } else {
            System.out.println("\nNenhum livro encontrado para o título informado!");
        }
    }

    private boolean isANumber(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void listarLivrosRegistrados() {
        List<Livro> livros = livroService.listarLivros();

        if (!livros.isEmpty()) {
            livros.forEach(l -> {
                System.out.println("\n================================================");
                System.out.println("Título: " + l.getTitulo());
                System.out.println("Autor: " + (l.getAutor() != null ? l.getAutor() : "Desconhecido"));
                System.out.println("Idioma: " + l.getIdioma());
                System.out.println("Número de downloads: " + (l.getDownloads() != null ? l.getDownloads() : "N/A"));
                System.out.println("================================================");
            });
        } else {
            System.out.println("\nNenhum livro cadastrado no banco de dados.");
        }
    }

    private void listarAutoresRegistrados() {
        List<DadosAutor> autores = autorRepository.findAll();
        if (!autores.isEmpty()) {
            autores.forEach(a -> {
                System.out.println("\n================================================");
                System.out.println("Nome: " + a.getNome());
                System.out.println("Ano de Nascimento: " + (a.getAnoNascimento() != null ? a.getAnoNascimento() : "Ano de nascimento não informado!"));
                System.out.println("Ano de Falecimento: " + (a.getAnoFalecimento() != null ? a.getAnoFalecimento() : "Ano de falecimento não informado!"));

                List<Livro> livros = livroService.buscarLivrosPorAutor(a.getNome());

                if (!livros.isEmpty()) {
                    System.out.println("\nLivros: ");
                    livros.forEach(l ->
                            System.out.println(" - " + l.getTitulo()));
                } else {
                    System.out.println("\nNenhum livro registrado para este autor.");
                }
                System.out.println("\n================================================");
            });
        } else {
            System.out.println("\nNenhum autor cadastrado no banco de dados.");
        }
    }

    private void listarAutoresVivos() {
        System.out.println("\nDigite o ano para consultar autores vivos: ");
        if (leitura.hasNextInt()) {
            var ano = leitura.nextInt();
            leitura.nextLine(); // Limpa o buffer do scanner
            List<DadosAutor> autores = autorRepository.findByAnoNascimentoLessThanEqualAndAnoFalecimentoIsNullOrAnoFalecimentoGreaterThanEqual(ano, ano);

            if (!autores.isEmpty()) {
                System.out.println("\nAutores vivos no ano de " + ano + ":");
                autores.forEach(System.out::println);
            } else {
                System.out.println("\nNenhum autor vivo encontrado no ano de " + ano + ".");
            }
        } else {
            System.out.println("\nEntrada inválida. Por favor, digite um número inteiro para o ano desejado.");
            leitura.next(); // Limpa a entrada inválida
        }
    }

    private void listarLivrosPorIdioma() {
        var opcao = -1;
        String siglaIdioma = "";

        var menuIdiomas = """
                \n
                ================================================
                        Selecione um idioma para consulta:
                ================================================
                \n
                > 1 - Inglês (en)
                > 2 - Espanhol (es)
                > 3 - Francês (fr)
                > 4 - Alemão (de)
                > 5 - Italiano (it)
                > 6 - Português (pt)
                > 0 - Voltar ao menu principal
                """;

        System.out.println(menuIdiomas);

        if (leitura.hasNextInt()) {
            opcao = leitura.nextInt();
            leitura.nextLine(); // Limpa o buffer do scanner

            switch (opcao) {
                case 1:
                    siglaIdioma = "en";
                    break;
                case 2:
                    siglaIdioma = "es";
                    break;
                case 3:
                    siglaIdioma = "fr";
                    break;
                case 4:
                    siglaIdioma = "de";
                    break;
                case 5:
                    siglaIdioma = "it";
                    break;
                case 6:
                    siglaIdioma = "pt";
                    break;
                case 0:
                    System.out.println("\nVoltando ao menu principal...");
                    return;
                default:
                    System.out.println("\nOpção inválida. Tente novamente.");
            }

            System.out.println("\nLivros cadastrados com o idioma: " + siglaIdioma.toUpperCase());
            List<Livro> livros = livroService.buscarLivrosPorIdioma(siglaIdioma);

            if (!livros.isEmpty()) {
                livros.forEach(System.out::println);
            } else {
                System.out.println("\nNenhum livro encontrado com o idioma: " + siglaIdioma.toUpperCase());
            }
        } else {
            System.out.println("\nEntrada inválida. Por favor, digite um número inteiro.");
            leitura.next(); // Limpa a entrada inválida
        }
    }

    private void listarTop10LivrosMaisBaixados() {
        List<Livro> topLivros = livroService.buscarTop10LivrosMaisBaixados();

        if (!topLivros.isEmpty()) {
            System.out.println("\n================================================");
            System.out.println("\n         TOP 10 LIVROS MAIS BAIXADOS");
            System.out.println("\n================================================");
            int posicao = 1;
            for(Livro livro : topLivros) {
                System.out.println(posicao++ + "º lugar: ");
                System.out.println("\nTítulo: " + livro.getTitulo());
                System.out.println("\nAutor: " + (livro.getAutor()));
                System.out.println("\nIdioma: " + livro.getIdioma());
                System.out.println("\nDownloads: " + (livro.getDownloads() != null ? livro.getDownloads() : "N/A"));
                System.out.println("\n================================================");
            }
        } else {
            System.out.println("\nNenhum livro encontrado no banco de dados.");
        }
    }

    private void mostrarEstatisticasDoBancoDeDados() {
        List<Livro> livros = livroService.findAll();

        if (!livros.isEmpty()) {
            IntSummaryStatistics livroStats = livros.stream()
                    .filter(l -> l.getDownloads() != null && l.getDownloads() > 0)
                    .collect(Collectors.summarizingInt(Livro::getDownloads));

            System.out.println("\n================================================");
            System.out.println("\n        ESTATÍSTICAS DO BANCO DE DADOS          ");
            System.out.println("\n================================================");
            System.out.println("Média de downloads: " + String.format("%.2f", livroStats.getAverage()));
            System.out.println("Máximo de downloads: " + livroStats.getMax());
            System.out.println("Mínimo de downloads: " + livroStats.getMin());
            System.out.println("Total de livros com downloads registrados: " + livroStats.getCount());
            System.out.println("\n================================================");
        } else {
            System.out.println("\nNenhum livro encontrado no banco de dados para calcular estatísticas.");
        }
    }
}
