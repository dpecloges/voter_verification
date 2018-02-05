package com.ots.dpel.android;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fasterxml.jackson.databind.JsonNode;
import com.ots.dpel.android.rest.DpelApiService;
import com.ots.dpel.android.rest.DpelApiServiceFactory;
import com.ots.dpel.android.rest.dto.AttachmentType;
import com.ots.dpel.android.rest.dto.CandidateDto;
import com.ots.dpel.android.rest.dto.ResultsDto;
import com.ots.dpel.android.utils.ErrorUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

public class ElectionDataFragment extends Fragment {

    private static final String TAG = ElectionDataFragment.class.getName();
    List<EditText> candidateEditTextList = new ArrayList<>();
    private ProgressBar progressBar;
    private TextInputEditText editTextTotal;
    private TextView textViewValid;
    private TextInputEditText editTextInvalid;
    private TextInputEditText editTextNeutral;
    private TextView textViewCandidateTotal;
    private View rootView;
    private ListView candidatesListView;
    private int candidateListSize;
    private Uri photoURI;
    private File photoFile;
    private CheckBox checkboxAttachResultsPhoto;
    private TextView textViewTotalVerifications;
    private List<CandidateDto> candidateDtoList;
    private boolean allowInconsistentSubmission;
    private int totalVerifications;
    private CheckBox checkboxAttachCashierPhoto;
    private Call<List<CandidateDto>> getAllCandidatesCall;
    private Call<ResultsDto> sendResultsCall;
    private Call<JsonNode> uploadFileCall;
    private Call<JsonNode> retrieveVerificationsCall;
    private int inconsistentSubmissionLimit;
    private ElectionProcessStateListener electionStateListener;


    public ElectionDataFragment() {
        // Required empty public constructor
    }

    public static ElectionDataFragment newInstance() {
        return new ElectionDataFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_election_data, container, false);

        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);

        // Sums
        editTextTotal = (TextInputEditText) rootView.findViewById(R.id.editTxtTotal);
        editTextTotal.requestFocus();
        editTextTotal.addTextChangedListener(new VoteChangeTextWatcher());
        editTextInvalid = (TextInputEditText) rootView.findViewById(R.id.editTxtInvalid);
        editTextInvalid.addTextChangedListener(new VoteChangeTextWatcher());
        editTextNeutral = (TextInputEditText) rootView.findViewById(R.id.editTxtNeutral);
        editTextNeutral.addTextChangedListener(new VoteChangeTextWatcher());

        textViewValid = (TextView) rootView.findViewById(R.id.textViewValid);
        textViewCandidateTotal = (TextView) rootView.findViewById(R.id.textViewCandidateTotal);

        candidatesListView = (ListView) rootView.findViewById(R.id.candidatesListView);
        candidatesListView.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                return (event.getAction() == MotionEvent.ACTION_MOVE);
            }
        });

        Button buttonSendResults = (Button) rootView.findViewById(R.id.buttonSendResults);
        buttonSendResults.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MainActivity.canStartStopProcess && checkProcedureHasStarted() && !checkProcedureHasEnded()) {
                    showStopProcessDialog(new YesNoDialogResponseListener() {
                        @Override
                        public void onPositive() {
                            stopElectionProcess();
                            validateAndSubmitResults();
                        }

                        @Override
                        public void onNegative() {
                            //do nothing
                        }
                    });
                } else {
                    validateAndSubmitResults();
                }
            }
        });

        // Attach results photo image action
        Button attachResultsPhoto = (Button) rootView.findViewById(R.id.attachResultsPhoto);
        attachResultsPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent(AttachmentType.RESULTS);
            }
        });
        checkboxAttachResultsPhoto = (CheckBox) rootView.findViewById(R.id.checkboxAttachResultsPhoto);

        // Attach cashier's photo image action
        Button attachCashierPhoto = (Button) rootView.findViewById(R.id.attachCashierPhoto);
        attachCashierPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent(AttachmentType.CASHIER);
            }
        });
        checkboxAttachCashierPhoto = (CheckBox) rootView.findViewById(R.id.checkboxAttachCashierPhoto);

        textViewTotalVerifications = (TextView) rootView.findViewById(R.id.textViewVerificationTotal);

        return rootView;
    }

    private void stopElectionProcess() {
        if (electionStateListener != null) {
            electionStateListener.onStopProcess();
        }
    }

    private void showStopProcessDialog(final YesNoDialogResponseListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.dialog_auto_stop_title)
                .setMessage(R.string.dialog_auto_stop_process)
                .setPositiveButton(R.string.Yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (listener != null) {
                            listener.onPositive();
                        }
                    }
                })
                .setNegativeButton(R.string.No, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (listener != null) {
                            listener.onNegative();
                        }
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void validateAndSubmitResults() {

        if (!validateCandidateTotalVotes() || !validateValidVotes()) {
            showVerificationErrorDialog();
        } else if (!allowInconsistentSubmission && (!validateCandidateTotalVotes() || !validateValidVotes() || !validateTotalEqualVerifications())) {
            showIncosistentErrorDialog();
        } else {
            DialogFragment dialog = new UploadResultVerificationDialogFragment(new UploadResultVerificationDialogFragment.VerificationDialogListener() {
                @Override
                public void onDialogVerifyOk() {
                    uploadResults();
                }
            });
            dialog.show(getChildFragmentManager(), "VerificationDialogFragment");
        }

    }

    private void showIncosistentErrorDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.error_results_inconsistent_votes)
                .setPositiveButton(R.string.OK, null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onStart() {
        super.onStart();
        retrieveResultSubmissionParams();
        getCandidates();
    }

    private int getTotalVoters() {
        String value = editTextTotal.getText().toString();
        if (value.isEmpty()) {
            return 0;
        }
        return Integer.valueOf(value);
    }

    private int getInvalidVotes() {
        String value = editTextInvalid.getText().toString();
        if (value.isEmpty()) {
            return 0;
        }
        return Integer.valueOf(value);
    }

    private int getNeutralVotes() {
        String value = editTextNeutral.getText().toString();
        if (value.isEmpty()) {
            return 0;
        }
        return Integer.valueOf(value);
    }

    private void setValidVotes(int validVotes) {
        textViewValid.setText(String.valueOf(validVotes));
    }

    private void setCandidateTotal(int candidateTotal) {
        textViewCandidateTotal.setText(String.valueOf(candidateTotal));
    }

    private void setResultsPhotoAttachmentCheck(boolean check) {
        checkboxAttachResultsPhoto.setChecked(check);
    }

    private void setCashierPhotoAttachmentCheck(boolean check) {
        checkboxAttachCashierPhoto.setChecked(check);
    }

    private void getCandidates() {
        // We check if the candidates list has been loaded before. There is the case of attaching a photo thus loading a camera activity
        // where the fragment goes to onStart() again but we don't want to reload the candidates list therefore losing user data.
        if (this.candidateDtoList == null) {
            showProgressBar(true);
            DpelApiService dpelApiService = DpelApiServiceFactory.createDpelApiService(MainActivity.baseUrl).create(DpelApiService.class);
            getAllCandidatesCall = dpelApiService.getCandidatesForElectionProcedure("Bearer " + MainActivity.accessToken);
            getAllCandidatesCall.enqueue(new Callback<List<CandidateDto>>() {
                @Override
                public void onResponse(Call<List<CandidateDto>> call, Response<List<CandidateDto>> response) {
                    showProgressBar(false);
                    if (response.isSuccessful()) {
                        displayCandidates(response.body());
                    } else {
                        ErrorUtils.handleResponseError(rootView.findViewById(R.id.frameLayout), response);
                    }
                }

                @Override
                public void onFailure(Call<List<CandidateDto>> call, Throwable t) {
                    if (!call.isCanceled()) {
                        showProgressBar(false);
                        ErrorUtils.showSnackBarError(rootView.findViewById(R.id.frameLayout), t.getMessage());
                    }
                }
            });
        }
    }

    private void displayCandidates(List<CandidateDto> candidateDtoList) {
        this.candidateDtoList = candidateDtoList;
        if (this.candidateDtoList != null) {
            // sort the candidates by order no
            Collections.sort(candidateDtoList, new Comparator<CandidateDto>() {
                @Override
                public int compare(CandidateDto o1, CandidateDto o2) {
                    if (o1.getOrder() < o2.getOrder()) {
                        return -1;
                    } else if (o1.getOrder() > o2.getOrder()) {
                        return 1;
                    }
                    return 0;
                }
            });
            candidateListSize = candidateDtoList.size();
            candidatesListView.setAdapter(new CandidatesListAdapter(this.getContext(), -1, candidateDtoList));
        }
    }

    private boolean validateAll() {
        // Ww can allow total voters not to be equal to the number of verifications
        if (allowInconsistentSubmission) {
            return validateCandidateTotalVotes() && validateValidVotes();
        } else {
            return validateCandidateTotalVotes() && validateValidVotes() && validateTotalEqualVerifications();
        }
    }

    private void uploadResults() {
        int total = getTotalVoters();
        int invalid = getInvalidVotes();
        int neutral = getNeutralVotes();
        int valid = total - invalid - neutral;

        ResultsDto resultsDto = new ResultsDto();
        // this is actually the department id !?
        resultsDto.setId(MainActivity.loggedUserElectionDepartmentId);
        resultsDto.setTotalVotes(total);
        resultsDto.setValidVotes(valid);
        resultsDto.setInvalidVotes(invalid);
        resultsDto.setWhiteVotes(neutral);

        for (int i = 0; i < candidateListSize; i++) {
            EditText editTextCandidateValue = (EditText) rootView.findViewWithTag("candidateVotes" + i);
            String candidate = editTextCandidateValue.getText().toString();
            int votesForCandidate = !candidate.isEmpty() ? Integer.valueOf(candidate) : 0;
            if (i == 0) {
                resultsDto.setCandidateOneVotes(votesForCandidate);
            }
            if (i == 1) {
                resultsDto.setCandidateTwoVotes(votesForCandidate);
            }
            if (i == 2) {
                resultsDto.setCandidateThreeVotes(votesForCandidate);
            }
            if (i == 3) {
                resultsDto.setCandidateFourVotes(votesForCandidate);
            }
            if (i == 4) {
                resultsDto.setCandidateFiveVotes(votesForCandidate);
            }
            if (i == 5) {
                resultsDto.setCandidateSixVotes(votesForCandidate);
            }
            if (i == 6) {
                resultsDto.setCandidateSevenVotes(votesForCandidate);
            }
            if (i == 7) {
                resultsDto.setCandidateEightVotes(votesForCandidate);
            }
            if (i == 8) {
                resultsDto.setCandidateNineVotes(votesForCandidate);
            }
            if (i == 9) {
                resultsDto.setCandidateTenVotes(votesForCandidate);
            }
        }
        resultsDto.setSubmitted(true);
        sendResults(resultsDto);
    }

    private void sendResults(ResultsDto dto) {
        showProgressBar(true);
        DpelApiService dpelApiService = DpelApiServiceFactory.createDpelApiService(MainActivity.baseUrl).create(DpelApiService.class);
        sendResultsCall = dpelApiService.saveResults("Bearer " + MainActivity.accessToken, dto);
        sendResultsCall.enqueue(new Callback<ResultsDto>() {

            @Override
            public void onResponse(Call<ResultsDto> call, Response<ResultsDto> response) {
                showProgressBar(false);
                if (response.isSuccessful()) {
                    ResultsDto responseResultsDto = response.body();
                    Log.d(TAG, "Saved Results: " + responseResultsDto);
                    showSuccessDialog(R.string.verify_upload_data_sucess);

                } else {
                    ErrorUtils.handleResponseError(rootView.findViewById(R.id.frameLayout), response);
                }
            }

            @Override
            public void onFailure(Call<ResultsDto> call, Throwable t) {
                if (!call.isCanceled()) {
                    showProgressBar(false);
                    ErrorUtils.showSnackBarError(rootView.findViewById(R.id.frameLayout), t.getMessage());
                }
            }
        });
    }

    private void showSuccessDialog(int stringResourceId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(stringResourceId)
                .setPositiveButton(R.string.OK, null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showVerificationErrorDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.validation_result_data_wrong_sums)
                .setPositiveButton(R.string.OK, null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showProgressBar(boolean show) {
        if (show) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    /**
     * Tests if the total number of voters is equal to the total
     * number of verifications from the backend.
     *
     * @return
     */
    private boolean validateTotalEqualVerifications() {

        if (getTotalVoters() == getTotalVerifications()) {
            return true;
        }
        if (Math.abs(getTotalVerifications() - getTotalVoters()) <= inconsistentSubmissionLimit) {
            return true;
        }

        return false;

        /*if (getTotalVoters() == getTotalVerifications()) {
            return true;
        }
        return false;*/
    }

    private boolean validateCandidateTotalVotes() {
        String valueStr = textViewCandidateTotal.getText().toString();
        int candidateTotalValue = valueStr.isEmpty() ? 0 : Integer.valueOf(valueStr);

        valueStr = textViewValid.getText().toString();
        int totalValidValue = valueStr.isEmpty() ? 0 : Integer.valueOf(valueStr);

        if (candidateTotalValue == totalValidValue) {
            return true;
        }
        return false;
    }

    private boolean validateValidVotes() {
        String valueStr = textViewValid.getText().toString();
        int value = valueStr.isEmpty() ? 0 : Integer.valueOf(valueStr);
        if (value >= 0) {
            return true;
        }
        return false;
    }

    private void showEditTextError(TextView textView, boolean show) {
        if (show) {
            textView.setTextColor(Color.RED);
        } else {
            textView.setTextColor(Color.BLACK);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Log.d(TAG, photoURI.getPath());
            if (requestCode == AttachmentType.CASHIER.getRequestCode()) {
                uploadFile(AttachmentType.CASHIER);
            } else if (requestCode == AttachmentType.RESULTS.getRequestCode()) {
                uploadFile(AttachmentType.RESULTS);
            }
        }
    }

    private void uploadFile(final AttachmentType attachmentType) {
        showProgressBar(true);
        RequestBody requestFile = RequestBody.create(
                MediaType.parse(getActivity().getContentResolver().getType(photoURI)), photoFile);

        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("picture", photoFile.getName(), requestFile);

        DpelApiService dpelApiService = DpelApiServiceFactory.createDpelApiService(MainActivity.baseUrl).create(DpelApiService.class);
        uploadFileCall = dpelApiService.uploadFile("Bearer " + MainActivity.accessToken, MainActivity.loggedUserElectionDepartmentId, attachmentType, body);
        uploadFileCall.enqueue(new Callback<JsonNode>() {
            @Override
            public void onResponse(Call<JsonNode> call, Response<JsonNode> response) {
                showProgressBar(false);
                if (response.isSuccessful()) {
                    if (attachmentType.equals(AttachmentType.CASHIER)) {
                        setCashierPhotoAttachmentCheck(true);
                        showSuccessDialog(R.string.verify_upload_cashier_photo_sucess);
                    } else {
                        setResultsPhotoAttachmentCheck(true);
                        showSuccessDialog(R.string.verify_upload_results_photo_sucess);
                    }
                }
                JsonNode node = response.body();
            }

            @Override
            public void onFailure(Call<JsonNode> call, Throwable t) {
                if (!call.isCanceled()) {
                    if (attachmentType.equals(AttachmentType.CASHIER)) {
                        setCashierPhotoAttachmentCheck(false);
                    } else {
                        setResultsPhotoAttachmentCheck(false);
                    }
                    showProgressBar(false);
                    ErrorUtils.showSnackBarError(rootView.findViewById(R.id.frameLayout), t.getMessage());
                    Log.e(TAG, t.getMessage());
                }
            }

        });
    }

    private void dispatchTakePictureIntent(AttachmentType attachmentType) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Log.e(TAG, ex.getMessage());
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                photoURI = FileProvider.getUriForFile(getContext(),
                        "com.ots.dpel.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, attachmentType.getRequestCode());
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String imageFileName = "election_result_img_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        //File storageDir = getActivity().getFilesDir();
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        //currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void retrieveResultSubmissionParams() {
        DpelApiService dpelApiService = DpelApiServiceFactory.createDpelApiService(MainActivity.baseUrl).create(DpelApiService.class);
        retrieveVerificationsCall = dpelApiService.getVoterCountByElectionDepartmentId("Bearer " + MainActivity.accessToken, MainActivity.loggedUserElectionDepartmentId);
        retrieveVerificationsCall.enqueue(new Callback<JsonNode>() {
            @Override
            public void onResponse(Call<JsonNode> call, Response<JsonNode> response) {
                if (response.isSuccessful()) {
                    JsonNode json = response.body();
                    // parse total verifications count
                    setTotalVerifications(json);
                    // parse allow inconsistent totals flag
                    setAllowInconsistentSubmission(json);
                    // parse incosistent limit
                    setInconsistentSubmissionLimit(json);
                } else {
                    Log.e(TAG, "Failed to retrieve result submission parameters: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<JsonNode> call, Throwable t) {
                if (!call.isCanceled()) {
                    ErrorUtils.showSnackBarError(rootView.findViewById(R.id.frameLayout), t.getMessage());
                }
            }
        });
    }

    private int getTotalVerifications() {
        return totalVerifications;
    }

    private void setTotalVerifications(JsonNode jsonNode) {
        JsonNode totalField = jsonNode.get("count");
        totalVerifications = totalField.asInt(0);
        textViewTotalVerifications.setText(String.valueOf(totalVerifications));
        Log.d(TAG, "count: " + totalVerifications);
    }

    private void setAllowInconsistentSubmission(JsonNode jsonNode) {
        JsonNode field = jsonNode.get("allowsInconsistentSubmission");
        allowInconsistentSubmission = field.asBoolean(false);
        Log.d(TAG, "allowInconsistentSubmission: " + allowInconsistentSubmission);
    }

    public void setInconsistentSubmissionLimit(JsonNode jsonNode) {
        JsonNode field = jsonNode.get("inconsistentSubmissionLimit");
        inconsistentSubmissionLimit = field.asInt(0);
        Log.d(TAG, "inconsistentSubmissionLimit: " + inconsistentSubmissionLimit);
    }

    @SuppressLint("ValidFragment")
    public static class UploadResultVerificationDialogFragment extends DialogFragment {

        private final VerificationDialogListener listener;

        public UploadResultVerificationDialogFragment(VerificationDialogListener listener) {
            this.listener = listener;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(R.string.upload_results_verification_dialog_title)
                    .setMessage(R.string.upload_results_verification_dialog)
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                        }
                    })
                    .setPositiveButton(R.string.Yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            if (listener != null) {
                                listener.onDialogVerifyOk();
                            }
                        }
                    });
            // Create the AlertDialog object and return it
            return builder.create();
        }

        interface VerificationDialogListener {
            void onDialogVerifyOk();
        }
    }

    private class CandidatesListAdapter extends ArrayAdapter<CandidateDto> {

        private final Context context;
        private final List<CandidateDto> candidateDtoList;

        public CandidatesListAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<CandidateDto> objects) {
            super(context, resource, objects);
            this.context = context;
            this.candidateDtoList = objects;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View rowView = inflater.inflate(R.layout.layout_candidates_row, parent, false);
            TextView txtViewCandidateName = (TextView) rowView.findViewById(R.id.txtViewCandidateName);

            EditText editTextCandidateValue = (EditText) rowView.findViewById(R.id.editTxtCandidateVotes);
            editTextCandidateValue.setTag("candidateVotes" + position);

            editTextCandidateValue.addTextChangedListener(new CandidateVoteChangeTextWatcher());
            candidateEditTextList.add(editTextCandidateValue);

            CandidateDto dto = candidateDtoList.get(position);
            txtViewCandidateName.setText(dto.getLastName() + " " + dto.getFirstName());

            return rowView;
        }
    }

    private class VoteChangeTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            // do nothing
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            // do nothing
        }

        @Override
        public void afterTextChanged(Editable editable) {
            int sum = getTotalVoters() - getNeutralVotes() - getInvalidVotes();
            setValidVotes(sum);
            if (validateValidVotes()) {
                showEditTextError(textViewValid, false);
            } else {
                showEditTextError(textViewValid, true);
            }
            if (validateCandidateTotalVotes()) {
                showEditTextError(textViewCandidateTotal, false);
            } else {
                showEditTextError(textViewCandidateTotal, true);
            }
            if (!allowInconsistentSubmission && !validateTotalEqualVerifications()) {
                showEditTextError(textViewTotalVerifications, true);
            } else {
                showEditTextError(textViewTotalVerifications, false);
            }
        }
    }

    private class CandidateVoteChangeTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            // do nothing
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            // do nothing
        }

        @Override
        public void afterTextChanged(Editable editable) {
            int sum = 0;
            for (EditText candidateEditText : candidateEditTextList) {
                String valueStr = candidateEditText.getText().toString();
                int value = valueStr.isEmpty() ? 0 : Integer.valueOf(valueStr);
                sum = sum + value;
            }
            setCandidateTotal(sum);
            if (validateCandidateTotalVotes()) {
                showEditTextError(textViewCandidateTotal, false);
            } else {
                showEditTextError(textViewCandidateTotal, true);
            }
        }
    }


    private boolean checkProcedureHasEnded() {
        return MainActivity.procedureEnded != null && MainActivity.procedureEnded ;
    }

    private boolean checkProcedureHasStarted() {
        return MainActivity.procedureStarted != null && MainActivity.procedureStarted;
    }

    public void setElectionProcessStateListener(ElectionProcessStateListener listener) {
        this.electionStateListener = listener;
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
        if (getAllCandidatesCall != null) {
            getAllCandidatesCall.cancel();
        }
        if (sendResultsCall != null) {
            sendResultsCall.cancel();
        }
        if (uploadFileCall != null) {
            uploadFileCall.cancel();
        }
        if (retrieveVerificationsCall != null) {
            retrieveVerificationsCall.cancel();
        }
    }
}
