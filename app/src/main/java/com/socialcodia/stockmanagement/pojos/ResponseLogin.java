package com.socialcodia.stockmanagement.pojos;

import com.socialcodia.stockmanagement.models.ModelUser;

public class ResponseLogin {
    private boolean error;
    private String message;
    private ModelUser user;

    public ResponseLogin(boolean error, String message, ModelUser user) {
        this.error = error;
        this.message = message;
        this.user = user;
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

    public ModelUser getUser() {
        return user;
    }

    public void setUser(ModelUser user) {
        this.user = user;
    }
}
