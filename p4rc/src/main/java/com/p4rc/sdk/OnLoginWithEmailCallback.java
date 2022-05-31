package com.p4rc.sdk;

/**
 * Created by alla on 5/10/16.
 */
public interface OnLoginWithEmailCallback {

    void onCompleted(String error);
    void onValidationError(String emailError, String passwordError);
}
