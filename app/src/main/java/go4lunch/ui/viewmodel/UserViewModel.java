package go4lunch.ui.viewmodel;

import android.net.Uri;
import android.os.Parcel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseUserMetadata;
import com.google.firebase.auth.MultiFactor;
import com.google.firebase.auth.MultiFactorInfo;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.auth.zzan;

import java.util.Collections;
import java.util.List;

import go4lunch.data.model.User;
import go4lunch.data.remote.Firebase;
import go4lunch.data.repository.UserRepository;

public class UserViewModel extends ViewModel {
    private final MutableLiveData<List<User>> users = new MutableLiveData<>();
    private final UserRepository repository;

    public UserViewModel() {
        repository = new UserRepository(
                new Firebase()
        );
    }


    public LiveData<List<User>> getUsers() {
        return users;
    }

    public void loadUsers() {

        repository.getUsers(querySnapshot -> {

            List<User> list = querySnapshot.toObjects(User.class);
            users.setValue(list);

        }, e -> {
        });
    }
}
