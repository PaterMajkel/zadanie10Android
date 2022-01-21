package com.example.zadanie9;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class activity_details_book extends AppCompatActivity {
    public static Drawable drawable;
    public static List<String> authors;
    public static String title;
    private ImageView imageView;
    private TextView edit_book_title;
    private TextView edit_book_author;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_book);

        imageView = findViewById(R.id.image_view);
        edit_book_title = findViewById(R.id.edit_book_title);
        edit_book_author = findViewById(R.id.edit_book_author);
    }
    @Override
    protected void onStart(){
        super.onStart();

        imageView.setImageDrawable(drawable);

        String output = "";
        if (authors != null)for (String author : authors) {
            output = output + ", "+ author;
        }
        edit_book_author.setText("All authors: "+ output);

        edit_book_title.setText("Title"+ title);
    }
}