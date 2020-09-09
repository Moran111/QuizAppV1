package com.QuizApp.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.QuizApp.dao.CustomerDBImple;
import com.QuizApp.dao.CustomerDb;

/**
 * Servlet implementation class FeedbackServlet
 */
@WebServlet("/FeedbackServlet")
public class FeedbackServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public FeedbackServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        doPost(request, response);
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session != null) {
            // get radio button
            String rating = request.getParameter("rating");
            System.out.println(rating);
            // get input 
            String txt = (String) request.getAttribute("feedback");
            // put to db
            CustomerDb cd = new CustomerDBImple();
            cd.uploadFeedback(txt, Integer.parseInt(rating));

            PrintWriter out = response.getWriter();
            out.print("you have already submit the feedback");
            request.getRequestDispatcher("/QuizTypeServlet").forward(request, response);

        } else {
            request.getRequestDispatcher("/").forward(request, response);
        }

    }

}
