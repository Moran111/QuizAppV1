package com.QuizApp.dao;

import java.util.List;

import com.QuizApp.domain.Question;
import com.QuizApp.domain.SubmitInfo;
import com.QuizApp.domain.UserInfo;

public interface HibCustDB {

    public SubmitInfo quizResultEachUser(int userId);

    public List<UserInfo> getAllUser();

    public void updateUserStates(UserInfo userid, String states);

    public List<SubmitInfo> getAllUserQuiz();

    public List<Question> listAllQuestion();
}
