package com.me.transaction.app;

import com.google.common.base.Strings;
import com.me.transaction.model.InputRecord;
import com.me.transaction.model.TransactionRecord;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.function.Function;
import java.util.stream.Collectors;


public class TransactionReport {
    private static final String COMMA_DELIMITER = ",";
    private static final String REVERSAL_TRANS = "REVERSAL";
    private static final Function<String, TransactionRecord> mapCsvToTransRecord = TransactionReport::apply;
    private Double relativeBalance;
    private Integer numOfTransactions;

    public Double getRelativeBalance() {
        return relativeBalance;
    }

    public void setRelativeBalance(Double relativeBalance) {
        this.relativeBalance = relativeBalance;
    }

    public Integer getNumOfTransactions() {
        return numOfTransactions;
    }

    public void setNumOfTransactions(Integer numOfTransactions) {
        this.numOfTransactions = numOfTransactions;
    }

    public static void main(String... args) {
        try {
            Scanner in = new Scanner(System.in);

            System.out.print("Enter the csv file path:");
            String filePath = in.nextLine();

            //Maps each line in csv to Transaction record
            List<TransactionRecord> transRecordList = mapCSV(filePath);

            //Method which scans the input and get the relative balance in an account
            InputRecord inputRecord = getSearchRecord(in);

            TransactionReport report = getRelativeBalanceInAccount(inputRecord, transRecordList);

            //Print the output
            printResult(report);

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    private static void printResult(TransactionReport report) {
        System.out.println("Relative Balance for the period is: " + report.getRelativeBalance());
        System.out.println("Number of transactions included is: " + report.getNumOfTransactions());
    }


    private static InputRecord getSearchRecord(Scanner in) {
        InputRecord sr = new InputRecord();
        System.out.print("Enter the accountid(ACC######):");
        sr.setAccountId(in.nextLine());
        System.out.print("Enter the start date(\"dd/MM/yyyy HH:mm:ss\"):");
        sr.setStartDate(LocalDateTime.parse(in.nextLine(), DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
        System.out.print("Enter the end date:(\"dd/MM/yyyy HH:mm:ss\"):");
        sr.setEndDate(LocalDateTime.parse(in.nextLine(), DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
        return sr;
    }

    private static List<TransactionRecord> mapCSV(String transCSVFilePath) {
        List<TransactionRecord> transrecordList = new ArrayList<>();
        try {
            File inputFile = new File(transCSVFilePath);
            InputStream inputFileStream = new FileInputStream(inputFile);
            BufferedReader br = new BufferedReader(new InputStreamReader(inputFileStream));
            transrecordList = br.lines().skip(1).map(mapCsvToTransRecord).collect(Collectors.toList());
            br.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        return transrecordList;
    }

    public static TransactionReport getRelativeBalanceInAccount(InputRecord sr, List<TransactionRecord> recordList) {
        TransactionReport report = new TransactionReport();
        String accountId = sr.getAccountId();
        LocalDateTime startDate = sr.getStartDate();
        LocalDateTime endDate = sr.getEndDate();
        //Retrieve Reversal transactions
        List<String> revTransNumList = recordList
                .stream()
                .filter(rec -> REVERSAL_TRANS.equalsIgnoreCase(rec.getTransactionType()))
                .map(TransactionRecord::getRelatedTransaction)
                .collect(Collectors.toList());
        //Remove reversal transactions matching the related transaction id
        recordList.removeIf(rec -> (revTransNumList.contains(rec.getTransactionid())
                || REVERSAL_TRANS.equalsIgnoreCase(rec.getTransactionType())));
        //Filter the data based on date input and account id
        List<TransactionRecord> creditTransList = recordList
                .stream()
                .filter(rec -> (rec.getCreateAt().isEqual(startDate) || rec.getCreateAt().isAfter(startDate))
                        && (rec.getCreateAt().isEqual(endDate) || rec.getCreateAt().isBefore(endDate)) && rec.getFromAccountid().equals(accountId)).collect(Collectors.toList());
        List<TransactionRecord> debitTransList = recordList
                .stream()
                .filter(rec -> (rec.getCreateAt().isEqual(startDate) || rec.getCreateAt().isAfter(startDate))
                        && (rec.getCreateAt().isEqual(endDate) || rec.getCreateAt().isBefore(endDate)) && rec.getToAccountid().equals(accountId)).collect(Collectors.toList());
        report.setRelativeBalance(getSum(creditTransList) - getSum(debitTransList));
        report.setNumOfTransactions(creditTransList.size() + debitTransList.size());
        return report;
    }

    private static double getSum(List<TransactionRecord> transList) {
        return transList.stream()
                .mapToDouble(TransactionRecord::getAmount).sum();
    }

    private static TransactionRecord apply(String line) {

        String[] record = line.split(COMMA_DELIMITER);
        TransactionRecord debitTransactionRecord = new TransactionRecord();
        if (!Strings.isNullOrEmpty(record[0].trim())) {
            debitTransactionRecord.setTransactionid(record[0].trim());

        }
        if (!Strings.isNullOrEmpty(record[1].trim())) {
            debitTransactionRecord.setFromAccountid(record[1].trim());

        }
        if (!Strings.isNullOrEmpty(record[1].trim())) {
            debitTransactionRecord.setToAccountid(record[1].trim());

        }
        if (!Strings.isNullOrEmpty(record[3].trim())) {
            debitTransactionRecord.setCreateAt(LocalDateTime.parse(record[3].trim(), DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
        }
        if (!Strings.isNullOrEmpty(record[4].trim())) {
            debitTransactionRecord.setAmount(Double.parseDouble(record[4].trim()) * -1);
        }
        if (!Strings.isNullOrEmpty(record[5].trim())) {
            debitTransactionRecord.setTransactionType(record[5].trim());
            if (REVERSAL_TRANS.equalsIgnoreCase(record[5].trim()) && !Strings.isNullOrEmpty(record[6])) {
                debitTransactionRecord.setRelatedTransaction(record[6].trim());
            }
        }

        return debitTransactionRecord;
    }
}

