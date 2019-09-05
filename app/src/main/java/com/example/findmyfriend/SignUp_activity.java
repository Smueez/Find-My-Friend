package com.example.findmyfriend;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.net.URI;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUp_activity extends AppCompatActivity {
    Spinner spinner_sex,spinner_month, spinner_date;
    CircleImageView imageView_pro_pic;
    TextView textView_year,textView_name,textView_email,text_error;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    FirebaseAuth auth;
    Uri uri;
    int result_loead_image =1;
    String TAG = "sign  up page";
    String date_str,year_str,month_str,sex_str,email_str,name_str,phone_no_str;
    Intent intent_map;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        spinner_date = findViewById(R.id.spinner2);
        spinner_month =findViewById(R.id.spinner3);
        spinner_sex = findViewById(R.id.spinner_gender);
        imageView_pro_pic = findViewById(R.id.profile_image);
        textView_year = findViewById(R.id.year);
        textView_email = findViewById(R.id.editText4);
        textView_name = findViewById(R.id.editText3);
        auth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        text_error = findViewById(R.id.textView5);
        intent_map = new Intent(this,MapsActivity.class);
        phone_no_str = getIntent().getExtras().getString("phone_no");
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == result_loead_image && resultCode == RESULT_OK && data != null && data.getData() != null){
            uri = data.getData();
            imageView_pro_pic.setImageURI(uri);
            Log.d(TAG, "onActivityResult: Pic is loading!");

        }
    }
    public void change_pic(View view){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,result_loead_image);
    }
    public void signup(View view){
        name_str = textView_name.getText().toString().trim();
        email_str = textView_email.getText().toString().trim();
        year_str = textView_year.getText().toString().trim();
        date_str = spinner_date.getSelectedItem().toString();
        month_str = spinner_month.getSelectedItem().toString();
        sex_str = spinner_sex.getSelectedItem().toString();

        if (!name_str.isEmpty() && !email_str.isEmpty() && year_str.isEmpty() && !date_str.isEmpty() && !month_str.isEmpty() && sex_str.isEmpty()){

            if(isValidEmailAddress(email_str)){

                storageReference.child("profile").child(phone_no_str).child("pro_pic").child(phone_no_str+getFileExtention(uri)).putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        String image_url = "profile/"+phone_no_str+"pro_pic"+phone_no_str+getFileExtention(uri);
                        String birthday = date_str+" - "+month_str+" - "+year_str;
                        Profile_info profile_info = new Profile_info(name_str,email_str,phone_no_str,sex_str,birthday,"empty","empty",image_url,"empty");
                        databaseReference.child("profile").setValue(profile_info);
                        startActivity(intent_map);
                        Toast.makeText(getApplicationContext(),"Sign up done! :)",Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),"Something went wrong! please try again",Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "onFailure: "+e.getMessage());
                    }
                });

            }
            else {
                text_error.setText("Email is not valid!");
            }

        }
        else {
            text_error.setText("Some information is may be missing!");
        }


    }
    private String getFileExtention(Uri imguri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(imguri));
    }
    public static boolean isValidEmailAddress(String emailAddress) {
        String emailRegEx;
        Pattern pattern;
        // Regex for a valid email address
        emailRegEx = "^[A-Za-z0-9._%+\\-]+@[A-Za-z0-9.\\-]+\\.[A-Za-z]{2,4}$";
        // Compare the regex with the email address
        pattern = Pattern.compile(emailRegEx);
        Matcher matcher = pattern.matcher(emailAddress);
        if (!matcher.find()) {
            return false;
        }
        return true;
    }
    @Override
    public void onBackPressed() {
        // super.onBackPressed(); commented this line in order to disable back press
        //Write your code here
        //Toast.makeText(getApplicationContext(), "Back press disabled!", Toast.LENGTH_SHORT).show();

    }
}
