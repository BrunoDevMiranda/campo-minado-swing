package br.com.campominadoswing.model;



import java.util.ArrayList;
import java.util.List;

public class Campo {

    private final int linha;
    private final int coluna;
    private boolean aberto = false;
    private boolean minado = false;
    private boolean marcado = false;
    private final List<Campo> vizinhos = new ArrayList<>();
    private final List<CampoObservador> observadores = new ArrayList<>();

    public Campo(int linha, int coluna) {
        this.linha = linha;
        this.coluna = coluna;
    }


    public void registrarObservadores(CampoObservador observador) {
        observadores.add(observador);
    }
    private void notificarObservadores(CampoEvento evento){
        observadores
                .forEach(o -> o.evento(this, evento));
    }

    public void adicionarVizinho(Campo vizinho) {
        boolean linhaDiferente = linha != vizinho.linha;
        boolean colunaDiferente = coluna != vizinho.coluna;
        boolean diagonal = linhaDiferente && colunaDiferente;

        int deltaLinha = Math.abs(linha - vizinho.linha);
        int deltaColuna = Math.abs(coluna - vizinho.coluna);
        int deltaGeral = deltaLinha + deltaColuna;

        if (deltaGeral == 1) vizinhos.add(vizinho);
        else if (deltaGeral == 2 && diagonal) {
            vizinhos.add(vizinho);
        }
    }

    public void alternaMarcacao() {
        if (!aberto) {
            marcado = !marcado;
            if (marcado){
                notificarObservadores(CampoEvento.MARCAR);
            }else {
                notificarObservadores(CampoEvento.DESMARCAR);
            }
        }
    }

    public void abrir() {
        if (!aberto && !marcado) {
            aberto = true;
            if (minado) {
                notificarObservadores(CampoEvento.EXPLODIR);
                return;
            }
            setAberto(true);

            if (vizinhaSegura()) {
                vizinhos.forEach(Campo::abrir);
            }
        }
    }

    public boolean vizinhaSegura() {
        return vizinhos.stream().noneMatch(v -> v.minado);
    }
    public boolean isMarcado() {
        return marcado;    }

    public void minar() {
        minado = true;
    }

    public boolean isMinado() {
        return minado;
    }

    public boolean objetivoAlcancado() {
        boolean desvendado = !minado && aberto;
        boolean protegido = minado && marcado;
        return desvendado || protegido;
    }

   public int minaNaVizinnhaca() {
        return (int) vizinhos.stream().filter(v -> v.minado).count();
    }

    void reniciar() {
        aberto = false;
        marcado = false;
        minado = false;
        notificarObservadores(CampoEvento.RENICIAR);
    }

    public int getLinha() {
        return linha;
    }

    public int getColuna() {
        return coluna;
    }

    public void setAberto(boolean aberto) {
        this.aberto = aberto;
        if (aberto){
            notificarObservadores(CampoEvento.ABRIR);
        }
    }

}
