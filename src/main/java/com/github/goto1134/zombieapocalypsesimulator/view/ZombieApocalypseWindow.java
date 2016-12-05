package com.github.goto1134.zombieapocalypsesimulator.view;

import javax.swing.*;

/**
 * Created by Andrew
 * on 03.12.2016.
 */
public class ZombieApocalypseWindow extends JFrame {
    private JFormattedTextField weaponsCountTextField;
    private JFormattedTextField characterNumberTextField;
    private JFormattedTextField mapSizeTextField;
    private JButton startButton;
    private JButton stopButton;
    private JPanel contentPanel;
    private StartListener startListener;
    private StopListener stopListener;

    public ZombieApocalypseWindow() {
        super("Зомби апокалипсис");
        setContentPane(contentPanel);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        pack();
        setVisible(true);

        startButton.addActionListener(e -> {
            if (startListener != null) {
                startListener.start();
            }
        });

        stopButton.addActionListener(e -> {
            if (stopListener != null) {
                stopListener.stop();
            }
        });
    }

    public void setStartListener(StartListener startListener) {
        this.startListener = startListener;
    }

    public void setStopListener(StopListener stopListener) {
        this.stopListener = stopListener;
    }

    private void createUIComponents() {
        weaponsCountTextField = new JFormattedTextField(2);
        characterNumberTextField = new JFormattedTextField(5);
        mapSizeTextField = new JFormattedTextField(4);
    }

    public SimulationProperties getSimulationProperties() {
        return new SimulationProperties((Integer) characterNumberTextField.getValue(),
                (Integer) weaponsCountTextField.getValue(),
                (Integer) mapSizeTextField.getValue());
    }

    public interface StartListener {
        void start();
    }

    public interface StopListener {
        void stop();
    }

}
