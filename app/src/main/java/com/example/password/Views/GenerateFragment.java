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



        binding.generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer[] inputs = getValues();

                Integer sum =  inputs[1] + inputs[2] + inputs[3];
                if(inputs[0] < sum){
                    inputs[0] = sum;
                    binding.lengthIn.setText(sum.toString());
                }
                binding.generatedPW.setText(genModel.generatePassword(inputs[0],inputs[1],inputs[2],inputs[3]));
            }
        });

        binding.submitGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                passModel.copyPasswordToClipboard(getContext(),binding.generatedPW.getText().toString());

            }
        });

    }

    private Integer[] getValues(){
        EditText lengthIn = binding.lengthIn;
        EditText capitalIn = binding.capitalIn;
        EditText digitsIn = binding.digitsIn;
        EditText specialsIn = binding.specialsIn;
        Integer length;
        Integer digits;
        Integer capitals;
        Integer specials;

        try {
            length = Integer.parseInt(lengthIn.getText().toString());
        }catch (NumberFormatException e){
            length = 0;
        }
        try {
            digits = Integer.parseInt(digitsIn.getText().toString());
        }catch (NumberFormatException e){
            digits = 0;
        }
        try {
            capitals = Integer.parseInt(capitalIn.getText().toString());
        }catch (NumberFormatException e){
            capitals = 0;
        }
        try {
            specials = Integer.parseInt(specialsIn.getText().toString());
        }catch (NumberFormatException e){
            specials = 0;
        }
        return new Integer[]{length,capitals,digits,specials};
    }

}