package lk.ijse.dep.dao.custom.impl;

import lk.ijse.dep.dao.CrudDAOImpl;
import lk.ijse.dep.dao.custom.CustomerDAO;
import lk.ijse.dep.entity.Customer;

public class CustomerDAOImpl extends CrudDAOImpl<Customer,String>implements CustomerDAO {

    public String getLastCustomerId()throws Exception{
        return (String) session.createNativeQuery("SELECT id FROM Customer ORDER BY id DESC  LIMIT 1").uniqueResult();

    }

}


