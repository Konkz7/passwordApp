package com.example.password.Views;

import static android.content.Context.MODE_PRIVATE;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.password.Models.AuthModel;
import com.example.password.Models.LogModel;
import com.example.password.Models.MainDatabase;
import com.example.password.R;

import org.jetbrains.annotations.Nullable;

public class SettingsDialogFragment extends DialogFragment {

    private static final String ARG_LAYOUT_ID = "layout_id";

    private int layout_id;

    private SharedPreferences preferences;
    private String method;

    private AuthModel authModel;
    private LogModel logModel;
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
        authModel = new ViewModelProvider(requireActivity()).get(AuthModel.class);
        logModel = new ViewModelProvider(requireActivity()).get(LogModel.class);

        authModel.setPrefID();
        preferences = getActivity().getSharedPreferences(authModel.getPrefID(), MODE_PRIVATE);
        method = preferences.getString("2fa", "None");
        //Log.d("AUTHC",authModel.getPrefID()+"hell0");

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(layout_id, container, false);

        if(layout_id == R.layout.fragment_settings_dialog) {
            Switch viewMode = view.findViewById(R.id.dark_in);
            TextView twofa = view.findViewById(R.id.twofa);
            TextView wipe = view.findViewById(R.id.wipe);

            if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
                viewMode.setChecked(true); // Dark mode
            } else {
                viewMode.setChecked(false);  // Light mode
            }

            viewMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
                SharedPreferences.Editor editor = preferences.edit();
                if (isChecked) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    editor.putBoolean("darkmode", true);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    editor.putBoolean("darkmode", false);
                }
                editor.apply();
            });
            twofa.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    SettingsDialogFragment settingsDialogFragment = SettingsDialogFragment.newInstance(R.layout.fragment_tfa_dialog);
                    settingsDialogFragment.show(getParentFragmentManager(), "SettingsDialog");


                }
            });

            wipe.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handleWipe(() -> {
                        // If user confirms, navigate back
                        logModel.deleteAccount(requireActivity());
                        requireActivity().finish();
                    });
                }
            });

        } else if (layout_id == R.layout.fragment_tfa_dialog) {

            RadioGroup group = view.findViewById(R.id.auth_methods);
            if (method.equals("None")){
                group.check(R.id.none_in);
            } else if (method.equals("Fingerprint Log-in")) {
                group.check(R.id.fingerprint_in);
            }else{

            }
            group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    SharedPreferences.Editor editor = preferences.edit();

                    RadioButton rb = view.findViewById(checkedId);
                    //String well = rb == null ? "yes" : "no";
                    //Log.d("help", well );


                    if (rb != null) {
                        editor.putString("2fa", rb.getText().toString());
                    }
                    editor.apply();
                    Log.d("well" , "2fahell");
                }
            });

        }
        //Log.d("well" , preferences.getString("2fa","hell"));
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

    public boolean handleWipe( Runnable onConfirmLeave) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Confirm Restore")
                .setMessage("Are you sure you want to completely wipe your database?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    // Execute the action to leave the fragment
                    if (onConfirmLeave != null) {
                        onConfirmLeave.run();
                    }
                })
                .setNegativeButton("No", (dialog, which) -> {
                    // Do nothing and close the dialog
                    dialog.dismiss();
                })
                .setCancelable(true); // Allow dialog dismissal when tapping outside

        AlertDialog dialog = builder.create();
        dialog.show();

        return false; // Return false to indicate navigation is handled
    }



}
