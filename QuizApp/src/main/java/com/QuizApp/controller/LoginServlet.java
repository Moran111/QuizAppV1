package com.QuizApp.controller;


import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.QuizApp.dao.*;
import com.QuizApp.domain.UserInfo;
import com.google.gson.Gson;

/**
 * Servlet implementation class LoginServlet
 */

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    Connection connection = DBConnection.getConnection();

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        PrintWriter out = response.getWriter();
        CustomerDb cd = new CustomerDBImple();

        request.getRequestDispatcher("/pages/link.html").include(request, response);

        // TODO Auto-generated method stub
        String username = request.getParameter("name");
        String password = request.getParameter("password");

        UserInfo info = cd.getUserInfo(username, password);
        //System.out.println(info);

        if (info.getUserName() != null && info.getUserName().equals(username) && info.getPassword().equals(password) && info.getStatus().contentEquals("ACTIVE")) {
            // succesfully login
            // get the old session and invalidate
            HttpSession oldSession = request.getSession(false);
            if (oldSession != null) {
                oldSession.invalidate();
            }
            // generate a new session
            HttpSession newSession = request.getSession(true);
            // setting session to expiry in 5 mins
            newSession.setMaxInactiveInterval(5 * 60);
            newSession.setAttribute("userInfo", info);
            request.getSession().setAttribute("username", info.getUserName());
            request.getSession().setAttribute("userId", info.getId());

            //if this person is admin
            if (info.getAdmin().equals("Y")) {
                out.println("welcome admin, this is user profile page");
                request.getRequestDispatcher("/pages/admin.jsp").forward(request, response);
            } else {
                request.getRequestDispatcher("/QuizTypeServlet").forward(request, response);
            }
        } else {
            System.out.println("this part accessed");
            out.print("Incorrect user name or password!");
            request.getRequestDispatcher("/").include(request, response); //means go to login page
        }
        out.close();
    }


}
