package go4lunch.data.remote;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Collection;

public class Firebase {
  private final FirebaseFirestore firestore;

  public Firebase() {
      firestore = FirebaseFirestore.getInstance();
  }

  public CollectionReference getUsersCollection(){
      return firestore.collection("users");
  }
}

