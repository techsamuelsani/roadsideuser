package com.techsamuel.roadsideprovider.listener;


import com.techsamuel.roadsideprovider.model.ServiceCategoryModel;

import java.util.List;

public interface ServiceCategoryItemClickListener {
    void onItemClick(List<ServiceCategoryModel.Subcategory> subcategories,ServiceCategoryModel.Category category);

}
