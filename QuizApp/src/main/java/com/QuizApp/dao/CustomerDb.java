package com.QuizApp.dao;

import java.util.List;

import javax.servlet.http.HttpSession;

import com.QuizApp.domain.Question;
import com.QuizApp.domain.ResultType1;
import com.QuizApp.domain.SubmitInfo;
import com.QuizApp.domain.UserInfo;
import com.QuizApp.domain.UserProfileRestType1;
import com.QuizApp.domain.UserQuiz;

public interface CustomerDb {
    public int insertInfo(UserInfo user);

    public UserInfo getUserInfo(String username, String password);

    public List<String> getQuizType();

    public UserQuiz generateQuiz(String username, String questType);

    public int submitQuiz(int quizId, int questId, int choiceId);

    public SubmitInfo getFinalResult(UserInfo user, int quizId, String startTime, String endTime);

    public int uploadFeedback(String text, int star);

    public List<ResultType1> findAllResultByUserInfo(int userId, int quizId);

    public String findQuizTypeById(int quizId, UserProfileRestType1 rs);

    public int findAnsweredQuestionById(int quizId, UserProfileRestType1 rs);

    public int getScore(int userId, int quizId);

    // generate 10 questions from question table and choice table randomly
    public List<Integer> randomlySelect10(String questType);

    public UserQuiz generateQuizUpdate(String username, String questType);

}
