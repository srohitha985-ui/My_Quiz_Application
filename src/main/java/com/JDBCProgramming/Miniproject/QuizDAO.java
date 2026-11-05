package com.JDBCProgramming.Miniproject;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class QuizDAO {

    // Method 1: Retrieve Questions from Database
    public ArrayList<Question> getQuestions() {

        ArrayList<Question> list = new ArrayList<>();

        String sql = "SELECT * FROM quiz_questions ORDER BY question_id";

        try (
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement pstmt = con.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery()
        ) {

            while (rs.next()) {

                Question q = new Question();

                q.setQuestionId(rs.getInt("question_id"));
                q.setQuestion(rs.getString("question"));
                q.setOption1(rs.getString("option1"));
                q.setOption2(rs.getString("option2"));
                q.setOption3(rs.getString("option3"));
                q.setOption4(rs.getString("option4"));
                q.setCorrectAnswer(rs.getInt("correct_answer"));
                q.setReward(rs.getInt("reward"));

                list.add(q);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }


    // Method 2: Save Quiz Result into Database
    public void saveResult(
            String playerName,
            int totalQuestions,
            int correctAnswers,
            int totalReward,
            String status) {

        String sql = "INSERT INTO quiz_result "
                + "(player_name, total_questions, correct_answers, "
                + "total_reward, result_status) "
                + "VALUES (?, ?, ?, ?, ?)";

        try (
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement pstmt = con.prepareStatement(sql)
        ) {

            pstmt.setString(1, playerName);
            pstmt.setInt(2, totalQuestions);
            pstmt.setInt(3, correctAnswers);
            pstmt.setInt(4, totalReward);
            pstmt.setString(5, status);

            int rows = pstmt.executeUpdate();
            
            if (rows > 0) {
                System.out.println("Quiz Result Saved Successfully");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}