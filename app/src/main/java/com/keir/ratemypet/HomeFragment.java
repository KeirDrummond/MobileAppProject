package com.keir.ratemypet;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    GoogleSignInClient googleSignInClient;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(getContext(), googleSignInOptions);

        ((MainActivity) getActivity()).ShowTaskbar();

        Button uploadBtn = view.findViewById(R.id.uploadbtn);
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).ChangeFragment(new FileUploadFragment());
            }
        });

        Button button = view.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenItemActivity();
            }
        });

        Button signOutButton = view.findViewById(R.id.signOut);
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                googleSignInClient.signOut();
                FirebaseAuth.getInstance().signOut();
            }
        });

        return view;
    }

    public void OpenItemActivity() {
        ItemFinder.getInstance().GetRandomItemList(5, new RandomListener() {
            @Override
            public void getResult(List<GalleryItem> items, List<Rating> ratings) {
                if (items.size() != 0) {
                    Intent intent = new Intent(getContext(), ItemViewActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("items", (Serializable) items);
                    bundle.putSerializable("ratings", (Serializable) ratings);
                    intent.putExtra("items", bundle);

                    startActivity(intent);
                }
            }
        });
    }

}
