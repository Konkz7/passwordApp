package com.example.password.Models;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModel;

import java.util.concurrent.Executor;

public class AuthModel extends ViewModel {

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
                .setNegativeButtonText("Cancel")
                .build();

        // Show the dialog
        biometricPrompt.authenticate(promptInfo);
    }
    public boolean buildFingerprint(Context context) {
        final boolean[] granted = {false};
        if (isBiometricAvailable(context)) {
            promptFingerprint(context, new BiometricPrompt.AuthenticationCallback() {
                @Override
                public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                    super.onAuthenticationSucceeded(result);
                    Toast.makeText(context, "Authentication succeeded!", Toast.LENGTH_SHORT).show();
                    granted[0] = true;
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
        return granted[0];
    }



}



