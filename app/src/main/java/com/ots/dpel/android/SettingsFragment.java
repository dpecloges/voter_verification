package com.ots.dpel.android;


import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;

public class SettingsFragment extends PreferenceFragmentCompat {

    public static final String KEY_API_URL = "pref_key_api_url";
    public static final String KEY_UPDATE_URL = "pref_key_update_url";

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.preferences);
    }


}
