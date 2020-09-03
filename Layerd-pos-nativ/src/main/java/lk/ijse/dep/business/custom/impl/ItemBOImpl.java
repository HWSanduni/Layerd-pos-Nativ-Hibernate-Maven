package lk.ijse.dep.business.custom.impl;

import lk.ijse.dep.business.custom.ItemBO;
import lk.ijse.dep.dao.DAOFactory;
import lk.ijse.dep.dao.DAOType;
import lk.ijse.dep.dao.custom.ItemDAO;
import lk.ijse.dep.db.HibernateUtil;
import lk.ijse.dep.entity.Item;
import org.hibernate.Session;
import org.hibernate.Transaction;
import lk.ijse.dep.util.ItemTM;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ItemBOImpl implements ItemBO {
    private ItemDAO itemDAO = DAOFactory.getInstance().getDAO(DAOType.ITEM);

    public List<ItemTM> getAllItems() throws Exception{


        Session session = HibernateUtil.getSessionFactory().openSession();
        itemDAO.setSession(session);
        Transaction transaction = null;
        List<Item> allItems = null;

        try {
            transaction = session.beginTransaction();
                allItems = itemDAO.findAll();
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        } finally {
            session.close();
        }

        ArrayList<ItemTM> items = new ArrayList<>();
        for (Object i : allItems) {
            Item item = (Item) i;
            items.add(new ItemTM(item.getItemCode(),item.getDescription(),item.getQtyOnHand(),item.getUnitprice().doubleValue()));
        }
        return items;



    }

    public String getNewitemCode()throws Exception{
        Session session = HibernateUtil.getSessionFactory().openSession();
        itemDAO.setSession(session);
        Transaction transaction = null;
        String lastitemCode = null;


        try {
            transaction = session.beginTransaction();
            lastitemCode = itemDAO.getLastitemCode();

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        } finally {
            session.close();
        }

        if (lastitemCode == null){
            return "I001";
        }else{
            int maxId=  Integer.parseInt(lastitemCode.replace("I",""));
            maxId = maxId + 1;
            String id = "";
            if (maxId < 10) {
                id = "I00" + maxId;
            } else if (maxId < 100) {
                id = "I0" + maxId;
            } else {
                id = "I" + maxId;
            }
            return id;
        }
    }

    public void saveItem(String code, String description, int qtyOnHand, double unitPrice)throws Exception{

        Session session = HibernateUtil.getSessionFactory().openSession();
        itemDAO.setSession(session);
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();
            itemDAO.save(new Item(code, description, BigDecimal.valueOf(unitPrice), qtyOnHand));

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        } finally {
            session.close();
        }

    }

    public void deleteItem(String itemCode) throws Exception{
        Session session = HibernateUtil.getSessionFactory().openSession();
        itemDAO.setSession(session);
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();
            itemDAO.delete(itemCode);

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        } finally {
            session.close();
        }

    }

    public void updateItem(String description, int qtyOnHand, double unitPrice, String itemCode)throws Exception{
        Session session = HibernateUtil.getSessionFactory().openSession();
        itemDAO.setSession(session);
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();
            itemDAO.update(new Item(itemCode, description, BigDecimal.valueOf(unitPrice),qtyOnHand));

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        } finally {
            session.close();
        }
    }
}
