package com.example.password.Views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.password.Models.GenModel;
import com.example.password.Models.PassModel;
import com.example.password.databinding.FragmentPasswordBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PasswordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PasswordFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FragmentPasswordBinding binding;
    private PassModel passModel;

    private GenModel genModel;


    private int selectedFolder = 0;



    public PasswordFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PasswordFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PasswordFragment newInstance(String param1, String param2) {
        PasswordFragment fragment = new PasswordFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        passModel = new ViewModelProvider(requireActivity()).get(PassModel.class);
        genModel = new ViewModelProvider(requireActivity()).get(GenModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentPasswordBinding.inflate(inflater, container, false);


        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Spinner folderSpinner = binding.categoryIn;
        passModel.implementFolderSpinner(folderSpinner);
        folderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedFolder = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.addPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String appName = binding.appNameIn.getText().toString();
                String userName =  binding.usernameIn.getText().toString();
                String passWord = binding.passwordIn.getText().toString();
                Long fid = passModel.getFolders().get(selectedFolder).getFid();
                Integer renewal;
                try {
                    renewal = Integer.parseInt(binding.validIn.getText().toString());
                }catch (NumberFormatException e){
                    renewal = 0;
                }

                if(!(passModel.isEmpty(appName) || passModel.isEmpty(passWord))){
                    try {
                        passModel.addPassword(passWord,appName,userName,fid,renewal);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }else {
                    Toast.makeText(requireContext(), "Invalid submission, try again", Toast.LENGTH_SHORT).show();
                }



                // Go back to the previous fragment
                requireActivity().getSupportFragmentManager().popBackStack();

            }
        });



    }

    @Override
    public void onResume() {
        super.onResume();
        if(genModel.getGenerated() != null){
            binding.passwordIn.setText(genModel.getGenerated());
            genModel.setGenerated(null);
        }

    }
}