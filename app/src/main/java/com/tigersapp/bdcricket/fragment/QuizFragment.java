package com.tigersapp.bdcricket.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tigersapp.bdcricket.R;
import com.tigersapp.bdcricket.activity.QuizActivity;
import com.tigersapp.bdcricket.util.SharedPrefData;
import com.tigersapp.bdcricket.util.Validator;

/**
 * @author Ripon
 */

public class QuizFragment extends Fragment{

    Button participateButton, startQuizButton, changeCredentialsButton;
    LinearLayout linearLayout;
    TextView userName,mobileNumber;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_quiz,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        participateButton = (Button) view.findViewById(R.id.btnParticipate);
        startQuizButton = (Button) view.findViewById(R.id.btnStartQuiz);
        changeCredentialsButton = (Button) view.findViewById(R.id.btnChangeCredentials);

        userName = (TextView) view.findViewById(R.id.name_credential);
        mobileNumber = (TextView) view.findViewById(R.id.phone_credential);

        linearLayout = (LinearLayout) view.findViewById(R.id.credential_container);
        linearLayout.setVisibility(View.GONE);

        participateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SharedPrefData.getMobileNo(getContext()).equals("no_mobileno")) {
                    insertCredentials();


                } else {
                    userName.setText("Name: "+SharedPrefData.getNickName(getContext()));
                    mobileNumber.setText("Mobile Number: "+SharedPrefData.getMobileNo(getContext()));
                    linearLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        changeCredentialsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertCredentials();
            }
        });

        startQuizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), QuizActivity.class);
                getActivity().startActivity(intent);
            }
        });
    }

    public void insertCredentials() {
        View promptsView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_quiz_credentials, null, false);
        final EditText writeComment = (EditText) promptsView.findViewById(R.id.etYourComment);
        final EditText yourName = (EditText) promptsView.findViewById(R.id.etYourName);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(promptsView);
        builder.setTitle("Your data").setPositiveButton("SUBMIT", null).setNegativeButton("CANCEL", null);

        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        Button okButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Validator.validateNotEmpty(yourName, "Required") && Validator.validateNotEmpty(writeComment, "Required")) {
                    String mobileno = writeComment.getText().toString().trim();
                    String name = yourName.getText().toString().trim();

                    SharedPrefData.setMobileNo(getContext(),mobileno);
                    SharedPrefData.setNickName(getContext(),name);
                    userName.setText("Name: "+name);
                    mobileNumber.setText("Mobile Number: "+mobileno);

                    linearLayout.setVisibility(View.VISIBLE);
                    alertDialog.dismiss();
                }
            }
        });
    }
}
