package com.QuizApp.domain;

public class ResultType {

    private int quizId;
    private int userId;
    private int choiceId;
    private String questDescribe;
    private String choiceDescribe;
    private String correctChoice;
    private int userChoice;
    private String firstname;
    private String lastname;
    private String username;
    private String type;
    private int score;

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getChoiceId() {
        return choiceId;
    }

    public void setChoiceId(int choiceId) {
        this.choiceId = choiceId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getQuizId() {
        return quizId;
    }

    public void setQuizId(int quizId) {
        this.quizId = quizId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getQuestDescribe() {
        return questDescribe;
    }

    public void setQuestDescribe(String questDescribe) {
        this.questDescribe = questDescribe;
    }

    public String getChoiceDescribe() {
        return choiceDescribe;
    }

    public void setChoiceDescribe(String choiceDescribe) {
        this.choiceDescribe = choiceDescribe;
    }

    public String getCorrectChoice() {
        return correctChoice;
    }

    public void setCorrectChoice(String correctChoice) {
        this.correctChoice = correctChoice;
    }

    public int getUserChoice() {
        return userChoice;
    }

    public void setUserChoice(int userChoice) {
        this.userChoice = userChoice;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


}
