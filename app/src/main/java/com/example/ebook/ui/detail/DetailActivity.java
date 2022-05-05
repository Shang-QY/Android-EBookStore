package com.example.ebook.ui.detail;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ebook.R;
import com.example.ebook.databinding.ActivityDetailBinding;

public class DetailActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        TextView book_name = findViewById(R.id.book_name);
        ImageView book_image = findViewById(R.id.book_image);
        // 获取启动该Activity的Intent
        Intent intent = getIntent();
        // 直接通过Intent取出它所携带的Bundle数据包中的数据
        String name = (String) intent.getSerializableExtra("name");
        int image = (int) intent.getSerializableExtra("image");
        book_name.setText(name);
        book_image.setImageResource(image);
    }
}
