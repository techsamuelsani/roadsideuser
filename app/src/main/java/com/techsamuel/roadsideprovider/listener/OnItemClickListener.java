package com.techsamuel.roadsideprovider.listener;

import com.techsamuel.roadsideprovider.model.ServiceModel;

public interface OnItemClickListener {
    void onItemClick(ServiceModel.Datum item,boolean isChecked);
}
