package org.example.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class MainView extends javax.swing.JFrame {
    private JLabel label = new JLabel();

    private JButton explorer = new JButton();
    private JButton standard = new JButton();
    private JButton speichern = new JButton();
    private JButton löschen = new JButton();

    private JTextField textfield = new JTextField();


    public static void main(String[] args) {
        new MainView();
    }


    public MainView() {

        setTitle("csv reader");
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);


        JPanel jPanel = new JPanel(new GridBagLayout());

        GridBagConstraints constraints = new GridBagConstraints();

        setSize(400,150);


        getContentPane().add(jPanel);

        label.setText("Wähle Datei zum auslesen aus oder nutze standard datei");
        explorer.setText("Explorer");
        standard.setText("Standardfile");
        speichern.setText("speichern");
        löschen.setText("löschen");

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 3;
        constraints.ipadx = 10;
        constraints.insets = new Insets(15,10,0,0);
        jPanel.add(label,constraints);


        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 1;
        constraints.insets = new Insets(10,0,10,0);
        jPanel.add(explorer,constraints);

        constraints.gridx = 2;
        constraints.gridy = 2;
        constraints.gridwidth = 1;
        constraints.insets = new Insets(10,0,10,0);
        jPanel.add(standard,constraints);

        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.gridwidth = 1;
        constraints.insets = new Insets(10,0,10,0);
        jPanel.add(speichern,constraints);

        constraints.gridx = 2;
        constraints.gridy = 3;
        constraints.gridwidth = 1;
        constraints.insets = new Insets(10,0,10,0);
        jPanel.add(löschen,constraints);

        // pack();

        setVisible(true);




    }

    public void explorer(ActionListener actionListener){
        explorer.addActionListener(actionListener);
    }

    public void standard(ActionListener actionListener){
        standard.addActionListener(actionListener);
    }

    public void speichern(ActionListener actionListener){
        speichern.addActionListener(actionListener);
    }

    public void löschen(ActionListener actionListener){
        löschen.addActionListener(actionListener);
    }

}
