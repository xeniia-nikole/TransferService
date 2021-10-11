package com.transfer;

import com.transfer.model.AmountCard;
import com.transfer.model.Card;
import com.transfer.repository.MoneyTransferRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@SpringBootApplication
public class MoneyTransferServiceApplication {

    public static final String time = new SimpleDateFormat("HH:mm:ss dd.MM.yyyy").format(new Date());

    public static void main(String[] args) {
        SpringApplication.run(MoneyTransferServiceApplication.class, args);
        System.out.println("\nWelcome to MoneyTransferService!");
        addCards();
        printRepo();
    }

    public static void addCards() {
        MoneyTransferRepository.cardsRepository.put("1111111111111111",
                new Card("1111111111111111",
                        "11/21",
                        "111",
                        new AmountCard(BigDecimal.valueOf(203_345.15), "RUR")));
        MoneyTransferRepository.cardsRepository.put("1111111111111112",
                new Card("1111111111111112",
                        "11/22",
                        "112",
                        new AmountCard(BigDecimal.valueOf(345.15), "RUR")));
        MoneyTransferRepository.cardsRepository.put("1111111111111113",
                new Card("1111111111111113",
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