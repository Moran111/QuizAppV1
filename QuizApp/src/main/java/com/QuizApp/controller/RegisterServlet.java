package com.QuizApp.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.QuizApp.dao.CustomerDBImple;
import com.QuizApp.dao.CustomerDb;
import com.QuizApp.dao.DBConnection;
import com.QuizApp.domain.UserInfo;

// @WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    Connection connection = DBConnection.getConnection();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        PrintWriter out = response.getWriter();
        CustomerDb cd = new CustomerDBImple();

        // TODO Auto-generated method stub
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String submitType = request.getParameter("submit");

        System.out.println(username);

        UserInfo info = cd.getUserInfo(username, password);
        // out.println(info.getUserName());
        // out.println(info.getName());

        // send data to db 
        info.setFirstName(request.getParameter("firstname"));
        info.setPassword(password);
        info.setUserName(username);
        info.setLastName(request.getParameter("lastname"));

        out.println(username);
        out.println(password);
        out.println(submitType);


        cd.insertInfo(info);
        // request.setAttribute("message", "Registration done, login again");
        request.getRequestDispatcher("/pages/login.html").forward(request, response);
    }
}

