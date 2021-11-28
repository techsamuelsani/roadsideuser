package com.techsamuel.roadsideprovider.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RadioButton;

import androidx.recyclerview.widget.RecyclerView;

import com.techsamuel.roadsideprovider.R;
import com.techsamuel.roadsideprovider.listener.OnItemClickListener;
import com.techsamuel.roadsideprovider.listener.VehicleItemOnclickListener;
import com.techsamuel.roadsideprovider.model.ServiceModel;
import com.techsamuel.roadsideprovider.model.VehicleModel;

public class DialogVehicleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    VehicleModel vehicleModel;
    private Context ctx;
    private final VehicleItemOnclickListener listener;


    public DialogVehicleAdapter(Context context, VehicleModel vehicleModel, VehicleItemOnclickListener listener) {
        this.vehicleModel = vehicleModel;
        ctx = context;
        this.listener=listener;

    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {

        public RadioButton serviceName;

        public OriginalViewHolder(View v) {
            super(v);
            serviceName=v.findViewById(R.id.service_name);
            //serviceName=v.findViewWithTag("service_tag");

        }

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_vehicle_dialog, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        if (holder instanceof OriginalViewHolder) {
            OriginalViewHolder view = (OriginalViewHolder) holder;
            //view.serviceName.setId(Integer.parseInt(vehicleModel.getData().get(position).getId()));
            view.serviceName.setText(vehicleModel.getData().get(position).getVmake()+" "+vehicleModel.getData().get(position).getVmodel()+" "+vehicleModel.getData().get(position).getVyear());
            view.serviceName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(vehicleModel.getData().get(position));
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return vehicleModel.getData().size();
    }

}

