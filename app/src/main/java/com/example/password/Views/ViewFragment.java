package com.example.password.Views;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.password.Models.GenModel;
import com.example.password.Models.PassModel;
import com.example.password.databinding.FragmentViewBinding;


public class ViewFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private FragmentViewBinding binding;
    private PassModel passModel;
    private GenModel genModel;

    private boolean hidden = true;
    private boolean editable = false;

    private Bundle arguments;
    private String password = "" , def = "";
    private EditText av,uv,pv,vv;
    private ProgressBar progressBar;
    private TextView strength_label;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        passModel = new ViewModelProvider(requireActivity()).get(PassModel.class);
        genModel = new ViewModelProvider(requireActivity()).get(GenModel.class);
        arguments = getArguments();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentViewBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view,  Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressBar = binding.strengthBar;
        strength_label = binding.strengthLabel;

        av = binding.appView;
        uv = binding.userView;
        vv = binding.validView;
        pv = binding.passView;

        if (arguments != null) {
            String appName = arguments.getString("appname", "???");
            String userName = arguments.getString("username", "N/A");
            password = arguments.getString("password", "???");
            int renewal = arguments.getInt("renewal", 0);

            def= password;

            av.setText(appName);
            uv.setText(userName);
            vv.setText(String.valueOf(renewal));
            pv.setText(password);
        }

        setProgress();

        pv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setProgress();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.copyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passModel.copyPasswordToClipboard(getContext(),password);
            }
        });

        binding.viewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!hidden) {
                    pv.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }else{
                    pv.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                }
                hidden = !hidden;
            }
        });

        binding.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editable = !editable;
                passModel.toggleEditable(vv,editable);
                passModel.toggleEditable(uv,editable);
                passModel.toggleEditable(av,editable);
                passModel.toggleEditable(pv, editable);

            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Long pid = arguments.getLong("pid");
        String appName = av.getText().toString();
        String userName = uv.getText().toString();
        password = pv.getText().toString();
        int renewal = Integer.parseInt(vv.getText().toString());

        try {
            passModel.changePassword(pid,appName,userName,password,renewal,def,getContext());
            passModel.changePasswordValidity(pid,false);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void setProgress(){
        int strength = genModel.measurePasswordStrength(pv.getText().toString());

        if (strength <= 2) {
            strength_label.setText("Weak Password");
            changeProgressColor(Color.RED);
        } else if (strength <= 4) {
            strength_label.setText("Moderate Password");
            changeProgressColor(Color.YELLOW);
        } else {
            strength_label.setText("Strong Password");
            changeProgressColor(Color.GREEN);
        }

        progressBar.setProgress(strength,true);

    }

    private void changeProgressColor(int color){
        Drawable progressDrawable = progressBar.getProgressDrawable().mutate();
        progressDrawable.setColorFilter(color, PorterDuff.Mode.SRC_IN);
        progressBar.setProgressDrawable(progressDrawable);
    }
}