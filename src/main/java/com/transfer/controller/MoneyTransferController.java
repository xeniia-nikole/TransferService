package com.transfer.controller;

import com.transfer.errors.ErrorConfirmation;
import com.transfer.errors.ErrorInputData;
import com.transfer.errors.ErrorTransfer;
import com.transfer.model.DataTransfer;
import com.transfer.model.ErrorResponse;
import com.transfer.model.IdOperationResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.transfer.model.Verification;
import com.transfer.service.MoneyTransferService;

@RestController
@RequestMapping("/")
public class MoneyTransferController {
    private static final String transfer ="Транзакция подготовлена";
    private static final String msgInput = "Ошибка ввода данных карты";
    private static final String success = "Транзакция успешно завершена";
    private static final String msgTransferError = "Ошибка перевода";
    private static final String msgConfirmationError = "Ошибка подтверждения";

    private final MoneyTransferService moneyTransferService;

    public MoneyTransferController(MoneyTransferService moneyTransferService) {
        this.moneyTransferService = moneyTransferService;
    }

    @PostMapping("/transfer")
    public ResponseEntity<IdOperationResponse> transfer(@RequestBody DataTransfer dataTransfer) {
        String operationId = moneyTransferService.transfer(dataTransfer);
        System.out.println(transfer);
        return new ResponseEntity<>(new IdOperationResponse(operationId), HttpStatus.OK);
    }

    @PostMapping("/confirmOperation")
    public ResponseEntity<IdOperationResponse> confirmOperation(@RequestBody Verification verification) {
        String operationId = moneyTransferService.confirmOperation(verification);
        System.out.println(success);
        return new ResponseEntity<>(new IdOperationResponse(operationId), HttpStatus.OK);
    }

    @ExceptionHandler(ErrorInputData.class)
    public ResponseEntity<ErrorResponse> handleErrorInputData(ErrorInputData e) {
        System.out.println(msgInput);
        return new ResponseEntity<>(new ErrorResponse(e.getMessage(), 400),
                HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ErrorTransfer.class)
    public ResponseEntity<ErrorResponse> handleErrorTransfer(ErrorTransfer e) {
        System.out.println(msgTransferError);
        return new ResponseEntity<>(new ErrorResponse(e.getMessage(), 500),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ErrorConfirmation.class)
    public ResponseEntity<ErrorResponse> handleErrorConfirmation(ErrorConfirmation e) {
        System.out.println(msgConfirmationError);
        return new ResponseEntity<>(new ErrorResponse(e.getMessage(), 500),
                HttpStatus.NOT_FOUND);
    }


}