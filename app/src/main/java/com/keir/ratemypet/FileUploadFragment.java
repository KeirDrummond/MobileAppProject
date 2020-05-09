package com.keir.ratemypet;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

public class FileUploadFragment extends Fragment {

    Uri selectedFile;
    ImageView imagePreview;
    EditText titleInputBox;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fileupload, container, false);

        ((MainActivity) getActivity()).ShowTaskbar();

        Button fileSelectBtn = view.findViewById(R.id.fileSelectBtn);
        Button uploadBtn = view.findViewById(R.id.uploadBtn);
        imagePreview = view.findViewById(R.id.imagePreview);
        titleInputBox = view.findViewById(R.id.titleInputBox);

        fileSelectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetFile();
            }
        });

        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ValidateItem()) { UploadImage(selectedFile); }
            }
        });

        String user = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();

        Log.d("muhtag", user);

        return view;
    }

    private void GetFile()
    {
        final int REQUEST_CODE = 42;

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 42 && resultCode == getActivity().RESULT_OK)
        {
            int rotation = getImageRotation(data.getData());
            Bitmap bitmap = intoBitmap(data.getData());
            Bitmap newBitmap = rotateBitmap(bitmap, rotation);

            selectedFile = data.getData();
            imagePreview.setImageBitmap(newBitmap);
        }
    }

    private boolean UploadImage(Uri image)
    {
        if (image == null) { return false; }

        UploadToStorage(image);

        return true;
    }

    private int getImageRotation(Uri uri) {
        int rotation = 0;
        try {
            InputStream in = getContext().getContentResolver().openInputStream(uri);
            ExifInterface exifInterface = new ExifInterface(in);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotation = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotation = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotation = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return rotation;
    }

    private Bitmap intoBitmap(Uri file) {
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    private Bitmap rotateBitmap(Bitmap bitmap, int rotation) {
        Matrix matrix = new Matrix();
        matrix.postRotate(rotation);
        Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

        return newBitmap;
    }

    private void UploadToStorage(Uri image) {
        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final String uuid = UUID.randomUUID().toString();

        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        final StorageReference imageReference = storageReference.child("images/" + userID + "/" + uuid);

        final UploadTask uploadTask = imageReference.putFile(image);

        uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                return imageReference.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                Uri downloadUri = task.getResult();
                String url = downloadUri.toString();

                UploadToDatabase(uuid, url);
            }
        });

    }

    private void UploadToDatabase(String imageId, String url) {
        String userId = Session.getInstance().getCurrentUser().getUserId();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String id = db.collection("images").document().getId();
        GalleryItem item = CreateItem(id, imageId, url);

        WriteBatch batch = db.batch();

        DocumentReference imageRef = db.collection("images").document(id);
        batch.set(imageRef, item);

        HashMap<String, Object> object = new HashMap<>();
        object.put("upload", id);
        DocumentReference userUploadRef = db.collection("users").document(userId).collection("uploads").document();
        batch.set(userUploadRef, object);

        batch.commit().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                ((MainActivity) getActivity()).ChangeFragment(new HomeFragment());
            }
        });
    }

    private GalleryItem CreateItem(String id, String imageId, String url)
    {
        String title = titleInputBox.getText().toString();
        String imageURL = url;

        String userId = Session.getInstance().getCurrentUser().getUserId();

        Date date = new Date();
        Timestamp timestamp = new Timestamp(date);

        GalleryItem newItem = new GalleryItem(id, title, imageId, imageURL, userId, timestamp);

        return newItem;
    }

    private boolean ValidateItem() {
        if (titleInputBox.getText().toString() == "") { return false; }

        return true;
    }

}
