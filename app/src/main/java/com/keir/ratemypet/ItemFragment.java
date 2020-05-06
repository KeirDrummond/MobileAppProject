package com.keir.ratemypet;

import android.media.ExifInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;

public class ItemFragment extends Fragment {

    GalleryItem item;

    TextView cuteRating;
    TextView funnyRating;
    TextView interestingRating;
    TextView happyRating;
    TextView surprisingRating;

    Button cuteBtn;
    Button funnyBtn;
    Button interestingBtn;
    Button happyBtn;
    Button surprisingBtn;

    TextView cuteScore;
    TextView funnyScore;
    TextView interestingScore;
    TextView happyScore;
    TextView surprisingScore;
    TextView totalScore;

    Button continueBtn;

    Rating currentRating;
    Rating newRating;

    public static ItemFragment newInstance(GalleryItem item, Rating currentRating) {
        ItemFragment fragment = new ItemFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("item", item);
        bundle.putSerializable("rating", currentRating);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item, container, false);

        this.item = (GalleryItem) getArguments().getSerializable("item");
        currentRating = (Rating) getArguments().getSerializable("rating");
        newRating = new Rating(currentRating);

        TextView textView = view.findViewById(R.id.title);
        ImageView imageView = view.findViewById(R.id.image);

        textView.setText(item.getTitle());
        Glide.with(view).load(item.getImageURL()).into(imageView);

        cuteRating = view.findViewById(R.id.cuteRating);
        funnyRating = view.findViewById(R.id.funnyRating);
        interestingRating = view.findViewById(R.id.interestingRating);
        happyRating = view.findViewById(R.id.happyRating);
        surprisingRating = view.findViewById(R.id.surprisingRating);

        UpdateView();

        cuteBtn = view.findViewById(R.id.cuteBtn);
        funnyBtn = view.findViewById(R.id.funnyBtn);
        interestingBtn = view.findViewById(R.id.interestingBtn);
        happyBtn = view.findViewById(R.id.happyBtn);
        surprisingBtn = view.findViewById(R.id.surprisingBtn);

        cuteScore = view.findViewById(R.id.cuteScore);
        funnyScore = view.findViewById(R.id.funnyScore);
        interestingScore = view.findViewById(R.id.interestingScore);
        happyScore = view.findViewById(R.id.happyScore);
        surprisingScore = view.findViewById(R.id.surprisingScore);
        totalScore = view.findViewById(R.id.totalScore);

        cuteScore.setText(String.valueOf(item.getCuteScore()));
        funnyScore.setText(String.valueOf(item.getFunnyScore()));
        interestingScore.setText(String.valueOf(item.getInterestingScore()));
        happyScore.setText(String.valueOf(item.getHappyScore()));
        surprisingScore.setText(String.valueOf(item.getSurprisingScore()));
        totalScore.setText(String.valueOf(item.getTotalScore()));

        continueBtn = view.findViewById(R.id.continueBtn);

        cuteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (newRating.cuteScore == 0) {
                    if (newRating.cuteScore == 1 || newRating.funnyScore == 1 || newRating.interestingScore == 1 || newRating.happyScore == 1 || newRating.surprisingScore == 1) {
                        if (newRating.cuteScore == 2 || newRating.funnyScore == 2 || newRating.interestingScore == 2 || newRating.happyScore == 2 || newRating.surprisingScore == 2) {
                            if (newRating.cuteScore == 3 || newRating.funnyScore == 3 || newRating.interestingScore == 3 || newRating.happyScore == 3 || newRating.surprisingScore == 3) {
                                // Nothing
                            } else {
                                newRating.cuteScore = 3;
                            }
                        } else {
                            newRating.cuteScore = 2;
                        }
                    } else {
                        newRating.cuteScore = 1;
                    }
                } else {
                    if (newRating.cuteScore > newRating.cuteScore) { newRating.cuteScore--; }
                    if (newRating.funnyScore > newRating.cuteScore) { newRating.funnyScore--; }
                    if (newRating.interestingScore > newRating.cuteScore) { newRating.interestingScore--; }
                    if (newRating.happyScore > newRating.cuteScore) { newRating.happyScore--; }
                    if (newRating.surprisingScore > newRating.cuteScore) { newRating.surprisingScore--; }
                    newRating.cuteScore = 0;
                }
                UpdateView();
            }
        });
        funnyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (newRating.funnyScore == 0) {
                    if (newRating.cuteScore == 1 || newRating.funnyScore == 1 || newRating.interestingScore == 1 || newRating.happyScore == 1 || newRating.surprisingScore == 1) {
                        if (newRating.cuteScore == 2 || newRating.funnyScore == 2 || newRating.interestingScore == 2 || newRating.happyScore == 2 || newRating.surprisingScore == 2) {
                            if (newRating.cuteScore == 3 || newRating.funnyScore == 3 || newRating.interestingScore == 3 || newRating.happyScore == 3 || newRating.surprisingScore == 3) {
                                // Nothing
                            } else {
                                newRating.funnyScore = 3;
                            }
                        } else {
                            newRating.funnyScore = 2;
                        }
                    } else {
                        newRating.funnyScore = 1;
                    }
                } else {
                    if (newRating.cuteScore > newRating.funnyScore) { newRating.cuteScore--; }
                    if (newRating.funnyScore > newRating.funnyScore) { newRating.funnyScore--; }
                    if (newRating.interestingScore > newRating.funnyScore) { newRating.interestingScore--; }
                    if (newRating.happyScore > newRating.funnyScore) { newRating.happyScore--; }
                    if (newRating.surprisingScore > newRating.funnyScore) { newRating.surprisingScore--; }
                    newRating.funnyScore = 0;
                }
                UpdateView();
            }
        });
        interestingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (newRating.interestingScore == 0) {
                    if (newRating.cuteScore == 1 || newRating.funnyScore == 1 || newRating.interestingScore == 1 || newRating.happyScore == 1 || newRating.surprisingScore == 1) {
                        if (newRating.cuteScore == 2 || newRating.funnyScore == 2 || newRating.interestingScore == 2 || newRating.happyScore == 2 || newRating.surprisingScore == 2) {
                            if (newRating.cuteScore == 3 || newRating.funnyScore == 3 || newRating.interestingScore == 3 || newRating.happyScore == 3 || newRating.surprisingScore == 3) {
                                // Nothing
                            } else {
                                newRating.interestingScore = 3;
                            }
                        } else {
                            newRating.interestingScore = 2;
                        }
                    } else {
                        newRating.interestingScore = 1;
                    }
                } else {
                    if (newRating.cuteScore > newRating.interestingScore) { newRating.cuteScore--; }
                    if (newRating.funnyScore > newRating.interestingScore) { newRating.funnyScore--; }
                    if (newRating.interestingScore > newRating.interestingScore) { newRating.interestingScore--; }
                    if (newRating.happyScore > newRating.interestingScore) { newRating.happyScore--; }
                    if (newRating.surprisingScore > newRating.interestingScore) { newRating.surprisingScore--; }
                    newRating.interestingScore = 0;
                }
                UpdateView();
            }
        });
        happyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (newRating.happyScore == 0) {
                    if (newRating.cuteScore == 1 || newRating.funnyScore == 1 || newRating.interestingScore == 1 || newRating.happyScore == 1 || newRating.surprisingScore == 1) {
                        if (newRating.cuteScore == 2 || newRating.funnyScore == 2 || newRating.interestingScore == 2 || newRating.happyScore == 2 || newRating.surprisingScore == 2) {
                            if (newRating.cuteScore == 3 || newRating.funnyScore == 3 || newRating.interestingScore == 3 || newRating.happyScore == 3 || newRating.surprisingScore == 3) {
                                // Nothing
                            } else {
                                newRating.happyScore = 3;
                            }
                        } else {
                            newRating.happyScore = 2;
                        }
                    } else {
                        newRating.happyScore = 1;
                    }
                } else {
                    if (newRating.cuteScore > newRating.happyScore) { newRating.cuteScore--; }
                    if (newRating.funnyScore > newRating.happyScore) { newRating.funnyScore--; }
                    if (newRating.interestingScore > newRating.happyScore) { newRating.interestingScore--; }
                    if (newRating.happyScore > newRating.happyScore) { newRating.happyScore--; }
                    if (newRating.surprisingScore > newRating.happyScore) { newRating.surprisingScore--; }
                    newRating.happyScore = 0;
                }
                UpdateView();
            }
        });
        surprisingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (newRating.surprisingScore == 0) {
                    if (newRating.cuteScore == 1 || newRating.funnyScore == 1 || newRating.interestingScore == 1 || newRating.happyScore == 1 || newRating.surprisingScore == 1) {
                        if (newRating.cuteScore == 2 || newRating.funnyScore == 2 || newRating.interestingScore == 2 || newRating.happyScore == 2 || newRating.surprisingScore == 2) {
                            if (newRating.cuteScore == 3 || newRating.funnyScore == 3 || newRating.interestingScore == 3 || newRating.happyScore == 3 || newRating.surprisingScore == 3) {
                                // Nothing
                            } else {
                                newRating.surprisingScore = 3;
                            }
                        } else {
                            newRating.surprisingScore = 2;
                        }
                    } else {
                        newRating.surprisingScore = 1;
                    }
                } else {
                    if (newRating.cuteScore > newRating.surprisingScore) { newRating.cuteScore--; }
                    if (newRating.funnyScore > newRating.surprisingScore) { newRating.funnyScore--; }
                    if (newRating.interestingScore > newRating.surprisingScore) { newRating.interestingScore--; }
                    if (newRating.happyScore > newRating.surprisingScore) { newRating.happyScore--; }
                    if (newRating.surprisingScore > newRating.surprisingScore) { newRating.surprisingScore--; }
                    newRating.surprisingScore = 0;
                }
                UpdateView();
            }
        });

        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Continue();
            }
        });

        return view;
    }

    private void UpdateView() {
        if (newRating.cuteScore == 1)
        { cuteRating.setText("1st"); }
        else if (newRating.cuteScore == 2)
        { cuteRating.setText("2nd"); }
        else if (newRating.cuteScore == 3)
        { cuteRating.setText("3rd"); } else
            { cuteRating.setText("-"); }
        if (newRating.funnyScore == 1) { funnyRating.setText("1st"); } else if (newRating.funnyScore == 2) { funnyRating.setText("2nd"); } else if (newRating.funnyScore == 3) { funnyRating.setText("3rd"); } else { funnyRating.setText("-"); }
        if (newRating.interestingScore == 1) { interestingRating.setText("1st"); } else if (newRating.interestingScore == 2) { interestingRating.setText("2nd"); } else if (newRating.interestingScore == 3) { interestingRating.setText("3rd"); } else { interestingRating.setText("-"); }
        if (newRating.happyScore == 1) { happyRating.setText("1st"); } else if (newRating.happyScore == 2) { happyRating.setText("2nd"); } else if (newRating.happyScore == 3) { happyRating.setText("3rd"); } else { happyRating.setText("-"); }
        if (newRating.surprisingScore == 1) { surprisingRating.setText("1st"); } else if (newRating.surprisingScore == 2) { surprisingRating.setText("2nd"); } else if (newRating.surprisingScore == 3) { surprisingRating.setText("3rd"); } else { surprisingRating.setText("-"); }
    }

    private void Continue() {
        if (CheckForChanges()) {
            final FirebaseFirestore firestore = FirebaseFirestore.getInstance();

            firestore.runTransaction(new Transaction.Function<Void>() {
                @Nullable
                @Override
                public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                    DocumentReference itemRef = firestore.collection("images").document(item.getId());
                    DocumentReference ratingRef = firestore.collection("users").document(Session.getInstance().getCurrentUser().getUserID()).collection("ratings")
                            .document(newRating.getRatingId());

                    long cuteScore = 0;
                    long funnyScore = 0;
                    long interestingScore = 0;
                    long happyScore = 0;
                    long surprisingScore = 0;

                    if (currentRating.cuteScore == 1) {
                        cuteScore = -8;
                    } else if (currentRating.cuteScore == 2) {
                        cuteScore = -5;
                    } else if (currentRating.cuteScore == 3) {
                        cuteScore = -2;
                    }
                    if (newRating.cuteScore == 1) {
                        cuteScore += 8;
                    } else if (newRating.cuteScore == 2) {
                        cuteScore += 5;
                    } else if (newRating.cuteScore == 3) {
                        cuteScore += 2;
                    }

                    if (currentRating.funnyScore == 1) {
                        funnyScore = -8;
                    } else if (currentRating.funnyScore == 2) {
                        funnyScore = -5;
                    } else if (currentRating.funnyScore == 3) {
                        funnyScore = -2;
                    }
                    if (newRating.funnyScore == 1) {
                        funnyScore += 8;
                    } else if (newRating.funnyScore == 2) {
                        funnyScore += 5;
                    } else if (newRating.funnyScore == 3) {
                        funnyScore += 2;
                    }

                    if (currentRating.interestingScore == 1) {
                        interestingScore = -8;
                    } else if (currentRating.interestingScore == 2) {
                        interestingScore = -5;
                    } else if (currentRating.interestingScore == 3) {
                        interestingScore = -2;
                    }
                    if (newRating.interestingScore == 1) {
                        interestingScore += 8;
                    } else if (newRating.interestingScore == 2) {
                        interestingScore += 5;
                    } else if (newRating.interestingScore == 3) {
                        interestingScore += 2;
                    }

                    if (currentRating.happyScore == 1) {
                        happyScore = -8;
                    } else if (currentRating.happyScore == 2) {
                        happyScore = -5;
                    } else if (currentRating.happyScore == 3) {
                        happyScore = -2;
                    }
                    if (newRating.happyScore == 1) {
                        happyScore += 8;
                    } else if (newRating.happyScore == 2) {
                        happyScore += 5;
                    } else if (newRating.happyScore == 3) {
                        happyScore += 2;
                    }

                    if (currentRating.surprisingScore == 1) {
                        surprisingScore = -8;
                    } else if (currentRating.surprisingScore == 2) {
                        surprisingScore = -5;
                    } else if (currentRating.surprisingScore == 3) {
                        surprisingScore = -2;
                    }
                    if (newRating.surprisingScore == 1) {
                        surprisingScore += 8;
                    } else if (newRating.surprisingScore == 2) {
                        surprisingScore += 5;
                    } else if (newRating.surprisingScore == 3) {
                        surprisingScore += 2;
                    }

                    transaction.update(itemRef, "cuteScore", FieldValue.increment(cuteScore));
                    transaction.update(itemRef, "funnyScore", FieldValue.increment(funnyScore));
                    transaction.update(itemRef, "interestingScore", FieldValue.increment(interestingScore));
                    transaction.update(itemRef, "happyScore", FieldValue.increment(happyScore));
                    transaction.update(itemRef, "surprisingScore", FieldValue.increment(surprisingScore));

                    long totalScore = (cuteScore + funnyScore + interestingScore + happyScore + surprisingScore) * 2;
                    transaction.update(itemRef, "totalScore", FieldValue.increment(totalScore));

                    transaction.set(ratingRef, newRating);

                    return null;
                }
            }).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    ((ItemViewActivity) getActivity()).Continue();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // Error
                }
            });
        }
        else { ((ItemViewActivity)getActivity()).Continue(); }
    }

    private boolean CheckForChanges() {
        if (currentRating.cuteScore != newRating.cuteScore) { return true; }
        if (currentRating.funnyScore != newRating.funnyScore) { return true; }
        if (currentRating.interestingScore != newRating.interestingScore) { return true; }
        if (currentRating.happyScore != newRating.happyScore) { return true; }
        if (currentRating.surprisingScore != newRating.surprisingScore) { return true; }
        return false;
    }

}
