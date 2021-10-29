package com.transfer.repository;

import com.transfer.errors.ErrorInputData;
import com.transfer.model.AmountCard;
import com.transfer.model.Card;
import com.transfer.model.DataOperation;
import com.transfer.model.DataTransfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings("SynchronizationOnLocalVariableOrMethodParameter")
@Repository
public class MoneyTransferRepository {

    public static Map<String, Card> cardsRepository = new ConcurrentHashMap<>();

    @Autowired
    private MoneyTransferRepository() {
    }

    public MoneyTransferRepository(Map<String, Card> cardsRepository) {
        MoneyTransferRepository.cardsRepository = cardsRepository;
    }

    public DataOperation transfer(DataTransfer dataTransfer) {
        Card currentCard;
        DataOperation dataNewOperation = null;

        for (Map.Entry<String, Card> cardRepoEntry : cardsRepository.entrySet()) {

            if (dataTransfer.getCardFromNumber().equals(cardRepoEntry.getKey())) {

                currentCard = cardRepoEntry.getValue();
                dataNewOperation = acceptData(currentCard, dataTransfer);
            }
        }
        return dataNewOperation;
    }

    public boolean confirmOperation(String operationId, DataOperation dataOperation) {
        if (operationId != null) {
            String cardFromNumber = dataOperation.getCard().getCardFromNumber();
            String cardToNumber = dataOperation.getCardToNumber();
            synchronized (cardFromNumber) {
                synchronized (cardToNumber) {
                    Card currentCard = dataOperation.getCard();
                    BigDecimal newValueCardFrom = dataOperation.getValue();
                    currentCard.setAmountCard(new AmountCard(newValueCardFrom, "RUR"));
                    cardsRepository.put(cardFromNumber, currentCard);
                    if (cardsRepository.containsKey(cardToNumber)) {
                        Card cardTo = cardsRepository.get(cardToNumber);
                        BigDecimal valueCardTo = cardTo.getAmountCard().getValue();
                        BigDecimal transferValue = dataOperation.getTransferValue();
                        BigDecimal newValueCardTo = valueCardTo.add(transferValue)
                                .setScale(2, RoundingMode.CEILING);
                        cardTo.setAmountCard(new AmountCard(newValueCardTo, "RUR"));
                        cardsRepository.put(cardToNumber, cardTo);
                    }
                    return true;
                }
            }
        }
        return false;
    }

    public static DataOperation acceptData(Card currentCard, DataTransfer dataTransfer) {

        DataOperation dataNewOperation;
        String cardToNumber = dataTransfer.getCardToNumber();
        String logAmount = "На карте списания недостаточно средств";

        if (!(currentCard.getCardFromNumber().equals(cardToNumber))
                && (currentCard.getCardFromCVV().equals(dataTransfer.getCardFromCVV()))
                && (currentCard.getCardFromValidTill().equals(dataTransfer.getCardFromValidTill()))) {

            BigDecimal currentCardValue = currentCard.getAmountCard().getValue()
                    .setScale(2, RoundingMode.CEILING);

            BigDecimal transferValue = BigDecimal.valueOf(dataTransfer.getAmount().getValue() / 100)
                    .setScale(2, RoundingMode.CEILING);

            BigDecimal fee = transferValue.multiply(BigDecimal.valueOf(0.01))
                    .setScale(2, RoundingMode.CEILING);

            BigDecimal newValueCardFrom = (currentCardValue.subtract(transferValue.multiply(BigDecimal.valueOf(1.01))))
                    .setScale(2, RoundingMode.CEILING);

            if (newValueCardFrom.compareTo(BigDecimal.valueOf(0.01)
                    .setScale(2, RoundingMode.CEILING)) > 0) {

                dataNewOperation = new DataOperation(currentCard, cardToNumber, transferValue, newValueCardFrom, fee);

            } else {
                System.out.println(logAmount);
                throw new ErrorInputData(logAmount);
            }

        } else {
            throw new ErrorInputData("Ошибка ввода данных карты");
        }

        return dataNewOperation;
    }

    public static boolean validateCardDate(String cardValid) {
        Date cardDate = null;
        SimpleDateFormat format = new SimpleDateFormat();
        format.applyPattern("MM/yy");
        try {
            cardDate = format.parse(cardValid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Date todayDate = new Date();
        if (cardDate != null) {
            long diffDate = cardDate.getTime() - todayDate.getTime();
            int month = Integer.parseInt(cardValid.substring(0, 2));

            return ((diffDate >= 0) && (month > 0) && (month < 13));
        }
        return false;
    }
}
