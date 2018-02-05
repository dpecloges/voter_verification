package com.ots.dpel.android;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.ots.dpel.android.SecurityManager.LoginCallback;
import com.ots.dpel.android.rest.DpelApiService;
import com.ots.dpel.android.rest.dto.Token;
import com.ots.dpel.android.utils.FileUtils;

import java.io.File;
import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();
    private TextInputEditText editTextUsername;
    private TextInputEditText editTextPassword;
    private TextInputLayout textInputLayoutUserName;
    private TextInputLayout textInputLayoutPassword;
    private SecurityManager securityManager;
    private ProgressBar progressBar;
    private Call<ResponseBody> checkVersionCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        securityManager = new SecurityManager(this);

        if (securityManager.isTokenValid()) {
            startMainActivity();
            finish();
        }

        setContentView(R.layout.activity_login);

        textInputLayoutUserName = (TextInputLayout) findViewById(R.id.textInputLayoutUserName);
        editTextUsername = (TextInputEditText) findViewById(R.id.editTextUserName);
        textInputLayoutPassword = (TextInputLayout) findViewById(R.id.textInputLayoutPassword);
        editTextPassword = (TextInputEditText) findViewById(R.id.editTextPassword);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        Button loginButton = (Button) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateForm()) {
                    doLogin();
                }
            }
        });
    }

    private boolean validateForm() {
        boolean isValid = true;
        if (textInputLayoutUserName.getEditText().length() == 0) {
            textInputLayoutUserName.setError(getString(R.string.verification_login_enter_username));
            isValid = false;
        } else {
            textInputLayoutUserName.setError(null);
            textInputLayoutUserName.setErrorEnabled(false);
        }

        if (textInputLayoutPassword.getEditText().length() == 0) {
            textInputLayoutPassword.setError(getString(R.string.verification_login_enter_password));
            isValid = false;
        } else {
            textInputLayoutPassword.setError(null);
            textInputLayoutPassword.setErrorEnabled(false);
        }

        return isValid;
    }

    private void doLogin() {
        showProgressBar(true);

        String username = editTextUsername.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (username != null && !username.isEmpty() && password != null && !password.isEmpty()) {

            securityManager.doAsyncLogin(getBaseUrl(), username, password, new LoginCallback() {
                @Override
                public void onLoginSuccess(Token token) {
                    showProgressBar(false);
                    if (securityManager.isTokenValid()) {
                        startMainActivity();
                        finish();
                    } else {
                        showError(getString(R.string.token_not_valid));
                    }
                }

                @Override
                public void onLoginFailure(String loginError) {
                    showProgressBar(false);
                    String error;
                    if ("unauthorized".equals(loginError)) {
                        error = getString(R.string.login_failure_unauthorized);
                    } else if ("invalid_grant".equals(loginError)) {
                        error = getString(R.string.login_failure_invalid_grant);
                    } else {
                        error = loginError;
                    }
                    showError(error);
                }
            });

        }
    }

    private void showError(String errorMsg) {
        Snackbar errorSnackBar = Snackbar.make(findViewById(R.id.rootView), errorMsg, Snackbar.LENGTH_LONG);
        errorSnackBar.show();
    }

    private void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void showProgressBar(boolean show) {
        if (show) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    private String getBaseUrl() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String baseUrl = sharedPref.getString(SettingsFragment.KEY_API_URL, "");
        return baseUrl;
    }

    private String getUpdateUrl() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String baseUrl = sharedPref.getString(SettingsFragment.KEY_UPDATE_URL, getString(R.string.default_update_url));
        return baseUrl;
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkForUpdates();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cancelPendingCalls();
    }

    /**
     * This method will cancel all pending retrofit calls. It is needed to avoid cases
     * where the result comes back to a context (Activity, Fragment) that is not active anymore
     * causing NPEs.
     */
    private void cancelPendingCalls() {
        if (securityManager != null) {
            securityManager.cancelLogin();
        }
        if (checkVersionCall != null) {
            checkVersionCall.cancel();
        }
    }

    private void checkForUpdates() {
        Log.d(TAG, "Checking for updates on " + getUpdateUrl());
        DpelApiService dpelApiService = new Retrofit.Builder().baseUrl(getUpdateUrl()).build().create(DpelApiService.class);
        checkVersionCall = dpelApiService.checkVersion();
        checkVersionCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    String updateFileName = parseUpdateFileName(response);
                    if (updateFileName != null) {
                        executeUpdate(updateFileName);
                    }
                } else {
                    try {
                        Log.d(TAG, response.errorBody().string());
                    } catch (IOException e) {
                        Log.e(TAG, e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(TAG, t.getMessage());
            }
        });

    }

    private void executeUpdate(final String updateFileName) {
        String updateVersion = FileUtils.getUpdateFileVersion(updateFileName);
        String currentVersion = FileUtils.getUpdateFileVersion(BuildConfig.VERSION_NAME);
        Log.d(TAG, "Current version is " + currentVersion + " - Update version is " + updateVersion);
        if (FileUtils.checkVersionEquality(updateVersion, currentVersion) == 1) {
            showUpdateDialog(updateVersion, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    // perform update
                    downloadAndUpdate(updateFileName);
                }
            });
        } else {
            Log.d(TAG, "No need to update");
        }
    }

    private void showUpdateDialog(String version, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(version)
                .setMessage(R.string.update_new_version_available)
                .setNegativeButton(R.string.No, null)
                .setPositiveButton(R.string.Yes, listener);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private String parseUpdateFileName(Response<ResponseBody> response) {
        try {
            return response.body().string();
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
        return null;
    }

    private void downloadAndUpdate(String updateFileName) {
        showProgressBar(true);
        UpdateApkTask updateApkTask = new UpdateApkTask();
        updateApkTask.setContext(getApplicationContext(), new UpdateApkTask.ProgressListener() {
            @Override
            public void onUpdate(int progress) {
                // do nothing
            }

            @Override
            public void onDownloadComplete(File outputFile) {
                showProgressBar(false);
                if (outputFile != null) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.fromFile(outputFile), "application/vnd.android.package-archive");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // without this flag android returned a intent error!
                    startActivity(intent);
                }
            }
        });
        updateApkTask.execute(getUpdateUrl() + updateFileName);
    }

}
