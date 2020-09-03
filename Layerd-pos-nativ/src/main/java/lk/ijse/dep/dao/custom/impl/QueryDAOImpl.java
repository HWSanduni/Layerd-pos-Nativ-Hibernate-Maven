package lk.ijse.dep.dao.custom.impl;

import lk.ijse.dep.dao.custom.QueryDAO;
import lk.ijse.dep.entity.CustomEntity;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;

public class QueryDAOImpl implements QueryDAO {

private Session session;

    @Override
    public void setSession(Session session) {
        this.session = session;
    }
    @Override
    public CustomEntity getOrderDetail(String orderId)throws Exception {


       return (CustomEntity) session.createQuery("SELECT o.OrderID AS orderId,c.CustomerName AS customerName, c.customerId AS customerId FROM  entity.Customer c INNER JOIN entity.Order o on c.id = o.customerId WHERE o.id =?1").setParameter(1,orderId)
                .setResultTransformer(Transformers.aliasToBean(CustomEntity.class));

    }

    @Override
    public CustomEntity getOrderDetail2(String customerId)throws Exception {


     return (CustomEntity) session.createQuery("SELECT NEW entity.CustomEntity(o.id,c.id,c.name)FROM Order o INNER JOIN customer c on o.customer  WHERE o.id =:id").setParameter("id",customerId).uniqueResult();

    }

    @Override
    public CustomEntity getOrderDetail3(String orderId)throws Exception {

        return  null;
//
//        return (CustomEntity) session.createQuery("SELECT NEW lk.ijse.dep.entity.CustomEntity(o.id,c.id,c.name)FROM customer c INNER JOIN Order o on c.id = o.customerId  WHERE o.customerId =?1").setParameter(1,customerId).uniqueResult();
//
//
//        ResultSet rst = CrudUtil.execute("SELECT o.orderId, o.orderDate, c.customerId, c.customerName, SUM(od.orderQty *od.unitprice) AS total\n" +
//                "FROM `order` o INNER JOIN customer c ON o.CustomerID = c.CustomerID\n" +
//                "                INNER JOIN orderdetail od on o.OrderID = od.OrderID WHERE o.OrderID=?", orderId);
//        if (rst.next()) {
//            return new CustomEntity(rst.getString(1),
//                    rst.getDate(2), rst.getString(3)
//                    , rst.getString(4), rst.getDouble(5));
//        }
//        return null;
    }

}
