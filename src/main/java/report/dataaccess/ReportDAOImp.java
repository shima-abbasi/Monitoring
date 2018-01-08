package report.dataaccess;

import org.hibernate.Query;
import org.hibernate.Session;

import java.sql.SQLException;
import java.util.Map;

public class ReportDAOImp implements ReportDAO {
    @Override
    public Object report(Map<String, Object> criteria) throws SQLException {
        return null;
    }

//    public Object report(Map<String, Object> criteria) throws SQLException {
//
//        Session session = HibernateUtil.getSessionFactory().openSession();
//        String sqlString = "SELECT :=projection (:=COLUMN_NAME ) FROM :=tableName WHERE ";
//        Query query = session.createSQLQuery(sqlString);
//
//        for (Map.Entry<String, Object> entry : criteria.entrySet()) {
//            if (entry.getKey().equals("tableName"))
//                query.setParameter("tableName", entry.getValue());
//            else if (entry.getKey().equals("sum_projection")) {
//                query.setParameter("projection", "sum");
//                query.setParameter("COLUMN_NAME", entry.getValue());
//            } else if (entry.getKey().equals("count_projection")) {
//                query.setParameter("projection", "count");
//                query.setParameter("COLUMN_NAME", entry.getValue());
//            } else
//        }
//
//    }
}
