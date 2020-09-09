package com.QuizApp.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

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
import com.QuizApp.domain.SubmitInfo;
import com.QuizApp.domain.UserInfo;
import com.QuizApp.domain.UserQuiz;

/**
 * Servlet implementation class GenerateQuizServlet
 */
@WebServlet("/GenerateQuizServlet")
public class GenerateQuizServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // get quiz type from home.jsp file
        // String quizType = request.getParameter(name)

        System.out.println("start to generate quiz questions");

        String username = (String) request.getSession().getAttribute("username");

        System.out.println(username);

        String[] checkedIds = request.getParameterValues("checkboxlist1");

        for (String checkId : checkedIds) {
            System.out.println("check id is " + checkId);
        }

        PrintWriter out = response.getWriter();

        CustomerDb cd = new CustomerDBImple();

        // if session is valid 
        HttpSession session = request.getSession(false);
        // sessioon will contians username, quiz id, curr question, start from 1, will end to 10 
        if (session != null && session.getAttribute("username") != null) {
            /*
             * right know assume only check one check box
             */
            UserInfo user = (UserInfo) session.getAttribute("userInfo");

            // change it to generate 10 quiz questions
            //UserQuiz quiz = cd.generateQuiz(username, checkedIds[0]);	
            UserQuiz quiz = cd.generateQuizUpdate(username, checkedIds[0]);


            // Map<String, List<String>> displayQuestion = new HashMap<>();

            long sessionStart = session.getCreationTime();
            Date startTime = new Date(sessionStart);
            quiz.setStartTime(sessionStart);
            session.setAttribute("startTime", startTime);
            session.setAttribute("finish", "false"); //if session expired, or click submit, it is finished

            // list of user choice based on answered question, index is currQuestion
            List<Integer> answers = new ArrayList<>();


            session.setAttribute("answers", answers);

//			Set<String> set = new HashSet<>();
//			Random rand = new Random();
//			// randomly select 10 questions
//			List<Question> allQuestions = quiz.getQuestions(); // db part looks create, can get all info from db 
//			System.out.println("all questins length " + allQuestions.size());
//			
//			List<Question> quizQuest = new ArrayList<>();
//			Set<Integer> duplicateNumber = new HashSet<>();
//			// all number has added (some type less than 10
//			while (quizQuest.size() < 10) {
//				int idx = rand.nextInt(allQuestions.size());
//				if (!duplicateNumber.contains(idx)) {
//					quizQuest.add(allQuestions.get(idx));
//					duplicateNumber.add(idx);
//				} 
//				if (duplicateNumber.size() == allQuestions.size()) {
//					break;
//				}
//			}

            List<Question> quizQuest = quiz.getQuestions();

            int currQuest = 0;
            int answeredQuest = 0;
            session.setAttribute("question", quizQuest);
            session.setAttribute("currQuest", currQuest);
            session.setAttribute("numOfQuest", quizQuest.size() - 1);
            session.setAttribute("answeredQuest", answeredQuest);
            System.out.println("generate " + quizQuest.size() + "questions");

            // get a list of information, quiz id, question id, choice id store in a list<int[]>, use to search in db
            // store in session - infoIndex


            System.out.println("how many quiz in question " + quizQuest.size());
            // System.out.println(quizQuest.toString());
            for (Question q : quizQuest) {
                System.out.println("question id is  " + q.getQuestionId());
                //System.out.println(q.getChoices());
                // out.print(q.toString() + "\n");
                for (Choice c : q.getChoices()) {
                    // out.print(c.toString() + "\n");
                    System.out.println("choice id is " + c.getId());
                }
            }

            // also store the choice into session

            request.getSession().setAttribute("quizObject", quiz);
            request.getSession().setAttribute("quizId", String.valueOf(quiz.getQuizId()));
            request.setAttribute("question", quiz.getQuestions());
            request.getRequestDispatcher("/pages/QuizDisplay.jsp").forward(request, response);

        } else {
            // if session is not valid, redirect to login page
            // do somthing else
            request.getRequestDispatcher("/").forward(request, response);
        }

        // redirect to a new jsp file to display the quiz and start the quiz 
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
