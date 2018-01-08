package report.manager;

import org.springframework.beans.factory.annotation.Autowired;
import report.dataaccess.ReportDAO;

import java.util.Map;

public class ReportManagerImp implements ReportManager{
    @Autowired
    private ReportDAO reportDAO;

    public Object report(Map<String, Object> criteria){
        return reportDAO.report(criteria);
    }
}
