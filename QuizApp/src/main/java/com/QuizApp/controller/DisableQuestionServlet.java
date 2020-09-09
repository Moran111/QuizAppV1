package com.QuizApp.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.QuizApp.dao.HibCustDB;
import com.QuizApp.dao.HibernateDBImple;
import com.QuizApp.domain.Question;
import com.QuizApp.domain.UserInfo;

import java.util.*;

/**
 * Servlet implementation class QuestionServlet
 */
@WebServlet("/DisableQuestionServlet")
public class DisableQuestionServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        doPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        HttpSession oldSession = request.getSession(false);
        HibCustDB db = new HibernateDBImple();

        if (oldSession != null && oldSession.getAttribute("userInfo") != null && ((UserInfo) oldSession.getAttribute("userInfo")).getAdmin().equals("Y")) {
            List<Question> list = db.listAllQuestion();

            request.setAttribute("AllQuest", list);


            request.getRequestDispatcher("/pages/UpdateQuestion.jsp").forward(request, response);

        } else {

        }
    }

}
