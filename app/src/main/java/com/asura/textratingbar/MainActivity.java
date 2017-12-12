package com.asura.textratingbar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.asura.library_textratingbar.TextRatingBar;

public class MainActivity extends AppCompatActivity {
    private TextRatingBar mTextRatingBar1;
    private TextRatingBar mTextRatingBar2;
    private TextRatingBar mTextRatingBar3;
    private Toast mToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextRatingBar1 = findViewById(R.id.trb_1);
        mTextRatingBar2 = findViewById(R.id.trb_2);
        mTextRatingBar3 = findViewById(R.id.trb_3);
        mToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
        TextRatingBar.OnRatingListener listener = new TextRatingBar.OnRatingListener() {
            @Override
            public void onRatingChanged(int rating, String ratingText) {
                mToast.setText(rating + " <----> " + ratingText);
                mToast.show();
            }
        };
        mTextRatingBar1.setOnRatingListener(listener);
        mTextRatingBar2.setOnRatingListener(listener);
        mTextRatingBar3.setOnRatingListener(listener);

        mTextRatingBar2.setRating(2);
        mTextRatingBar3.setRating(3);
//        mTextRatingBar2.setTexts();
    }
}
