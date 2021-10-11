package com.transfer.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.transfer.model.TransferData;
import com.transfer.model.Verification;
import com.transfer.service.MoneyTransferService;

@RestController
@RequestMapping("/")
public class MoneyTransferController {

    private final MoneyTransferService moneyTransferService;

    public MoneyTransferController(MoneyTransferService moneyTransferService) {
        this.moneyTransferService = moneyTransferService;
    }

    @PostMapping("/transfer/{id}")
    public ResponseEntity<String> transfer(@RequestBody TransferData transferData, @PathVariable long id) {
        if (moneyTransferService.transfer(transferData)) {
            return new ResponseEntity<> ("Поздравляем! Деньги успешно переведены!", HttpStatus.OK);
        }
        return new ResponseEntity<> ("Перевод совершить не удалось. Попробуйте ещё раз.", HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/confirmOperation/{id}")
    public boolean confirmOperation(@RequestBody Verification verification, @PathVariable long id) {
        return moneyTransferService.confirmOperation(verification);
    }


}
