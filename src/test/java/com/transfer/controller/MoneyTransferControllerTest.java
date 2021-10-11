package com.transfer.controller;

import com.transfer.model.*;
import com.transfer.service.MoneyTransferService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

public class MoneyTransferControllerTest {

    MoneyTransferService moneyTransferServiceMock = Mockito.mock(MoneyTransferService.class);
    Map operationsRepositoryMock = Mockito.mock(Map.class);
    Map verificationRepositoryMock = Mockito.mock(Map.class);

    BigDecimal testCardValue = BigDecimal.valueOf(203_345.15);
    Card testCard = new Card("1111111111111111",
            "11/21",
            "111",
            new AmountCard(testCardValue, "RUR"));

    String testCardToNumber = "222222222222";

    DataTransfer testTransferData = new DataTransfer("1111111111111111", testCardToNumber, "11/21",
            "111",
            new Amount(100_000, "RUR"));

    BigDecimal transferValue = BigDecimal.valueOf(100_000)
            .setScale(2, RoundingMode.CEILING);
    BigDecimal fee = transferValue.multiply(BigDecimal.valueOf(0.01))
            .setScale(2, RoundingMode.CEILING);
    BigDecimal newValueCardFrom = (testCardValue.subtract(transferValue.multiply(BigDecimal.valueOf(1.01))))
            .setScale(2, RoundingMode.CEILING);

    String testOperationId = "Bn@Operation#0001";
    String testCode = "7777";
    Verification testVerification = new Verification(testOperationId, testCode);


    @BeforeEach
    public void mockBefore() {
        Mockito.when(moneyTransferServiceMock.transfer(testTransferData))
                .thenReturn(testOperationId);
        Mockito.when(moneyTransferServiceMock.confirmOperation(testVerification))
                .thenReturn(testOperationId);

    }

    @Test
    void testOperationIdTransferController() {

        IdOperationResponse resultResponse = new IdOperationResponse(moneyTransferServiceMock.transfer(testTransferData));

        IdOperationResponse expectedResponse = new IdOperationResponse(testOperationId);

        Assertions.assertEquals(expectedResponse, resultResponse);
    }

    @Test
    void testOperationIdConfirmController() {

        IdOperationResponse expectedResponse = new IdOperationResponse(testOperationId);

        IdOperationResponse resultResponse = new IdOperationResponse(moneyTransferServiceMock.confirmOperation(testVerification));

        Assertions.assertEquals(expectedResponse, resultResponse);
    }


    @Test
    void testTransferController() {

        ResponseEntity<IdOperationResponse> result = new MoneyTransferController(moneyTransferServiceMock).transfer(testTransferData);

        ResponseEntity<IdOperationResponse> expected = new ResponseEntity<>((new IdOperationResponse(testOperationId)), HttpStatus.OK);

        Assertions.assertEquals(expected, result);
    }

    @Test
    void testConfirmController() {

        ResponseEntity<IdOperationResponse> expected = new ResponseEntity<>((new IdOperationResponse(testOperationId)), HttpStatus.OK);

        ResponseEntity<IdOperationResponse> result = new MoneyTransferController(moneyTransferServiceMock).confirmOperation(testVerification);

        Assertions.assertEquals(expected, result);

    }
}