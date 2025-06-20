package br.com.domain.literalura.service;

public interface IConverteDados {
    <T> T obterDados(String json, Class<T> classe);
}
