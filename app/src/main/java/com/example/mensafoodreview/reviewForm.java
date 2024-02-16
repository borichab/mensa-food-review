package com.example.mensafoodreview;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mensafoodreview.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.logging.SimpleFormatter;

public class reviewForm extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 0;
    private ImageView imageView, dishImage;
    private ImageButton cameraButton, galleryButton;
    private Button submitBtn;
    private TextView dishName, mensaName;
    private  int position;
    private TextInputLayout fullName, email;
    private EditText description;
    Uri imageUri;
    private ProgressBar progressBar;
    private RatingBar ratingBar;
    int myRating = 0;

    FirebaseDatabase rootNode, database;
    DatabaseReference reference;
    FirebaseStorage storage;

    StorageReference storageReference;

    ActivityMainBinding binding;
    ActivityResultLauncher<String> launcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_review_form);
        String sessionId = getIntent().getStringExtra("EXTRA_SESSION_ID");
        String mensa_name = getIntent().getStringExtra("mensa");
        String dish_name = getIntent().getStringExtra("dish");
        String dish_id = getIntent().getStringExtra("dish_id");

        imageView = (ImageView) findViewById(R.id.imageView);
        //cameraButton = (ImageButton) findViewById(R.id.cameraButton);
        galleryButton = (ImageButton) findViewById(R.id.galleryButton);
        submitBtn = (Button) findViewById(R.id.submitBtn);
        progressBar = (ProgressBar) findViewById(R.id.uploadDataProgressBar);
        dishName = (TextView) findViewById(R.id.dishName);
        mensaName = (TextView) findViewById(R.id.mensaName);
        fullName = (TextInputLayout) findViewById(R.id.fullname);
        email = (TextInputLayout) findViewById(R.id.email);
        description = (EditText) findViewById(R.id.description);
        dishImage = (ImageView) findViewById(R.id.dishImage);

        String uri = "@drawable/"+dish_id;  // where myresource (without the extension) is the file

        int imageResource = getResources().getIdentifier(uri, null, getPackageName());
        Drawable res = getResources().getDrawable(imageResource);
        dishImage.setImageDrawable(res);

        ratingBar = (RatingBar)findViewById(R.id.ratingBar);

        ratingBar.setNumStars(5);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                int rating = (int) v;
                myRating = (int) ratingBar.getRating();
                switch(rating){
                    case 1:
                        Toast.makeText(reviewForm.this,"Not Good",Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        Toast.makeText(reviewForm.this,"Average",Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        Toast.makeText(reviewForm.this,"Good",Toast.LENGTH_SHORT).show();
                        break;
                    case 4:
                        Toast.makeText(reviewForm.this,"Very Good",Toast.LENGTH_SHORT).show();
                        break;
                    case 5:
                        Toast.makeText(reviewForm.this,"Awesome",Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(reviewForm.this,"Nothing",Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });

        dishName.setText(dish_name);
        mensaName.setText(mensa_name);

//        cameraButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dispatchTakePictureIntent();
//            }
//        });

        galleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //selectFile();
                launcher.launch("image/*");
            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadData(mensa_name, dish_name, myRating, imageUri);
            }
        });



        launcher = registerForActivityResult(new ActivityResultContracts.GetContent()
                , new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {
                        imageUri = result;
                        Toast.makeText(getApplicationContext(), ""+imageUri,Toast.LENGTH_SHORT).show();
                        imageView.setImageURI(result);
                    }
                });
    }

    private void uploadData(String mensa_name, String dish_name, int myRating, Uri imageUri) {

        progressBar.setVisibility(View.VISIBLE);
        //Disable User Interation
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.GERMANY);
        Date now = new Date();
        String timeStamp = formatter.format(now);


        //Get Inserted Values by User
        String name =  fullName.getEditText().getText().toString();
        String emailId =  email.getEditText().getText().toString();
        String reviewDescription =  description.getText().toString();

        //Validation of inputs
        if (TextUtils.isEmpty(name)){
            fullName.setError("Name is Required");
            progressBar.setVisibility(View.INVISIBLE);
            return;
        }
        if (TextUtils.isEmpty(emailId)){
            email.setError("Email is Required");
            progressBar.setVisibility(View.INVISIBLE);
            return;
        }
        if (TextUtils.isEmpty(reviewDescription)){
            description.setError("Description is Required");
            progressBar.setVisibility(View.INVISIBLE);
            return;
        }

        if(imageUri == null) {
            String imageUrl = "";
            UserHelperClass helperClass = new UserHelperClass(name, emailId, reviewDescription, mensa_name, dish_name, myRating, imageUrl);
            rootNode = FirebaseDatabase.getInstance();
            reference = rootNode.getReference("Reviews");
            reference.child(timeStamp).setValue(helperClass).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(reviewForm.this, "Review Uploaded SuccessFully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(reviewForm.this,MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                    progressBar.setVisibility(View.INVISIBLE);
                }
            });
        }else {//
            storageReference = FirebaseStorage.getInstance().getReference(timeStamp + "/");
            storageReference.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
//                                    database.getReference(timeStamp + "/").setValue(uri + timeStamp).addOnSuccessListener(new OnSuccessListener<Void>() {
//                                        @Override
//                                        public void onSuccess(Void unused) {
//                                            Toast.makeText(getApplicationContext(), "Image Uploaded", Toast.LENGTH_SHORT).show();
//                                        }
//                                    });
                                    String imageUrl = uri + timeStamp;
                                    UserHelperClass helperClass = new UserHelperClass(name, emailId, reviewDescription, mensa_name, dish_name, myRating, imageUrl);
                                    rootNode = FirebaseDatabase.getInstance();
                                    reference = rootNode.getReference("Reviews");
                                    reference.child(timeStamp).setValue(helperClass).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(reviewForm.this, "Review Uploaded SuccessFully", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(reviewForm.this,MainActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                            finish();
                                            progressBar.setVisibility(View.INVISIBLE);
                                        }
                                    });
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(reviewForm.this, "Failed to Upload", Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.INVISIBLE);
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(reviewForm.this, "Failed to Upload", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    });
        }

//        UserHelperClass helperClass = new UserHelperClass(name, emailId, reviewDescription, mensa_name, dish_name, myRating);
//        rootNode = FirebaseDatabase.getInstance();
//        reference = rootNode.getReference("Reviews");
//        reference.child(timeStamp).setValue(helperClass).addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                Toast.makeText(reviewForm.this, "Review Uploaded SuccessFully", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(reviewForm.this,MainActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                startActivity(intent);
//                finish();
//                progressBar.setVisibility(View.INVISIBLE);
//            }
//        });
    }

//    private void selectFile() {
//        Intent selectFileIntent = new Intent();
//        selectFileIntent.setType("image/*");
//        selectFileIntent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(selectFileIntent,100);
//    }

   // @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
//            Bundle extras = data.getExtras();
//            Bitmap imageBitmap = (Bitmap) extras.get("data");
//            imageView.setImageBitmap(imageBitmap);
//        }else if(requestCode==100 && data!=null && data.getData() != null){
//            imageView.setImageURI(imageUri);
//        }
//    }
//
    //String currentPhotoPath;

//    private File createImageFile() throws IOException {
//        // Create an image file name
//        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//        String imageFileName = "JPEG_" + timeStamp + "_";
//        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
//        File image = File.createTempFile(
//                imageFileName,  /* prefix */
//                ".jpg",         /* suffix */
//                storageDir      /* directory */
//        );
//
//        // Save a file: path for use with ACTION_VIEW intents
//        currentPhotoPath = image.getAbsolutePath();
//        return image;
//    }
//
//    private void dispatchTakePictureIntent() {
//        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        // Ensure that there's a camera activity to handle the intent
//        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
//            // Create the File where the photo should go
//            File photoFile = null;
//            try {
//                photoFile = createImageFile();
//            } catch (IOException ex) {
//                // Error occurred while creating the File
//                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
//            }
//            // Continue only if the File was successfully created
//            if (photoFile != null) {
//                Uri photoURI = FileProvider.getUriForFile(this,
//                        "com.example.android.fileprovider",
//                        photoFile);
//                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
//                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
//            }
//        }
//    }
//
//    private void galleryAddPic() {
//        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//        File f = new File(currentPhotoPath);
//        Uri contentUri = Uri.fromFile(f);
//        mediaScanIntent.setData(contentUri);
//        this.sendBroadcast(mediaScanIntent);
//    }
//
//    private void setPic() {
//        // Get the dimensions of the View
//        int targetW = imageView.getWidth();
//        int targetH = imageView.getHeight();
//
//        // Get the dimensions of the bitmap
//        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
//        bmOptions.inJustDecodeBounds = true;
//
//        BitmapFactory.decodeFile(currentPhotoPath, bmOptions);
//
//        int photoW = bmOptions.outWidth;
//        int photoH = bmOptions.outHeight;
//
//        // Determine how much to scale down the image
//        int scaleFactor = Math.max(1, Math.min(photoW/targetW, photoH/targetH));
//
//        // Decode the image file into a Bitmap sized to fill the View
//        bmOptions.inJustDecodeBounds = false;
//        bmOptions.inSampleSize = scaleFactor;
//        bmOptions.inPurgeable = true;
//
//        Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath, bmOptions);
//        imageView.setImageBitmap(bitmap);
//    }
}