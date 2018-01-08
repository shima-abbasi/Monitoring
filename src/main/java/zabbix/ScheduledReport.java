package zabbix;

import io.github.hengyunabc.zabbix.sender.DataObject;
import io.github.hengyunabc.zabbix.sender.SenderResult;
import io.github.hengyunabc.zabbix.sender.ZabbixSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import report.manager.ReportManager;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.apache.commons.lang.time.DateUtils.MILLIS_PER_HOUR;
import static org.apache.commons.lang.time.DateUtils.MILLIS_PER_MINUTE;


@Component
public class ScheduledReport {
    private static final Logger log = LoggerFactory.getLogger(ScheduledReport.class);
    private final String host = "192.168.110.105";
    private final int port = 10051;
    private final ZabbixSender sender = new ZabbixSender(host, port);
    private final Integer applicationCode = 6100;

    private DataObject dataObject = new DataObject();

    @Autowired
    private ReportManager reportManager;

    @Scheduled(fixedRate = 10000)
    public void reportTodaySuccessDeploy() {

        Map<String , Object> criteria = new HashMap<>();
        criteria.put("tableName", "t_card");
        criteria.put("count_projection", "*");
        criteria.put("cardIssueStatus", "ISSUED_SUCESS");
        criteria.put("requestDate", new Date());
        criteria.put("applicationCode", applicationCode);

        try {
            Long count = (Long) reportManager.report(criteria);

            System.out.println("TodaySuccessDeploy: " + count + new Date());
            dataObject.setKey("NFC_TodaySuccessDeploy");
            dataObject.setValue(count.toString());
//            sendToZabbix(dataObject);
        } catch (Exception e) {
            e.getMessage();
        }
    }

    @Scheduled(fixedRate = MILLIS_PER_HOUR)
    public void reportAllSuccessDeploy() {
        Map<String , Object> criteria = new HashMap<>();
        criteria.put("tableName", "t_card");
        criteria.put("count_projection", "*");
        criteria.put("cardIssueStatus", "ISSUED_SUCESS");
        criteria.put("applicationCode", applicationCode);

        try {
            String[] status = new String[]{"ISSUED_SUCESS"};
            Long count = (Long) reportManager.report(criteria);
            System.out.println("AllSuccessDeploy: " + count);
            dataObject.setKey("NFC_AllSuccessDeploy");
            dataObject.setValue(count.toString());
//            sendToZabbix(dataObject);
        } catch (Exception e) {
            log.error(e.getMessage());
        }

    }

    @Scheduled(fixedRate = MILLIS_PER_HOUR)
    public void reportAllFailDeploy() {
        Map<String , Object> criteria = new HashMap<>();
        criteria.put("tableName", "t_card");
        criteria.put("count_projection", "*");
        criteria.put("cardIssueStatus", new String[]{"DEPLOYED_FAILED", "ISSUED_FAILED"});
        criteria.put("applicationCode", applicationCode);
        try {
            Long count = (Long) reportManager.report(criteria);
            System.out.println("AllFailDeploy: " + count);
            dataObject.setKey("NFC_AllFailDeploy");
            dataObject.setValue(count.toString());
//            sendToZabbix(dataObject);
        } catch (Exception e) {
            log.error(e.getMessage());
        }

    }

    @Scheduled(fixedRate = MILLIS_PER_HOUR)
    public void reportTodayFailDeploy() {
        Map<String , Object> criteria = new HashMap<>();
        criteria.put("tableName", "t_card");
        criteria.put("count_projection", "*");
        criteria.put("cardIssueStatus", new String[]{"DEPLOYED_FAILED", "ISSUED_FAILED"});
        criteria.put("requestDate", new Date());
        criteria.put("applicationCode", applicationCode);
        try {
            Long count = (Long) reportManager.report(criteria);
            System.out.println("TodayFailDeploy: " + count);
            dataObject.setKey("NFC_TodayFailDeploy");
            dataObject.setValue(count.toString());
//            sendToZabbix(dataObject);
        } catch (Exception e) {
            log.error(e.getMessage());
        }

    }

    @Scheduled(fixedRate = 10 * MILLIS_PER_MINUTE)
    public void reportTodayChargeAmount() {
        Map<String , Object> criteria = new HashMap<>();
        criteria.put("tableName", "t_ChargeRequest");
        criteria.put("sum_projection", "amount");
        criteria.put("purchaseChargeDate", new Date());
        try {
            BigDecimal amount = (BigDecimal) reportManager.report(criteria);
            System.out.println("TodaySuccessCharge: " + amount);
            dataObject.setKey("NFC_TodaySuccessCharge");
            dataObject.setValue(amount.toString());
//            sendToZabbix(dataObject);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @Scheduled(fixedRate = 10 * MILLIS_PER_MINUTE)
    public void reportAllChargeAmount() {
        Map<String , Object> criteria = new HashMap<>();
        criteria.put("tableName", "t_ChargeRequest");
        criteria.put("sum_projection", "amount");
        try {
            BigDecimal amount = (BigDecimal) reportManager.report(criteria);
            System.out.println("AllSuccessCharge: " + amount);
            dataObject.setKey("NFC_AllSuccessCharge");
            dataObject.setValue(amount.toString());
//            sendToZabbix(dataObject);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public void sendToZabbix(DataObject dataObject) {
        try {
            dataObject.setHost("NFC");
            dataObject.setClock(System.currentTimeMillis()/1000);
            SenderResult senderResult = sender.send(dataObject);
            if (!senderResult.success()) {
                log.warn("metrics reporting to zabbix {} unsuccessful: {}", sender.getHost(), sender.getPort(), senderResult);
            } else if (log.isDebugEnabled()) {
                log.debug("metrics reported to zabbix {} {}: {}", sender.getHost(), sender.getPort(), senderResult);
            }
        } catch (Exception e) {
            log.error("failed to ReportManager metrics to " + sender.getHost() + ':' + sender.getPort(), e);
        }
    }
}