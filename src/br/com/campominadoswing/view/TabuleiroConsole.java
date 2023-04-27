package br.com.campominadoswing.view;

import br.com.campominadoswing.exception.ExplosaoException;
import br.com.campominadoswing.exception.SairException;
import br.com.campominadoswing.model.Tabuleiro;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Scanner;

public class TabuleiroConsole {
    private Tabuleiro tabuleiro;
    private Scanner entrada = new Scanner(System.in);

    public TabuleiroConsole(Tabuleiro tabuleiro) {
        this.tabuleiro = tabuleiro;

        executarJogo();
    }

    private void executarJogo() {
        try {
            boolean continuar = true;
            while (continuar){
                cicloDoJogo();
                System.out.println("Outra partida? (S/N)");
                String resposta = entrada.nextLine();
                if ("N".equalsIgnoreCase(resposta)){
                    continuar = false;
                }else {
                    tabuleiro.reneciar();
                }
            }

        }catch (SairException e){
            System.out.println("Fim");
        }finally {
            entrada.close();
        }
    }

    private void cicloDoJogo() {
        try {
            while (!tabuleiro.objetivoAlcancado()){
                System.out.println(tabuleiro);
                String digitado = capturaDigitado("Digite ( x, Y): ");

                Iterator<Integer> xy = Arrays.stream(digitado.split(",")).map(e -> Integer.parseInt(e.trim())).iterator();
                digitado = capturaDigitado(("1 - Abrir ou 2 - (Des)Marcar:"));

                if("1".equals(digitado)){
                    tabuleiro.abrir(xy.next(),xy.next());
                }else if("2".equals(digitado)){
                    tabuleiro.alternaMarcacao(xy.next(), xy.next());
                }
            }
            System.out.println("Voce Ganhou!!");
        }catch (ExplosaoException e){
            System.out.println(tabuleiro);
            System.out.println("voce perdeu!");
        }
    }
    private String capturaDigitado(String texto){
        System.out.print(texto);
        String digitado = entrada.nextLine();
        if ("sair".equalsIgnoreCase(digitado)){
            throw new SairException();
        }
        return digitado;
    }

}
