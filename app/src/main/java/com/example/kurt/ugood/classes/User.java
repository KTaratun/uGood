package com.example.kurt.ugood.classes;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.example.kurt.ugood.classes.Interfaces.ProfilePicResultHandler;
import com.example.kurt.ugood.classes.Interfaces.UserUpdatedDataHandler;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.Map;

import javax.annotation.Nullable;

@IgnoreExtraProperties
public class User {
    private static final String TAG = "User";

    private String username;
    private String email;
    private String profilePic;

    //used transient keyword to ignore variable when parsing with GSON
    private transient Date dateCreated; // = new Date();

    @Exclude
    private String UserID;

    //firebase
    @Exclude
    private transient FirebaseAuth fbAuth =  FirebaseAuth.getInstance();

    @Exclude
    private transient FirebaseFirestore ff = FirebaseFirestore.getInstance();

    @Exclude
    transient FirebaseStorage storage = FirebaseStorage.getInstance();

    @Exclude
    transient StorageReference storageRef = storage.getReference();

    //must have an empty constructor for firebase to parse data to ur object
    public User() {
    }

    //used for signing in
    public User(String email){
        this.email = email;
    }

    //used for creating new user
    public User(String username, String email) {
        this.username = username;
        this.email = email;
        this.dateCreated = new Date();
    }

    //When saving User object to shared preferences we use GSON to parse object unto string to be saved in sharedpreferences
    //GSON only works with basic objects (String, int, ect). So Firebase objects have the "TRANSIENT" Key word which is used so GSON doesnt try to parse an object they cant
    //When retrieving User object from shared preference all Firebase objects are null so must reInit
    //We could make a custom JsonSerializer/JsonDeserializer to be used with GSON so when GSON attemtps to parse firebase objects we can have it just save them as strings then when we ->
    //DeSerialized the custom object we check for those strings and REINIT those firebase objects (example: DeSerialized https://futurestud.io/tutorials/gson-advanced-custom-deserialization-basics
    //OR we could just add this function. Once you pull user object from shared preferences just call this function to reinit firebase stuff.
    @Exclude
    public void reInitFirebase(){
        fbAuth = FirebaseAuth.getInstance();
        ff = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
    }


    //region Getters And Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    @Exclude
    public String getUserID() {
        return UserID;
    }

    @Exclude
    public void setUserID(String userID) {
        UserID = userID;
    }
    //endregion


    //region Firebase Auth - Signing user in or create users Account
    @Exclude
    public Task<AuthResult> RegisterUser(String password){
        return fbAuth.createUserWithEmailAndPassword(this.email, password);
    }

    @Exclude
    public Task<AuthResult> SignUserIn(String password){
        return fbAuth.signInWithEmailAndPassword(this.email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                setUserID(FirebaseAuth.getInstance().getUid());
            }
        });
    }
    //endregion


    //region Write or update user info
    @Exclude
    public Task<Void> WriteNewUser(){
        return ff.collection("Users").document(this.UserID).set(this);
    }

    //Gson used to map our user info data. Gson is used so datecreated is not updated, the "transient"
    //keeps the DateCreated variable from being parsed into Map
    @Exclude
    public  Task<Void> UpdateUserInfo(){
        Gson gson = new Gson();
        String json = gson.toJson(this);
        Map<String , Object> map = gson.fromJson(json, Map.class);
        //Log.i(TAG, "UpdateUserInfo: json " + json);
        Log.i(TAG, "UpdateUserInfo: map  " + map);
       return ff.collection("Users").document(this.UserID).update(map);
    }
    //endregion


    //region Default Profile Pic Creation
    @Exclude
    public void CreateProfilePic(final ProfilePicResultHandler<Uri> handler){
        char firstCharacter = username.charAt(0);
        ColorGenerator generator = ColorGenerator.MATERIAL;
        // generate  random color based on a key (same key returns the same color)
        int randomColor = generator.getColor(this.UserID);

        //declare builder object
        TextDrawable drawable = TextDrawable.builder().buildRound(Character.toString(firstCharacter).toUpperCase(), randomColor);
        Bitmap bitMap = drawableToBitmap(drawable);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        bitMap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        //image storage reference
        final StorageReference userProfilePicRef = storageRef.child(this.UserID);

        final UploadTask uploadTask = userProfilePicRef.putBytes(data);
        uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()){
                    Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()){
                                throw task.getException();
                            }
                            return userProfilePicRef.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()){
                                Uri downloadUri = task.getResult();
                                handler.onSuccess(downloadUri);
                            }
                        }
                    });
                }
            }
        });


    }
    //Used to convert drawable into bitMap
    @Exclude
    private static Bitmap drawableToBitmap (Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable)drawable).getBitmap();
        }


        int width = drawable.getIntrinsicWidth();
        width = width > 0 ? width : 96; // Replaced the 1 by a 96
        int height = drawable.getIntrinsicHeight();
        height = height > 0 ? height : 96; // Replaced the 1 by a 96

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }
    //endregion


    //region Save/Retrieve Users Info into userdefaults
    //Parses users data using GSON then Saves users info in shared preferences
    @Exclude
    public void saveObjectInUserDefaults(Context context){

        SharedPreferences mPrefs = context.getSharedPreferences(
                "com.example.kurt.ugood", Context.MODE_PRIVATE);

        SharedPreferences.Editor prefsEditor = mPrefs.edit();

        Gson gson = new Gson();
        String json = gson.toJson(this);
        prefsEditor.putString("CurrentUser", json);
        prefsEditor.commit();

    }

    //Grabs user data saved in shared preferences and parse back into user object also reInit firebase stuff
    @Exclude
    public static User retreiveUserObjectFromUserDefaults(Context context){
        SharedPreferences mPrefs = context.getSharedPreferences(
                "com.example.kurt.ugood", Context.MODE_PRIVATE);

        Gson gson = new Gson();
        String json = mPrefs.getString("CurrentUser", "");
        User user = gson.fromJson(json, User.class);
        user.reInitFirebase();
        return user;
    }
    //endregion


    //region Firebase user Listeners and get user data once
    @Exclude
    public Task<DocumentSnapshot> grabUsersData(){
        return ff.collection("Users").document(this.UserID).get();
    }
    @Exclude
    public ListenerRegistration CheckForUserDataUpdates(final UserUpdatedDataHandler<User> updatedDataHandler, final Context context){
        final DocumentReference userRef = ff.collection("Users").document(this.UserID);
        final User currentUserData = this;
        return  userRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                String source = snapshot != null && snapshot.getMetadata().hasPendingWrites()
                        ? "Local" : "Server";

                if (snapshot != null && snapshot.exists()) {
                    if(source.equals("Server")){
                        User newUserData = snapshot.toObject(User.class);
                        if (!newUserData.equals(currentUserData)){
                                newUserData.setUserID(snapshot.getId());
                                newUserData.saveObjectInUserDefaults(context);
                                updatedDataHandler.updateUserData(newUserData);
                        }

                    }
                    Log.d(TAG, source + " data: " + snapshot.getData());
                } else {
                    Log.d(TAG, source + " data: null");
                }



            }
        });


    }
    //endregion


}


