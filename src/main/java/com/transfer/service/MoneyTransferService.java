package com.transfer.service;

import org.springframework.stereotype.Service;
import com.transfer.model.TransferData;
import com.transfer.model.Verification;
import com.transfer.repository.MoneyTransferRepository;

@Service
public class MoneyTransferService {

    private final MoneyTransferRepository moneyTransferRepository;

    public MoneyTransferService(MoneyTransferRepository moneyTransferRepository) {
        this.moneyTransferRepository = moneyTransferRepository;
    }

    public boolean transfer(TransferData transferData) {

        return moneyTransferRepository.transfer(transferData);
    }

    public boolean confirmOperation(Verification verification) {
        return moneyTransferRepository.confirmOperation(verification);
    }


}
