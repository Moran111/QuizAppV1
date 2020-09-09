package com.QuizApp.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.*;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.QuizApp.dao.HibCustDB;
import com.QuizApp.dao.CustomerDBImple;
import com.QuizApp.dao.CustomerDb;
import com.QuizApp.dao.HibernateDBImple;
import com.QuizApp.domain.Choice;
import com.QuizApp.domain.Question;
import com.QuizApp.domain.ResultType;
import com.QuizApp.domain.SubmitInfo;
import com.QuizApp.domain.UserInfo;
import com.QuizApp.domain.UserQuiz;

/**
 * Servlet implementation class SubmissionDetailServlet
 */
@WebServlet("/SubmissionDetailServlet")
public class SubmissionDetailServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public SubmissionDetailServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub

        System.out.println("go to submit servlet ");

        CustomerDb db = new CustomerDBImple();
        HttpSession oldSession = request.getSession(false);

        if (oldSession != null && oldSession.getAttribute("userInfo") != null && ((UserInfo) oldSession.getAttribute("userInfo")).getAdmin().equals("Y")) {
            List<SubmitInfo> submis = (List<SubmitInfo>) oldSession.getAttribute("allQuizInfo");

            for (SubmitInfo info : submis) {
                System.out.println(info.getUsername());
                System.out.println(info.getUserInfo().getFirstName());
                System.out.println(info.getUserInfo().getLastName());
                System.out.println(info.getResult());
                System.out.println(info.getQuizId());
            }

            //get button
            String buttVal = request.getParameter("button");
            if (buttVal != null) {
                int index = Integer.parseInt(buttVal) - 1;
                System.out.println("counter " + index);
                SubmitInfo sinfo = submis.get(index); // submit info - start time, end time, quiz id, result
                UserInfo uinfo = sinfo.getUserInfo(); // user full name

                System.out.println("who take the quiz " + uinfo.getId());
                System.out.println("the quiz id is  " + sinfo.getId());

                List<ResultType> resultList = db.findAllResultByUserInfo(uinfo.getId(), sinfo.getQuizId());

                String firstname = uinfo.getFirstName();
                String lastname = uinfo.getLastName();

                // result type: 
                // question description, List<choice description>>
                Map<String, Set<String>> qtoc = new HashMap<>();
                Map<String, String> ctou = new HashMap<>(); // question - user choice describe
                Map<String, String> ctoc = new HashMap<>(); // questioon - correct choice

                for (ResultType r : resultList) {
                    qtoc.putIfAbsent(r.getQuestDescribe(), new HashSet<>());
                    qtoc.get(r.getQuestDescribe()).add(r.getChoiceDescribe());
                }

                for (ResultType r : resultList) {
                    if (r.getUserChoice() == r.getChoiceId()) {
                        ctou.put(r.getQuestDescribe(), r.getChoiceDescribe());
                    }
                }

                for (ResultType r : resultList) {
                    if (r.getCorrectChoice().equals("Y")) {
                        ctoc.put(r.getQuestDescribe(), r.getChoiceDescribe());
                    }
                }

                request.setAttribute("questAllChoice", qtoc);
                request.setAttribute("questUserChoice", ctou);
                request.setAttribute("questCorrectChoice", ctoc);

                request.setAttribute("totalScore", sinfo.getResult() / 10);
                request.setAttribute("pass", 6);

                System.out.println("this is the final result");
                System.out.print(qtoc);
                System.out.println();
                System.out.print(ctou);
                System.out.println();
                System.out.print(ctoc);

                request.getRequestDispatcher("/pages/UserSubmitDetailPage.jsp").forward(request, response);
            }

        } else {

        }
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        doGet(request, response);
    }

}
