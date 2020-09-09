package com.QuizApp.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.QuizApp.dao.CustomerDBImple;
import com.QuizApp.dao.CustomerDb;
import com.QuizApp.domain.Choice;
import com.QuizApp.domain.Question;
import com.QuizApp.domain.ResultType1;
import com.QuizApp.domain.SubmitInfo;
import com.QuizApp.domain.UserInfo;
import com.QuizApp.domain.UserQuiz;

/**
 * Servlet implementation class QuizServlet
 */
@WebServlet("/QuizServlet")
public class QuizServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // get the radio id, 
        // check if prev is clicked, or next is clicked, or submit is clicked 

        // when click submit, submit the choice id, quest id, id auto, quiz id to quiz_quest table 
        // return sumbit info, including username, quiz id, total score 

        // if the user doesn't submit the quiz ??? 
        PrintWriter out = response.getWriter();
        CustomerDb cd = new CustomerDBImple();


        Date endTime;

        String quizId = "";

        HttpSession oldSession = request.getSession(false);
        if (oldSession != null && oldSession.getAttribute("username") != null) {
            String username = (String) oldSession.getAttribute("username");
            System.out.println("the name is " + username);
            quizId = (String) oldSession.getAttribute("quizId");
            // System.out.println("the quiz id is " + quizId);

            long currTime = System.currentTimeMillis();
            endTime = new Date(currTime);
            oldSession.setAttribute("endTime", endTime.toString());

            // get user quiz and currQuestion from session
            UserQuiz quiz = (UserQuiz) request.getSession().getAttribute("quizObject");


            int currQuestion = (int) oldSession.getAttribute("currQuest");
            System.out.println("currQuestion is " + currQuestion);

            //info.setEndTime(currTime);
            //info.setStartTime(Long.parseLong((String) oldSession.getAttribute("startTime")));

            String radio = request.getParameter("answer");

            // set user choice to be true
            String[] options = {"1", "2", "3", "4"};

            // count how many questions are answered 
            System.out.println("the user already answered ? questions");
            int answeredQuest = (int) oldSession.getAttribute("answeredQuest");
            System.out.println(answeredQuest);
            if (radio == null) {
                // choose nothing
                if ((int) oldSession.getAttribute("answeredQuest") > 0) {
                    oldSession.setAttribute("answeredQuest", answeredQuest - 1);
                }
            } else {
                oldSession.setAttribute("answeredQuest", answeredQuest + 1);
//				for (String opt: options) {
//					if (radio.equals(opt)) {
////						int currQuestIndex = (int)oldSession.getAttribute("currQuest");
////						// list of user choice based on answered question, index is currQuestion
////						List<Integer> answers = (List<Integer>) oldSession.getAttribute("answers");
////						answers.add(currQuestIndex, Integer.parseInt(opt));
//						
//						List<Question> quest = (List<Question>) oldSession.getAttribute("question");
//						quest.get(currQuestion).getChoices().get(Integer.parseInt(opt)-1).setUserChoice(true);
//						System.out.println("current question is " + currQuestion);
//					}
//				}
            }

            String submitButtons = request.getParameter("submit");
            // prev button is clicked 
            if (submitButtons.equals("Prev")) {
                System.out.println("click prev successful! ");
                // get prev answer
                List<Integer> answerPrev = (List<Integer>) oldSession.getAttribute("answers");
                int prevChoice = answerPrev.get((int) oldSession.getAttribute("currQuest") - 1);
                int currQuestIndex = (int) oldSession.getAttribute("currQuest");
                oldSession.setAttribute("prevChoice", prevChoice);
                if ((int) oldSession.getAttribute("answeredQuest") > 0) {
                    // get 
                    oldSession.setAttribute("answeredQuest", answeredQuest - 1);
                }

                // if click prev, if you don't click any choice in this question, fine
                String radio1 = request.getParameter("answer");
                if (radio1 == null) {

                } else {
                    List<Question> quest = (List<Question>) oldSession.getAttribute("question");
                    if (answerPrev.size() > currQuestIndex) {
                        answerPrev.remove(answerPrev.size() - 1);
                        quest.get(currQuestion).getChoices().get(Integer.parseInt(radio1) - 1).setUserChoice(false);
                    }
                }

                System.out.println("after clicke prev button answers should be all user's choice");
                System.out.println(answerPrev);

                oldSession.setAttribute("currQuest", currQuestion - 1);

            } else if (submitButtons.equals("Next")) {
                System.out.println("click next successful! ");

                // check current radio, if not null, add to curr question
                String radio1 = request.getParameter("answer");
                List<Integer> answers = (List<Integer>) oldSession.getAttribute("answers");
                if (radio1 != null) {
                    List<Question> quest = (List<Question>) oldSession.getAttribute("question");
                    int currQuestIndex = (int) oldSession.getAttribute("currQuest");
                    System.out.println("the current index is" + currQuestIndex);
                    if (answers.size() > currQuestIndex) {
                        quest.get(currQuestion).getChoices().get(answers.get(currQuestIndex) - 1).setUserChoice(false);
                        answers.set(currQuestIndex, Integer.parseInt(radio1));
                        quest.get(currQuestion).getChoices().get(Integer.parseInt(radio1) - 1).setUserChoice(true);
                    } else {
                        answers.add(Integer.parseInt(radio1));
                        quest.get(currQuestion).getChoices().get(Integer.parseInt(radio1) - 1).setUserChoice(true);
                    }

                    System.out.println("answers should be all user's choice");
                    System.out.println(answers);
                    oldSession.setAttribute("prevChoice", Integer.parseInt(radio1));
                } else {
                    answers.add(-1);
                }


                oldSession.setAttribute("prevChoice", 0);
                // change prev choice 
                oldSession.setAttribute("currQuest", currQuestion + 1);

            } else if (submitButtons.equals("SubmitAll")) {
                String radio1 = request.getParameter("answer");
                if (radio1 != null) {
                    List<Question> quest = (List<Question>) oldSession.getAttribute("question");
                    quest.get(currQuestion).getChoices().get(Integer.parseInt(radio1) - 1).setUserChoice(true);
                }
                oldSession.setAttribute("finish", "true");
                // insert quiz id, question id, user choice id in quiz_question table
                List<Question> questions = quiz.getQuestions();
                for (Question q : questions) {
                    List<Choice> choices = q.getChoices();
                    for (Choice c : choices) {
                        if (c.getUserChoice()) {
                            System.out.println("------------------ useful information " + quiz.getQuizId() + " " + q.getQuestionId() + " " + c.getId());
                            cd.submitQuiz(quiz.getQuizId(), q.getQuestionId(), c.getId());
                        }
                    }
                }


                UserInfo userInformation = (UserInfo) oldSession.getAttribute("userInfo");


                String startTime = request.getSession().getAttribute("startTime").toString();

                // get quiz correct answer
                List<Question> questIds = (List<Question>) oldSession.getAttribute("question");
                List<String> corrAns = new ArrayList<>();
                List<String> userAns = new ArrayList<>();
                for (Question q : questIds) {
                    for (Choice c : q.getChoices()) {
                        if (c.getIsCorrect().equals("Y")) {
                            corrAns.add(c.getChoiceDesribe());
                        }

                        if (c.getUserChoice()) {
                            userAns.add(c.getChoiceDesribe());
                        }
                    }
                }

                SubmitInfo info = cd.getFinalResult(userInformation, Integer.parseInt(quizId), startTime, endTime.toString());
                List<ResultType1> resultList = cd.findAllResultByUserInfo(userInformation.getId(), Integer.parseInt(quizId));

                // request.setAttribute("firstname", );
//				request.setAttribute("username", info.getUsername());
//				request.setAttribute("quizname", quiz.getQuizName());
//				request.setAttribute("firstname", userInformation.getFirstName()); //sql have syntax error
//				request.setAttribute("lastname", userInformation.getLastName()); //sql have syntax error
//				request.setAttribute("starttime", startTime);
//				request.setAttribute("endtime", endTime);
//				request.setAttribute("UserAnswer", userAns);
//				request.setAttribute("answer", corrAns);
//				request.setAttribute("result", info.getResult());

                String firstname = userInformation.getFirstName();
                String lastname = userInformation.getLastName();

                // result type: 
                // question description, List<choice description>>
                Map<String, List<String>> qtoc = new HashMap<>();
                Map<String, String> ctou = new HashMap<>(); // question - user choice describe
                Map<String, String> ctoc = new HashMap<>(); // questioon - correct choice

                for (ResultType1 r : resultList) {
                    qtoc.putIfAbsent(r.getQuestDescribe(), new ArrayList<>());
                    qtoc.get(r.getQuestDescribe()).add(r.getChoiceDescribe());
                }

                for (ResultType1 r : resultList) {
                    if (r.getUserChoice() == r.getChoiceId()) {
                        ctou.put(r.getQuestDescribe(), r.getChoiceDescribe());
                    }
                }

                for (ResultType1 r : resultList) {
                    if (r.getCorrectChoice().equals("Y")) {
                        ctoc.put(r.getQuestDescribe(), r.getChoiceDescribe());
                    }
                }

                request.setAttribute("questAllChoice", qtoc);
                request.setAttribute("questUserChoice", ctou);
                request.setAttribute("questCorrectChoice", ctoc);

                // get the correct answer from db given user id and quiz id
                int score = cd.getScore(userInformation.getId(), Integer.parseInt(quizId));
                request.setAttribute("totalScore", score);

                request.setAttribute("pass", qtoc.size() * 0.6);

                System.out.println("this is the final result");
                System.out.print(qtoc);
                System.out.println();
                System.out.print(ctou);
                System.out.println();
                System.out.print(ctoc);

                request.getRequestDispatcher("/pages/UserSubmitDetailPage.jsp").forward(request, response);

                // request.getRequestDispatcher("/pages/UserSubmitDetailPage.jsp").forward(request, response);
            }


            if (oldSession.getAttribute("finish").equals("false")) {
                request.getRequestDispatcher("/pages/QuizDisplay.jsp").forward(request, response);
            }

        } else {
            // session is invalid
            // must login
            request.getRequestDispatcher("/").forward(request, response);
        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

}
