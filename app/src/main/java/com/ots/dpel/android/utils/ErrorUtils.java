package com.ots.dpel.android.utils;

import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ots.dpel.android.rest.ErrorCodes;

import java.io.IOException;
import java.net.SocketTimeoutException;

import retrofit2.Response;

/**
 * Created by tasos on 10/15/17.
 */

public class ErrorUtils {

    public static final String RESPONSE_ERROR = "RESPONSE ERROR";
    public static final int ERROR_DURATION = 5000; //in ms
    private static ObjectMapper mapper = new ObjectMapper();

    public static void handleResponseError(View rootView, Response<?> response) {
        String error = parseRestResponseError(response);
        int errorMsgResourceId = ErrorCodes.getErrorMessage(error);
        Log.e(RESPONSE_ERROR, error);
        ErrorUtils.showSnackBarError(rootView, errorMsgResourceId);
    }

    public static void handleResponseError(View rootView, Throwable t) {
        String error = parseRestResponseError(t);
        int errorMsgResourceId = ErrorCodes.getErrorMessage(error);
        Log.e(RESPONSE_ERROR, error);
        ErrorUtils.showSnackBarError(rootView, errorMsgResourceId);
    }

    public static String parseRestResponseError(Response<?> response) {
        String error;
        try {
            error = response.errorBody().string();
            JsonNode rootNode = mapper.readTree(error);
            JsonNode errorCodeNode = rootNode.findPath("errorCode");
            error = errorCodeNode.asText();
        } catch (IOException e) {
            error = e.getMessage();
        }
        return error;
    }

    public static String parseLoginError(Response<?> response) {
        String error;
        /*String description;*/
        try {
            error = response.errorBody().string();
            JsonNode rootNode = mapper.readTree(error);

            JsonNode errorCodeNode = rootNode.findPath("error");
            error = errorCodeNode.asText();

            /*JsonNode errorDescriptionNode = rootNode.findPath("error_description");
            description = errorCodeNode.asText();*/
        } catch (IOException e) {
            error = e.getMessage();
        }
        return error;
    }

    public static String parseRestResponseError(Throwable t) {
        String error = "Unexpected Error";
        if (t instanceof SocketTimeoutException) {
            error = "Timeout Exception";
        }
        return error;
    }

    public static void showSnackBarError(View rootView, String errorMsg) {
        Snackbar errorSnackBar = Snackbar.make(rootView, errorMsg, ERROR_DURATION);
        errorSnackBar.show();
    }

    public static void showSnackBarError(View rootView, int errorMsg) {
        Snackbar errorSnackBar = Snackbar.make(rootView, errorMsg, ERROR_DURATION);
        errorSnackBar.show();
    }

}
