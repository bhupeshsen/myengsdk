package com.p4rc.sdk;

/**
 * Created by alla on 5/10/16.
 */
public interface OnSignupWithEmailCallback {

    void onCompleted(String error);

    void onValidationError(String firstNameError, String lastNameError, String emailError, String passwordError, String dobError);
}
