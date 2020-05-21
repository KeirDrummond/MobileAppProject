package com.keir.ratemypet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;

import java.util.HashMap;

public class GenericItemFragment extends ItemFragment {

    private GalleryItem item;

    private TextView cuteRating;
    private TextView funnyRating;
    private TextView interestingRating;
    private TextView happyRating;
    private TextView surprisingRating;

    private long cuteScore;
    private long funnyScore;
    private long interestingScore;
    private long happyScore;
    private long surprisingScore;
    private long totalScore;

    private TextView cuteScoreText;
    private TextView funnyScoreText;
    private TextView interestingScoreText;
    private TextView happyScoreText;
    private TextView surprisingScoreText;
    private TextView totalScoreText;

    private Rating currentRating;
    private Rating newRating;

    private int firstValue = 8;
    private int secondValue = 5;
    private int thirdValue = 2;

    public static GenericItemFragment newInstance(GalleryItem item, Rating currentRating) {
        GenericItemFragment fragment = new GenericItemFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("item", item);
        bundle.putSerializable("rating", currentRating);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_genericitem, container, false);

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

        ImageButton cuteBtn = view.findViewById(R.id.cuteBtn);
        ImageButton funnyBtn = view.findViewById(R.id.funnyBtn);
        ImageButton interestingBtn = view.findViewById(R.id.interestingBtn);
        ImageButton happyBtn = view.findViewById(R.id.happyBtn);
        ImageButton surprisingBtn = view.findViewById(R.id.surprisingBtn);

        cuteScoreText = view.findViewById(R.id.cuteScore);
        funnyScoreText = view.findViewById(R.id.funnyScore);
        interestingScoreText = view.findViewById(R.id.interestingScore);
        happyScoreText = view.findViewById(R.id.happyScore);
        surprisingScoreText = view.findViewById(R.id.surprisingScore);
        totalScoreText = view.findViewById(R.id.totalScore);

        cuteScore = item.getCuteScore();
        funnyScore = item.getFunnyScore();
        interestingScore = item.getInterestingScore();
        happyScore = item.getHappyScore();
        surprisingScore = item.getSurprisingScore();
        totalScore = item.getTotalScore();

        if (currentRating.cuteScore == 1) { cuteScore -= firstValue; totalScore -= firstValue * 2; } else if (currentRating.cuteScore == 2) { cuteScore -= secondValue; totalScore -= secondValue * 2; } else if (currentRating.cuteScore == 3) { cuteScore -= thirdValue; totalScore -= thirdValue * 2; }
        if (currentRating.funnyScore == 1) { funnyScore -= firstValue; totalScore -= firstValue * 2; } else if (currentRating.funnyScore == 2) { funnyScore -= secondValue; totalScore -= secondValue * 2; } else if (currentRating.funnyScore == 3) { funnyScore -= thirdValue; totalScore -= thirdValue * 2; }
        if (currentRating.interestingScore == 1) { interestingScore -= firstValue; totalScore -= firstValue * 2; } else if (currentRating.interestingScore == 2) { interestingScore -= secondValue; totalScore -= secondValue * 2; } else if (currentRating.interestingScore == 3) { interestingScore -= thirdValue; totalScore -= thirdValue * 2; }
        if (currentRating.happyScore == 1) { happyScore -= firstValue; totalScore -= firstValue * 2; } else if (currentRating.happyScore == 2) { happyScore -= secondValue; totalScore -= secondValue * 2; } else if (currentRating.happyScore == 3) { happyScore -= thirdValue; totalScore -= thirdValue * 2; }
        if (currentRating.surprisingScore == 1) { surprisingScore -= firstValue; totalScore -= firstValue * 2; } else if (currentRating.surprisingScore == 2) { surprisingScore -= secondValue; totalScore -= secondValue * 2; } else if (currentRating.surprisingScore == 3) { surprisingScore -= thirdValue; totalScore -= thirdValue * 2; }

        Button continueBtn = view.findViewById(R.id.continueBtn);

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

        UpdateView();

        return view;
    }

    private void UpdateView() {

        long newTotalScore = totalScore;

        switch((int)newRating.cuteScore)
        {
            case 1:
                cuteRating.setText("1st");
                cuteScoreText.setText(String.valueOf(cuteScore + firstValue));
                newTotalScore += firstValue * 2;
                break;
            case 2:
                cuteRating.setText("2nd");
                cuteScoreText.setText(String.valueOf(cuteScore + secondValue));
                newTotalScore += secondValue * 2;
                break;
            case 3:
                cuteRating.setText("3rd");
                cuteScoreText.setText(String.valueOf(cuteScore + thirdValue));
                newTotalScore += thirdValue * 2;
                break;
            default:
                cuteRating.setText("-");
                cuteScoreText.setText(String.valueOf(cuteScore));
                break;
        }
        switch((int)newRating.funnyScore)
        {
            case 1:
                funnyRating.setText("1st");
                funnyScoreText.setText(String.valueOf(funnyScore + firstValue));
                newTotalScore += firstValue * 2;
                break;
            case 2:
                funnyRating.setText("2nd");
                funnyScoreText.setText(String.valueOf(funnyScore + secondValue));
                newTotalScore += secondValue * 2;
                break;
            case 3:
                funnyRating.setText("3rd");
                funnyScoreText.setText(String.valueOf(funnyScore + thirdValue));
                newTotalScore += thirdValue * 2;
                break;
            default:
                funnyRating.setText("-");
                funnyScoreText.setText(String.valueOf(funnyScore));
                break;
        }
        switch((int)newRating.interestingScore)
        {
            case 1:
                interestingRating.setText("1st");
                interestingScoreText.setText(String.valueOf(interestingScore + firstValue));
                newTotalScore += firstValue * 2;
                break;
            case 2:
                interestingRating.setText("2nd");
                interestingScoreText.setText(String.valueOf(interestingScore + secondValue));
                newTotalScore += secondValue * 2;
                break;
            case 3:
                interestingRating.setText("3rd");
                interestingScoreText.setText(String.valueOf(interestingScore + thirdValue));
                newTotalScore += thirdValue * 2;
                break;
            default:
                interestingRating.setText("-");
                interestingScoreText.setText(String.valueOf(interestingScore));
                break;
        }
        switch((int)newRating.happyScore)
        {
            case 1:
                happyRating.setText("1st");
                happyScoreText.setText(String.valueOf(happyScore + firstValue));
                newTotalScore += firstValue * 2;
                break;
            case 2:
                happyRating.setText("2nd");
                happyScoreText.setText(String.valueOf(happyScore + secondValue));
                newTotalScore += secondValue * 2;
                break;
            case 3:
                happyRating.setText("3rd");
                happyScoreText.setText(String.valueOf(happyScore + thirdValue));
                newTotalScore += thirdValue * 2;
                break;
            default:
                happyRating.setText("-");
                happyScoreText.setText(String.valueOf(happyScore));
                break;
        }
        switch((int)newRating.surprisingScore)
        {
            case 1:
                surprisingRating.setText("1st");
                surprisingScoreText.setText(String.valueOf(surprisingScore + firstValue));
                newTotalScore += firstValue * 2;
                break;
            case 2:
                surprisingRating.setText("2nd");
                surprisingScoreText.setText(String.valueOf(surprisingScore + secondValue));
                newTotalScore += secondValue * 2;
                break;
            case 3:
                surprisingRating.setText("3rd");
                surprisingScoreText.setText(String.valueOf(surprisingScore + thirdValue));
                newTotalScore += thirdValue * 2;
                break;
            default:
                surprisingRating.setText("-");
                surprisingScoreText.setText(String.valueOf(surprisingScore));
                break;
        }

        totalScoreText.setText(String.valueOf(newTotalScore));
    }

    private void Continue() {
        if (CheckForChanges()) {
            final FirebaseFirestore firestore = FirebaseFirestore.getInstance();

            firestore.runTransaction(new Transaction.Function<Void>() {
                @Nullable
                @Override
                public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                    DocumentReference itemRef = firestore.collection("images").document(item.getId());
                    DocumentReference ratingRef = firestore.collection("images").document(item.getId()).collection("ratings").document(newRating.getRatingId());
                    DocumentReference userRatingRef = firestore.collection("users").document(Session.getInstance().getCurrentUser().getUserId()).collection("ratings")
                            .document(newRating.getRatingId());

                    long cuteOldScore = 0;
                    long funnyOldScore = 0;
                    long interestingOldScore = 0;
                    long happyOldScore = 0;
                    long surprisingOldScore = 0;

                    long cuteNewScore = 0;
                    long funnyNewScore = 0;
                    long interestingNewScore = 0;
                    long happyNewScore = 0;
                    long surprisingNewScore = 0;

                    if (currentRating.cuteScore == 1) {
                        cuteOldScore = firstValue;
                    } else if (currentRating.cuteScore == 2) {
                        cuteOldScore = secondValue;
                    } else if (currentRating.cuteScore == 3) {
                        cuteOldScore = thirdValue;
                    }
                    if (newRating.cuteScore == 0) {
                        cuteNewScore = -cuteOldScore;
                    } else if (newRating.cuteScore == 1) {
                        cuteNewScore = -cuteOldScore + firstValue;
                    } else if (newRating.cuteScore == thirdValue) {
                        cuteNewScore = -cuteOldScore + secondValue;
                    } else if (newRating.cuteScore == 3) {
                        cuteNewScore = -cuteOldScore + thirdValue;
                    }

                    if (currentRating.funnyScore == 1) {
                        funnyOldScore = firstValue;
                    } else if (currentRating.funnyScore == 2) {
                        funnyOldScore = secondValue;
                    } else if (currentRating.funnyScore == 3) {
                        funnyOldScore = thirdValue;
                    }
                    if (newRating.funnyScore == 0) {
                        funnyNewScore = -funnyOldScore;
                    } else if (newRating.funnyScore == 1) {
                        funnyNewScore = -funnyOldScore + firstValue;
                    } else if (newRating.funnyScore == thirdValue) {
                        funnyNewScore = -funnyOldScore + secondValue;
                    } else if (newRating.funnyScore == 3) {
                        funnyNewScore = -funnyOldScore + thirdValue;
                    }

                    if (currentRating.interestingScore == 1) {
                        interestingOldScore = firstValue;
                    } else if (currentRating.interestingScore == 2) {
                        interestingOldScore = secondValue;
                    } else if (currentRating.interestingScore == 3) {
                        interestingOldScore = thirdValue;
                    }
                    if (newRating.interestingScore == 0) {
                        interestingNewScore = -interestingOldScore;
                    } else if (newRating.interestingScore == 1) {
                        interestingNewScore = -interestingOldScore + firstValue;
                    } else if (newRating.interestingScore == thirdValue) {
                        interestingNewScore = -interestingOldScore + secondValue;
                    } else if (newRating.interestingScore == 3) {
                        interestingNewScore = -interestingOldScore + thirdValue;
                    }

                    if (currentRating.happyScore == 1) {
                        happyOldScore = firstValue;
                    } else if (currentRating.happyScore == 2) {
                        happyOldScore = secondValue;
                    } else if (currentRating.happyScore == 3) {
                        happyOldScore = thirdValue;
                    }
                    if (newRating.happyScore == 0) {
                        happyNewScore = -happyOldScore;
                    } else if (newRating.happyScore == 1) {
                        happyNewScore = -happyOldScore + firstValue;
                    } else if (newRating.happyScore == thirdValue) {
                        happyNewScore = -happyOldScore + secondValue;
                    } else if (newRating.happyScore == 3) {
                        happyNewScore = -happyOldScore + thirdValue;
                    }

                    if (currentRating.surprisingScore == 1) {
                        surprisingOldScore = firstValue;
                    } else if (currentRating.surprisingScore == 2) {
                        surprisingOldScore = secondValue;
                    } else if (currentRating.surprisingScore == 3) {
                        surprisingOldScore = thirdValue;
                    }
                    if (newRating.surprisingScore == 0) {
                        surprisingNewScore = -surprisingOldScore;
                    } else if (newRating.surprisingScore == 1) {
                        surprisingNewScore = -surprisingOldScore + firstValue;
                    } else if (newRating.surprisingScore == thirdValue) {
                        surprisingNewScore = -surprisingOldScore + secondValue;
                    } else if (newRating.surprisingScore == 3) {
                        surprisingNewScore = -surprisingOldScore + thirdValue;
                    }

                    transaction.update(itemRef, "cuteScore", FieldValue.increment(cuteNewScore));
                    transaction.update(itemRef, "funnyScore", FieldValue.increment(funnyNewScore));
                    transaction.update(itemRef, "interestingScore", FieldValue.increment(interestingNewScore));
                    transaction.update(itemRef, "happyScore", FieldValue.increment(happyNewScore));
                    transaction.update(itemRef, "surprisingScore", FieldValue.increment(surprisingNewScore));

                    long totalNewScore = (cuteNewScore + funnyNewScore + interestingNewScore + happyNewScore + surprisingNewScore) * 2;
                    transaction.update(itemRef, "totalScore", FieldValue.increment(totalNewScore));

                    DocumentReference userRef = firestore.collection("users").document(Session.getInstance().getCurrentUser().getUserId());
                    transaction.update(userRef, "ratingScore", FieldValue.increment(totalNewScore));

                    DocumentReference uploaderRef = firestore.collection("users").document(item.getUploaderId());
                    transaction.update(uploaderRef, "uploadScore", FieldValue.increment(totalNewScore));

                    transaction.set(ratingRef, newRating);
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("ratingId", newRating.getRatingId());
                    map.put("uploadId", newRating.getUploadId());
                    transaction.set(userRatingRef, map);

                    return null;
                }
            }).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Session.getInstance().getCurrentUser().UpdateUser();
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
