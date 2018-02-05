package com.ots.dpel.android;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.ots.dpel.android.rest.DpelApiService;
import com.ots.dpel.android.rest.DpelApiServiceFactory;
import com.ots.dpel.android.rest.dto.ElectionDepartmentTimerDto;
import com.ots.dpel.android.rest.dto.ElectionRound;
import com.ots.dpel.android.rest.dto.VerificationDto;
import com.ots.dpel.android.utils.ErrorUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements VerifyVoterFragment.VerifyVoterResultListener, ElectionProcessStateListener {

    private static final String TAG = MainActivity.class.getName();
    private static final int VOTER_VERIFY_TAB = 0;
    private static final int ELECTION_DATA_TAB = 1;
    public static final String TAG_VERIFY_FRAGMENT = "verify_fragment";
    public static final String TAG_ELECTION_DATA_FRAGMENT = "election_data_fragment";
    public static String accessToken;
    public static long loggedUserElectionDepartmentId;
    public static long currentElectionProcedureId;
    private int selectedTab = 0;
    //public static boolean isChairman;
    private boolean canVerifyVoter;
    public static boolean canUndoVote;
    public static boolean canSubmitResults;
    public static boolean canStartStopProcess;
    private SecurityManager securityManager;
    //public static String currentElectionProcedureRound;
    public static ElectionRound ELECTION_ROUND;
    private int settingsClickedCount = 0;
    public static String baseUrl;
    public static Boolean procedureStarted;
    public static Boolean procedureEnded;

    private SharedPreferences.OnSharedPreferenceChangeListener prefsListener =
            new SharedPreferences.OnSharedPreferenceChangeListener() {
                public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
                    if (key.equals(SettingsFragment.KEY_API_URL)) {
                        baseUrl = prefs.getString(SettingsFragment.KEY_API_URL, "");
                        Log.d(TAG, "Base Url changed to: " + baseUrl);
                    }

                }
            };

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_voter_verification:
                    changeFragment(VOTER_VERIFY_TAB);
                    return true;
                case R.id.navigation_election_data:
                    changeFragment(ELECTION_DATA_TAB);
                    return true;
            }
            return false;
        }

    };
    private Call<ElectionDepartmentTimerDto> getDepartmentTimersCall;
    private Call<Boolean> startProcedureCall;
    private Call<Boolean> stopProcedureCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(prefsListener);
        baseUrl = getBaseUrl();
        securityManager = new SecurityManager(this);
        accessToken = securityManager.getToken();
        loggedUserElectionDepartmentId = securityManager.getLoggedUserElectionDepartmentId();
        currentElectionProcedureId = securityManager.getCurrentElectionProcedureId();
        ELECTION_ROUND = ElectionRound.valueOf(securityManager.getCurrentElectionProcedureRound());
        canUndoVote = securityManager.hasPermissionUndoVote();
        canSubmitResults = securityManager.hasPermissionSubmitResults();
        canVerifyVoter = securityManager.hasPermissionVerifyVoter();
        canStartStopProcess = securityManager.hasPermissionTimer();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        MenuItem navItemElectionData = navigation.getMenu().findItem(R.id.navigation_election_data);
        // enable election data tab when user is chairman
        navItemElectionData.setEnabled(canSubmitResults);

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // switch to voter verification tab on startup
        changeFragment(selectedTab);

        retrieveProcessTimers();
    }

    private void retrieveProcessTimers() {
        DpelApiService dpelApiService = DpelApiServiceFactory.createDpelApiService(baseUrl).create(DpelApiService.class);
        getDepartmentTimersCall = dpelApiService.getUserElectionDepartmentTimer("Bearer " + MainActivity.accessToken);
        getDepartmentTimersCall.enqueue(new Callback<ElectionDepartmentTimerDto>() {
            @Override
            public void onResponse(Call<ElectionDepartmentTimerDto> call, Response<ElectionDepartmentTimerDto> response) {
                if (response.isSuccessful()) {
                    ElectionDepartmentTimerDto timers = response.body();
                    procedureStarted = timers.getStarted();
                    procedureEnded = timers.getEnded();
                    Log.d(TAG, "Procedure started = " + procedureStarted + " ended = " + procedureEnded);
                } else {
                    Log.d(TAG, ErrorUtils.parseRestResponseError(response));
                }
            }

            @Override
            public void onFailure(Call<ElectionDepartmentTimerDto> call, Throwable t) {
                if (!call.isCanceled()) {
                    Log.e(TAG, ErrorUtils.parseRestResponseError(t));
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        // enable setting menu only when user is chairman
        /*MenuItem settingsMenu = menu.findItem(R.id.action_settings);
        settingsMenu.setVisible(canUndoVote);*/

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        if (procedureStarted != null && procedureEnded != null) {
            MenuItem startProcedureItem = menu.findItem(R.id.action_start_procedure);
            startProcedureItem.setVisible(canStartStopProcess && !procedureStarted && !procedureEnded);

            MenuItem endProcedureItem = menu.findItem(R.id.action_stop_procedure);
            endProcedureItem.setVisible(canStartStopProcess && procedureStarted && !procedureEnded);
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_signout) {
            doLogout();
            return true;
        }
        if (id == R.id.action_info) {
            showAboutFragment();
            return true;
        }
        if (id == R.id.action_settings) {
            if (settingsClickedCount == 4) {
                showSettingsFragment();
            } else {
                settingsClickedCount++;
            }
            return true;
        }
        if (id == R.id.action_start_procedure) {
            startElectionProcedure();
            return true;
        }
        if (id == R.id.action_stop_procedure) {
            stopElectionProcedure();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void startElectionProcedure() {
        showProcedureTransitionDialog(R.string.dialog_start_procedure_title, R.string.dialog_start_procedure_msg, new DialogResponseListener() {
            @Override
            public void onPositiveResponse() {
                doStartProcedure();
            }

            @Override
            public void onNegativeResponse() {
                // do nothing
            }
        });
    }

    private void doStartProcedure() {
        DpelApiService dpelApiService = DpelApiServiceFactory.createDpelApiService(baseUrl).create(DpelApiService.class);
        startProcedureCall = dpelApiService.timerStart("Bearer " + MainActivity.accessToken);
        startProcedureCall.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful()) {
                    procedureStarted = response.body();
                    Snackbar snackbar = Snackbar.make(findViewById(R.id.container), R.string.snackbar_procedure_started, Snackbar.LENGTH_LONG);
                    snackbar.show();
                } else {
                    Log.d(TAG, ErrorUtils.parseRestResponseError(response));
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                if (!call.isCanceled()) {
                    Log.e(TAG, ErrorUtils.parseRestResponseError(t));
                }
            }
        });
    }

    private void stopElectionProcedure() {
        showProcedureTransitionDialog(R.string.dialog_stop_procedure_title, R.string.dialog_stop_procedure_msg, new DialogResponseListener() {
            @Override
            public void onPositiveResponse() {
                doStopProcedure();
            }

            @Override
            public void onNegativeResponse() {
                // do nothing
            }
        });
    }

    private void doStopProcedure() {
        DpelApiService dpelApiService = DpelApiServiceFactory.createDpelApiService(baseUrl).create(DpelApiService.class);
        stopProcedureCall = dpelApiService.timerEnd("Bearer " + MainActivity.accessToken);
        stopProcedureCall.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful()) {
                    procedureEnded = response.body();
                    Snackbar snackbar = Snackbar.make(findViewById(R.id.container), R.string.snackbar_procedure_ended, Snackbar.LENGTH_LONG);
                    snackbar.show();
                } else {
                    Log.d(TAG, ErrorUtils.parseRestResponseError(response));
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                if (!call.isCanceled()) {
                    Log.e(TAG, ErrorUtils.parseRestResponseError(t));
                }
            }
        });
    }

    private void showProcedureTransitionDialog(int titleResId, int messageResId, final DialogResponseListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(titleResId)
                .setMessage(messageResId)
                .setPositiveButton(R.string.Yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (listener != null) {
                            listener.onPositiveResponse();
                        }
                    }
                })
                .setNegativeButton(R.string.No, null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showAboutFragment() {
        AboutFragment aboutFragment = new AboutFragment();
        aboutFragment.show(getSupportFragmentManager(), "fragment_about");
    }

    private void doLogout() {
        securityManager.invalidateToken();
        startLoginActivity();
        finish();
    }

    private void changeFragment(int position) {
        Fragment newFragment = null;
        String tag = null;

        if (position == VOTER_VERIFY_TAB) {
            VerifyVoterFragment verifyVoterFragment = VerifyVoterFragment.newInstance();
            verifyVoterFragment.setVerifyResultListener(this);
            newFragment = verifyVoterFragment;
            tag = TAG_VERIFY_FRAGMENT;

        } else if (position == ELECTION_DATA_TAB) {
            newFragment = ElectionDataFragment.newInstance();
            ((ElectionDataFragment) newFragment).setElectionProcessStateListener(this);
            tag = TAG_ELECTION_DATA_FRAGMENT;
        }

        getSupportFragmentManager().beginTransaction().replace(
                R.id.fragmentContainer, newFragment, tag)
                .commit();

        selectedTab = position;
    }

    private void startLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void showSettingsFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, new SettingsFragment())
                .commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("SELECTED_TAB", selectedTab);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onFindVoterResultSuccess(VerificationDto dto) {
        Log.d(TAG, "Received verification dto: " + dto);
        VerifyResultFragment resultFragment = new VerifyResultFragment();
        resultFragment.setElectionProcessStateListener(this);
        resultFragment.setVerification(dto);
        resultFragment.setNewSearchButtonClickListener(new VerifyResultFragment.NewSearchButtonClickListener() {
            @Override
            public void onNewSearchButtonClickEvent() {
                changeFragment(VOTER_VERIFY_TAB);
            }
        });

        getSupportFragmentManager().beginTransaction().replace(
                R.id.fragmentContainer, resultFragment)
                .commit();
    }

    private String getBaseUrl() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String baseUrl = sharedPref.getString(SettingsFragment.KEY_API_URL, "");
        return baseUrl;
    }

    @Override
    protected void onResume() {
        super.onResume();
        PreferenceManager.getDefaultSharedPreferences(this)
                .registerOnSharedPreferenceChangeListener(prefsListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(prefsListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cancelPendingCalls();
    }

    private void cancelPendingCalls() {
        if (getDepartmentTimersCall != null) {
            getDepartmentTimersCall.cancel();
        }
        if (startProcedureCall != null) {
            startProcedureCall.cancel();
        }
        if (stopProcedureCall != null) {
            stopProcedureCall.cancel();
        }
    }

    @Override
    public void onStartProcess() {
        doStartProcedure();
    }

    @Override
    public void onStopProcess() {
        doStopProcedure();
    }

    interface DialogResponseListener {
        void onPositiveResponse();
        void onNegativeResponse();
    }
}
