package com.example.password.Views;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.DialogFragment;

import com.example.password.R;

import org.jetbrains.annotations.Nullable;

public class SettingsDialogFragment extends DialogFragment {

    private static final String ARG_LAYOUT_ID = "layout_id";
    private Switch viewMode;
    private TextView twofa;

    private int layout_id;

    // Factory method to create an instance of the fragment with a layout ID
    public static SettingsDialogFragment newInstance(int layoutId) {
        SettingsDialogFragment fragment = new SettingsDialogFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_LAYOUT_ID, layoutId); // Add layout ID to arguments
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Retrieve layout ID from arguments
        if (getArguments() != null) {
            layout_id = getArguments().getInt(ARG_LAYOUT_ID);
        }
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(layout_id, container, false);
        if(layout_id == R.layout.fragment_settings_dialog) {
            viewMode = view.findViewById(R.id.dark_in);
            twofa = view.findViewById(R.id.twofa);
            if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
                viewMode.setChecked(true); // Dark mode
            } else {
                viewMode.setChecked(false);  // Light mode
            }

            viewMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES); // Light mode
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO); // Dark mode
                }
            });
            twofa.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    SettingsDialogFragment settingsDialogFragment = SettingsDialogFragment.newInstance(R.layout.fragment_tfa_dialog);
                    settingsDialogFragment.show(getParentFragmentManager(), "SettingsDialog");


                }
            });
        } else if (layout_id == R.layout.fragment_tfa_dialog) {

        }

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null && getDialog().getWindow() != null) {
            // Set the dialog size and position
            Window window = getDialog().getWindow();
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            window.setGravity(Gravity.CENTER); // Example: Align at the bottom
            window.setBackgroundDrawableResource(android.R.color.transparent); // Make the background transparent


        }

    }



}
