package com.example.password.Views;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.password.Models.PassModel;
import com.example.password.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * A fragment representing a list of Items.
 */
public class MainFragment extends Fragment {


    private LinearLayout folderView;
    private RecyclerView recyclerView;
    private PassModel passModel;
    Button movef,delete;


    public MainFragment() {
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        passModel = new ViewModelProvider(requireActivity()).get(PassModel.class);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_item_list, container, false);
        folderView = view.findViewById(R.id.folder_list);
        recyclerView = view.findViewById(R.id.list2);

        movef = view.findViewById(R.id.movef_button);
        delete = view.findViewById(R.id.delete_button2);


        passModel.initLayout(folderView,this);
        passModel.initRview(recyclerView);
        passModel.initButtons(movef,delete);

        passModel.maintainFolders();
        passModel.maintainPasswords(false);


        passModel.getFolderDao().getAllFoldersLive().observe(getViewLifecycleOwner(), folderData -> {
            // Automatically updates when a folder is added
            passModel.maintainFolders();
        });

        passModel.getPasswordDao().getAllPasswordDataLive(false).observe(getViewLifecycleOwner(), passwordData -> {
            // Automatically updates when a folder is added
            passModel.maintainPasswords(false);
        });



        return view;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FloatingActionButton add = view.findViewById(R.id.fab);
        CheckBox select = view.findViewById(R.id.selectAll_button2);


        movef.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passModel.moveFolderButton();
            }
        });

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

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create an AlertDialog builder
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Choose an Option")
                        .setMessage("Do you want to create a new category or password entry?")
                        .setPositiveButton("Category", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Create an AlertDialog for input
                                AlertDialog.Builder inputDialog = new AlertDialog.Builder(view.getContext());
                                inputDialog.setTitle("Enter Category name");

                                // Add an EditText to the dialog
                                final EditText inputField = new EditText(view.getContext());
                                inputField.setHint("Type here...");
                                inputDialog.setView(inputField);

                                // Set up the buttons
                                inputDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        String userInput = inputField.getText().toString();
                                        // Handle the user input (e.g., store it or display it)
                                        passModel.addFolder(userInput);
                                        passModel.maintainFolders();

                                    }
                                });

                                inputDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });

                                // Show the input dialog
                                inputDialog.show();
                            }
                        })
                        .setNegativeButton("Password", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Handle Option 2 selection
                                NavHostFragment.findNavController(MainFragment.this)
                                        .navigate(R.id.action_MainFragment_to_PasswordFragment);
                            }
                        })
                        .setCancelable(true); // Allows the dialog to be dismissed when touched outside

                // Create and show the dialog
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

}