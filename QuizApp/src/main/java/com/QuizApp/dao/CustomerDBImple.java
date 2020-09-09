package com.QuizApp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.http.HttpSession;

import java.util.*;

import com.QuizApp.domain.Choice;
import com.QuizApp.domain.Question;
import com.QuizApp.domain.ResultType1;
import com.QuizApp.domain.SubmitInfo;
import com.QuizApp.domain.UserInfo;
import com.QuizApp.domain.UserProfileRestType1;
import com.QuizApp.domain.UserQuiz;

public class CustomerDBImple implements CustomerDb {

    static Connection connection;
    static PreparedStatement preparedStatement;
    static Statement statement;
    private final static String VALIDATE_INFO = "select * from oz.user where user.username = ? and user.password = ?";
    private final static String INSERT_INFO = "insert into oz.user values (default,?,?,?,?,?,?)";
    private final static String QUIZ_TYPE_INFO = "select Distinct(type) from oz.question;";


    @Override
    public int insertInfo(UserInfo user) {

        System.out.println(user.getUserName());

        int status = 0;
        try {
            connection = DBConnection.getConnection();
            preparedStatement = connection.prepareStatement(INSERT_INFO);
            preparedStatement.setString(1, user.getUserName());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getFirstName());
            preparedStatement.setString(4, user.getLastName());
            preparedStatement.setString(5, "N");
            preparedStatement.setString(6, "ACTIVE");

            status = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return status;
    }

    @Override
    public UserInfo getUserInfo(String username, String password) {

        UserInfo user = new UserInfo();

        try {
            connection = DBConnection.getConnection();
            preparedStatement = connection.prepareStatement(VALIDATE_INFO);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            ResultSet rs = preparedStatement.executeQuery();

            //System.out.println("in result set");
            while (rs.next()) {
                user.setId(rs.getInt(1));
                user.setUserName(rs.getString(2));
                //System.out.println(rs.getString(1));
                user.setPassword(rs.getString(3));
                //System.out.println(rs.getString(2));
                user.setFirstName(rs.getString(4));
                user.setLastName(rs.getString(5));
                user.setAdmin(rs.getString(6));
                user.setStatus(rs.getString(7));
                //System.out.println(rs.getString(3));
            }

//			System.out.println("in DB part");
//			System.out.println(user.getUserName());
//			System.out.println(user.getPassword());
//			System.out.println(user.getName());

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return user;
    }

    @Override
    public List<String> getQuizType() {
        List<String> res = new ArrayList<>();
        try {
            connection = DBConnection.getConnection();
            statement = connection.createStatement();

            ResultSet rs = statement.executeQuery(QUIZ_TYPE_INFO);

            //System.out.println("in result set");
            while (rs.next()) {
                res.add(rs.getString(1));
                //System.out.println(rs.getString(3));
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // TODO Auto-generated method stub
        return res;
    }

    @Override
    public UserQuiz generateQuiz(String username, String questType) {
        // TODO Auto-generated method stub
        UserQuiz quiz = new UserQuiz();
        quiz.setUsername(username);
        quiz.setQuizName("quiz" + username); // quiz name is quiz  + session ids

        String selectQuestion = "select oz.question.desribe, oz.choice.desribe, oz.choice.correct, oz.question.questId, oz.choice.id from oz.question inner join oz.choice on question.questId = choice.questId where question.status != 'disable' and question.type = ?";
        // create a quiz, need to add to quiz table in db 

        try {
            connection = DBConnection.getConnection();
            preparedStatement = connection.prepareStatement(selectQuestion);
            preparedStatement.setString(1, questType);

            ResultSet rs = preparedStatement.executeQuery();
            List<Question> questions = new ArrayList<>();
            Question question;
            Set<String> set = new HashSet<>(); // question set
            List<Choice> list = new ArrayList<>();

            //rs have information about question1 choice 1, question1 choice 2, question1 choice 3 
            // want to split question and put into Question class, choice into Choice class 
            // use set to check if this is a new question, if it is a new question, create a new list of choice
            // else, add to existing list of choice
            while (rs.next()) {
                question = new Question();
                String qdescribe = rs.getString(1);

                if (set.contains(qdescribe)) {
                    Choice c = new Choice();
                    c.setChoiceDesribe(rs.getString(2));
                    c.setIsCorrect(rs.getString(3));
                    c.setId(rs.getInt(5));
                    list.add(c);
                    // System.out.println(list);
                    // System.out.println(question.getChoices());
                } else {
                    if (list.size() > 0) {
                        System.out.print(list);
                        System.out.println();
                        question.addChoices(list);
                    }
                    list = new ArrayList<>();
                    question.setQuestionId(rs.getInt(4));
                    questions.add(question);
                    question.setqDescribe(qdescribe);
                    Choice c = new Choice();
                    c.setChoiceDesribe(rs.getString(2));
                    c.setIsCorrect(rs.getString(3));
                    c.setId(rs.getInt(5));
                    list.add(c);
                    // System.out.println(list);
                    set.add(qdescribe);
                }
                question.addChoices(list);
            }

            quiz.setQuestions(questions);


            // add quiz to db 
            String insertQuiz = "insert into oz.quiz values (default, ?);";

            preparedStatement = connection.prepareStatement(insertQuiz);
            preparedStatement.setString(1, quiz.getQuizName());

            int updateRes = preparedStatement.executeUpdate();
            System.out.println("lines of quiz inserted " + updateRes);

            String getQuizId = "select oz.quiz.quizId from oz.quiz;";

            connection = DBConnection.getConnection();
            statement = connection.createStatement();

            ResultSet qid = statement.executeQuery(getQuizId);

            //System.out.println("in result set");
            while (qid.next()) {
                quiz.setQuizId(qid.getInt(1));
                //System.out.println(rs.getString(3));
            }

            // quiz - questions (quest desribe) - choice (c)
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return quiz;
    }

    @Override
    public int submitQuiz(int quizId, int questId, int choiceId) {
        System.out.println("------------db part ---------------");

        int insertRows = 0;
        SubmitInfo info = new SubmitInfo();
        // after the user submit the quiz, quiz info should be in db quiz, quiz quest, and quiz submission
        try {
            connection = DBConnection.getConnection();
            // insert choice id, quest id, id auto, quiz id to quiz_quest table 
            String userAnswer = "insert into oz.quiz_question values (default,?,?,?)";
            connection = DBConnection.getConnection();
            preparedStatement = connection.prepareStatement(userAnswer);
            preparedStatement.setInt(1, questId);
            preparedStatement.setInt(2, quizId);
            preparedStatement.setInt(3, choiceId);

            insertRows = preparedStatement.executeUpdate();

            if (insertRows >= 1) {
                // correct insert into rows
            } else {
                // should handle error exception in here
            }

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return insertRows;
    }

    @Override
    public SubmitInfo getFinalResult(UserInfo info, int quizId, String startTime, String endTime) {
        // TODO Auto-generated method stub
        SubmitInfo submitInfo = new SubmitInfo();
        String createView = "select c.correct from oz.quiz_question qq inner join oz.question q on qq.questId = q.questId inner join oz.choice c on c.questId = q.questId where qq.userChoice = c.id and qq.quizId = ?;";
        connection = DBConnection.getConnection();
        try {
            int countCorrect = 0;
            preparedStatement = connection.prepareStatement(createView);
            //System.out.println("in db, the quiz id is ");
            //System.out.println(info.getQuizId());
            preparedStatement.setInt(1, quizId);
            ResultSet countRs = preparedStatement.executeQuery();

            while (countRs.next()) {
                // info.setQuizId(countRS.getInt(1));
                if (countRs.getString(1).equals("Y")) {
                    countCorrect++;
                }
            }
            submitInfo.setResult(countCorrect);

            String insertToQuizSubmission = "insert into oz.quiz_submission values (default, ?, ?, ?, ?, ?, ?);";
            preparedStatement = connection.prepareStatement(insertToQuizSubmission);
            preparedStatement.setInt(1, info.getId());
            preparedStatement.setString(2, info.getUserName());
            preparedStatement.setInt(3, quizId);
            preparedStatement.setString(4, startTime);
            preparedStatement.setString(5, endTime);
            preparedStatement.setString(6, String.valueOf(submitInfo.getResult()));

            int updateRows = preparedStatement.executeUpdate();

            if (updateRows < 1) {
                // should throw some errors
            } else {
                System.out.println(updateRows + "are inserted into the db");
            }

            String getFirstLastName = "select * from oz.user where username = ?;";
            preparedStatement = connection.prepareStatement(getFirstLastName);
            preparedStatement.setString(1, info.getUserName());
            ResultSet flRS = preparedStatement.executeQuery();

            while (flRS.next()) {
                info.setId(flRS.getInt(1));
                // info.getUserInfo().setFirstName(flRS.getString(4));
                // info.getUserInfo().setLastName(flRS.getString(5));
            }

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return submitInfo;
    }

    @Override
    public int uploadFeedback(String text, int star) {
        // TODO Auto-generated method stub
        int row = 0;
        connection = DBConnection.getConnection();
        String upFeedback = "insert into oz.feedback values (default, ?, ?);";
        try {
            preparedStatement = connection.prepareStatement(upFeedback);
            if (text == null) {
                preparedStatement.setString(1, "");
            } else {
                preparedStatement.setString(1, text);
            }
            preparedStatement.setInt(2, star);

            row = preparedStatement.executeUpdate();


        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return row;
    }

    @Override
    public List<ResultType1> findAllResultByUserInfo(int userId, int quizId) {
        // TODO Auto-generated method stub
        List<ResultType1> result = new ArrayList<>();
        String query = "select qq.quizId, qs.userId, qs.username, qq.userChoice, q.desribe, q.type, c.desribe, c.correct, c.id from oz.quiz_submission qs inner join oz.quiz_question qq on qs.quizId = qq.quizId inner join oz.question q on q.questId = qq.questId right join oz.choice c on q.questId = c.questId where qs.userId = ? and qq.quizId = ?;";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, quizId);

            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                ResultType1 tmp = new ResultType1();
                tmp.setQuizId(rs.getInt(1));
                tmp.setUserId(rs.getInt(2));
                tmp.setUsername(rs.getString(3));
                tmp.setUserChoice(rs.getInt(4));
                tmp.setQuestDescribe(rs.getString(5));
                tmp.setType(rs.getString(6));
                tmp.setChoiceDescribe(rs.getString(7));
                tmp.setCorrectChoice(rs.getString(8));
                tmp.setChoiceId(rs.getInt(9));
                result.add(tmp);
            }

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public String findQuizTypeById(int quizId, UserProfileRestType1 rs) {
        String res = "";
        String query = "select q.type from oz.question q inner join oz.quiz_question qq on qq.questId = q.questId where qq.quizId = ?;";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, quizId);

            ResultSet rss = preparedStatement.executeQuery();
            while (rss.next()) {
                res = rss.getString(1);
            }

            rs.setCategroy(res);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return res;
    }

    @Override
    public int findAnsweredQuestionById(int quizId, UserProfileRestType1 rs) {
        int res = 0;
        String query = "select count(q.questId) from oz.question q inner join oz.quiz_question qq on qq.questId = q.questId where qq.quizId = ?;";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, quizId);

            ResultSet rss = preparedStatement.executeQuery();
            while (rss.next()) {
                res = rss.getInt(1);
            }

            rs.setNumOfQuest(res);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return res;
    }

    @Override
    public int getScore(int userId, int quizId) {
        // TODO Auto-generated method stub
        int res = 0;
        String query = "select result from oz.quiz_submission qs where qs.quizId = ? and qs.userId = ?;";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, quizId);
            preparedStatement.setInt(2, userId);

            ResultSet rss = preparedStatement.executeQuery();
            while (rss.next()) {
                res = rss.getInt(1);
            }

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return res;
    }

    @Override
    public List<Integer> randomlySelect10(String questType) {
        // TODO Auto-generated method stub
        List<Integer> questNumber = new ArrayList<>();
        String query = "select questId from question where type = ? order by Rand() limit 10;";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, questType);

            ResultSet rss = preparedStatement.executeQuery();
            while (rss.next()) {
                questNumber.add(rss.getInt(1));
            }

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return questNumber;
    }

    @Override
    public UserQuiz generateQuizUpdate(String username, String questType) {
        // TODO Auto-generated method stub
        UserQuiz quiz = new UserQuiz();
        quiz.setUsername(username);
        quiz.setQuizName("quiz" + username); // quiz name is quiz  + session ids

        String selectQuestion = "select oz.question.desribe, oz.choice.desribe, oz.choice.correct, oz.question.questId, oz.choice.id from oz.question inner join oz.choice on question.questId = choice.questId inner join (select questId from oz.question where type = ? order by Rand() limit 10) as v2 on v2.questId = oz.question.questId";
        // create a quiz, need to add to quiz table in db 

        try {
            connection = DBConnection.getConnection();
            preparedStatement = connection.prepareStatement(selectQuestion);
            preparedStatement.setString(1, questType);

            ResultSet rs = preparedStatement.executeQuery();
            List<Question> questions = new ArrayList<>();
            Question question;
            Set<String> set = new HashSet<>(); // question set
            List<Choice> list = new ArrayList<>();

            //rs have information about question1 choice 1, question1 choice 2, question1 choice 3 
            // want to split question and put into Question class, choice into Choice class 
            // use set to check if this is a new question, if it is a new question, create a new list of choice
            // else, add to existing list of choice
            while (rs.next()) {
                question = new Question();
                String qdescribe = rs.getString(1);

                if (set.contains(qdescribe)) {
                    Choice c = new Choice();
                    c.setChoiceDesribe(rs.getString(2));
                    c.setIsCorrect(rs.getString(3));
                    c.setId(rs.getInt(5));
                    list.add(c);
                    // System.out.println(list);
                    // System.out.println(question.getChoices());
                } else {
                    if (list.size() > 0) {
                        System.out.print(list);
                        System.out.println();
                        question.addChoices(list);
                    }
                    list = new ArrayList<>();
                    question.setQuestionId(rs.getInt(4));
                    questions.add(question);
                    question.setqDescribe(qdescribe);
                    Choice c = new Choice();
                    c.setChoiceDesribe(rs.getString(2));
                    c.setIsCorrect(rs.getString(3));
                    c.setId(rs.getInt(5));
                    list.add(c);
                    // System.out.println(list);
                    set.add(qdescribe);
                }
                question.addChoices(list);
            }

            quiz.setQuestions(questions);


            // add quiz to db 
            String insertQuiz = "insert into oz.quiz values (default, ?);";

            preparedStatement = connection.prepareStatement(insertQuiz);
            preparedStatement.setString(1, quiz.getQuizName());

            int updateRes = preparedStatement.executeUpdate();
            System.out.println("lines of quiz inserted " + updateRes);

            String getQuizId = "select oz.quiz.quizId from oz.quiz;";

            connection = DBConnection.getConnection();
            statement = connection.createStatement();

            ResultSet qid = statement.executeQuery(getQuizId);

            //System.out.println("in result set");
            while (qid.next()) {
                quiz.setQuizId(qid.getInt(1));
                //System.out.println(rs.getString(3));
            }

            // quiz - questions (quest desribe) - choice (c)
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return quiz;
    }
}
