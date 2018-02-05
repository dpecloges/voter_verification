package com.ots.dpel.android;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.ots.dpel.android.rest.dto.ElectionRound;

/**
 * Created by tasos on 10/15/17.
 */

public class AboutFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View rootView = inflater.inflate(R.layout.fragment_about, null);

        SecurityManager securityManager = new SecurityManager(getContext());

        TextView txtViewVersion = (TextView) rootView.findViewById(R.id.versionValue);
        txtViewVersion.setText(BuildConfig.VERSION_NAME);

        TextView txtViewUserFullName = (TextView) rootView.findViewById(R.id.userFullNameValue);
        txtViewUserFullName.setText(securityManager.getLoggedUserFullName());

        TextView txtViewElectoralDepartment = (TextView) rootView.findViewById(R.id.electoralDepartmentNameValue);
        txtViewElectoralDepartment.setText(securityManager.getElectionDepartmentDisplayName());

        TextView txtViewElectoralRound = (TextView) rootView.findViewById(R.id.electoralRoundNameValue);
        txtViewElectoralRound.setText(ElectionRound.valueOf(securityManager.getCurrentElectionProcedureRound()).toString());

        TextView txtViewElectoralCenter = (TextView) rootView.findViewById(R.id.electoralCenterNameValue);
        txtViewElectoralCenter.setText(securityManager.getElectionCenterName());

        builder.setView(rootView)
                .setPositiveButton(R.string.OK, null);

        return builder.create();
    }

}
