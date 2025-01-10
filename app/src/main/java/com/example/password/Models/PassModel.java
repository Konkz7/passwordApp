package com.example.password.Models;

import static com.example.password.Models.ModelRepository.repo;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.lifecycle.ViewModel;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.password.Daos.FolderDao;
import com.example.password.Daos.PasswordDao;
import com.example.password.Encryptor;
import com.example.password.Entities.FolderData;
import com.example.password.Entities.PasswordData;
import com.example.password.R;
import com.example.password.Repositories.FolderRepository;
import com.example.password.Repositories.PasswordRepository;
import com.example.password.Views.MainFragment;
import com.example.password.Views.PasswordRecyclerAdapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PassModel extends ViewModel {

    private Long pickedFolder = 0L;

    private MainFragment mainInstance;

    private RecyclerView pList = null;

    private List<String> categories  = new ArrayList<>();

    private int selectedFolder = 0;

    private PasswordRecyclerAdapter.ViewHolder holder;

    private LinearLayout layout = null;

    private Set<PasswordData> pickedPasswords = new HashSet<>();
    private Button moveFolder;

    private Button delete;


    public void initButtons(Button mf, Button d){
        moveFolder = mf;
        delete = d;
    }

    private void enableDMButtons(boolean value){
        if(moveFolder != null) {
            moveFolder.setEnabled(value);
        }
        delete.setEnabled(value);
    }
    public List<PasswordData> getFiltered(){
        return repo.getFiltered();
    }
    public void setFiltered(List<PasswordData> filtered){
        repo.setFiltered(filtered);
    }

    public List<FolderData> getFolders(){
        return repo.getFolders();
    }

    public void setFolders(List<FolderData> folders){
        repo.setFolders(folders);
    }

    public PasswordRepository getPasswordRepo(){
        return repo.getPasswordRepo();
    }

    public FolderRepository getFolderRepo(){
        return repo.getFolderRepo();
    }
    public void setHolder(PasswordRecyclerAdapter.ViewHolder holder) {
        this.holder = holder;
    }


    public void initRview(RecyclerView r){
        pList = r;
        setFiltered(getPasswordRepo().get_Filtered_Password_Data(pickedFolder,false));
    }

    public void initLayout(LinearLayout l,MainFragment m){
        layout = l;
        mainInstance = m;

        setFolders(getFolderRepo().get_All_Folders());
        if(getFolders().isEmpty()){
            MainDatabase.databaseWriteExecutor.execute(() -> {
                getFolderRepo().insert_Folder(new FolderData(0L,repo.getCurrentUser().getId(),"Unspecified"));
            });

            Log.d("Folder","Empty ahh nig"+getFolderRepo().get_All_Folders().size());
        }

    }

    public void addPassword(String password, String appname, String username, Long fid,int renewal) throws Exception {

        String pw = Encryptor.encrypt(password,repo.getKey());

        MainDatabase.databaseWriteExecutor.execute(() -> {
            getPasswordRepo().insert_Password(new PasswordData(null, pw, appname , username, new Date(), fid, repo.getCurrentUser().getId(), false, renewal));
        });



    }

    public void changePasswordValidity(Long pid,boolean redacted){
        MainDatabase.databaseWriteExecutor.execute(() -> {
            getPasswordRepo().change_Password_Validity(pid, redacted);
        });
    }

    public void deletePasswords() {

        for (PasswordData i: pickedPasswords
             ) {
            MainDatabase.databaseWriteExecutor.execute(() -> {
                getPasswordRepo().delete_Password(i.getPid());
            });
        }
        enableDMButtons(false);

    }


    private void addFolderButton(String name, Long fid ){
        //LinearLayout linearLayout = findViewById(R.id.horizontalScrollView).findViewById(R.id.linearLayout);

        // Add buttons dynamically

        Button button = new Button(layout.getContext());
        button.setText(name);
        button.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        button.setPadding(10, 10, 10, 10);
        button.setOnClickListener(view -> {
            pickedFolder = fid;
            removeAllPickedPasswords();
            setFiltered(getPasswordRepo().get_Filtered_Password_Data(fid,false));
            Log.d("Folder",  "size =" + getFiltered().size());
            maintainPasswords(false);
        });

        button.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(fid.equals(0L)){
                    return true;
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(layout.getContext());
                builder.setTitle("Choose an Option")
                        .setMessage("Do you want to rename or delete the category?")
                        .setPositiveButton("Rename", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Create an AlertDialog for input
                                AlertDialog.Builder inputDialog = new AlertDialog.Builder(layout.getContext());
                                inputDialog.setTitle("Enter New Category name");

                                // Add an EditText to the dialog
                                final EditText inputField = new EditText(layout.getContext());
                                inputField.setHint("Type here...");
                                inputDialog.setView(inputField);

                                // Set up the buttons
                                inputDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        String userInput = inputField.getText().toString();
                                        // Handle the user input (e.g., store it or display it)
                                        MainDatabase.databaseWriteExecutor.execute(() -> {
                                            getFolderRepo().rename_Folder(fid,userInput);
                                        });
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
                        .setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Handle Option 2 selection
                                for (PasswordData x:getPasswordRepo().get_Filtered_Password_Data(fid,false)
                                     ) {
                                    MainDatabase.databaseWriteExecutor.execute(() -> {
                                        getPasswordRepo().delete_Password(x.getPid());
                                    });
                                }

                                if(fid == pickedFolder) {
                                    pickedFolder = 0L;
                                }

                                MainDatabase.databaseWriteExecutor.execute(() -> {
                                    getFolderRepo().delete_Folder(fid);
                                });

                                removeAllPickedPasswords();

                            }
                        })
                        .setCancelable(true); // Allows the dialog to be dismissed when touched outside

                // Create and show the dialog
                AlertDialog dialog = builder.create();
                dialog.show();
                return true;
            }
        });

        // Add button to the LinearLayout
        layout.addView(button);
    }

    public void moveFolderButton(){
        AlertDialog.Builder builder = new AlertDialog.Builder(layout.getContext());
        final Spinner spinner = new Spinner(layout.getContext());
        implementFolderSpinner(spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedFolder = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        builder.setTitle("Choose Folder To Move To ")
            .setView(spinner)
            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    for (PasswordData x: pickedPasswords
                         ) {
                        MainDatabase.databaseWriteExecutor.execute(() -> {
                            getPasswordRepo().re_Folder_Password(x.getPid(),getFolders().get(selectedFolder).getFid());
                        });
                        enableDMButtons(false);
                    }
                    }

            })
            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Handle Option 2 selection
                    dialog.cancel();
                }
            })
            .setCancelable(true); // Allows the dialog to be dismissed when touched outside


        // Create and show the dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void implementFolderSpinner(Spinner folderSpinner){
        ArrayAdapter<String> adapter = new ArrayAdapter<>(layout.getContext(), android.R.layout.simple_spinner_item,categories);
        adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        folderSpinner.setAdapter(adapter);
    }

    public void addFolder(String name){
        FolderData temp = new FolderData(null,repo.getCurrentUser().getId(),name);

        MainDatabase.databaseWriteExecutor.execute(() -> {
            getFolderRepo().insert_Folder(temp);
        });
    }

    public void maintainFolders(){
        layout.removeAllViews();

        setFolders(getFolderRepo().get_All_Folders());

        List<FolderData> folders = getFolders();

        for (FolderData i: folders
        ) {
            addFolderButton(i.getFolderName(), i.getFid());
        }

        categories.clear();
        for (FolderData i: getFolders()
        ) {
            categories.add(i.getFolderName());
        }

        Log.d("ASS",  ": " + folders.size());
    }
    public void maintainPasswords(boolean value){
        // Find the RecyclerView
        if(!value) {
            setFiltered(getPasswordRepo().get_Filtered_Password_Data(pickedFolder, false));
        }else{
            setFiltered(getPasswordRepo().get_All_Password_Data( true));
        }
        if (pList != null) {
            Context context = pList.getContext();
            pList.setLayoutManager(new LinearLayoutManager(context));

            pList.setAdapter(new PasswordRecyclerAdapter(getFiltered(), this));// used to be another function call
            Log.d("RVIEW",  "Recycler isnt null" + pickedFolder);
        }else{
            Log.d("RVIEW",  "Recycler is null");

        }


    }

    //TEST FUNCTION
    public void deleteFolders(){

        MainDatabase.databaseWriteExecutor.execute(() -> {
            getFolderRepo().delete_All();

        });

    }

    public boolean isEmpty(String string){
        if (string.trim().length() > 0){
            return false;
        }
        return true;
    }

    private void addPickedPassword(PasswordData p){

        pickedPasswords.add(findPickedPassword(p));
        enableDMButtons(true);
        for (PasswordData b:pickedPasswords
             ) {
            Log.d("message", b.getPid() + " :PID");
        }
    }

    public int getPickedPasswordSize(){
        return pickedPasswords.size();
    }
    private void deletePickedPassword(PasswordData p){
        pickedPasswords.remove(findPickedPassword(p));
        if(pickedPasswords.isEmpty()){
            enableDMButtons(false);
        }
        for (PasswordData b:pickedPasswords
        ) {
            Log.d("message", b.getPid() + " :PID");
        }
    }

    public void removeAllPickedPasswords(){
        pickedPasswords.clear();
        enableDMButtons(false);
    }

    public void addAllPickedPasswords(){
        pickedPasswords.addAll(getFiltered());
    }

    private PasswordData findPickedPassword(PasswordData p){
        for (PasswordData x:getFiltered()
             ) {
            if(x.getPid().equals(p.getPid())){
                return x;
            }
        }
        return null;
    }

    public void holdPassword(Context context){
        if(!holder.selected){
            addPickedPassword(holder.mItem);
            holder.selected = true;
        }else{
            deletePickedPassword(holder.mItem);
            holder.selected = false;
        }
        Toast.makeText(context, getPickedPasswordSize() + " :Selected password(s)" + holder.selected + holder.mItem.getPid(), Toast.LENGTH_SHORT).show();
    }

    public void clickPassword(Context context) throws Exception {
        PasswordData item = holder.mItem;
        String password = Encryptor.decrypt(item.getPassword(),repo.getKey());
        if(!holder.selected){

            Bundle bundle = new Bundle();
            bundle.putLong("pid", item.getPid());
            bundle.putString("appname", item.getAppName());
            bundle.putString("username", item.getUserName());
            bundle.putString("password", password);
            bundle.putInt("renewal", item.getRenewal());

            NavHostFragment.findNavController(mainInstance)
                    .navigate(R.id.action_to_ViewFragment,bundle);
        }else{
            deletePickedPassword(holder.mItem);
            holder.selected = false;
            Toast.makeText(context, getPickedPasswordSize() + " :Selected password(s)", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean setAllSelected(boolean value){
        if(getFiltered().isEmpty()){
            return false;
        }
        holder.setAllSelected(value);
        enableDMButtons(value);
        return true;
    }

    public void copyPasswordToClipboard(Context context, String password) {
        // Get the Clipboard Manager
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);

        if (clipboard != null) {
            // Create a new ClipData with the password
            ClipData clip = ClipData.newPlainText("Password", password);

            // Set the ClipData to the Clipboard Manager
            clipboard.setPrimaryClip(clip);

            // Show a confirmation message
            Toast.makeText(context, "Password copied to clipboard", Toast.LENGTH_SHORT).show();
        } else {
            // Show an error message if the Clipboard Manager is null
            Toast.makeText(context, "Failed to access clipboard", Toast.LENGTH_SHORT).show();
        }
    }

    public void toggleEditable(EditText editText, boolean isEditable) {

            editText.setFocusable(isEditable);
            editText.setFocusableInTouchMode(isEditable);
            editText.setCursorVisible(isEditable);
    }

    public void changePassword(Long pid,String appName, String userName,String password,int renewal, String def) throws Exception {
        if(isEmpty(password)){
            password = def;
        }

        if(isEmpty(appName)){
            appName = "N/A";
        }

        String finalPassword = Encryptor.encrypt(password,repo.getKey());
        String finalAppName = appName;
        MainDatabase.databaseWriteExecutor.execute(() -> {
            getPasswordRepo().change_Password(pid, finalAppName,userName, finalPassword,renewal,new Date());
        });
    }






}
