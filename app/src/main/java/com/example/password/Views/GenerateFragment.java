package com.example.password.Views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.password.Models.GenModel;
import com.example.password.Models.PassModel;
import com.example.password.databinding.FragmentGenerateBinding;

public class GenerateFragment extends Fragment {

    private FragmentGenerateBinding binding;
    private GenModel genModel;

    private PassModel passModel;

    public GenerateFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentGenerateBinding.inflate(inflater, container, false);
        genModel = new ViewModelProvider(requireActivity()).get(GenModel.class);
        passModel = new ViewModelProvider(requireActivity()).get(PassModel.class);



        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EditText lengthIn = binding.lengthIn;
        EditText capitalIn = binding.capitalIn;
        EditText digitsIn = binding.digitsIn;
        EditText specialsIn = binding.specialsIn;


        binding.generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer length;
                Integer digits;
                Integer capitals;
                Integer specials;
                try {
                    length = Integer.parseInt(lengthIn.getText().toString());
                }catch (NumberFormatException e){
                    length = 1;
                }
                try {
                    digits = Integer.parseInt(digitsIn.getText().toString());
                }catch (NumberFormatException e){
                    digits = 1;
                }
                try {
                    capitals = Integer.parseInt(capitalIn.getText().toString());
                }catch (NumberFormatException e){
                    capitals = 1;
                }
                try {
                    specials = Integer.parseInt(specialsIn.getText().toString());
                }catch (NumberFormatException e){
                    specials = 1;
                }
                binding.generatedPW.setText(genModel.generatePassword(length,capitals,digits,specials));
            }
        });

        binding.submitGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                passModel.copyPasswordToClipboard(getContext(),binding.generatedPW.getText().toString());

            }
        });
    }

}