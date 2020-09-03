package com.me.transaction.app;

import com.me.transaction.model.InputRecord;
import com.me.transaction.model.TransactionRecord;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import junit.framework.TestCase;

public class TransactionReportTest extends TestCase {

    public void setUp() throws Exception {
        super.setUp();
    }

    public void tearDown() {
    }

    public void testGetRelativeBalanceInAccount() {
        InputRecord ir = getInputRecord();
        TransactionReport report = TransactionReport.getRelativeBalanceInAccount(ir, getTransList());
        assertEquals(new Double("-25.0"), report.getRelativeBalance());
        assertEquals(new Integer("1"), report.getNumOfTransactions());
        ir.setAccountId("ACC778899");
        report = TransactionReport.getRelativeBalanceInAccount(ir, getTransList());
        assertEquals(new Double("30.0"), report.getRelativeBalance());
        assertEquals(new Integer("2"), report.getNumOfTransactions());
        ir.setAccountId("ACC998877");
        report = TransactionReport.getRelativeBalanceInAccount(ir, getTransList());
        assertEquals(new Double("-5.0"), report.getRelativeBalance());
        assertEquals(new Integer("1"), report.getNumOfTransactions());
    }

    private List<TransactionRecord> getTransList() {
        List<TransactionRecord> transRecordList = new ArrayList<>();
        transRecordList.add(new TransactionRecord("TX10001", "ACC334455", "ACC778899", LocalDateTime.parse("20/10/2018 12:45:55",
                DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")), 25.00,
                "PAYMENT", ""));
        transRecordList.add(new TransactionRecord("TX10002", "ACC334455", "ACC998877", LocalDateTime.parse("20/10/2018 17:33:43",
                DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")), 10.50,
                "PAYMENT", ""));
        transRecordList.add(new TransactionRecord("TX10003", "ACC998877", "ACC778899", LocalDateTime.parse("20/10" +
                        "/2018 18:00:00",
                DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")), 5.00,
                "PAYMENT", ""));
        transRecordList.add(new TransactionRecord("TX10004", "ACC334455", "ACC998877", LocalDateTime.parse("20/10/2018 19:45:00",
                DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")), 10.50,
                "REVERSAL", "TX10002"));
        transRecordList.add(new TransactionRecord("TX10005", "ACC334455", "ACC778899", LocalDateTime.parse("20/10" +
                        "/2018 09:30:00",
                DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")), 7.25,
                "PAYMENT", ""));
        return transRecordList;
    }

    private InputRecord getInputRecord() {
        InputRecord ir = new InputRecord();
        ir.setAccountId("ACC334455");
        ir.setStartDate(LocalDateTime.parse("20/10/2018 12:45:55",
                DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
        ir.setEndDate(LocalDateTime.parse("20/10/2018 19:45:55",
                DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
        return ir;

    }
}