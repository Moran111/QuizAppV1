package com.QuizApp.domain;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "user")
public class Question implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "questId")
    private int questionId;

    @Column(name = "desribe")
    private String qDescribe;

    @Column(name = "type")
    private String type;

    @OneToMany(mappedBy = "question")
    List<Choice> choices = new ArrayList<>();

    @Column(name = "status")
    private String disable;


    public List<Choice> getChoices() {
        return choices;
    }

    public void addChoices(List<Choice> c) {
        System.out.println("how many elements in the choice before add current c ");
        System.out.println(choices.size());
        this.choices = c;
        System.out.println("how many elements in the choice after add current c ");
        System.out.print(choices.size());
        System.out.println();
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getqDescribe() {
        return qDescribe;
    }

    public void setqDescribe(String qDescribe) {
        this.qDescribe = qDescribe;
    }

    public String toString() {
        return qDescribe;
    }

}
