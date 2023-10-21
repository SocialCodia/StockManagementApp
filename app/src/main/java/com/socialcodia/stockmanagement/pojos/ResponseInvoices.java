package com.socialcodia.stockmanagement.pojos;

import com.socialcodia.stockmanagement.models.ModelInvoice;

import java.util.List;

public class ResponseInvoices {
    private boolean error;
    private String message;
    private List<ModelInvoice> invoices;

    public ResponseInvoices(boolean error, String message, List<ModelInvoice> invoices) {
        this.error = error;
        this.message = message;
        this.invoices = invoices;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<ModelInvoice> getInvoices() {
        return invoices;
    }

    public void setInvoices(List<ModelInvoice> invoices) {
        this.invoices = invoices;
    }
}
