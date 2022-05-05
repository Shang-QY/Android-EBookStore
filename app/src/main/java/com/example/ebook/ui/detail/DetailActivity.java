package com.example.ebook.ui.detail;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ebook.R;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DetailActivity extends Activity {

    private static SimpleAdapter simpleAdapter;
    private final int[] images = new int[]{R.drawable.fenix, R.drawable.csapp,
            R.drawable.mysql, R.drawable.java, R.drawable.python, R.drawable.jvm,
            R.drawable.fenix, R.drawable.csapp, R.drawable.python};
    private List<String> fileNames = new ArrayList<>();
    private ImageView book_image;
    private RecyclerView show;
    private View viewDialog;

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

        viewDialog = getLayoutInflater().inflate(R.layout.view, null);
        show = viewDialog.findViewById(R.id.show);
        // 为RecyclerView设置布局管理器
        show.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));

        // 请求读取外部存储器的权限
        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0x123);

        Button editor = findViewById(R.id.change_image);
        editor.setOnClickListener(view -> {
            Log.i(TAG, "file path: " + fileNames);
            // 使用对话框显示用户单击的图片
            new AlertDialog.Builder(DetailActivity.this)
                    .setView(viewDialog).setPositiveButton("确定", null).show();
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults[0] == 0) {
            if (requestCode == 0x123) {
                // 清空names、descs、fileNames集合里原有的数据
                fileNames.clear();
                // 通过ContentResolver查询所有图片信息
                Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null, null);
                while (cursor.moveToNext()) {
                    // 获取图片的保存位置的数据
                    @SuppressLint("Range") byte[] data = cursor.getBlob(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                    // 将图片保存路径添加到fileNames集合中
                    fileNames.add(new String(data, 0, data.length - 1));
                }
                Log.i(TAG, "In func file path: " + fileNames);
                cursor.close();
                RecyclerView.Adapter adapter = new RecyclerView.Adapter<LineViewHolder>(){
                    @NonNull @Override
                    public LineViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
                    {
                        View itemView = getLayoutInflater().inflate(R.layout.image_editor_item,
                                new LinearLayout(DetailActivity.this), false);
                        return new LineViewHolder(itemView);
                    }
                    @Override
                    public void onBindViewHolder(@NonNull LineViewHolder lineViewHolder,
                                                 @SuppressLint("RecyclerView") int i)
                    {
                        lineViewHolder.image_view.setImageBitmap(BitmapFactory.decodeFile(fileNames.get(i)));
                        lineViewHolder.file_path = fileNames.get(i);
                        lineViewHolder.index = i;
                    }
                    @Override
                    public int getItemCount()
                    {
                        return fileNames.size();
                    }
                };
                // 为show RecyclerView组件设置Adapter
                show.setAdapter(adapter);
            }
        } else {
            Toast.makeText(this, R.string.permisssion_tip, Toast.LENGTH_SHORT)
                    .show();
        }
    }

    class LineViewHolder extends RecyclerView.ViewHolder
    {
        ImageView image_view;
        String file_path;
        int index;

        public LineViewHolder(@NonNull View itemView)
        {
            super(itemView);
            image_view = itemView.findViewById(R.id.book_image_item);
            image_view.setOnClickListener(view -> {
                book_image.setImageBitmap(BitmapFactory.decodeFile(file_path));
                String tips = "封面已更新为系统图片：" + file_path;
                Toast.makeText(getApplicationContext(), tips, Toast.LENGTH_SHORT).show();
            });
        }
    }
}
