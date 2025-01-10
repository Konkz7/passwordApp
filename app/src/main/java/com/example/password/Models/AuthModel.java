package com.example.password.Models;

import static android.content.Context.MODE_PRIVATE;

import static com.example.password.Models.ModelRepository.repo;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModel;

import java.util.concurrent.Executor;

public class AuthModel extends ViewModel {

    private String prefID;

    public String getPrefID() {
        return this.prefID;
    }

    public void setPrefID() {
        this.prefID = repo.getCurrentUser().getId() + "2fa";
    }


    @FunctionalInterface
    public interface Callback{
        void operation();
    }

    private boolean isBiometricAvailable(Context context) {
        BiometricManager biometricManager = BiometricManager.from(context);
        int result = biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG);
        return result == BiometricManager.BIOMETRIC_SUCCESS;
    }

    // Function to show the fingerprint authentication dialog
    private void promptFingerprint(Context context, BiometricPrompt.AuthenticationCallback callback) {
        if (!isBiometricAvailable(context)) {
            throw new IllegalStateException("Biometric authentication is not available or not set up on this device.");
        }

        // Create an Executor
        Executor executor = ContextCompat.getMainExecutor(context);

        // Create BiometricPrompt
        BiometricPrompt biometricPrompt = new BiometricPrompt((androidx.fragment.app.FragmentActivity) context,
                executor,
                callback);

        // Configure the dialog
        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Fingerprint Authentication")
                .setSubtitle("Authenticate using your fingerprint")
                .setDescription("Place your finger on the fingerprint sensor to proceed.")
                .setAllowedAuthenticators(BiometricManager.Authenticators.DEVICE_CREDENTIAL | BiometricManager.Authenticators.BIOMETRIC_WEAK)
                .build();

        // Show the dialog
        biometricPrompt.authenticate(promptInfo);
    }
    private void buildFingerprint(Context context, Callback callback) {
        if (isBiometricAvailable(context)) {
            promptFingerprint(context, new BiometricPrompt.AuthenticationCallback() {
                @Override
                public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                    super.onAuthenticationSucceeded(result);
                    Toast.makeText(context, "Authentication succeeded!", Toast.LENGTH_SHORT).show();
                    callback.operation();
                }

                @Override
                public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                    super.onAuthenticationError(errorCode, errString);
                    Toast.makeText(context, "Authentication error: " + errString, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onAuthenticationFailed() {
                    super.onAuthenticationFailed();
                    Toast.makeText(context, "Authentication failed. Try again.", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(context, "Biometric authentication not available", Toast.LENGTH_SHORT).show();
        }
    }

    public void authenticate(Context context,Callback callback){

        SharedPreferences preferences = context.getSharedPreferences("AppPrefs", MODE_PRIVATE);
        String method = preferences.getString( getPrefID(),"None");

        Log.d("AUTH" , getPrefID());
        if(method.equals("None")){
            callback.operation();
        } else if (method.equals("Fingerprint Log-in")) {
            buildFingerprint(context,callback);
        }

    }



}



