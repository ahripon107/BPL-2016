package com.tigersapp.bdcricket.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.tigersapp.bdcricket.R;
import com.tigersapp.bdcricket.model.Comment;
import com.tigersapp.bdcricket.model.Question;
import com.tigersapp.bdcricket.util.Constants;
import com.tigersapp.bdcricket.util.FetchFromWeb;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import dmax.dialog.SpotsDialog;

/**
 * @author Ripon
 */

public class QuizActivity extends AppCompatActivity {

    CountDownTimer tmr;

    Question q;
    TextView quesno;
    TextView ques;
    TextView Timer;
    TextView img;
    RadioGroup rg;
    RadioButton Option1;
    RadioButton Option2;
    RadioButton Option3;
    RadioButton Option4;
    Button Submit;
    Button Skip;
    int index = 0;
    int marks = 0;
    int endFlag;
    Typeface banglafont;

    ArrayList<Question> questions;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        questions = new ArrayList<>();

        quesno = (TextView) findViewById(R.id.tvQuesNo);
        ques = (TextView) findViewById(R.id.tvQuestion);
        Timer = (TextView) findViewById(R.id.tvTimer);
        rg = (RadioGroup) findViewById(R.id.OptionsGroup);
        Option1 = (RadioButton) findViewById(R.id.option1);
        Option2 = (RadioButton) findViewById(R.id.option2);
        Option3 = (RadioButton) findViewById(R.id.option3);
        Option4 = (RadioButton) findViewById(R.id.option4);
        Submit = (Button) findViewById(R.id.btnSubmit);
        Skip = (Button) findViewById(R.id.btnSkip);
        img = (TextView) findViewById(R.id.myImageView);
        ques.setTypeface(banglafont);
        Option1.setTypeface(banglafont);
        Option2.setTypeface(banglafont);
        Option3.setTypeface(banglafont);
        Option4.setTypeface(banglafont);

        endFlag = 0;

        fetchQuestions();


        Submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                RadioButton temp = (RadioButton) findViewById(rg
                        .getCheckedRadioButtonId());
                if (temp == null) {
                    Toast.makeText(getApplicationContext(),
                            "Please select an answer", Toast.LENGTH_LONG)
                            .show();
                } else {
                    String ans = temp.getText().toString();
                    if (ans.equals(q.getCorrect())) {
                        marks++;
                    }
                    index++;
                }

                if (index < questions.size()) {
                    q = questions.get(index);
                    quesno.setText("QUES NO: ");
                    img.setText(" " + (index + 1) + "/" + questions.size());
                    ques.setText(q.getQues());
                    Option1.setText(q.getOption1());
                    Option2.setText(q.getOption2());
                    Option3.setText(q.getOption3());
                    Option4.setText(q.getOption4());
                    rg.clearCheck();

                } else {
                    tmr.cancel();
                    endFlag = 1;
                    Intent intent = new Intent(QuizActivity.this,QuizResultActivity.class);
                    intent.putExtra("marks", marks + "");
                    intent.putExtra("total", index + "");
                    startActivity(intent);
                }
            }
        });
        Skip.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                index++;
                if (index < questions.size()) {
                    q = questions.get(index);
                    quesno.setText("QUES NO: ");
                    img.setText(" " + (index + 1) + "/" + questions.size());
                    ques.setText(q.getQues());
                    Option1.setText(q.getOption1());
                    Option2.setText(q.getOption2());
                    Option3.setText(q.getOption3());
                    Option4.setText(q.getOption4());
                    rg.clearCheck();

                } else {
                    tmr.cancel();
                    endFlag = 1;
                    Intent intent = new Intent(QuizActivity.this,QuizResultActivity.class);
                    intent.putExtra("marks", marks + "");
                    intent.putExtra("total", index + "");
                    startActivity(intent);
                }
            }
        });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub

        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                new AlertDialog.Builder(QuizActivity.this)
                        .setMessage("Are you sure to quit the exam?")
                        .setCancelable(false)
                        .setPositiveButton("Yes",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        tmr.cancel();
                                        finish();
                                        dialog.cancel();
                                    }
                                })
                        .setNegativeButton("No",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        dialog.cancel();
                                    }
                                }).show();
                break;
        }

        return super.onKeyDown(keyCode, event);
    }

    public void fetchQuestions() {
        final Gson gson = new Gson();
        final AlertDialog progressDialog = new SpotsDialog(QuizActivity.this, R.style.Custom);
        progressDialog.show();
        progressDialog.setCancelable(true);
        RequestParams requestParams = new RequestParams();

        requestParams.add("key", "bl905577");
        requestParams.add("quizid", Constants.QUIZ_ID);
        String url = Constants.FETCH_QUIZ_QUESTIONS;

        FetchFromWeb.get(url, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                progressDialog.dismiss();
                try {
                    if (response.getString("msg").equals("Successful")) {
                        JSONArray jsonArray = response.getJSONArray("content");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            questions.add(gson.fromJson(String.valueOf(jsonObject),Question.class));
                        }
                    }

                    if (questions.size() > 0) {
                        q = questions.get(0);
                        quesno.setText("QUES NO: ");
                        img.setText(" " + (index + 1) + "/" + questions.size());
                        ques.setText(q.getQues());
                        Option1.setText(q.getOption1());
                        Option2.setText(q.getOption2());
                        Option3.setText(q.getOption3());
                        Option4.setText(q.getOption4());
                        rg.clearCheck();
                    }

                    tmr = new CountDownTimer(questions.size() * 1000 * 10, 1000) {

                        @Override
                        public void onTick(long millisUntilFinished) {
                            int hour = (int) millisUntilFinished / (3600 * 1000);
                            millisUntilFinished = millisUntilFinished % (3600 * 1000);
                            int minute = (int) millisUntilFinished / (60 * 1000);
                            int second = (int) millisUntilFinished % (60 * 1000);
                            Timer.setText("Time remaining: " + hour + " : " + minute
                                    + " : " + second / 1000);

                        }

                        @Override
                        public void onFinish() {
                            // TODO Auto-generated method stub
                            Toast.makeText(getApplicationContext(), "Time up!!!",
                                    Toast.LENGTH_LONG).show();
                            endFlag = 1;
                            Intent intent = new Intent(QuizActivity.this,QuizResultActivity.class);
                            intent.putExtra("marks", marks + "");
                            intent.putExtra("total", index + "");
                            startActivity(intent);
                        }
                    };
                    tmr.start();

                    Log.d(Constants.TAG, response.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                progressDialog.dismiss();
                Toast.makeText(QuizActivity.this, "Failed", Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (endFlag == 1) {
            finish();
        }
    }

}

