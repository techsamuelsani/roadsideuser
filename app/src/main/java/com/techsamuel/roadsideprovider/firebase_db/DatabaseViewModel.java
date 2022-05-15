package com.techsamuel.roadsideprovider.firebase_db;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;
import com.techsamuel.roadsideprovider.model.OrderRequest;

public class DatabaseViewModel extends ViewModel {
    private FirebaseInstanceDatabase instance;
    public LiveData<Boolean> successAddOrderRequest;
    public LiveData<DataSnapshot> fetchedOrderRequest;
<<<<<<< HEAD
    public LiveData<DataSnapshot> fetchedProviderLocation;
    public LiveData<Boolean> successAddTrueFalseInDatabase;
=======
>>>>>>> 02f1a7db7727c46dadc56cea6317dbadb9372209

    public DatabaseViewModel() {
        instance = new FirebaseInstanceDatabase();
    }

<<<<<<< HEAD
    public void addOrderRequestInDatabase(OrderRequest orderRequest) {
=======
    public void addUserDatabase(OrderRequest orderRequest) {
>>>>>>> 02f1a7db7727c46dadc56cea6317dbadb9372209
        successAddOrderRequest = instance.addOrderRequest(orderRequest);
    }
    public void fetchOrderRequest() {
        fetchedOrderRequest = instance.fetchOrderRequest();
    }

<<<<<<< HEAD
    public void fetchProviderLocation(int orderId) {
        fetchedProviderLocation = instance.fetchProviderLocation(orderId);
    }
    public void addTrueFalseInDatabase(String type, boolean b, int id) {
        successAddTrueFalseInDatabase=instance.addTrueFalseInDatabase(type,b,id);
    }

=======
>>>>>>> 02f1a7db7727c46dadc56cea6317dbadb9372209

}
