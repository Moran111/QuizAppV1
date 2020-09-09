package com.QuizApp.controller;


import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/LogoutServlet")
public class LogoutServlet extends HttpServlet {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        // request.getRequestDispatcher("/pages/link.html").include(request, response);

        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("username") != null) {
            session.invalidate();
            out.print("you are successfully logged out!");
            request.getRequestDispatcher("/").forward(request, response);
        } else {
            request.getRequestDispatcher("/").forward(request, response);
        }


    }
}
