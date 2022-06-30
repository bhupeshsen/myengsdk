package com.coderivium.p4rcintegrationsample;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.p4rc.sdk.OnLoginWithEmailCallback;
import com.p4rc.sdk.P4RC;
import com.p4rc.sdk.utils.AppUtils;

public class MainActivity extends AppCompatActivity {

    private int level;
    private int points;
    private int finishStep;
    private AlertDialog dialog;

    private Button p4rcwd,userauthentication;

    private CallbackManager mCallbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        P4RC.getInstance().setContext(this);

        p4rcwd = findViewById(R.id.btn_p4rc_button_without_dialog);
        userauthentication = findViewById(R.id.btn_user_login_level);

        mCallbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = findViewById(R.id.fb_login_button);
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("label", loginResult.getAccessToken().getToken());
                clipboard.setPrimaryClip(clip);
                P4RC.getInstance().loginWithFacebook(loginResult.getAccessToken().getToken(), null);
            }

            @Override
            public void onCancel() {
                Log.e("Login Canceled", "Canceled Facebook Login");
            }

            @Override
            public void onError(FacebookException exception) {
                exception.printStackTrace();
            }
        });
    }

    private void showAuthDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Authentication");
        builder.setMessage("Please enter your email and password");

        final EditText input = new EditText(this);
        input.setHint("Enter Email");
        input.setText("bhupesh.sen@kiwitech.com");
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

        final EditText input2 = new EditText(this);
        input2.setText("Bhupesh11@");
        input2.setHint("Enter Password");
        input2.setInputType(InputType.TYPE_CLASS_TEXT /*| InputType.TYPE_TEXT_VARIATION_PASSWORD*/);

        final LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);
        int dp50 = Utils.dpToPx(20);
        ll.setPadding(dp50, dp50, dp50, dp50);
        ll.addView(input);
        ll.addView(input2);


        builder.setView(ll);
        builder.setCancelable(false);
        builder.setPositiveButton("Login", null);
        builder.setNegativeButton("CANCEL", (dialog, which) -> {
            dialog.cancel();
        });

        final AlertDialog dialog = builder.create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(view -> {
                    String email = input.getText().toString();
                    String password = input2.getText().toString();
//            P4RC.getInstance().userLogin("bhupeshsen11@gmail.com","Bhs11!@#",null);
                    P4RC.getInstance().userLogin(email, password, new OnLoginWithEmailCallback() {
                        @Override
                        public void onCompleted(String error) {
                            if (error != null)
                                Toast.makeText(MainActivity.this, error, Toast.LENGTH_SHORT).show();
                            else
                                dialog.dismiss();
                        }

                        @Override
                        public void onValidationError(String emailError, String passwordError) {
                            if (emailError != null && passwordError != null)
                                Toast.makeText(MainActivity.this, emailError + ", " + passwordError, Toast.LENGTH_SHORT).show();
                            else if (emailError != null)
                                Toast.makeText(MainActivity.this, emailError, Toast.LENGTH_SHORT).show();
                            else if (passwordError != null)
                                Toast.makeText(MainActivity.this, passwordError, Toast.LENGTH_SHORT).show();
                        }
                    });
                });
            }
        });

        dialog.show();
    }

    public void onClick(View v) {
        if (v.getId() == R.id.btn_start_level) {
            P4RC.getInstance().didStartLevel();
        } else if (v.getId() == R.id.btn_finish_level) {
            if (P4RC.getInstance().isLevelStarted()) {
                startLevelFinishDialog().show();
            } else {
                Toast.makeText(this, getResources().getString(R.string.toast_press_start_level),
                        Toast.LENGTH_SHORT).show();
            }
        } else if (v.getId() == R.id.btn_restart_game) {
            P4RC.getInstance().gameWasRestarted();
        } else if (v.getId() == R.id.btn_p4rc_button_without_dialog) {
            if (AppUtils.isOnline(getApplicationContext()))
                p4rcwd.setEnabled(false);
            P4RC.getInstance().showMainP4RCPage();
        }
        else if (v.getId() == R.id.webViewBtn) {
            Intent intent = new Intent(this, WebMyXRSDK.class);
            startActivity(intent);
        }else if (v.getId() == R.id.btn_current_state) {
            P4RC.getInstance().showDescriptiveAlertView();
        } else if (v.getId() == R.id.btn_user_dashboard) {
            Intent intent = new Intent(this, DashboardActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_user_login_level) {
            Intent intent = new Intent(this, P4RC.getInstance().isLoggedIn() ? DashboardActivity.class : LoginActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_logout) {
            P4RC.getInstance().logout();
        }
    }

    private AlertDialog startLevelFinishDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(getResources().getString(R.string.alert_dialog_finished));

        RelativeLayout wrapper = new RelativeLayout(MainActivity.this);
        final TextView text = new TextView(MainActivity.this);
        text.setText(getResources().getString(R.string.dialog_enter_level));
        text.setId(View.generateViewId());
        final EditText input = new EditText(MainActivity.this);
        input.setId(View.generateViewId());
        input.setInputType(InputType.TYPE_CLASS_NUMBER);

        RelativeLayout.LayoutParams textParams = new RelativeLayout.LayoutParams(RelativeLayout.
                LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        textParams.addRule(RelativeLayout.CENTER_HORIZONTAL, text.getId());
        textParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, text.getId());
        wrapper.addView(text, textParams);

        RelativeLayout.LayoutParams inputParams = new RelativeLayout.LayoutParams(RelativeLayout.
                LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        inputParams.addRule(RelativeLayout.BELOW, text.getId());
        inputParams.addRule(RelativeLayout.CENTER_HORIZONTAL, input.getId());

        Button okBtn = new Button(MainActivity.this);
        okBtn.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout
                .LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        okBtn.setId(View.generateViewId());
        okBtn.setText(getResources().getString(R.string.dialog_button_ok));
        okBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (finishStep == 0) {
                    if ("".equals(input.getText().toString())) {
                        return;
                    }
                    finishStep = finishStep + 1;

                    level = input.getText().toString().length() > 10 ? Integer.MAX_VALUE
                            : Integer.parseInt(input.getText().toString());

                    text.setText(getResources().getString(R.string.dialog_enter_point));
                    input.setText("");
                    return;
                } else if (finishStep == 1) {
                    if ("".equals(input.getText().toString())) {
                        return;
                    }
                    finishStep = 0;
                    points = input.getText().toString().length() > 10 ? Integer.MAX_VALUE
                            : Integer.parseInt(input.getText().toString());
                    P4RC.getInstance().didCompleteLevelWithPoints(level, points);
                    level = 0;
                    points = 0;
                    dialog.dismiss();
                }
            }
        });
        Button cancelBtn = new Button(MainActivity.this);
        cancelBtn.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout
                .LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        cancelBtn.setText(getResources().getString(R.string.dialog_button_cancel));
        cancelBtn.setId(View.generateViewId());
        cancelBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        RelativeLayout.LayoutParams okBtnPar = new RelativeLayout.LayoutParams(RelativeLayout.
                LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        okBtnPar.addRule(RelativeLayout.ALIGN_PARENT_LEFT, okBtn.getId());
        okBtnPar.addRule(RelativeLayout.BELOW, input.getId());

        RelativeLayout.LayoutParams cancelBtnPar = new RelativeLayout.LayoutParams(RelativeLayout.
                LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        cancelBtnPar.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, cancelBtn.getId());
        cancelBtnPar.addRule(RelativeLayout.BELOW, input.getId());

        wrapper.addView(input, inputParams);
        wrapper.addView(okBtn, okBtnPar);
        wrapper.addView(cancelBtn, cancelBtnPar);
        builder.setView(wrapper);

        dialog = builder.create();
        return dialog;
    }

    @Override
    protected void onResume() {
        super.onResume();
        p4rcwd.setEnabled(true);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }
}