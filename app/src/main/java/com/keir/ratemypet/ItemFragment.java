package com.keir.ratemypet;

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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
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

    long cuteScore;
    long funnyScore;
    long interestingScore;
    long happyScore;
    long surprisingScore;
    long totalScore;

    TextView cuteScoreText;
    TextView funnyScoreText;
    TextView interestingScoreText;
    TextView happyScoreText;
    TextView surprisingScoreText;
    TextView totalScoreText;

    Button continueBtn;

    Rating currentRating;
    Rating newRating;

    int firstValue = 8;
    int secondValue = 5;
    int thirdValue = 2;

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

        cuteBtn = view.findViewById(R.id.cuteBtn);
        funnyBtn = view.findViewById(R.id.funnyBtn);
        interestingBtn = view.findViewById(R.id.interestingBtn);
        happyBtn = view.findViewById(R.id.happyBtn);
        surprisingBtn = view.findViewById(R.id.surprisingBtn);

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
                    DocumentReference ratingRef = firestore.collection("users").document(Session.getInstance().getCurrentUser().getUserID()).collection("ratings")
                            .document(newRating.getRatingId());

                    long cuteNewScore = 0;
                    long funnyNewScore = 0;
                    long interestingNewScore = 0;
                    long happyNewScore = 0;
                    long surprisingNewScore = 0;

                    if (currentRating.cuteScore == 1) {
                        cuteNewScore = -firstValue;
                    } else if (currentRating.cuteScore == thirdValue) {
                        cuteNewScore = -secondValue;
                    } else if (currentRating.cuteScore == 3) {
                        cuteNewScore = -thirdValue;
                    }
                    if (newRating.cuteScore == 1) {
                        cuteNewScore += firstValue;
                    } else if (newRating.cuteScore == thirdValue) {
                        cuteNewScore += secondValue;
                    } else if (newRating.cuteScore == 3) {
                        cuteNewScore += thirdValue;
                    }

                    if (currentRating.funnyScore == 1) {
                        funnyNewScore = -firstValue;
                    } else if (currentRating.funnyScore == thirdValue) {
                        funnyNewScore = -secondValue;
                    } else if (currentRating.funnyScore == 3) {
                        funnyNewScore = -thirdValue;
                    }
                    if (newRating.funnyScore == 1) {
                        funnyNewScore += firstValue;
                    } else if (newRating.funnyScore == thirdValue) {
                        funnyNewScore += secondValue;
                    } else if (newRating.funnyScore == 3) {
                        funnyNewScore += thirdValue;
                    }

                    if (currentRating.interestingScore == 1) {
                        interestingNewScore = -firstValue;
                    } else if (currentRating.interestingScore == thirdValue) {
                        interestingNewScore = -secondValue;
                    } else if (currentRating.interestingScore == 3) {
                        interestingNewScore = -thirdValue;
                    }
                    if (newRating.interestingScore == 1) {
                        interestingNewScore += firstValue;
                    } else if (newRating.interestingScore == thirdValue) {
                        interestingNewScore += secondValue;
                    } else if (newRating.interestingScore == 3) {
                        interestingNewScore += thirdValue;
                    }

                    if (currentRating.happyScore == 1) {
                        happyNewScore = -firstValue;
                    } else if (currentRating.happyScore == thirdValue) {
                        happyNewScore = -secondValue;
                    } else if (currentRating.happyScore == 3) {
                        happyNewScore = -thirdValue;
                    }
                    if (newRating.happyScore == 1) {
                        happyNewScore += firstValue;
                    } else if (newRating.happyScore == thirdValue) {
                        happyNewScore += secondValue;
                    } else if (newRating.happyScore == 3) {
                        happyNewScore += thirdValue;
                    }

                    if (currentRating.surprisingScore == 1) {
                        surprisingNewScore = -firstValue;
                    } else if (currentRating.surprisingScore == thirdValue) {
                        surprisingNewScore = -secondValue;
                    } else if (currentRating.surprisingScore == 3) {
                        surprisingNewScore = -thirdValue;
                    }
                    if (newRating.surprisingScore == 1) {
                        surprisingNewScore += firstValue;
                    } else if (newRating.surprisingScore == thirdValue) {
                        surprisingNewScore += secondValue;
                    } else if (newRating.surprisingScore == 3) {
                        surprisingNewScore += thirdValue;
                    }

                    transaction.update(itemRef, "cuteScore", FieldValue.increment(cuteNewScore));
                    transaction.update(itemRef, "funnyScore", FieldValue.increment(funnyNewScore));
                    transaction.update(itemRef, "interestingScore", FieldValue.increment(interestingNewScore));
                    transaction.update(itemRef, "happyScore", FieldValue.increment(happyNewScore));
                    transaction.update(itemRef, "surprisingScore", FieldValue.increment(surprisingNewScore));

                    long totalScore = (cuteNewScore + funnyNewScore + interestingNewScore + happyNewScore + surprisingNewScore) * 2;
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
