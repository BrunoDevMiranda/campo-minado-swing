package br.com.campominadoswing.application;

import br.com.campominadoswing.model.Tabuleiro;
import br.com.campominadoswing.view.TabuleiroConsole;

public class Main {
    public static void main(String[] args) {
        Tabuleiro tabuleiro = new Tabuleiro(5, 5, 5);
        new TabuleiroConsole(tabuleiro);
    }
}