package com.transfer.model;

public class IdOperationResponse {
    private String idOperation;

    public IdOperationResponse(String operationId) {
        this.idOperation = operationId;
    }

    public String getOperationId() {
        return idOperation;
    }

    public void setOperationId(String operationId) {
        this.idOperation = operationId;
    }

    @Override
    public String toString() {
        return "OperationIdResponse{" +
                "idOperation='" + idOperation + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IdOperationResponse that = (IdOperationResponse) o;
        return idOperation.equals(that.idOperation);
    }

}
