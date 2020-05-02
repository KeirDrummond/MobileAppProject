package com.keir.ratemypet;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.UUID;

public class FileUploadFragment extends Fragment {

    Uri selectedFile;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fileupload, container, false);

        ((MainActivity) getActivity()).HideTaskbar();

        Button fileSelectBtn = view.findViewById(R.id.fileselectbtn);
        Button uploadBtn = view.findViewById(R.id.uploadbtn);

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
            selectedFile = data.getData();
        }
    }

    private boolean UploadImage(Uri file)
    {
        if (file == null) { return false; }

        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String uuid = UUID.randomUUID().toString();

        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        final StorageReference imageReference = storageReference.child("images/" + userID + "/" + uuid);

        UploadToStorage(file, imageReference);

        return true;
    }

    private void UploadToStorage(Uri file, final StorageReference imageReference) {
        final UploadTask uploadTask = imageReference.putFile(file);

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

                UploadToDatabase(url);
            }
        });

    }

    private void UploadToDatabase(String url) {
        GalleryItem item = GetUploadDetails(url);

        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        String id = db.collection("images").document().getId();

        db.collection("images").document(id).set(item);

        HashMap<String, Object> object = new HashMap<>();
        object.put("upload", id);
        db.collection("users").document(userID).collection("uploads").document().set(object);
    }

    private GalleryItem GetUploadDetails(String url)
    {
        String title = "";
        String imageURL = url;

        GalleryItem newItem = new GalleryItem(title, imageURL);

        return newItem;
    }

    private boolean ValidateItem() {
        return true;
    }

}
