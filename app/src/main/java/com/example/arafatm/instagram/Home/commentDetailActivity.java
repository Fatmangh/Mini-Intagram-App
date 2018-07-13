package com.example.arafatm.instagram.Home;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.arafatm.instagram.R;

public class commentDetailActivity extends AppCompatActivity {
    private Button post;
    private EditText comment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_detail);

        post = (Button) findViewById(R.id.c_post);
        comment= (EditText) findViewById(R.id.c_comment);
        final LinearLayout linearLayout = (LinearLayout) findViewById(R.id.listView);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    TextView textView = new TextView(commentDetailActivity.this);
                    String x = comment.getText().toString();
                    textView.setText(comment.getText().toString());
                    linearLayout.addView(textView);
            }
        });
    }
}
