package com.QuizApp.controller;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.QuizApp.dao.CustomerDBImple;
import com.QuizApp.dao.CustomerDb;
import com.QuizApp.dao.HibCustDB;
import com.QuizApp.dao.HibernateDBImple;
import com.QuizApp.domain.ResultType;
import com.QuizApp.domain.SubmitInfo;
import com.QuizApp.domain.UserInfo;
import com.QuizApp.domain.UserProfileRestType;

/**
 * Servlet implementation class DisplayAllUserQuizServlet
 */
@WebServlet("/DisplayAllUserQuizServlet")
public class DisplayAllUserQuizServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // get data from database save to userProfileRestType

        System.out.println("this is display user quiz type");

        HibCustDB db = new HibernateDBImple();
        List<SubmitInfo> quizRes = db.getAllUserQuiz();

        for (SubmitInfo info : quizRes) {
            System.out.println(info.getUsername());
            System.out.println(info.getUserInfo().getFirstName());
            System.out.println(info.getUserInfo().getLastName());
            System.out.println(info.getResult());
            System.out.println(info.getQuizId());
        }

        CustomerDb db2 = new CustomerDBImple();

        List<UserProfileRestType> listSet = new ArrayList<>();
        for (SubmitInfo info : quizRes) {
            UserProfileRestType rs = new UserProfileRestType();
            rs.setDate(info.getStartTime());
            rs.setScore(info.getResult());

            if ((double) rs.getScore() / 10 >= 0.6) {
                rs.setIsPass("Pass");
            } else {
                rs.setIsPass("Fail");
            }

            rs.setUserfullname(info.getUserInfo().getFirstName() + info.getUserInfo().getLastName());
            // find quiz type based on quiz id and how many questions user answered 
            db2.findQuizTypeById(info.getQuizId(), rs);
            db2.findAnsweredQuestionById(info.getQuizId(), rs);
            listSet.add(rs);
        }

        // sort by taking date descending
//		Collections.sort(listSet, new Comparator<UserProfileRestType>() {
//			public int compare(UserProfileRestType o1, UserProfileRestType o2) {
//				// TODO Auto-generated method stub
//				try {
//					Date date1=new SimpleDateFormat("dd/MM/yyyy").parse(o1.getDate());
//					Date date2=new SimpleDateFormat("dd/MM/yyyy").parse(o2.getDate());
//					return date2.compareTo(date1);
//				} catch (ParseException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} 
//				return -1;
//			}
//		});

        System.out.println("before the session check");

        HttpSession oldSession = request.getSession(false);
        System.out.println("after check session, the user name is " + oldSession.getAttribute("userInfo"));

        if (oldSession != null && oldSession.getAttribute("userInfo") != null && ((UserInfo) oldSession.getAttribute("userInfo")).getAdmin().equals("Y")) {
            oldSession.setAttribute("allQuizInfo", quizRes);
            oldSession.setAttribute("userResultSet", listSet);
            // find user first name and last name given user id and user submit information
            for (SubmitInfo submit : quizRes) {
                System.out.println("first name is " + submit.getUserInfo().getFirstName());
                System.out.println("last name is " + submit.getUserInfo().getLastName());
            }

            int currPage = 1;
            int recordsPerPage = 10;
            int totalPages = (int) Math.ceil(quizRes.size() * 1.0 / recordsPerPage);
            System.out.println("total pages is " + totalPages);
            //set total pages
            oldSession.setAttribute("totalPages", totalPages);
            int start = 0;
            int end = start + recordsPerPage - 1;

            String viewDetailButton = request.getParameter("button");
            if (viewDetailButton != null) {
                request.getRequestDispatcher("/SubmissionDetailServlet").forward(request, response);
            } else {
                String buttVal = request.getParameter("submit");
                if (buttVal != null) {
                    if (buttVal.equals("Next")) {
                        System.out.println("go to next page");
                        request.setAttribute("currPage", (int) oldSession.getAttribute("currPage") + 1);
                        System.out.println("Next page is " + oldSession.getAttribute("currPage"));

                        oldSession.setAttribute("start", (int) oldSession.getAttribute("start") + recordsPerPage);
                        oldSession.setAttribute("end", (int) oldSession.getAttribute("end") + recordsPerPage);

                        request.getRequestDispatcher("/pages/displayAllQuizResult.jsp").forward(request, response);
                    } else if (buttVal.equals("Prev")) {
                        System.out.println("got to prev page");
                        request.setAttribute("currPage", (int) oldSession.getAttribute("currPage") - 1);
                        System.out.println("Prev page is " + oldSession.getAttribute("currPage"));

                        oldSession.setAttribute("start", (int) oldSession.getAttribute("start") - recordsPerPage);
                        oldSession.setAttribute("end", (int) oldSession.getAttribute("end") - recordsPerPage);

                        request.getRequestDispatcher("/pages/displayAllQuizResult.jsp").forward(request, response);
                    }
                } else {
                    // when the list start and list end
                    // still on the current page
                    System.out.println("in the current page");
                    oldSession.setAttribute("currPage", currPage);
                    System.out.println("current page is " + oldSession.getAttribute("currPage"));

                    oldSession.setAttribute("start", start);
                    oldSession.setAttribute("end", end);


                    request.getRequestDispatcher("/pages/displayAllQuizResult.jsp").forward(request, response);
                }
            }

        } else {
            request.getRequestDispatcher("/").forward(request, response); //means go to login page
        }
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        // doGet(request, response);
    }

}
