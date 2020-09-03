package lk.ijse.dep.business.custom.impl;

import lk.ijse.dep.business.custom.CustomerBO;
import lk.ijse.dep.dao.DAOFactory;
import lk.ijse.dep.dao.DAOType;
import lk.ijse.dep.dao.custom.CustomerDAO;
import lk.ijse.dep.db.HibernateUtil;
import lk.ijse.dep.entity.Customer;
import org.hibernate.Session;
import org.hibernate.Transaction;
import lk.ijse.dep.util.CustomerTM;

import java.util.ArrayList;
import java.util.List;


public class CustomerBOImpl implements CustomerBO {
    private CustomerDAO customerDAO = DAOFactory.getInstance().getDAO(DAOType.CUSTOMER);

    public String getNewCustomerId() throws Exception {

        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        customerDAO.setSession(session);

        String lastCustomerId="";

        try{
            transaction = session.beginTransaction();

            lastCustomerId = customerDAO.getLastCustomerId();

            transaction.commit();
        }catch (Exception e){
            transaction.rollback();
            throw e;
        }finally {
            session.close();
        }
        if (lastCustomerId == null){
            return "C001";
        }else{
            int maxId=  Integer.parseInt(lastCustomerId.replace("C",""));
            maxId = maxId + 1;
            String id = "";
            if (maxId < 10) {
                id = "C00" + maxId;
            } else if (maxId < 100) {
                id = "C0" + maxId;
            } else {
                id = "C" + maxId;
            }
            return id;
        }




    }

    public List<CustomerTM> getAllCustomers() throws Exception {

        Session session = HibernateUtil.getSessionFactory().openSession();
        customerDAO.setSession(session);
        Transaction transaction = null;
        List<Customer> allCustomers=null;

        try{
            transaction = session.beginTransaction();
            allCustomers = customerDAO.findAll();
            transaction.commit();
        }catch (Exception e){
            transaction.rollback();
            throw e;
        }finally {
            session.close();
        }

        List<CustomerTM> customers = new ArrayList<>();
        for (Customer customer : allCustomers) {
            customers.add(new CustomerTM(customer.getId(),customer.getName(),customer.getAddress()));
        }
        return customers;


    }

    public void saveCustomer(String id, String name, String address) throws Exception {

        Session session = HibernateUtil.getSessionFactory().openSession();
        customerDAO.setSession(session);
        Transaction transaction = null;

        try{
            transaction = session.beginTransaction();

            customerDAO.save(new Customer(id,name,address));

            transaction.commit();
        }catch (Exception e){
            transaction.rollback();
            throw e;
        }finally {
            session.close();
        }

    }

    public void deleteCustomer(String customerId)throws Exception {

        Session session = HibernateUtil.getSessionFactory().openSession();
        customerDAO.setSession(session);
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();

            customerDAO.delete(customerId);

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        } finally {
            session.close();
        }

    }


    public void updateCustomer(String name, String address, String customerId)throws Exception{


        Session session = HibernateUtil.getSessionFactory().openSession();
        customerDAO.setSession(session);
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();

            customerDAO.update(new Customer(customerId, name, address));

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        } finally {
            session.close();
        }

    }
}
