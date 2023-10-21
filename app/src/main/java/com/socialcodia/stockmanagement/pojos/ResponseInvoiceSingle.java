package com.socialcodia.stockmanagement.pojos;

import com.socialcodia.stockmanagement.models.ModelInvoice;

public class ResponseInvoiceSingle {
    private boolean error;
    private String message;
    private ModelInvoice invoice;

    public ResponseInvoiceSingle(boolean error, String message, ModelInvoice invoice) {
        this.error = error;
        this.message = message;
        this.invoice = invoice;
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

    public ModelInvoice getInvoice() {
        return invoice;
    }

    public void setInvoice(ModelInvoice invoice) {
        this.invoice = invoice;
    }
}
