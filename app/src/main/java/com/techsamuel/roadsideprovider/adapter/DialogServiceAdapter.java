package com.techsamuel.roadsideprovider.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.techsamuel.roadsideprovider.Config;
import com.techsamuel.roadsideprovider.R;
import com.techsamuel.roadsideprovider.listener.OnItemClickListener;
import com.techsamuel.roadsideprovider.model.ServiceModel;

public class DialogServiceAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ServiceModel serviceModel;
    private Context ctx;
    private final OnItemClickListener listener;


    public DialogServiceAdapter(Context context, ServiceModel serviceModel,OnItemClickListener listener) {
        this.serviceModel = serviceModel;
        ctx = context;
        this.listener=listener;

    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {

        public CheckBox serviceName;

        public OriginalViewHolder(View v) {
            super(v);
            serviceName=v.findViewById(R.id.service_name);

        }

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_service_dialog, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        if (holder instanceof OriginalViewHolder) {
            OriginalViewHolder view = (OriginalViewHolder) holder;
            view.serviceName.setText(serviceModel.getData().get(position).getName());
            view.serviceName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(view.serviceName.isChecked()){
                        listener.onItemClick(serviceModel.getData().get(position),true);
                    }else{
                        listener.onItemClick(serviceModel.getData().get(position),false);
                    }

                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return serviceModel.getData().size();
    }



}

