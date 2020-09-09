package com.QuizApp.dao;

import com.QuizApp.domain.Question;
import com.QuizApp.domain.SubmitInfo;
import com.QuizApp.domain.UserInfo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Restrictions;

public class HibernateDBImple implements HibCustDB1 {

    //1. configuring hibernate
    Configuration configuration = new Configuration().configure();
    //2. create session factory
    SessionFactory sessionFactory = configuration.buildSessionFactory();


    @Override
    public SubmitInfo quizResultEachUser(int userId) {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public List<UserInfo> getAllUser() {
        //3. get session object
        Session session = sessionFactory.openSession();

        //4. starting transaction and other operation
        Transaction tx = null;

        List<UserInfo> users = null;

        try {
            System.out.println("ready to show all records");
            tx = session.beginTransaction();
            TypedQuery<UserInfo> query = session.createQuery("FROM UserInfo u where u.admin = 'N'");
            users = query.getResultList();
            tx.commit();
        } catch (Exception e) {

            e.printStackTrace();
        } finally {
            session.close();
        }
        return users;
    }


    @Override
    public void updateUserStates(UserInfo userid, String states) {
        //3. get session object
        Session session = sessionFactory.openSession();
        //4. starting transaction and other operation
        Transaction tx = null;
        List<UserInfo> users = null;

        try {
            System.out.println("ready to update");
            System.out.println("ready to update user id " + userid);
            System.out.println("ready to update status " + states);
            tx = session.beginTransaction();
            String hql = "UPDATE UserInfo s set s.status = :status WHERE s.id = :user_id";
            Query query = session.createQuery(hql);
            query.setParameter("status", states);
            query.setParameter("user_id", userid.getId());
            int count = query.executeUpdate();

            userid.setStatus(states);

            System.out.println("update " + count + " rows ");
            tx.commit();
        } catch (Exception e) {

            e.printStackTrace();
        } finally {
            session.close();
        }
    }


    @Override
    public List<SubmitInfo> getAllUserQuiz() {
        // TODO Auto-generated method stub
        //3. get session object
        Session session = sessionFactory.openSession();
        //4. starting transaction and other operation
        Transaction tx = null;

        System.out.println("ready to display all user quizes ");

        tx = session.beginTransaction();
        Query query = session.createQuery("from SubmitInfo");

        List<SubmitInfo> lists = query.getResultList();

        for (SubmitInfo li : lists) {
            System.out.println("the quiz id is " + li.getQuizId() + " the user naem is " + li.getUsername() + " the user id is " + li.getUserId() + " the result is " + li.getResult());
        }

        return lists;
    }


    @Override
    public List<Question> listAllQuestion() {
        // TODO Auto-generated method stub
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        tx = session.beginTransaction();
        Query query = session.createQuery("from question");

        List<Question> lists = query.getResultList();

        for (Question li : lists) {
            System.out.println("the quiz id is " + li.getqDescribe());
        }

        return lists;
    }

}
