package com.example.password.Views;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.example.password.Entities.SignData;
import com.example.password.Models.AuthModel;
import com.example.password.Models.LogModel;
import com.example.password.R;
import com.example.password.databinding.FragmentFirstBinding;

import java.security.NoSuchAlgorithmException;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;

    private LogModel logModel;
    private AuthModel authModel;



    boolean test = false;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        logModel = new ViewModelProvider(requireActivity()).get(LogModel.class);
        authModel = new ViewModelProvider(requireActivity()).get(AuthModel.class);



        binding.buttonForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });

        

        binding.buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FirstFragment.this).navigate(R.id.action_FirstFragment_to_ThirdFragment);
            }
        });

        binding.buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {

                    if(logModel.validateLogin(binding.inputEmail.getText().toString(),binding.inputPw.getText().toString())){
                        /*
                        Intent intent1 = new Intent(getActivity(), AppActivity.class);
                        startActivity(intent1);

                         */
                        SignData user = logModel.getSignRepo().get_User_By_Email(binding.inputEmail.getText().toString());
                        logModel.setCurrentID(user);
                        Log.d("AUTH", "hi"+ user.getId());
                        authModel.setPrefID();

                        AuthModel.Callback callback =() ->{

                            Intent intent = new Intent(getActivity(), MainActivity2.class);
                            startActivity(intent);
                        };


                        authModel.authenticate(getContext(),callback);



                        //NavHostFragment.findNavController(FirstFragment.this)
                         //       .navigate(R.id.action_FirstFragment_to_MainFragment);


                    }else{
                        Log.e("Error :" , "Incorrect Login");
                    }
                } catch (NoSuchAlgorithmException e) {
                    throw new RuntimeException(e);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

            }
        });

        binding.buttonFingerAuth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    logModel.addLogin("","t","");
                } catch (NoSuchAlgorithmException e) {
                    throw new RuntimeException(e);
                }
            }
        });

    }






    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        binding.inputEmail.getText().clear();
        binding.inputPw.getText().clear();

    }
}