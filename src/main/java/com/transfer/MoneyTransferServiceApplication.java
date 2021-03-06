package com.transfer;

import com.transfer.model.AmountCard;
import com.transfer.model.Card;
import com.transfer.repository.MoneyTransferRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@SpringBootApplication
public class MoneyTransferServiceApplication {

    public static final String nameLog = "fileOperatiosLogs.log";
    public static final String time = new SimpleDateFormat("HH:mm:ss dd.MM.yyyy").format(new Date());

    public static void main(String[] args) {
        SpringApplication.run(MoneyTransferServiceApplication.class, args);
        System.out.println("\nWelcome to MoneyTransferService!");
        createFiles();
        addCards();

        printRepo();
    }

    public static void createFiles() {

        String msgLog = "Файл fileOperatiosLogs.log успешно создан";
        File logFile = new File(nameLog);

        if (!logFile.exists()) {
            try {
                if (logFile.createNewFile())
                    System.out.println(msgLog);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try (FileWriter writerLogs = new FileWriter(nameLog, true)) {
                writerLogs.write("Время операции:" + time + ": " + msgLog + "\n");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void addCards() {
        MoneyTransferRepository.cardsRepository.put("01000100001000010",
                new Card("01000100001000010",
                        "11/21",
                        "111",
                        new AmountCard(BigDecimal.valueOf(203_345.15), "RUR")));
        MoneyTransferRepository.cardsRepository.put("01000100001000011",
                new Card("01000100001000011",
                        "11/22",
                        "112",
                        new AmountCard(BigDecimal.valueOf(345.15), "RUR")));
        MoneyTransferRepository.cardsRepository.put("01000100001000001",
                new Card("01000100001000001",
                        "11/23",
                        "113",
                        new AmountCard(BigDecimal.valueOf(203_345_111.15), "RUR")));
    }

    public static void printRepo() {
        System.out.println("Актуальный репозиторий банковских карт");
        for (Map.Entry<String, Card> cardRepoEntry : MoneyTransferRepository.cardsRepository.entrySet()) {
            System.out.println("CardNumber: " + cardRepoEntry.getKey() + " DataCard: " + cardRepoEntry.getValue().toString());

        }

    }
}