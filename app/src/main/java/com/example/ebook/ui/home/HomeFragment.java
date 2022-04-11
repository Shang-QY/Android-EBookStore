package com.example.ebook.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.ebook.R;
import com.example.ebook.databinding.FragmentHomeBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private final String[] names = new String[]{"凤凰架构", "计算机系统", "凤凰架构",
            "计算机系统", "凤凰架构", "计算机系统", "凤凰架构", "计算机系统", "凤凰架构",
            "计算机系统", "凤凰架构"};
    private final int[] images = new int[]{R.drawable.fenix, R.drawable.csapp,
            R.drawable.fenix, R.drawable.csapp, R.drawable.fenix, R.drawable.csapp,
            R.drawable.fenix, R.drawable.csapp, R.drawable.fenix, R.drawable.csapp,
            R.drawable.fenix};

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textHome;
        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        GridView mGridView = (GridView) root.findViewById(R.id.grid_view);
        List<Map<String, Object>> listItems = new ArrayList<>();
        for (int i = 0; i < names.length; i++)
        {
            Map<String, Object> listItem = new HashMap<>();
            listItem.put("name", names[i]);
            listItem.put("image", images[i]);
            listItems.add(listItem);
        }
        // 创建一个SimpleAdapter
        SimpleAdapter simpleAdapter = new SimpleAdapter(root.getContext(), listItems,
                R.layout.simple_item, new String[]{"name", "image"},
                new int[]{R.id.book_name, R.id.book_image});

        mGridView.setAdapter(simpleAdapter);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}