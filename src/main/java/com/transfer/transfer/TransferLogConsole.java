package com.transfer.transfer;

import com.transfer.MoneyTransferServiceApplication;
import com.transfer.model.AmountCard;
import com.transfer.model.Card;
import com.transfer.model.DataOperation;
import com.transfer.transfer.TransferLogs;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class TransferLogConsole implements TransferLogs {

    @Override
    public boolean transferLog(String operationsLogs) {
        if (operationsLogs != null) {
            System.out.println(operationsLogs);
            return true;
        }
        return false;
    }

    public static String writeStringLog(String operationId, DataOperation dataOperation) {
        Card currentCard = dataOperation.getCard();

        String cardToNumber = dataOperation.getCardToNumber();

        BigDecimal transferValue = dataOperation.getTransferValue();

        BigDecimal newValueCardFrom = dataOperation.getValue();

        BigDecimal fee = dataOperation.getFee();

        currentCard.setAmountCard(new AmountCard(newValueCardFrom, currentCard.getAmountCard().getCurrency()));

        return "Время транзакции: "
                + MoneyTransferServiceApplication.time
                + ",\n Id транзакции: "
                + operationId
                + ",\n карта списания: "
                + currentCard.getCardFromNumber()
                + ",\n карта зачисления: "
                + cardToNumber
                + ",\n сумма перевода: "
                + transferValue
                + ",\n валюта перевода: "
                + currentCard.getAmountCard().getCurrency()
                + ",\n комиссия в валюте перевода: "
                + fee
                + ",\n остаток на карте списания, руб.: "
                + newValueCardFrom;
    }
}

