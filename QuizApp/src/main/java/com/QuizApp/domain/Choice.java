package com.QuizApp.domain;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


public class Choice {


    String choiceDesribe;

    String isCorrect;

    int id;

    boolean userChoice;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getChoiceDesribe() {
        return choiceDesribe;
    }

    public void setChoiceDesribe(String choiceDesribe) {
        this.choiceDesribe = choiceDesribe;
    }

    public boolean getUserChoice() {
        return userChoice;
    }

    public void setUserChoice(boolean userChoice) {
        this.userChoice = userChoice;
    }

    public String getIsCorrect() {
        return isCorrect;
    }

    public void setIsCorrect(String isCorrect) {
        this.isCorrect = isCorrect;
    }

    public String toString() {
        return choiceDesribe;
    }
}
