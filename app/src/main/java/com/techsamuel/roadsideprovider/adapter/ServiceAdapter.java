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
import com.techsamuel.roadsideprovider.listener.ServiceItemClickListener;
import com.techsamuel.roadsideprovider.model.ServiceModel;
import com.techsamuel.roadsideprovider.tools.Tools;

public class ServiceAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ServiceModel serviceModel;
    private Context ctx;
    private boolean isProviderInfo;
    private final ServiceItemClickListener listener;


    public ServiceAdapter(Context context, ServiceModel serviceModel, boolean isProviderInfo, ServiceItemClickListener listener) {
        this.serviceModel = serviceModel;
        ctx = context;
        this.isProviderInfo=isProviderInfo;
        this.listener=listener;

    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public ImageView serviceImage;
        public TextView serviceName;
        public TextView serviceDetails;
        public LinearLayout lytService;

        public OriginalViewHolder(View v) {
            super(v);
            serviceImage=v.findViewById(R.id.service_image);
            serviceName=v.findViewById(R.id.service_title);
            serviceDetails=v.findViewById(R.id.service_details);
            lytService=v.findViewById(R.id.lyt_service);

        }

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_service, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        if (holder instanceof OriginalViewHolder) {
            OriginalViewHolder view = (OriginalViewHolder) holder;
            view.serviceName.setText(serviceModel.getData().get(position).getName());
            view.serviceDetails.setText(serviceModel.getData().get(position).getDetails());
            if(isProviderInfo){
                view.serviceName.setTextColor(ctx.getResources().getColor(R.color.white));
                view.serviceDetails.setTextColor(ctx.getResources().getColor(R.color.white));

            }
           view.lytService.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Tools.showToast(ctx,serviceModel.getData().get(position).getId());
                        listener.onItemClick(serviceModel.getData().get(position));
                    }
                });

            String imageUrl=Config.IMAGE_URL+"services/"+serviceModel.getData().get(position).getImage();
            Glide.with(ctx).load(imageUrl).into(view.serviceImage);



        }
    }

    @Override
    public int getItemCount() {
        return serviceModel.getData().size();
    }



}

