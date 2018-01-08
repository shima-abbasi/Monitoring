package report.manager;

import java.util.Map;

public interface ReportManager {
    Object report(Map<String, Object> criteria);
}
