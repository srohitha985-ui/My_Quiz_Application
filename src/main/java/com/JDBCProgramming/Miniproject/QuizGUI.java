package com.JDBCProgramming.Miniproject;




import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

public class QuizGUI extends JFrame {

    private ArrayList<Question> questions;

    private QuizDAO quizDAO = new QuizDAO();

    private String playerName;

    private int currentQuestion = 0;
    private int correctAnswers = 0;
    private int totalReward = 0;

    private boolean lifelineUsed = false;

    private JLabel questionLabel;
    private JLabel rewardLabel;

    private JRadioButton option1;
    private JRadioButton option2;
    private JRadioButton option3;
    private JRadioButton option4;

    private ButtonGroup group;

    private JButton nextButton;
    private JButton lifelineButton;

    public QuizGUI(String playerName) {

        this.playerName = playerName;

        questions = quizDAO.getQuestions();

        if (questions == null || questions.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "No questions found in the database."
            );

            dispose();
            return;
        }

        setTitle("Java Quiz Application");
        setSize(650, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        questionLabel = new JLabel();

        rewardLabel = new JLabel();

        option1 = new JRadioButton();
        option2 = new JRadioButton();
        option3 = new JRadioButton();
        option4 = new JRadioButton();

        group = new ButtonGroup();

        group.add(option1);
        group.add(option2);
        group.add(option3);
        group.add(option4);

        JPanel questionPanel = new JPanel(
                new GridLayout(2, 1)
        );

        questionPanel.add(questionLabel);
        questionPanel.add(rewardLabel);

        JPanel optionsPanel = new JPanel(
                new GridLayout(4, 1)
        );

        optionsPanel.add(option1);
        optionsPanel.add(option2);
        optionsPanel.add(option3);
        optionsPanel.add(option4);

        nextButton = new JButton("Next");

        lifelineButton = new JButton("Lifeline");

        JPanel buttonPanel = new JPanel();

        buttonPanel.add(lifelineButton);
        buttonPanel.add(nextButton);

        add(questionPanel, BorderLayout.NORTH);
        add(optionsPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        nextButton.addActionListener(e -> checkAnswer());

        lifelineButton.addActionListener(e -> useLifeline());

        loadQuestion();

        setVisible(true);
    }

    // Display Question
    private void loadQuestion() {

        if (currentQuestion >= questions.size()) {
            finishQuiz("COMPLETED");
            return;
        }

        Question q = questions.get(currentQuestion);

        questionLabel.setText(
                "Question " + (currentQuestion + 1)
                + ": " + q.getQuestion()
        );

        rewardLabel.setText(
                "Reward: Rs. " + q.getReward()
        );

        option1.setText("A. " + q.getOption1());
        option2.setText("B. " + q.getOption2());
        option3.setText("C. " + q.getOption3());
        option4.setText("D. " + q.getOption4());

        option1.setVisible(true);
        option2.setVisible(true);
        option3.setVisible(true);
        option4.setVisible(true);

        group.clearSelection();

        lifelineButton.setEnabled(
                !lifelineUsed && currentQuestion < 7
        );
    }

    // Check Answer
    private void checkAnswer() {

        int selectedAnswer = 0;

        if (option1.isSelected()) {
            selectedAnswer = 1;
        } else if (option2.isSelected()) {
            selectedAnswer = 2;
        } else if (option3.isSelected()) {
            selectedAnswer = 3;
        } else if (option4.isSelected()) {
            selectedAnswer = 4;
        }

        if (selectedAnswer == 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select an answer."
            );

            return;
        }

        Question q = questions.get(currentQuestion);

        if (selectedAnswer == q.getCorrectAnswer()) {

            correctAnswers++;

            totalReward = q.getReward();

            JOptionPane.showMessageDialog(
                    this,
                    "Correct Answer!\nReward: Rs. " + totalReward
            );

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Wrong Answer!"
            );

            if (currentQuestion >= 4) {

                finishQuiz("GAME OVER");
                return;

            } else {

                totalReward = 0;
            }
        }

        currentQuestion++;

        if (currentQuestion == questions.size()) {
            finishQuiz("COMPLETED");
        } else {
            loadQuestion();
        }
    }

    // Lifeline
    private void useLifeline() {

        String[] choices = {
                "Audience Poll",
                "50-50"
        };

        int choice = JOptionPane.showOptionDialog(
                this,
                "Select a Lifeline",
                "Lifeline",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                choices,
                choices[0]
        );

        if (choice == 0) {

            audiencePoll();

        } else if (choice == 1) {

            fiftyFifty();

        } else {

            return;
        }

        lifelineUsed = true;

        lifelineButton.setEnabled(false);
    }

    // Audience Poll
    private void audiencePoll() {

        Question q = questions.get(currentQuestion);

        int correct = q.getCorrectAnswer();

        Random random = new Random();

        int correctPercentage = 60 + random.nextInt(21);

        int remaining = 100 - correctPercentage;

        int wrong1 = remaining / 3;
        int wrong2 = remaining / 3;
        int wrong3 = remaining - wrong1 - wrong2;

        int[] percentages = new int[4];

        int index = 0;

        for (int i = 1; i <= 4; i++) {

            if (i == correct) {

                percentages[i - 1] = correctPercentage;

            } else {

                if (index == 0) {
                    percentages[i - 1] = wrong1;
                } else if (index == 1) {
                    percentages[i - 1] = wrong2;
                } else {
                    percentages[i - 1] = wrong3;
                }

                index++;
            }
        }

        String message =
                "Audience Poll Results\n"
                + "A: " + percentages[0] + "%\n"
                + "B: " + percentages[1] + "%\n"
                + "C: " + percentages[2] + "%\n"
                + "D: " + percentages[3] + "%";

        JOptionPane.showMessageDialog(this, message);
    }

    // 50-50 Lifeline
    private void fiftyFifty() {

        Question q = questions.get(currentQuestion);

        int correct = q.getCorrectAnswer();

        JRadioButton[] options = {
                option1, option2, option3, option4
        };

        int removed = 0;

        for (int i = 0; i < 4; i++) {

            if (i + 1 != correct && removed < 2) {

                options[i].setVisible(false);

                removed++;
            }
        }

        JOptionPane.showMessageDialog(
                this,
                "Two wrong options have been removed!"
        );
    }

    // Save Result
    private void finishQuiz(String status) {

        quizDAO.saveResult(
                playerName,
                questions.size(),
                correctAnswers,
                totalReward,
                status
        );

        JOptionPane.showMessageDialog(
                this,
                "Quiz Finished!\n\n"
                + "Player Name: " + playerName + "\n"
                + "Total Questions: " + questions.size() + "\n"
                + "Correct Answers: " + correctAnswers + "\n"
                + "Final Reward: Rs. " + totalReward + "\n"
                + "Status: " + status
        );

        dispose();
    }
}