package com.example.password.Views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.password.Models.PassModel;
import com.example.password.R;

public class ExpiredFragment extends Fragment {


    private PassModel passModel;

    private RecyclerView recyclerView;
    private CheckBox select;
    private Button delete;
    public ExpiredFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        passModel = new ViewModelProvider(requireActivity()).get(PassModel.class);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_expired_list, container, false);

        recyclerView = view.findViewById(R.id.list2);
        delete = view.findViewById(R.id.delete_button2);
        select = view.findViewById(R.id.selectAll_button2);

        passModel.initRview(recyclerView);
        passModel.initButtons(null,delete);

        passModel.maintainPasswords(true);

        passModel.getPasswordRepo().get_All_Password_Data_Live(false).observe(getViewLifecycleOwner(), passwordData -> {
            // Automatically updates when a folder is added
            passModel.maintainPasswords(true);
        });
        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(select.isChecked()){
                    passModel.setAllSelected(true);
                    passModel.addAllPickedPasswords();
                    Toast.makeText(v.getContext(), passModel.getPickedPasswordSize() + " :Selected password(s)", Toast.LENGTH_SHORT).show();
                }else{
                    passModel.setAllSelected(false);
                    passModel.removeAllPickedPasswords();
                    Toast.makeText(v.getContext(), passModel.getPickedPasswordSize() + " :Selected password(s)", Toast.LENGTH_SHORT).show();
                }
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passModel.deletePasswords();
                passModel.removeAllPickedPasswords();
                passModel.maintainPasswords(false);
            }
        });

    }
}