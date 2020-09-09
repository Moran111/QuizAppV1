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

import com.QuizApp.dao.HibCustDB1;
import com.QuizApp.dao.HibernateDBImple;
import com.QuizApp.domain.UserInfo;
import com.google.gson.Gson;

/**
 * Servlet implementation class DisplayProfile
 * admin can suspend and active the profile page
 */
@WebServlet("/DisplayProfileServlet")
public class DisplayProfileServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HibCustDB1 db = new HibernateDBImple();
        PrintWriter out = response.getWriter();

        List<UserInfo> profiles = db.getAllUser();
        if (profiles == null) {
            out.println("no user using the quiz app");
        } else {
            request.setAttribute("profiles", profiles);
            System.out.println(profiles);
        }


        int page = 1;
        int recordsPerPage = 2; // just have 4 rows in db right now
        int numOfPage = (int) Math.ceil(profiles.size() * 1.0 / recordsPerPage);
        request.setAttribute("numOfPage", numOfPage);
        int start = 0;
        int end = start + recordsPerPage - 1;

        HttpSession oldSession = request.getSession(false);
        if (oldSession != null && oldSession.getAttribute("userInfo") != null && ((UserInfo) oldSession.getAttribute("userInfo")).getAdmin().equals("Y")) {
            String username = (String) oldSession.getAttribute("username");
            System.out.println(username);

            String htmlPage = (String) request.getParameter("page");
            if (htmlPage != null && htmlPage.equals("prev")) {
                System.out.println("go to prev page");
                oldSession.setAttribute("currentPage", (int) oldSession.getAttribute("currentPage") - 1);
                oldSession.setAttribute("start", (int) oldSession.getAttribute("start") - recordsPerPage);
                oldSession.setAttribute("end", (int) oldSession.getAttribute("end") - recordsPerPage);

                request.getRequestDispatcher("/pages/userProfile.jsp").forward(request, response);

            } else if (htmlPage != null && htmlPage.equals("next")) {
                System.out.println("go to next page");
                oldSession.setAttribute("currentPage", (int) oldSession.getAttribute("currentPage") + 1);
                oldSession.setAttribute("start", (int) oldSession.getAttribute("start") + recordsPerPage);
                oldSession.setAttribute("end", (int) oldSession.getAttribute("end") + recordsPerPage);

                request.getRequestDispatcher("/pages/userProfile.jsp").forward(request, response);
            } else {
                System.out.println("still on the same page");
                String buttonWithIndex = (String) request.getParameter("button");


                if (buttonWithIndex != null) {

                    System.out.println("the button and index " + buttonWithIndex);
                    String button = buttonWithIndex.split(" ")[0];
                    String index = buttonWithIndex.split(" ")[1];
                    // System.out.println("the button is " + button);
                    // System.out.println("the button indx is " + index);

                    int userId = Integer.parseInt(index);
                    // get the user from its id
                    UserInfo user = findUserById(profiles, userId);
                    String currStatus = user.getStatus();
                    System.out.println("before changes, the status is " + currStatus);

                    if (currStatus.equals("ACTIVE")) {
                        //System.out.println("the button is active");
                        // update the user table, set to suspend
                        db.updateUserStates(user, "SUSPEND");
                        // still on the same page after reload
                        oldSession.setAttribute("currentPage", (int) oldSession.getAttribute("currentPage"));
                        oldSession.setAttribute("start", (int) oldSession.getAttribute("start"));
                        oldSession.setAttribute("end", (int) oldSession.getAttribute("end"));
                        System.out.println("after changes, the status is " + user.getStatus());
                        request.getRequestDispatcher("/pages/userProfile.jsp").forward(request, response);

                    } else if (currStatus.equals("SUSPEND")) {
                        System.out.println("the button is active");
                        // update the user table, set to active
                        db.updateUserStates(user, "ACTIVE");
                        // still on the same page after reload
                        oldSession.setAttribute("currentPage", (int) oldSession.getAttribute("currentPage"));
                        oldSession.setAttribute("start", (int) oldSession.getAttribute("start"));
                        oldSession.setAttribute("end", (int) oldSession.getAttribute("end"));
                        System.out.println("after changes, the status is " + user.getStatus());
                        request.getRequestDispatcher("/pages/userProfile.jsp").forward(request, response);
                    }
                } else {
                    System.out.println("cannot get the button");

                    oldSession.setAttribute("currentPage", page);
                    oldSession.setAttribute("start", start);
                    oldSession.setAttribute("end", end);

                    request.getRequestDispatcher("/pages/userProfile.jsp").forward(request, response);
                }

            }
        } else {
            System.out.println("session is null");
            request.getRequestDispatcher("/").forward(request, response); //means go to login page
        }
        //set button

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    public UserInfo findUserById(List<UserInfo> profiles, int userId) {
        for (UserInfo user : profiles) {
            if (user.getId() == userId) {
                return user;
            }
        }
        return null;
    }
}
