package com.QuizApp.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.QuizApp.dao.CustomerDBImple;
import com.QuizApp.dao.CustomerDb;

/**
 * Servlet implementation class DisplayQuizTypeServlet
 */
@WebServlet("/QuizTypeServlet")
public class DisplayQuizTypeServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("redirect to display quiz page");
        // TODO Auto-generated method stub
        PrintWriter out = response.getWriter();
        CustomerDb cd = new CustomerDBImple();

        // if session is valid 
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("username") != null) {
            System.out.println("session still alive");
            // display List<String> type to home.jsp 
            List<String> types = cd.getQuizType();
            request.setAttribute("types", types);
            request.getRequestDispatcher("pages/home.jsp").forward(request, response);
            // request.getRequestDispatcher("/GenerateQuizServlet").forward(request, response);
        } else {
            // if session is not valid, redirect to login page
            // do somthing else
            request.getRequestDispatcher("/").forward(request, response);
        }

    }


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

}
