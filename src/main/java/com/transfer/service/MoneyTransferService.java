package com.transfer.service;

import com.transfer.errors.ErrorConfirmation;
import com.transfer.errors.ErrorInputData;
import com.transfer.model.DataOperation;
import com.transfer.model.DataTransfer;
import com.transfer.model.ValidationCode;
import com.transfer.model.Verification;
import com.transfer.repository.MoneyTransferRepository;
import com.transfer.transfer.TransferLogConsole;
import com.transfer.transfer.TransferLogFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class MoneyTransferService {
    private static final String logData = "Ошибка ввода данных карты";
    private static final String logTime = "Срок действия вашей карты истёк";
    private static final String logCode = "Неверный код подтверждения";
    private static final String logId = "Транзакция отклонена";
    private static final String operationConfirmed = "Транзакция подтверждена";
    private static final String infoSentToClient = "Вся информация о транзакции передана клиенту";

    private final TransferLogFile transferLogFile;
    private final MoneyTransferRepository moneyTransferRepository;
    private final TransferLogConsole transferLogConsole;
    public Map<String, DataOperation> operationsRepository = new ConcurrentHashMap<>();
    final private AtomicInteger idNumber = new AtomicInteger(1);
    public Map<String, String> verificationRepository = new ConcurrentHashMap<>();

    @Autowired
    public MoneyTransferService(TransferLogFile transferLogFile,
                                MoneyTransferRepository moneyTransferRepository,
                                TransferLogConsole transferLogConsole) {
        this.transferLogFile = transferLogFile;
        this.moneyTransferRepository = moneyTransferRepository;
        this.transferLogConsole = transferLogConsole;
    }

    public MoneyTransferService(TransferLogFile transferLogFile,
                                MoneyTransferRepository moneyTransferRepository,
                                TransferLogConsole transferLogConsole,
                                Map<String, DataOperation> operationsRepository,
                                Map<String, String> verificationRepository) {

        this.transferLogFile = transferLogFile;
        this.moneyTransferRepository = moneyTransferRepository;
        this.transferLogConsole = transferLogConsole;
        this.verificationRepository = verificationRepository;
        this.operationsRepository = operationsRepository;
    }


    public String transfer(DataTransfer dataTransfer) {
        String operationId;
        String code = ValidationCode.generateCode();

        String cardValidTill = dataTransfer.getCardFromValidTill();

        if (validateCardDate(cardValidTill)) {
            DataOperation dataNewOperation = moneyTransferRepository.transfer(dataTransfer);
            if (dataNewOperation != null) {
                operationId = "Bn@Operation#000" + idNumber.getAndIncrement();
                operationsRepository.put(operationId, dataNewOperation);
                verificationRepository.put(operationId, code);
                ValidationCode.sendCodeToPhone(code);
            } else {
                throw new ErrorInputData(logData);
            }
        } else {
            System.out.println(logTime);
            throw new ErrorInputData(logTime);
        }

        return operationId;
    }

    public String confirmOperation(Verification verification) {
        String operationId = verification.getOperationId();

        if (verificationRepository.containsKey(operationId) && operationId != null) {
            String code = verificationRepository.get(operationId);
            if (code != null && ValidationCode.isCodeCorrect(code)) {
                DataOperation currentDataOperation = operationsRepository.get(operationId);
                if (moneyTransferRepository.confirmOperation(operationId, currentDataOperation)) {
                    System.out.println(operationConfirmed);
                    String operationLogs = writeTransferLog(operationId, currentDataOperation);
                    synchronized (transferLogFile) {
                        if (transferLogFile.transferLog(operationLogs)
                                && transferLogConsole.transferLog(operationLogs)) {
                            System.out.println(infoSentToClient);
                        }
                    }
                } else {
                    System.out.println(logId);
                    throw new ErrorConfirmation(logId);
                }
            } else {
                System.out.println(logCode);
                throw new ErrorConfirmation(logCode);
            }
        } else {
            System.out.println(logId);
            throw new ErrorConfirmation(logId);
        }
        return operationId;
    }

    public static String writeTransferLog(String operationId, DataOperation dataOperation) {
       return TransferLogConsole.writeStringLog(operationId, dataOperation);
    }

    public static boolean validateCardDate(String cardValid) {
        return MoneyTransferRepository.validateCardDate(cardValid);
    }

}