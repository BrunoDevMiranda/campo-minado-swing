package br.com.campominadoswing.view;

import br.com.campominadoswing.model.Campo;
import br.com.campominadoswing.model.CampoEvento;
import br.com.campominadoswing.model.CampoObservador;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class ButtonCampo extends JButton implements CampoObservador, MouseListener {
    private final Campo campo;
    private final Color BG_PADRAO = new Color(184, 184, 184);
    private final Color BG_MARCAR = new Color(8, 179, 247);
    private final Color BG_EXPLODIR = new Color(189, 66, 68);
    private final Color TEXTO_VERDE = new Color(0, 100, 0);

    private final ImageIcon IMG_BOMBA = new ImageIcon("image/bomba.png");
    private final ImageIcon IMG_BANDEIRA = new ImageIcon("image/bandeira.png");

    public ButtonCampo(Campo campo) {
        this.campo = campo;
        setBackground(BG_PADRAO);
        setOpaque(true);
        setBorder(BorderFactory.createBevelBorder(0));

        addMouseListener(this);
        campo.registrarObservadores(this);

    }


    @Override
    public void evento(Campo campo, CampoEvento evento) {
        switch (evento) {
            case ABRIR ->  aplicarEstiloAbrir();

            case MARCAR ->  aplicarEstiloMarcar();

            case EXPLODIR -> aplicarEstiloExplodir();

            default -> aplicarEstiloPadrao();
        }
        SwingUtilities.invokeLater(() -> {
            repaint();
            validate();
        });
    }

    private void aplicarEstiloPadrao() {
        setBackground(BG_PADRAO);
        setBorder(BorderFactory.createBevelBorder(0));
        setText("");
        setIcon(null);
    }

    private void aplicarEstiloExplodir() {
        setBackground(BG_EXPLODIR);
        setIcon(IMG_BOMBA);
    }


    private void aplicarEstiloMarcar() {
        setBackground(BG_MARCAR);
//        setText("M"); // aqui uma imagem de bandeira
        setIcon(IMG_BANDEIRA);


    }

    private void aplicarEstiloAbrir() {
        if (campo.isMinado()) {
            setBackground(BG_EXPLODIR);
            return;
        }
        setBackground(BG_PADRAO);
        setBorder(BorderFactory.createLineBorder(Color.GRAY));
        switch (campo.minaNaVizinnhaca()) {
            case 1 -> setForeground(TEXTO_VERDE);
            case 2 -> setForeground(Color.BLUE);
            case 3 -> setForeground(Color.yellow);
            case 4, 5, 6 -> setForeground(Color.RED);
            default -> setForeground(Color.PINK);
        }
        String valor = !campo.vizinhaSegura() ? campo.minaNaVizinnhaca() + "" : "";
        setText(valor);
    }


    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getButton() == 1) {
            campo.abrir();
        } else campo.alternaMarcacao();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // TODO document why this method is empty
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // TODO document why this method is empty
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // TODO document why this method is empty
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // TODO document why this method is empty
    }


}
