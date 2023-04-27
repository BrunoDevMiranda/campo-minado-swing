package br.com.campominadoswing.model;

import br.com.campominadoswing.exception.ExplosaoException;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class Tabuleiro {
    private int linhas;
    private int colunas;
    private int minas;

    private final List<Campo> campos = new ArrayList<>();

    public Tabuleiro(int linhas, int colunas, int minas) {
        this.linhas = linhas;
        this.colunas = colunas;
        this.minas = minas;

        gerarCampo();
        associarVizinhos();
        sorteaMinas();
    }

    public void abrir(int linha, int coluna) {
        try {
            campos.parallelStream()
                    .filter(campo -> campo.getLinha() == linha && campo.getColuna() == coluna)
                    .findFirst()
                    .ifPresent(campo -> campo.abrir());

        } catch (ExplosaoException e) {
            campos.forEach(campo -> campo.setAberto(true));
            throw e;
        }
    }

    public void alternaMarcacao(int linha, int coluna) {
        campos.parallelStream()
                .filter(campo -> campo.getLinha() == linha && campo.getColuna() == coluna)
                .findFirst()
                .ifPresent(campo -> campo.alternaMarcacao());
    }

    private void gerarCampo() {
        for (int linha = 0; linha < linhas; linha++) {
            for (int coluna = 0; coluna < colunas; coluna++) {
                campos.add(new Campo(linha, coluna));

            }
        }
    }

    private void associarVizinhos() {
        for (Campo c1 : campos) {
            for (Campo c2 : campos) {
                c1.adicionarVizinho(c2);
            }
        }

    }

    private void sorteaMinas() {
        long minasArmadas = 0;
        Predicate<Campo> minado = campo -> campo.isMinado();

        do {
            int aleatorio = (int) (Math.random() * campos.size());
            minasArmadas = campos.stream().filter(minado).count();
            campos.get(aleatorio).minar();
        } while (minasArmadas < minas);
    }

    public boolean objetivoAlcancado() {
        return campos.stream().allMatch(campo -> campo.objetivoAlcancado());
    }

    public void reneciar() {
        campos.stream().forEach(campo -> campo.reniciar());
        sorteaMinas();
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("  ");
        for (int coluna = 0; coluna < colunas; coluna++) {
            stringBuilder.append(" ");
            stringBuilder.append(coluna);
            stringBuilder.append(" ");
        }
        stringBuilder.append("\n");

        int i = 0;
        for (int linha = 0; linha < linhas; linha++) {
            stringBuilder.append(linha);
            stringBuilder.append(" ");
            for (int coluna = 0; coluna < colunas; coluna++) {
                stringBuilder.append(" ");
                stringBuilder.append(campos.get(i));
                stringBuilder.append(" ");
                i++;
            }
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }
}
