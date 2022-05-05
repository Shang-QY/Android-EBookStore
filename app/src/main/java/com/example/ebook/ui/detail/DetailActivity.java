package com.example.ebook.ui.detail;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ebook.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DetailActivity extends AppCompatActivity {

    private static SimpleAdapter simpleAdapter;
    private final int[] images = new int[]{R.drawable.fenix, R.drawable.csapp,
            R.drawable.mysql, R.drawable.java, R.drawable.python, R.drawable.jvm,
            R.drawable.fenix, R.drawable.csapp, R.drawable.python};
    private ImageView book_image;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        TextView book_name = findViewById(R.id.book_name);
        book_image = findViewById(R.id.book_image);
        // 获取启动该Activity的Intent
        Intent intent = getIntent();
        // 直接通过Intent取出它所携带的Bundle数据包中的数据
        String name = (String) intent.getSerializableExtra("name");
        int image = (int) intent.getSerializableExtra("image");
        book_name.setText(name);
        book_image.setImageResource(image);

        // 准备Adapter的数据
        List<Map<String, Object>> listItems = new ArrayList<>();
        for (int i = 0; i < images.length; i++)
        {
            Map<String, Object> listItem = new HashMap<>();
            listItem.put("image", images[i]);
            listItems.add(listItem);
        }
        // 创建一个SimpleAdapter
        simpleAdapter = new SimpleAdapter(this, listItems,
                R.layout.image_editor_item, new String[]{"image"},
                new int[]{R.id.book_image_item});

        Button editor = findViewById(R.id.change_image);
        editor.setOnClickListener(view -> {
            // 加载view.xml界面布局代表的视图
            View viewDialog = getLayoutInflater().inflate(R.layout.view, null);
            // 栅格排列的书籍卡片**************
            GridView mGridView = viewDialog.findViewById(R.id.grid_view);
            // 设置image显示指定图片
            mGridView.setAdapter(simpleAdapter);
            mGridView.setOnItemClickListener((parent, item_view, position, id) -> {
                book_image.setImageResource(images[position]);
                String tips = "封面已更新为第 " + (position + 1) + " 张图片";
                Toast.makeText(getApplicationContext(), tips, Toast.LENGTH_SHORT).show();
            });
            // 使用对话框显示用户单击的图片
            new AlertDialog.Builder(DetailActivity.this)
                    .setView(viewDialog).setPositiveButton("确定", null).show();
        });
    }
}
