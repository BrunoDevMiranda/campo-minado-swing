package br.com.campominadoswing.model;


import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class Tabuleiro implements CampoObservador {
    private final int linhas;
    private final int colunas;
    private final int minas;

    public int getLinhas() {
        return linhas;
    }

    public int getColunas() {
        return colunas;
    }

    public int getMinas() {
        return minas;
    }

    private final List<Campo> campos = new ArrayList<>();
    private final List<Consumer<Boolean>> observadores = new ArrayList<>();

    public Tabuleiro(int linhas, int colunas, int minas) {
        this.linhas = linhas;
        this.colunas = colunas;
        this.minas = minas;

        gerarCampo();
        associarVizinhos();
        sorteaMinas();
    }
    public void paraCada(Consumer<Campo> funcao){
        campos.forEach(funcao);
    }

    public void registrarObservador(Consumer<Boolean> observador) {
        observadores.add(observador);
    }

    private void notificarObservadores(Boolean resultado) {
        observadores.stream()
                .forEach(o -> o.accept(resultado));
    }


    public void abrir(int linha, int coluna) {

            campos.parallelStream()
                    .filter(campo -> campo.getLinha() == linha && campo.getColuna() == coluna)
                    .findFirst()
                    .ifPresent(campo -> campo.abrir());
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
                Campo campo = new Campo(linha, coluna);
                campo.registrarObservadores(this);
                campos.add(campo);
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
    @Override
    public void evento(Campo campo, CampoEvento evento) {
        if (evento == CampoEvento.EXPLODIR) {
            mostrarMinas();
            notificarObservadores(false);
        } else if (objetivoAlcancado()) {
            notificarObservadores(true);
        }
    }
    public void mostrarMinas(){
        campos.stream()
                .filter(c ->  c.isMinado())
                .filter(c -> !c.isMarcado())
                .forEach(c -> c.setAberto(true));
    }

}
