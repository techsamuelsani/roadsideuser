package com.techsamuel.roadsideprovider.firebase_db;

import androidx.annotation.NonNull;
<<<<<<< HEAD
import androidx.lifecycle.LiveData;
=======
>>>>>>> 02f1a7db7727c46dadc56cea6317dbadb9372209
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
<<<<<<< HEAD
import com.google.android.gms.tasks.OnSuccessListener;
=======
>>>>>>> 02f1a7db7727c46dadc56cea6317dbadb9372209
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.techsamuel.roadsideprovider.model.OrderRequest;

<<<<<<< HEAD
import java.util.HashMap;

=======
>>>>>>> 02f1a7db7727c46dadc56cea6317dbadb9372209
public class FirebaseInstanceDatabase {
    FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();


    public MutableLiveData<Boolean> addOrderRequest(OrderRequest orderRequest){
        final MutableLiveData<Boolean> successOrderRequest = new MutableLiveData<>();

<<<<<<< HEAD
        firebaseDatabase.getReference(DatabaseReferenceName.ORDER_REQUESTS).child(String.valueOf(orderRequest.getId())).setValue(orderRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
=======
        firebaseDatabase.getReference(DatabaseReferenceName.ORDER_REQUESTS).push().setValue(orderRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
>>>>>>> 02f1a7db7727c46dadc56cea6317dbadb9372209
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                successOrderRequest.setValue(true);
            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                successOrderRequest.setValue(false);
            }
        });

        return successOrderRequest;
    }
    public MutableLiveData<DataSnapshot> fetchOrderRequest() {
        final MutableLiveData<DataSnapshot> fetchOrderRequest = new MutableLiveData<>();
        firebaseDatabase.getReference(DatabaseReferenceName.ORDER_REQUESTS).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                fetchOrderRequest.setValue(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return fetchOrderRequest;
    }


<<<<<<< HEAD
    public LiveData<DataSnapshot> fetchProviderLocation(int orderId) {
        final MutableLiveData<DataSnapshot> fetchProviderLocation = new MutableLiveData<>();
        firebaseDatabase.getReference(DatabaseReferenceName.PROVIDER_LOCATION).child(String.valueOf(orderId)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                fetchProviderLocation.setValue(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return fetchProviderLocation;
    }

    public LiveData<Boolean> addTrueFalseInDatabase(String type,boolean accepted, int orderId) {
        final MutableLiveData<Boolean> successAddTrueFalseInDatabase = new MutableLiveData<>();
        HashMap<String, Object> map = new HashMap<>();
        map.put(type, accepted);
        firebaseDatabase.getReference(DatabaseReferenceName.ORDER_REQUESTS).child(String.valueOf(orderId)).updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                successAddTrueFalseInDatabase.setValue(true);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                successAddTrueFalseInDatabase.setValue(false);
            }
        });

        return successAddTrueFalseInDatabase;
    }
=======
>>>>>>> 02f1a7db7727c46dadc56cea6317dbadb9372209
}
