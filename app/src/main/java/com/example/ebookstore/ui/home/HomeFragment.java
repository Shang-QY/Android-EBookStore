package com.example.ebookstore.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.ebookstore.R;
import com.example.ebookstore.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private int[] images = new int []{R.drawable.ios_home, R.drawable.ios_book_detail,
            R.drawable.ios_book_detail2};
    private int currentImg = 0;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // 程序创建ImageView组件
        ImageView image = root.findViewById(R.id.imageView1);
        // 初始化时显示第一张图片
        image.setImageResource(images[0]);
        image.setOnClickListener(view -> {
            // 改变ImageView里显示的图片
            image.setImageResource(images[++currentImg % images.length]);
        });

        final TextView textView = binding.textHome;
        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}