package com.ots.dpel.android;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.ots.dpel.android.rest.DpelApiService;
import com.ots.dpel.android.rest.DpelApiServiceFactory;
import com.ots.dpel.android.rest.dto.ElectionRound;
import com.ots.dpel.android.rest.dto.VerificationDto;
import com.ots.dpel.android.rest.dto.VoterDto;
import com.ots.dpel.android.utils.DateUtils;
import com.ots.dpel.android.utils.ErrorUtils;

import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class VerifyResultFragment extends Fragment {

    private static final int COLOR_GREEN = Color.rgb(0, 215, 0);
    private static final String TAG = VerifyResultFragment.class.getName();
    private View rootView;
    private ProgressBar progressBar;
    private View hasVotedLayout;
    private TextView txtViewElectoralNumValue;
    private TextView txtViewSurnameValue;
    private TextView txtViewNameValue;
    private TextView txtViewFatherNameValue;
    private TextView txtViewMotherNameValue;
    private TextView txtViewBirthDateValue;
    private TextView txtViewMunicipalityValue;
    private TextView txtViewDimotologioValue;
    private TextView txtViewIsPreregisteredValue;
    private TextView txtViewIsPreregisteredLabel;
    private CheckBox checkboxIsMember;
    private TextInputEditText textInputEditTextStreet;
    private TextInputEditText textInputEditTextStreetNum;
    private TextInputEditText textInputEditTextPostCode;
    private TextInputEditText textInputEditTextCity;
    private TextInputEditText textInputEditTextEmail;
    private TextInputEditText textInputEditTextPhone;
    private TextInputEditText textInputEditTextCountry;
    private TextInputEditText textInputEditTextPayment;
    private TextInputLayout textInputLayoutPayment;
    private TextView textViewVoteDate;
    private TextView textViewVoteLocation;
    private Button commitButton;
    private Button backToSearchButton;
    private NewSearchButtonClickListener newSearchButtonClickedListener;
    private VerificationDto verification;
    private VoterDto voter;
    private MenuItem undoMenuItem;
    private Spinner spinnerIdOption;
    private TextInputEditText textInputEditTextIdValue;
    private TextView txtViewVerificationNumber;
    private Call<Boolean> undoVoteCall;
    private Call<VoterDto> saveVoterCall;
    private ElectionProcessStateListener electionStateListener;

    public VerifyResultFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Data came from search
        if (verification != null) {
            loadVerificationData();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        undoMenuItem = menu.add(R.string.menu_label_undo_vote);
        undoMenuItem.setVisible(MainActivity.canUndoVote);
        undoMenuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == 0) {
            undoVote();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_verify_result, container, false);

        // personal details and vote status
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        txtViewElectoralNumValue = (TextView) rootView.findViewById(R.id.txtViewElectoralNumValue);
        txtViewSurnameValue = (TextView) rootView.findViewById(R.id.txtViewSurnameValue);
        txtViewNameValue = (TextView) rootView.findViewById(R.id.txtViewNameValue);
        txtViewFatherNameValue = (TextView) rootView.findViewById(R.id.txtViewFatherNameValue);
        txtViewMotherNameValue = (TextView) rootView.findViewById(R.id.txtViewMotherNameValue);
        txtViewBirthDateValue = (TextView) rootView.findViewById(R.id.txtViewBirthDateValue);
        txtViewMunicipalityValue = (TextView) rootView.findViewById(R.id.txtViewMunicipalityValue);
        txtViewDimotologioValue = (TextView) rootView.findViewById(R.id.txtViewDimotologioValue);
        txtViewIsPreregisteredValue = (TextView) rootView.findViewById(R.id.txtViewIsPreregisteredValue);
        txtViewIsPreregisteredLabel = (TextView) rootView.findViewById(R.id.txtViewIsPreregisteredLabel);

        hasVotedLayout = rootView.findViewById(R.id.hasVotedLayout);
        textViewVoteDate = (TextView) rootView.findViewById(R.id.textViewVoteDate);
        textViewVoteLocation = (TextView) rootView.findViewById(R.id.textViewVoteLocation);

        // payment and member details
        checkboxIsMember = (CheckBox) rootView.findViewById(R.id.checkboxIsMember);
        textInputEditTextPayment = (TextInputEditText) rootView.findViewById(R.id.editTextPayment);
        textInputLayoutPayment = (TextInputLayout) rootView.findViewById(R.id.textInputLayoutPayment);

        // contact details
        textInputEditTextStreet = (TextInputEditText) rootView.findViewById(R.id.editTextStreet);
        textInputEditTextStreetNum = (TextInputEditText) rootView.findViewById(R.id.editTextStreetNum);

        textInputEditTextPostCode = (TextInputEditText) rootView.findViewById(R.id.editTextPostcode);
        textInputEditTextCity = (TextInputEditText) rootView.findViewById(R.id.editTextCity);
        textInputEditTextEmail = (TextInputEditText) rootView.findViewById(R.id.editTextEmail);
        textInputEditTextPhone = (TextInputEditText) rootView.findViewById(R.id.editTextPhone);
        textInputEditTextCountry = (TextInputEditText) rootView.findViewById(R.id.editTextStreetCountry);

        commitButton = (Button) rootView.findViewById(R.id.commitButton);
        commitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MainActivity.canStartStopProcess && !checkProcedureIsStarted()) {
                    showStartProcessDialog(new YesNoDialogResponseListener() {
                        @Override
                        public void onPositive() {
                            startElectionProcess();
                            saveVoter();
                        }

                        @Override
                        public void onNegative() {
                            //do nothing
                        }
                    });
                } else {
                    saveVoter();
                }
            }
        });

        backToSearchButton = (Button) rootView.findViewById(R.id.backToSearchButton);
        backToSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (newSearchButtonClickedListener != null) {
                    newSearchButtonClickedListener.onNewSearchButtonClickEvent();
                }
            }
        });

        // voter identification options
        spinnerIdOption = (Spinner) rootView.findViewById(R.id.selectIdSpinner);
        spinnerIdOption.setAdapter(new IdTypeEnumAdapter(getContext()));

        textInputEditTextIdValue = (TextInputEditText) rootView.findViewById(R.id.editTextIdValue);
        txtViewVerificationNumber = (TextView) rootView.findViewById(R.id.txtViewVoterVerificationNumberLabelValue);

        // Στον 2ο γύρο δεν μας ενδιαφέρει αν έχει κάνει προεγγραφή ή οχι (κρύβουμε το πεδίο). Ουσιαστικά αλλάζουμε
        // το λεκτικό και στην τιμή φέρνουμε την τιμη του αν εχει διαπιστευτεί στον 1ο γύρο. Αυτό γιατί χαλούσε η
        // στοίχηση μέσα στο κελί του πίνακα όταν προστέθηκε 2ο textfield που άλλαζε το visibility.
        txtViewIsPreregisteredLabel.setText(R.string.has_voted_in_round_one);

        return rootView;
    }

    private void startElectionProcess() {
        if (electionStateListener != null) {
            electionStateListener.onStartProcess();
        }
    }

    private boolean checkProcedureIsStarted() {
        // Εάν δεν έχει ξεκινήσει η διαδικασία και ο χειριστής πατήσει για διαπίστευση να τον ενημερώσουμε
        // εάν θέλει να την ξεκινήσουμε αυτόματα.
        return MainActivity.procedureStarted != null && MainActivity.procedureStarted;
    }

    // Identification Number
    private String getSpinnerIdOption() {
        return spinnerIdOption.getSelectedItem().toString();
    }

    private void setSpinnerIdOption(String idOption) {
        if (idOption != null) {
            spinnerIdOption.setSelection(IdType.valueOf(idOption).ordinal());
        }
    }

    // Electoral number (view)
    private void setTxtViewElectoralNumValue(String electoralNumValue) {
        txtViewElectoralNumValue.setText(electoralNumValue);
    }

    // Verification number
    private void setTxtViewVerificationNumberValue(Integer verificationNumber) {
        txtViewVerificationNumber.setText(verificationNumber != null ? String.valueOf(verificationNumber) : "");
    }

    // Surname (view)
    private void setTxtViewSurnameValue(String lastName) {
        txtViewSurnameValue.setText(lastName);
    }

    // First name (view)
    private void setTxtViewNameValue(String firstName) {
        txtViewNameValue.setText(firstName);
    }

    // Father Name (view)
    private void setTxtViewFatherNameValue(String fatherName) {
        txtViewFatherNameValue.setText(fatherName);
    }

    // Mother Name (view)
    private void setTxtViewMotherNameValue(String motherName) {
        txtViewMotherNameValue.setText(motherName);
    }

    // Birth date (view)
    private void setTxtViewBirthDateValue(Date birthDate) {
        txtViewBirthDateValue.setText(DateUtils.formatDate(birthDate));
    }

    // Municipality Name (view)
    private void setTxtViewMunicipalityValue(String municipalUnitDescription) {
        txtViewMunicipalityValue.setText(municipalUnitDescription);
    }

    // Dimotologio (view)
    private void setTxtViewDimotologioValue(String dimotologio) {
        txtViewDimotologioValue.setText(dimotologio);
    }

    // Is pre-registered (view)
    private void setTxtViewIsPreregisteredValue(Boolean isPreregisteredOrVotedInFirstRound) {
        // Ugly...I know :-(
        txtViewIsPreregisteredValue.setText(isPreregisteredOrVotedInFirstRound ? getString(R.string.Yes) : getString(R.string.No));
        if (isCurrentRoundTwo()) {
            txtViewIsPreregisteredValue.setTextColor(isPreregisteredOrVotedInFirstRound ? COLOR_GREEN : Color.RED);
        }
    }

    /*private void setTxtViewRoundOneVotedValue(Boolean hasVotedInFirstRound) {
        txtViewRoundOneVotedValue.setText(hasVotedInFirstRound ? getString(R.string.Yes) : getString(R.string.No));
        txtViewRoundOneVotedValue.setTextColor(hasVotedInFirstRound ? Color.BLUE : Color.RED);
    }*/

    private boolean getCheckboxIsMember() {
        return checkboxIsMember.isChecked();
    }

    // Is Member (checkbox)
    private void setCheckboxIsMember(Boolean isMember) {
        checkboxIsMember.setChecked(isMember != null ? isMember : false);
    }

    private String getTextInputEditTextStreet() {
        return textInputEditTextStreet.getText().toString();
    }

    // Street Name (edit)
    private void setTextInputEditTextStreet(String addressStreet) {
        textInputEditTextStreet.setText(addressStreet != null ? addressStreet : "");
    }

    private String getTextInputEditTextStreetNum() {
        return textInputEditTextStreetNum.getText().toString();
    }

    // Street Num (edit)
    private void setTextInputEditTextStreetNum(String addressNum) {
        textInputEditTextStreetNum.setText(addressNum != null ? addressNum : "");
    }

    private String getTextInputEditTextPostCode() {
        return textInputEditTextPostCode.getText().toString();
    }

    // Postal Code (edit)
    private void setTextInputEditTextPostCode(String postCode) {
        textInputEditTextPostCode.setText(postCode != null ? postCode : "");
    }

    private String getTextInputEditTextCity() {
        return textInputEditTextCity.getText().toString();
    }

    // City (edit)
    private void setTextInputEditTextCity(String city) {
        textInputEditTextCity.setText(city != null ? city : "");
    }

    private String getTextInputEditTextEmail() {
        return textInputEditTextEmail.getText().toString();
    }

    // Email (edit)
    private void setTextInputEditTextEmail(String email) {
        textInputEditTextEmail.setText(email != null ? email : "");
    }

    private String getTextInputEditTextPhoneNumber() {
        return textInputEditTextPhone.getText().toString();
    }

    // Phone Number (edit)
    private void setTextInputEditTextPhoneNumber(String phoneNumber) {
        textInputEditTextPhone.setText(phoneNumber != null ? phoneNumber : "");
    }

    private String getTextInputEditTextCountry() {
        return textInputEditTextCountry.getText().toString();
    }

    // Country (edit)
    private void setTextInputEditTextCountry(String country) {
        textInputEditTextCountry.setText(country != null ? country : "");
    }

    private Double getTextInputEditTextPayment() {
        return !textInputEditTextPayment.getText().toString().isEmpty() ? Double.valueOf(textInputEditTextPayment.getText().toString()) : 0.0d;
    }

    // Payment (edit)
    private void setTextInputEditTextPayment(Double payment) {
        textInputEditTextPayment.setText(payment != null ? String.valueOf(payment) : "0.0");
    }

    // Vote datetime (view)
    private void setTxtViewVoteDateTime(Date voteDateTime) {
        textViewVoteDate.setText(DateUtils.formatDateTime(voteDateTime));
    }

    // Vote location (view)
    private void setTxtViewVoteLocation(String voteLocation) {
        textViewVoteLocation.setText(voteLocation != null ? voteLocation : "");
    }

    // Identification Number
    private String getTextInputEditTextIdValue() {
        return textInputEditTextIdValue.getText().toString();
    }

    private void setTextInputEditTextIdValue(String idValue) {
        textInputEditTextIdValue.setText(idValue != null ? idValue : "");
    }

    private void showHasVotedPanel(Date voteDateTime, String voteElectionDepartmentName) {
        hasVotedLayout.setVisibility(View.VISIBLE);
        setTxtViewVoteDateTime(voteDateTime);
        setTxtViewVoteLocation(voteElectionDepartmentName);
    }

    private void loadVerificationData() {
        // ήρθε απο αναζήτηση
        loadFormFields(verification.getEklSpecialNo(),
                verification.getLastName(),
                verification.getFirstName(),
                verification.getFatherFirstName(),
                verification.getMotherFirstName(),
                verification.getBirthDate(),
                verification.getMunicipalRecordNo(),
                verification.getMunicipalUnitDescription(),
                verification.getHasPreregistrationRecord(),
                verification.getHasVoterRecord() ? verification.getVoterMember() : verification.getPreregistrationMember(),
                verification.getVoterPayment(),
                verification.getDefaultPayment(),
                verification.getVoterAddress(),
                verification.getVoterAddressNo(),
                verification.getVoterCity(),
                verification.getVoterCountry(),
                verification.getVoterPostalCode(),
                verification.getVoterEmail(),
                verification.getVoterCellphone(),
                verification.getHasVoterRecord(),
                verification.getVoteDateTime(),
                verification.getVoteElectionDepartmentName(),
                verification.getVoterIdType(),
                verification.getVoterIdNumber(),
                verification.getVoterVerificationNumber(),
                verification.getVoterFirstRound());

    }

    // Data came from call to an already existing voter
    private void loadVoterData() {
        loadFormFields(verification.getEklSpecialNo(),
                verification.getLastName(),
                verification.getFirstName(),
                verification.getFatherFirstName(),
                verification.getMotherFirstName(),
                verification.getBirthDate(),
                verification.getMunicipalRecordNo(),
                verification.getMunicipalUnitDescription(),
                verification.getHasPreregistrationRecord(),
                voter.getMember(),
                voter.getPayment(),
                null,
                voter.getAddress(),
                voter.getAddressNo(),
                voter.getCity(),
                voter.getCountry(),
                voter.getPostalCode(),
                voter.getEmail(),
                voter.getCellphone(),
                voter.getVoted(),
                voter.getVoteDateTime(),
                verification.getVoteElectionDepartmentName(),
                voter.getIdType(),
                voter.getIdNumber(),
                voter.getVerificationNumber(),
                verification.getVoterFirstRound());
    }

    // load views with data
    private void loadFormFields(String eklSpecialNo, String lastName, String firstName, String fatherName,
                                String motherName, Date birthDate, String dimotologio, String municipalityName,
                                Boolean isPreregistered, Boolean isMember, Double payment, Double defaultPayment,
                                String addressStreet, String addressNum, String city, String country,
                                String postalCode, String email, String phone, Boolean hasVoted,
                                Date voteDateTime, String voteElectionDepartment, String idType, String iddNumber,
                                Integer verificationNumber, Boolean voterFirstRound) {

        setTxtViewElectoralNumValue(eklSpecialNo);
        setTxtViewSurnameValue(lastName);
        setTxtViewNameValue(firstName);
        setTxtViewFatherNameValue(fatherName);
        setTxtViewMotherNameValue(motherName);
        setTxtViewBirthDateValue(birthDate);
        setTxtViewDimotologioValue(dimotologio);
        setTxtViewMunicipalityValue(municipalityName);
        setTxtViewIsPreregisteredValue(isCurrentRoundTwo() ? voterFirstRound : isPreregistered);
        setCheckboxIsMember(isMember);

        setTextInputEditTextPayment(payment != null ? payment : (!voterFirstRound ? 0 : defaultPayment));
        // στον 2ο γύρο εκλογών δεν μας ενδιαφέρει το ποσό πληρωμής.
        if (isCurrentRoundTwo()) {
            textInputEditTextPayment.setEnabled(false);
            //checkboxIsMember.setEnabled(false);
        } else {
            // να κλειδωνει αν εχει ηδη πληρωσει ή ειναι απο προεγγραφή
            textInputEditTextPayment.setEnabled(payment == null && !isPreregistered);
        }


        //setTxtViewRoundOneVotedValue(voterFirstRound);

        setTextInputEditTextStreet(addressStreet);
        setTextInputEditTextStreetNum(addressNum);
        setTextInputEditTextCity(city);
        setTextInputEditTextCountry(country);
        setTextInputEditTextPostCode(postalCode);
        setTextInputEditTextEmail(email);
        setTextInputEditTextPhoneNumber(phone);

        setSpinnerIdOption(idType);
        setTextInputEditTextIdValue(iddNumber);

        setTxtViewVerificationNumberValue(verificationNumber);

        // έχει ψηφίσει ήδη
        if (hasVoted) {
            showHasVotedPanel(voteDateTime, voteElectionDepartment);
            commitButton.setText(R.string.update_voter_info);
        }
    }

    private boolean isCurrentRoundTwo() {
        return MainActivity.ELECTION_ROUND.equals(ElectionRound.SECOND);
    }

    private void showStartProcessDialog(final YesNoDialogResponseListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.dialog_auto_start_title)
                .setMessage(R.string.dialog_auto_start_process)
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

    private void saveVoter() {

        // Εάν είμαστε στον 2ο γύρο και ο ψηφοφόρος δεν είχε διαπιστευτεί στον 1ο γύρο από λάθος, ενημέρωσε
        // τον χειριστή για το τι θέλει να κάνει. Να προχωρήσει με την διαπίστευση ή οχι.
        if (isCurrentRoundTwo() && !isVoterFromFirstRound() && !isVoterVerified()) {
            showVerifyNonFirstRoundVoterDialog(new YesNoDialogResponseListener() {
                @Override
                public void onPositive() {
                    Log.d(TAG, "verify");
                    doSaveVoter();
                }

                @Override
                public void onNegative() {
                    Log.d(TAG, "No do not verify");
                }
            });
        } else {
            doSaveVoter();
        }
    }

    private void doSaveVoter() {
        // existing voter, check if they are in the same department
        if (isVoterVerified() && !isVerifiedInSameDepartment()) {
            ErrorUtils.showSnackBarError(rootView, R.string.voter_verified_other_department);
            return;
        }

        // Check payment but only if we are in first electoral round
        if (!isCurrentRoundTwo() && !verifyPayment()) {
            return;
        }

        showProgressBar(true);
        if (voter == null) {
            // New voter process
            voter = new VoterDto();
            voter.setId(verification.getVoterId());
            voter.setEklSpecialNo(verification.getEklSpecialNo());
            voter.setElectorId(verification.getElectorId());
            voter.setBirthDate(verification.getBirthDate());
            voter.setVerificationNumber(verification.getVoterVerificationNumber());
        }
        voter.setAddress(getTextInputEditTextStreet());
        voter.setAddressNo(getTextInputEditTextStreetNum());
        voter.setCountry(getTextInputEditTextCountry());
        voter.setCellphone(getTextInputEditTextPhoneNumber());
        voter.setCity(getTextInputEditTextCity());
        voter.setEmail(getTextInputEditTextEmail());
        voter.setPostalCode(getTextInputEditTextPostCode());
        voter.setPayment(getTextInputEditTextPayment());
        voter.setMember(getCheckboxIsMember());
        voter.setElectionDepartmentId(MainActivity.loggedUserElectionDepartmentId);
        voter.setIdType(getSpinnerIdOption());
        voter.setIdNumber(getTextInputEditTextIdValue());


        // change success message by checking whether its a confirmation
        // or an update save (voter has voted)
        final int msgId;
        if (isVoterVerified()) {
            msgId = R.string.update_voter_info_success;
        } else {
            msgId = R.string.verify_sucess;
        }

        DpelApiService dpelApiService = DpelApiServiceFactory.createDpelApiService(MainActivity.baseUrl).create(DpelApiService.class);
        saveVoterCall = dpelApiService.saveVoter("Bearer " + MainActivity.accessToken, voter);
        saveVoterCall.enqueue(new Callback<VoterDto>() {
            @Override
            public void onResponse(Call<VoterDto> call, Response<VoterDto> response) {
                showProgressBar(false);
                if (response.isSuccessful()) {
                    Log.d(TAG, "Voter save response: " + response.body());
                    //after save keep voter as field variable
                    voter = response.body();
                    loadVoterData();
                    showSuccessDialog(msgId);
                } else {
                    ErrorUtils.handleResponseError(rootView.findViewById(R.id.frameLayout), response);
                }
            }

            @Override
            public void onFailure(Call<VoterDto> call, Throwable t) {
                if (!call.isCanceled()) {
                    showProgressBar(false);
                    ErrorUtils.showSnackBarError(rootView.findViewById(R.id.frameLayout), t.getMessage());
                }
            }
        });

    }

    private boolean isVoterFromFirstRound() {
        return verification.getVoterFirstRound();
    }

    private void undoVote() {
        if (isVoterVerified()) {
            if (isVerifiedInSameDepartment()) {
                // verify undo action with a dialog
                DialogFragment dialog = new UndoVoteVerificationDialogFragment(new UndoVoteVerificationDialogFragment.VerificationDialogListener() {
                    @Override
                    public void onDialogVerifyOk() {
                        performUndo();
                    }
                });
                dialog.show(getChildFragmentManager(), "UndoVoteVerificationDialogFragment");
            } else {
                // do not allow undo
                ErrorUtils.showSnackBarError(rootView, R.string.voter_verified_other_department);
            }
        } else {
            ErrorUtils.showSnackBarError(rootView, R.string.voter_not_verified);
        }
    }

    // checks to see if the voter is verified (has voted)
    private boolean isVoterVerified() {
        return ((verification != null && verification.getHasVoterRecord() != null && verification.getHasVoterRecord()) || (voter != null && voter.getVoted() != null && voter.getVoted()));
    }

    // Checks to see if the voter has been verified in the same department as the user
    private boolean isVerifiedInSameDepartment() {
        if (isVoterVerified()) {
            if (verification.getVoterElectionDepartmentId() != null && verification.getVoterElectionDepartmentId() == MainActivity.loggedUserElectionDepartmentId
                    || voter != null && voter.getElectionDepartmentId() == MainActivity.loggedUserElectionDepartmentId) {
                return true;
            }
        }
        return false;
    }

    private void performUndo() {
        showProgressBar(true);
        //proceed they are in the same election department
        DpelApiService dpelApiService = DpelApiServiceFactory.createDpelApiService(MainActivity.baseUrl).create(DpelApiService.class);
        undoVoteCall = dpelApiService.undoVote("Bearer " + MainActivity.accessToken, verification.getVoterId() != null ? verification.getVoterId() : voter.getId());
        undoVoteCall.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                showProgressBar(false);
                if (response.isSuccessful()) {
                    showProgressBar(false);
                    Boolean isUndoSuccess = response.body();
                    Log.d(TAG, "Voter undo response: " + isUndoSuccess);
                    if (isUndoSuccess) {
                        showUndoSuccessDialog();
                    }

                } else {
                    ErrorUtils.handleResponseError(rootView.findViewById(R.id.frameLayout), response);
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                if (!call.isCanceled()) {
                    showProgressBar(false);
                    ErrorUtils.showSnackBarError(rootView.findViewById(R.id.frameLayout), t.getMessage());
                }
            }
        });
    }

    private boolean verifyPayment() {
        if (verification.getHasPreregistrationRecord()) {
            return true;
        }
        double payment = getTextInputEditTextPayment();
        double defaultAmount = verification.getDefaultPayment();
        if (payment < defaultAmount) {
            textInputLayoutPayment.setErrorEnabled(true);
            textInputLayoutPayment.setError(getString(R.string.verify_payment_amount) + defaultAmount);
            return false;
        } else {
            textInputLayoutPayment.setErrorEnabled(false);
            textInputLayoutPayment.setError(null);
            return true;
        }
    }

    private void showVerifyNonFirstRoundVoterDialog(final YesNoDialogResponseListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.dialog_verify_non_first_round_voter_title)
                .setMessage(R.string.dialog_verify_non_first_round_voter)
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

    private void showSuccessDialog(int stringResourceId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // if he has already voted notify about the update of the contact info
        //R.string.update_voter_info_success;
        builder.setMessage(stringResourceId)
                .setPositiveButton(R.string.OK, null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showUndoSuccessDialog() {
        final NewSearchButtonClickListener backToSearchListener = newSearchButtonClickedListener;
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.undo_voter_success)
                .setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // go back to search voter after the popup is dismissed
                        if (backToSearchListener != null) {
                            backToSearchListener.onNewSearchButtonClickEvent();
                        }
                    }
                });
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

    public void setNewSearchButtonClickListener(NewSearchButtonClickListener listener) {
        this.newSearchButtonClickedListener = listener;
    }

    public void setVerification(VerificationDto verification) {
        this.verification = verification;
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
        if (undoVoteCall != null) {
            undoVoteCall.cancel();
        }
        if (saveVoterCall != null) {
            saveVoterCall.cancel();
        }
    }

    public void setElectionProcessStateListener(ElectionProcessStateListener listener) {
        this.electionStateListener = listener;
    }

    public enum IdType {
        POLICE_ID(R.string.id_police),
        PASSPORT(R.string.id_passport),
        OTHER(R.string.id_other);

        private final int stringResourceId;

        IdType(int idType) {
            this.stringResourceId = idType;
        }

        public int getStringResourceId() {
            return stringResourceId;
        }

    }

    interface NewSearchButtonClickListener {
        void onNewSearchButtonClickEvent();
    }

    @SuppressLint("ValidFragment")
    public static class UndoVoteVerificationDialogFragment extends DialogFragment {

        private final UndoVoteVerificationDialogFragment.VerificationDialogListener listener;

        public UndoVoteVerificationDialogFragment(UndoVoteVerificationDialogFragment.VerificationDialogListener listener) {
            this.listener = listener;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(R.string.undo_vote_verification_dialog)
                    .setPositiveButton(R.string.Yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            if (listener != null) {
                                listener.onDialogVerifyOk();
                            }
                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                        }
                    });
            // Create the AlertDialog object and return it
            return builder.create();
        }

        interface VerificationDialogListener {
            void onDialogVerifyOk();
        }
    }

    class IdTypeEnumAdapter extends ArrayAdapter<IdType> {

        IdTypeEnumAdapter(Context context) {
            super(context, 0, IdType.values());
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            CheckedTextView text = (CheckedTextView) convertView;

            if (text == null) {
                text = (CheckedTextView) LayoutInflater.from(getContext()).inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
            }
            text.setText(getItem(position).getStringResourceId());

            return text;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            CheckedTextView text = (CheckedTextView) convertView;

            if (text == null) {
                text = (CheckedTextView) LayoutInflater.from(getContext()).inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
            }
            text.setText(getItem(position).getStringResourceId());

            return text;
        }
    }

}
