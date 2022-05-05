package com.example.ebook.ui.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.ebook.R;
import com.example.ebook.databinding.FragmentDashboardBinding;
import com.example.ebook.ui.detail.DetailActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;

    int[] book_image = new int []{R.drawable.csapp, R.drawable.jvm, R.drawable.fenix};
    String[] book_name = { "深入理解计算机系统", "深入理解Java虚拟机", "凤凰架构" }; // 定义文字数据
    String[] author_name = { "作者:兰德尔", "作者:周志明", "作者:周志明" }; // 定义文字数据
    String[] cost = { "39.0", "65.0", "37.0" };
    ArrayList<Map<String, Object>> list; // 定义ArrayList
    Map<String, Object> map;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ListView person_list = (ListView) root.findViewById(R.id.list_shopping_cart);
        list = new ArrayList<Map<String, Object>>(); // 定义ArrayList集合
        for (int i = 0; i < book_name.length; i++) { // 把原始数据放放进map集合里
            map = new HashMap<String, Object>(); // 定义map集合(数组），一个map集合对应ListView的一栏。
            map.put("book_image", book_image[i]);
            map.put("book_name", book_name[i]);
            map.put("author_name", author_name[i]); // 添加数据给map集合
            map.put("cost", cost[i]);
            list.add(map); // 把map集合放进list集合里
        }
        // 调用SimpleAdapter适配器
        SimpleAdapter adapter = new SimpleAdapter(getActivity(), list,
                R.layout.cart_list_item, // 与数据匹配的布局
                new String[] { "book_image", "book_name", "author_name", "cost" }, // 字符串数组，里面放参数名。
                new int[] { R.id.book_image, R.id.book_name, R.id.author_name, R.id.cost });
        person_list.setAdapter(adapter);

        person_list.setOnItemClickListener((parent, view, position, id) -> {
            // 创建一个Bundle对象
            Bundle data = new Bundle();
            data.putSerializable("name", book_name[position]);
            data.putSerializable("image", book_image[position]);
            Intent intent = new Intent(getActivity(), DetailActivity.class);
            intent.putExtras(data);
            startActivity(intent);
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}