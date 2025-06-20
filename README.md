# <h1 align="center"> :books: Challenge ONE Praticando Spring Boot: LiterAlura :books: </h1>

<div align="center">

LiterAlura é uma aplicação Java desenvolvida para explorar e registrar livros utilizando a API [Gutendex](https://gutendex.com/). O projeto permite buscar livros, listar autores, consultar estatísticas e gerenciar um banco de dados local com informações relevantes sobre literatura mundial.

</div>

## :gear: Funcionalidades
- **Buscar livro por título:** Pesquisa livros na API do Gutendex e cadastra no banco de dados.
- **Listar livros registrados:** Exibe todos os livros salvos localmente.
- **Listar autores registrados:** Mostra todos os autores presentes no banco de dados.
- **Listar autores vivos em determinado ano:** Filtra autores que estavam vivos em um ano específico.
- **Listar livros por idioma:** Permite visualizar livros cadastrados por idioma.
- **Top 10 livros mais baixados:** Exibe os livros mais populares segundo a API.
- **Estatísticas do banco de dados:** Mostra dados agregados sobre livros e autores cadastrados.

## :computer: Tecnologias Utilizadas
- Java 17+
- Spring Boot
- JPA/Hibernate
- H2 Database (padrão, pode ser alterado)
- Maven

## :arrow_forward: Como Executar
1. Clone o repositório:
   ```bash
   git clone git@github.com:gyselle-marques/ChallengeONE-LiterAlura.git
   ```
2. Acesse a pasta do projeto:
   ```bash
   cd literalura
   ```
3. Execute o projeto com Maven:
   ```bash
   ./mvnw spring-boot:run
   ```
   Ou, se preferir, utilize sua IDE favorita.

## :pushpin: Estrutura do Projeto
- `main/java/br/com/domain/literalura/` - Código fonte principal
- `model/` - Entidades e modelos de dados
- `repository/` - Interfaces de acesso a dados
- `service/` - Serviços e integrações com API externa
- `main/` - Classe principal e menu de interação

## :wrench: Configuração
As configurações do banco de dados e outras propriedades estão no arquivo `src/main/resources/application.properties`.

## :pencil2: Créditos
Desenvolvido como parte de estudos de Java e Spring Framework da formação ONE | TECH FOUNDATION - Especialização Back-End.

## :page_facing_up: Licença
Este projeto está sob a licença MIT.