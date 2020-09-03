package lk.ijse.dep.business.custom.impl;

import lk.ijse.dep.business.custom.OrderBO;
import lk.ijse.dep.dao.DAOFactory;
import lk.ijse.dep.dao.DAOType;
import lk.ijse.dep.dao.custom.CustomerDAO;
import lk.ijse.dep.dao.custom.ItemDAO;
import lk.ijse.dep.dao.custom.OrderDAO;
import lk.ijse.dep.dao.custom.OrderDetailDAO;
import lk.ijse.dep.db.HibernateUtil;
import lk.ijse.dep.entity.Item;
import lk.ijse.dep.entity.Order;
import lk.ijse.dep.entity.OrderDetail;
import org.hibernate.Session;
import org.hibernate.Transaction;
import lk.ijse.dep.util.OrderDetailTM;
import lk.ijse.dep.util.OrderTM;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

public class OrderBOImpl implements OrderBO {
   private OrderDAO orderDAO = DAOFactory.getInstance().getDAO(DAOType.ORDER);
   private OrderDetailDAO orderDetailDAO = DAOFactory.getInstance().getDAO(DAOType.ORDERDETAIL);
   private ItemDAO itemDAO = DAOFactory.getInstance().getDAO(DAOType.ITEM);
   private CustomerDAO customerDAO = DAOFactory.getInstance().getDAO(DAOType.CUSTOMER);


    public String getNewOrderId() throws Exception{
        Session session = HibernateUtil.getSessionFactory().openSession();
        orderDAO.setSession(session);
        String lastOrderId = null;
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();
            lastOrderId = orderDAO.getLastOrderId();
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        } finally {
            session.close();
        }

        if (lastOrderId == null){
            return "OD001";
        }else{
            int maxId=  Integer.parseInt(lastOrderId.replace("OD",""));
            maxId = maxId + 1;
            String id = "";
            if (maxId < 10) {
                id = "OD00" + maxId;
            } else if (maxId < 100) {
                id = "OD0" + maxId;
            } else {
                id = "OD" + maxId;
            }
            return id;
        }
    }
    public void placeOrders(OrderTM order, List<OrderDetailTM> orderDetails) throws Exception {


        Session session = HibernateUtil.getSessionFactory().openSession();
        orderDAO.setSession(session);
        itemDAO.setSession(session);
        orderDetailDAO.setSession(session);
        customerDAO.setSession(session);

        try {

            session.getTransaction().begin();
            orderDAO.save(new Order(order.getOrderId(), Date.valueOf(order.getOrderDate()),
                  customerDAO.find(order.getCustomerId())));

            for (OrderDetailTM orderDetail : orderDetails) {
                 orderDetailDAO.save(new OrderDetail(order.getOrderId(),
                         orderDetail.getCode(), orderDetail.getQty(),
                         BigDecimal.valueOf(orderDetail.getUnitPrice())));

                Item item = itemDAO.find(orderDetail.getCode());
                item.setQtyOnHand(item.getQtyOnHand()-orderDetail.getQty());
                System.out.println(item.getQtyOnHand()-orderDetail.getQty());
               itemDAO.update(item);
                System.out.println(item);

            }
            session.getTransaction().commit();
        } catch (Throwable t) {
          session.getTransaction().rollback();
          throw  t;
        } finally {
          session.close();
        }

    }
}
