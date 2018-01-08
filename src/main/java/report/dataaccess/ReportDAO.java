package report.dataaccess;

import java.sql.*;
import java.util.Map;

public interface ReportDAO {
    Object report(Map<String, Object> criteria) throws SQLException;


}
