package com.keir.ratemypet;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.UUID;

public class FileUploadFragment extends Fragment {

    Uri selectedFile;

    private StorageReference storageReference;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fileupload, container, false);

        ((MainActivity) getActivity()).HideTaskbar();

        storageReference = FirebaseStorage.getInstance().getReference();

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
                UploadImage(selectedFile);
            }
        });

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

        final HashMap<String, Object> details = GetUploadDetails();

        final String rnduuid = UUID.randomUUID().toString();
        final StorageReference imageRef = storageReference.child("images/" + rnduuid + ".jpg");

        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        UploadTask uploadTask = imageRef.putFile(file);

        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                return imageRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                db.collection("images").document(rnduuid).set(details);
                db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("uploads").document(rnduuid).set(details);
            }
        });

        return true;
    }

    private HashMap<String, Object> GetUploadDetails()
    {
        HashMap<String, Object> details = new HashMap<>();

        return details;
    }

}
