package com.ots.dpel.android.rest;

import com.ots.dpel.android.R;

/**
 * Created by tasos on 10/15/17.
 */

public enum ErrorCodes {

    ERROR_VERIFY_NO_RECORD_FOUND("NO_RECORD_FOUND"),
    ERROR_VERIFY_MULTIPLE_RECORDS_FOUND("MULTIPLE_RECORDS_FOUND"),
    ERROR_VERIFY_ILLEGAL_ARGS("ILLEGAL_ARGS"),
    ERROR_VERIFY_INVALID_ARGS_PERSONAL_INFO("INVALID_ARGS_PERSONAL_INFO"),
    ERROR_VERIFY_INVALID_ARGS_EKLSPECIALNO("INVALID_ARGS_EKLSPECIALNO"),
    ERROR_SAVEVOTER_CREATE_DUPLICATE_VERIFICATION_NUMBER("CREATE_DUPLICATE_VERIFICATION_NUMBER"),
    ERROR_SAVEVOTER_CREATE_DEPARTMENT_HAS_SUBMITTED("CREATE_DEPARTMENT_HAS_SUBMITTED"),
    ERROR_SAVEVOTER_CREATE_ALREADY_VOTED("CREATE_ALREADY_VOTED"),
    ERROR_SAVEVOTER_CREATE_SECOND_VERIFY_ANY_ELECTOR_REJECT("CREATE_SECOND_VERIFY_ANY_ELECTOR_REJECT"),
    ERROR_SAVEVOTER_CREATE_DEPARTMENT_HAS_NOT_STARTED("CREATE_DEPARTMENT_HAS_NOT_STARTED"),
    ERROR_SAVEVOTER_CREATE_DEPARTMENT_HAS_ENDED("CREATE_DEPARTMENT_HAS_ENDED"),
    ERROR_UNDOVOTE_DEPARTMENT_HAS_SUBMITTED("DEPARTMENT_HAS_SUBMITTED"),
    ERROR_RESULTS_SUBMITTED("RESULTS_SUBMITTED"),
    ERROR_RESULTS_TOTAL_SUM_ERROR("TOTAL_SUM_ERROR"),
    ERROR_RESULTS_CANDIDATE_SUM_ERROR("CANDIDATE_SUM_ERROR"),
    ERROR_RESULTS_INCONSISTENT_VOTES("INCONSISTENT_VOTES"),
    ERROR_RESULTS_DEPARTMENT_HAS_NOT_ENDED("DEPARTMENT_HAS_NOT_ENDED");

    public final String errorCode;

    ErrorCodes(String errorCode) {
        this.errorCode = errorCode;
    }


    public static int getErrorMessage(String code) {
        if (ERROR_VERIFY_NO_RECORD_FOUND.errorCode.equals(code)) {
            return R.string.error_verify_no_record_found;
        }
        if (ERROR_VERIFY_MULTIPLE_RECORDS_FOUND.errorCode.equals(code)) {
            return R.string.error_multiple_record_found;
        }
        if (ERROR_VERIFY_ILLEGAL_ARGS.errorCode.equals(code)) {
            return R.string.error_verify_illegal_args;
        }
        if (ERROR_VERIFY_INVALID_ARGS_PERSONAL_INFO.errorCode.equals(code)) {
            return R.string.error_verify_illegal_args_personal_info;
        }
        if (ERROR_VERIFY_INVALID_ARGS_EKLSPECIALNO.errorCode.equals(code)) {
            return R.string.error_verify_illegal_args_ekl_no;
        }
        if (ERROR_RESULTS_SUBMITTED.errorCode.equals(code)) {
            return R.string.error_results_already_submitted;
        }
        if (ERROR_RESULTS_TOTAL_SUM_ERROR.errorCode.equals(code)) {
            return R.string.error_results_total_sum_error;
        }
        if (ERROR_RESULTS_CANDIDATE_SUM_ERROR.errorCode.equals(code)) {
            return R.string.error_results_candidate_sum_error;
        }
        if (ERROR_SAVEVOTER_CREATE_DUPLICATE_VERIFICATION_NUMBER.errorCode.equals(code)) {
            return R.string.error_save_voter_duplicate_verification_number;
        }
        if (ERROR_SAVEVOTER_CREATE_DEPARTMENT_HAS_SUBMITTED.errorCode.equals(code)) {
            return R.string.error_save_voter_department_results_submitted;
        }
        if (ERROR_UNDOVOTE_DEPARTMENT_HAS_SUBMITTED.errorCode.equals(code)) {
            return R.string.error_undo_vote_department_results_submitted;
        }
        if (ERROR_SAVEVOTER_CREATE_ALREADY_VOTED.errorCode.equals(code)) {
            return R.string.error_save_voter_already_voted;
        }
        if (ERROR_RESULTS_INCONSISTENT_VOTES.errorCode.equals(code)) {
            return R.string.error_results_inconsistent_votes;
        }
        if (ERROR_SAVEVOTER_CREATE_SECOND_VERIFY_ANY_ELECTOR_REJECT.errorCode.equals(code)) {
            return R.string.error_save_voter_verify_any_elector_reject;
        }
        if (ERROR_RESULTS_DEPARTMENT_HAS_NOT_ENDED.errorCode.equals(code)) {
            return R.string.error_results_department_has_not_ended;
        }
        if (ERROR_SAVEVOTER_CREATE_DEPARTMENT_HAS_NOT_STARTED.errorCode.equals(code)) {
            return R.string.error_save_voter_department_has_not_started;
        }
        if (ERROR_SAVEVOTER_CREATE_DEPARTMENT_HAS_ENDED.errorCode.equals(code)) {
            return R.string.error_save_voter_department_has_ended;
        }

        return R.string.general_error;
    }

}
