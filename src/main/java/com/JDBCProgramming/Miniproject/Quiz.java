package com.JDBCProgramming.Miniproject;


import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class Quiz {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            String playerName = JOptionPane.showInputDialog(
                    null,
                    "Enter Player Name:"
            );

            if (playerName == null || playerName.trim().isEmpty()) {

                JOptionPane.showMessageDialog(
                        null,
                        "Player name is required."
                );

                return;
            }

            new QuizGUI(playerName);
        });
    }
}

	