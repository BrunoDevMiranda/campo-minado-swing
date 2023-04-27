package br.com.campominadoswing.view;

import br.com.campominadoswing.model.Tabuleiro;

import javax.swing.*;
import java.awt.*;

public class PainelTabuleiro extends JPanel {
    public PainelTabuleiro(Tabuleiro tabuleiro) {
        setLayout(new GridLayout(tabuleiro.getLinhas(),tabuleiro.getColunas()));

        tabuleiro.paraCada(c-> add(new ButtonCampo(c)));


        tabuleiro.registrarObservador(e -> {
            SwingUtilities.invokeLater(() ->{
                if (e){
                    JOptionPane.showMessageDialog(null,"Ganhou!");
                }else {
                    JOptionPane.showMessageDialog(null,"Perdeu!");
                }
                tabuleiro.reneciar();
            });



        });


    }
}
