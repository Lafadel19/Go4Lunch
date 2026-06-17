package go4lunch.data.repository;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.QuerySnapshot;

import go4lunch.data.remote.Firebase;

public class UserRepository {

    private final Firebase usersData;

    public UserRepository(Firebase usersData) {
        this.usersData = usersData;
    }

    public void getUsers(OnSuccessListener<QuerySnapshot>success,
                         OnFailureListener failure){

        usersData.getUsersCollection()
                .get()
                .addOnSuccessListener(success)
                .addOnFailureListener(failure);
    }
}
