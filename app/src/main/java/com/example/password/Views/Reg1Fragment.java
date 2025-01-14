package com.example.password.Views;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.password.R;
import com.example.password.databinding.FragmentReg1Binding;
import com.example.password.databinding.FragmentThirdBinding;

public class Reg1Fragment extends Fragment {

    private FragmentReg1Binding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentReg1Binding.inflate(inflater, container, false);

        binding.nextPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(Reg1Fragment.this)
                        .navigate(R.id.action_Reg1Fragment_to_Reg2Fragment);
            }
        });

        return binding.getRoot();
    }
}