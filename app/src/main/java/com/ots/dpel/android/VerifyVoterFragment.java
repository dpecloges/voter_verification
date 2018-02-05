package com.ots.dpel.android;


import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.ots.dpel.android.rest.DpelApiService;
import com.ots.dpel.android.rest.DpelApiServiceFactory;
import com.ots.dpel.android.rest.dto.VerificationArgs;
import com.ots.dpel.android.rest.dto.VerificationDto;
import com.ots.dpel.android.utils.ErrorUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VerifyVoterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VerifyVoterFragment extends Fragment {

    private final String TAG = VerifyVoterFragment.class.getName();
    private TextInputLayout textInputLayoutElectoralNum;
    private TextInputLayout textInputLayoutSurname;
    private TextInputLayout textInputLayoutFirstName;
    private TextInputLayout textInputLayoutFatherName;
    private TextInputLayout textInputLayoutMotherName;
    private TextInputLayout textInputLayoutYearOfBirth;
    private Button searchButton;
    private ProgressBar progressBar;
    private SearchButtonClickedListener searchButtonClickedListener;
    private VerifyVoterResultListener verifyResultListener;
    private View rootView;
    private TextInputEditText editTextElectoralNum;
    private TextInputEditText editTextSurname;
    private TextInputEditText editTextName;
    private TextInputEditText editTextFatherName;
    private TextInputEditText editTextMotherName;
    private TextInputEditText editTextYearOfBirth;
    private TextInputLayout textInputLayoutVerificationNumber;
    private TextInputEditText editTextVerificationNumber;
    private Call<VerificationDto> verificationCall;

    public VerifyVoterFragment() {
        // Required empty public constructor
    }

    public static VerifyVoterFragment newInstance() {
        return new VerifyVoterFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_verify, container, false);

        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);

        //TextInputLayouts referenced for validation annotations
        textInputLayoutElectoralNum = rootView.findViewById(R.id.textInputLayoutElectoralNum);
        textInputLayoutSurname = rootView.findViewById(R.id.textInputLayoutSurname);
        textInputLayoutFirstName = rootView.findViewById(R.id.textInputLayoutFirstName);
        textInputLayoutFatherName = rootView.findViewById(R.id.textInputLayoutFatherName);
        textInputLayoutMotherName = rootView.findViewById(R.id.textInputLayoutMotherName);
        textInputLayoutYearOfBirth = rootView.findViewById(R.id.textInputLayoutYearOfBirth);
        textInputLayoutVerificationNumber = rootView.findViewById(R.id.textInputLayoutVerificationNum);

        editTextElectoralNum = (TextInputEditText) rootView.findViewById(R.id.editTextElectoralNum);
        editTextSurname = (TextInputEditText) rootView.findViewById(R.id.editTextSurname);
        editTextName = (TextInputEditText) rootView.findViewById(R.id.editTextName);
        editTextFatherName = (TextInputEditText) rootView.findViewById(R.id.editTextFatherName);
        editTextMotherName = (TextInputEditText) rootView.findViewById(R.id.editTextMotherName);
        editTextYearOfBirth = (TextInputEditText) rootView.findViewById(R.id.editTextYearOfBirth);
        editTextVerificationNumber = (TextInputEditText) rootView.findViewById(R.id.editTextVerificationNum);

        searchButton = (Button) rootView.findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateFields()) {
                    doVoterSearch();
                }
            }
        });

        Button clearButton = (Button) rootView.findViewById(R.id.clearFormButton);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearFields();
            }
        });

        // Inflate the layout for this fragment
        return rootView;
    }

    private boolean validateFields() {
        if (textInputLayoutElectoralNum.getEditText().length() > 0) {
            if (validateElectoralNumber()) {
                return true;
            } else {
                textInputLayoutElectoralNum.setError(getText(R.string.validation_ekl_num_length));
                return false;
            }
        } else if (textInputLayoutVerificationNumber.getEditText().length() > 0) {
            if (validateVerificationNumber()) {
                return true;
            } else {
                textInputLayoutVerificationNumber.setError(getText(R.string.validation_verification_num));
                return false;
            }
        } else {
            return validateSurname(textInputLayoutSurname) &
                    validateMinTwoChars(textInputLayoutFirstName) &
                    validateMinTwoChars(textInputLayoutFatherName) &
                    validateMinTwoChars(textInputLayoutMotherName) &
                    validateYearOfBirth(textInputLayoutYearOfBirth);
        }
    }

    private boolean validateVerificationNumber() {
        // We placed contraints, numeric and size on the edittext through properties
        return true;
    }

    private boolean validateSurname(TextInputLayout textInputLayout) {
        if (textInputLayout.getEditText().length() > 0) {
            textInputLayout.setError(null);
            textInputLayout.setErrorEnabled(false);
            return true;
        }
        textInputLayout.setError(getText(R.string.validation_surname));
        return false;
    }

    private boolean validateYearOfBirth(TextInputLayout textInputLayout) {
        if (textInputLayout.getEditText().length() == 4) {
            textInputLayout.setError(null);
            textInputLayout.setErrorEnabled(false);
            return true;
        }
        textInputLayout.setError(getText(R.string.validation_year_min_length_4));
        return false;
    }

    private boolean validateElectoralNumber() {
        TextInputLayout field = textInputLayoutElectoralNum;
        if (field.getEditText().length() == 0 || field.getEditText().length() <= 13) {
            field.setError(null);
            field.setErrorEnabled(false);
            return true;
        }
        field.setError(getText(R.string.validation_ekl_num_length));
        return false;
    }

    public boolean validateMinTwoChars(TextInputLayout textInputLayout) {
        if (textInputLayout.getEditText().length() >= 2) {
            textInputLayout.setError(null);
            textInputLayout.setErrorEnabled(false);
            return true;
        }
        textInputLayout.setError(getText(R.string.validation_at_least_3_chars));
        return false;
    }

    private void clearFields() {
        editTextElectoralNum.getText().clear();
        textInputLayoutElectoralNum.setError(null);
        textInputLayoutElectoralNum.setErrorEnabled(false);

        editTextVerificationNumber.getText().clear();
        textInputLayoutVerificationNumber.setError(null);
        textInputLayoutVerificationNumber.setErrorEnabled(false);

        editTextSurname.getText().clear();
        textInputLayoutSurname.setError(null);
        textInputLayoutSurname.setErrorEnabled(false);

        editTextName.getText().clear();
        textInputLayoutFirstName.setError(null);
        textInputLayoutFirstName.setErrorEnabled(false);

        editTextFatherName.getText().clear();
        textInputLayoutFatherName.setError(null);
        textInputLayoutFatherName.setErrorEnabled(false);

        editTextMotherName.getText().clear();
        textInputLayoutMotherName.setError(null);
        textInputLayoutMotherName.setErrorEnabled(false);

        editTextYearOfBirth.getText().clear();
        textInputLayoutYearOfBirth.setError(null);
        textInputLayoutYearOfBirth.setErrorEnabled(false);

    }

    private VerificationArgs createVerificationArgsFromFormData() {
        VerificationArgs args = new VerificationArgs();

        String eklSpecialNum = editTextElectoralNum.getText().toString();
        String verificationNum = editTextVerificationNumber.getText().toString();

        if (!eklSpecialNum.isEmpty()) {
            //search by eklogiko arithmo
            args.setEklSpecialNo(eklSpecialNum);
        } else if (!verificationNum.isEmpty()) {
            // search by verification number
            try {
                args.setVoterVerificationNumber(Integer.parseInt(verificationNum));
            } catch (NumberFormatException e) {
                Log.d(TAG, e.getMessage());
            }
        } else {
            // search by personal info
            args.setLastName(editTextSurname.getText().toString());
            args.setFirstName(editTextName.getText().toString());
            args.setFatherFirstName(editTextFatherName.getText().toString());
            args.setMotherFirstName(editTextMotherName.getText().toString());
            String dobStr = editTextYearOfBirth.getText().toString();
            if (dobStr != null && !dobStr.isEmpty()) {
                args.setBirthYear(Integer.parseInt(dobStr));
            }
        }
        return args;
    }

    private void doVoterSearch() {
        showProgressBar(true);

        VerificationArgs args = createVerificationArgsFromFormData();

        DpelApiService dpelApiService = DpelApiServiceFactory.createDpelApiService(MainActivity.baseUrl).create(DpelApiService.class);
        verificationCall = dpelApiService.verify("Bearer " + MainActivity.accessToken, args);
        verificationCall.enqueue(new Callback<VerificationDto>() {

            @Override
            public void onResponse(Call<VerificationDto> call, Response<VerificationDto> response) {
                showProgressBar(false);
                if (response.isSuccessful()) {
                    VerificationDto verificationDto = response.body();
                    if (verifyResultListener != null) {
                        verifyResultListener.onFindVoterResultSuccess(verificationDto);
                    }
                } else {
                    ErrorUtils.handleResponseError(rootView.findViewById(R.id.frameLayout), response);
                }
            }

            @Override
            public void onFailure(Call<VerificationDto> call, Throwable t) {
                if (!call.isCanceled()) {
                    showProgressBar(false);
                    ErrorUtils.showSnackBarError(rootView.findViewById(R.id.frameLayout), t.getMessage());
                }
            }
        });
    }

    private void showProgressBar(boolean show) {
        if (show) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    public void setVerifyResultListener(VerifyVoterResultListener listener) {
        this.verifyResultListener = listener;
    }

    public void setSearchButtonClickedListener(SearchButtonClickedListener listener) {
        this.searchButtonClickedListener = listener;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        cancelPendingCalls();
    }

    /**
     * This method will cancel all pending retrofit calls. It is needed to avoid cases
     * where the result comes back to a context (Activity, Fragment) that is not active anymore
     * causing NPEs.
     */
    private void cancelPendingCalls() {
        if (verificationCall != null) {
            verificationCall.cancel();
        }
    }

    interface VerifyVoterResultListener {
        void onFindVoterResultSuccess(VerificationDto verificationDto);
    }

    interface SearchButtonClickedListener {
        void onSearchButtonClickedEvent();
    }

}
