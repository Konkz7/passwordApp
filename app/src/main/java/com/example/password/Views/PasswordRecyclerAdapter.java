package com.example.password.Views;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.password.Entities.PasswordData;
import com.example.password.Models.PassModel;
import com.example.password.databinding.FragmentItemBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link PasswordData}.
 * TODO: Replace the implementation with code for your data type.
 */
public class PasswordRecyclerAdapter extends RecyclerView.Adapter<PasswordRecyclerAdapter.ViewHolder> {

    private final List<PasswordData> pValues;

    private  List<ViewHolder> holders = new ArrayList<>();
    private PassModel passModel;

    public PasswordRecyclerAdapter(List<PasswordData> items,PassModel passModel) {
        pValues = items;
        this.passModel = passModel;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(FragmentItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        holder.mItem = pValues.get(position);
        holder.appView.setText(holder.mItem.getAppName());
        holder.userView.setText(holder.mItem.getUserName());
        passModel.setHolder(holder);


        holder.itemView.setOnClickListener(v -> {
            passModel.setHolder(holder);
            try {
                passModel.clickPassword( v.getContext());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                passModel.setHolder(holder);
                passModel.holdPassword(v.getContext());
                return true;
            }
        });

        holders.add(holder);

    }

    @Override
    public int getItemCount() {
        return pValues.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView appView;
        public final TextView userView;
        public PasswordData mItem;
        public boolean selected = false;

        public ViewHolder(FragmentItemBinding binding) {
            super(binding.getRoot());
            appView = binding.appName;
            userView = binding.userName;
        }
        public void  setAllSelected(boolean value){
            for (ViewHolder h: holders
            ) {
                h.selected = value;
                if(value){
                    h.userView.setBackgroundColor(Color.GRAY);
                }else{
                    h.userView.setBackgroundColor(Color.parseColor("#283593"));
                }
            }
        }
        @Override
        public String toString() {
            return super.toString() + " '" + userView.getText() + "'";
        }
    }
}