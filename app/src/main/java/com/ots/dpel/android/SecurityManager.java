package com.ots.dpel.android;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.auth0.android.jwt.Claim;
import com.auth0.android.jwt.DecodeException;
import com.auth0.android.jwt.JWT;
import com.ots.dpel.android.rest.DpelApiService;
import com.ots.dpel.android.rest.DpelApiServiceFactory;
import com.ots.dpel.android.rest.dto.Token;
import com.ots.dpel.android.utils.ErrorUtils;

import okhttp3.Credentials;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by tasos on 12/10/2017.
 */

public class SecurityManager {

    private static final String TAG = SecurityManager.class.getName();
    private final Context context;
    private static String electionProcedureRound;
    private static String userFullName;
    private static String electionDepartmentName;
    private static String electionDepartmentDisplayName;
    private static String electionCenterName;
    private static String electionCenterDisplayName;
    private static long electionCenterId;
    private static long electionProcedureId;
    private static long electionDepartmentId;
    private static String[] userAuthorities;
    private boolean working;
    private Call<Token> loginCall;

    public SecurityManager(Context context) {
        this.context = context;
    }

    public void doAsyncLogin(String baseUrl, String username, String password, final LoginCallback callBack) {

        if (!working) {
            working = true;
            DpelApiService dpelApiService = DpelApiServiceFactory.createDpelApiService(baseUrl).create(DpelApiService.class);
            String auth = Credentials.basic(Constants.CLIENT_USERNAME, Constants.CLIENT_PASSWORD);

            loginCall = dpelApiService.login(auth, "password", username, password);
            loginCall.enqueue(new Callback<Token>() {
                @Override
                public void onResponse(Call<Token> call, Response<Token> response) {
                    working = false;
                    if (response.isSuccessful()) {
                        Token token = response.body();
                        saveToken(token.getAccessToken());
                        if (callBack != null) {
                            callBack.onLoginSuccess(token);
                        }
                    } else {
                        String loginError = ErrorUtils.parseLoginError(response);
                        if (callBack != null) {
                            callBack.onLoginFailure(loginError);
                        }
                    }

                }

                @Override
                public void onFailure(Call<Token> call, Throwable t) {
                    working = false;
                    if (!call.isCanceled()) {
                        if (callBack != null) {
                            callBack.onLoginFailure(t.getMessage());
                        }
                    }
                }
            });
        }
    }

    public void cancelLogin() {
        if (loginCall != null) {
            loginCall.cancel();
        }
    }

    public String getElectionCenterName() {
        return electionCenterName;
    }

    public String getElectionCenterDisplayName() {
        return electionCenterDisplayName;
    }

    public String getElectionDepartmentDisplayName() {
        return electionDepartmentDisplayName;
    }

    public String getElectionDepartmentName() {
        return electionDepartmentName;
    }

    public long getLoggedUserElectionDepartmentId() {
        return electionDepartmentId;
    }

    public String getLoggedUserFullName() {
        return userFullName;
    }

    public long getCurrentElectionProcedureId() {
        return electionProcedureId;
    }

    public String getCurrentElectionProcedureRound() {
        return electionProcedureRound;
    }

    public long getElectionCenterId() {
        return electionCenterId;
    }

    public boolean hasPermission(String permission) {
        for (String authority : userAuthorities) {
            if (authority.equals(permission)) {
                return true;
            }
        }
        return false;
    }

    public boolean isChairman() {
        if (hasPermission("ep.voter.undo") && hasPermission("rs.submission")) {
            return true;
        }
        return false;
    }

    public boolean hasPermissionVerifyVoter() {
        if (hasPermission("ep.verification")) {
            return true;
        }
        return false;
    }

    public boolean hasPermissionUndoVote() {
        if (hasPermission("ep.voter.undo")) {
            return true;
        }
        return false;
    }

    public boolean hasPermissionSubmitResults() {
        if (hasPermission("rs.submission")) {
            return true;
        }
        return false;
    }

    public boolean hasPermissionTimer() {
        if (hasPermission("ep.timer")) {
            return true;
        }
        return false;
    }

    private boolean validateClaims(JWT jwt) {
        Claim claim = jwt.getClaim("electionProcedureRound");
        if (claim.asString() == null) {
            return false;
        }
        electionProcedureRound = claim.asString();

        claim = jwt.getClaim("electionProcedureId");
        if (claim.asInt() == null) {
            return false;
        }
        electionProcedureId = claim.asInt();

        claim = jwt.getClaim("fullName");
        if (claim.asString() == null) {
            return false;
        }
        userFullName = claim.asString();

        claim = jwt.getClaim("electionDepartmentId");
        if (claim.asInt() == null) {
            return false;
        }
        electionDepartmentId = claim.asInt();

        claim = jwt.getClaim("electionDepartmentName");
        if (claim.asString() == null) {
            return false;
        }
        electionDepartmentName = claim.asString();

        claim = jwt.getClaim("electionDepartmentDisplayName");
        if (claim.asString() == null) {
            return false;
        }
        electionDepartmentDisplayName = claim.asString();

        claim = jwt.getClaim("electionCenterName");
        if (claim.asString() == null) {
            return false;
        }
        electionCenterName = claim.asString();

        claim = jwt.getClaim("electionCenterDisplayName");
        if (claim.asString() == null) {
            return false;
        }
        electionCenterDisplayName = claim.asString();

        claim = jwt.getClaim("electionCenterId");
        if (claim.asInt() == null) {
            return false;
        }
        electionCenterId = claim.asInt();

        claim = jwt.getClaim("authorities");
        if (claim.asArray(String.class) == null) {
            return false;
        }
        userAuthorities = claim.asArray(String.class);

        return true;
    }

    public boolean isTokenValid() {
        String token = loadToken();
        if (token == null) {
            Log.d(TAG, "Token does not exist in storage");
            // token does not exist in storage
            return false;
        } else {
            // check token for validity and if it has expired
            JWT jwt = null;
            try {
                jwt = new JWT(token);
                if (!validateClaims(jwt)) {
                    return false;
                }
            } catch (DecodeException e) {
                Log.e(TAG, e.getMessage());
                return false;
            }
            return !jwt.isExpired(1);
        }
    }

    public String getToken() {
        // FIXME: 16/11/2017 maybe throw exception
        return loadToken();
    }

    private String loadToken() {
        SharedPreferences accessPreferences = context.getSharedPreferences("security", 0);
        return accessPreferences.getString("access_token", null);
    }


    private void saveToken(String token) {
        SharedPreferences accessPreferences = context.getSharedPreferences("security", 0);
        SharedPreferences.Editor editor = accessPreferences.edit();
        editor.putString("access_token", token);
        editor.apply();
    }

    public void invalidateToken() {
        SharedPreferences accessPreferences = context.getSharedPreferences("security", 0);
        SharedPreferences.Editor editor = accessPreferences.edit();
        editor.remove("access_token");
        editor.apply();
    }

    interface LoginCallback {
        void onLoginSuccess(Token token);

        void onLoginFailure(String loginError);
    }

}
