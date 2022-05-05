package com.example.ebook.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.ebook.R;
import com.example.ebook.databinding.FragmentHomeBinding;
import com.example.ebook.ui.detail.DetailActivity;
import com.example.ebook.ui.login.LoginActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class HomeFragment extends Fragment {

    private ViewPager viewPager; // android-support-v4中的滑动组件
    private List<ImageView> imageViews; // 滑动的图片集合
    private String[] titles; // 图片标题
    private int[] imageResId; // 图片ID
    private List<View> dots; // 图片标题正文的那些点
    private TextView tv_title;
    private int currentItem = 0; // 当前图片的索引号
    private ScheduledExecutorService scheduledExecutorService;
    // 切换当前显示的图片
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            viewPager.setCurrentItem(currentItem);// 切换当前显示的图片
        };
    };

    private FragmentHomeBinding binding;
    private final String[] names = new String[]{"凤凰架构", "计算机系统", "MySQL实战",
            "精通Java微服务", "Python编程", "深入理解JVM", "凤凰架构", "计算机系统", "Python编程"};
    private final int[] images = new int[]{R.drawable.fenix, R.drawable.csapp,
            R.drawable.mysql, R.drawable.java, R.drawable.python, R.drawable.jvm,
            R.drawable.fenix, R.drawable.csapp, R.drawable.python};

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //广告轮播图*******************
        imageResId = new int[] { R.drawable.book1, R.drawable.book2, R.drawable.book3, R.drawable.book4 };
        titles = new String[imageResId.length];
        titles[0] = "新书抢鲜看";
        titles[1] = "图书5折封顶";
        titles[2] = "极价书屋";
        titles[3] = "阅读悦快乐";

        imageViews = new ArrayList<>();

        // 初始化图片资源
        for (int j : imageResId) {
            ImageView imageView = new ImageView(getActivity());
            imageView.setImageResource(j);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageViews.add(imageView);
        }

        dots = new ArrayList<>();
        dots.add(root.findViewById(R.id.v_dot0));
        dots.add(root.findViewById(R.id.v_dot1));
        dots.add(root.findViewById(R.id.v_dot2));
        dots.add(root.findViewById(R.id.v_dot3));

        tv_title = root.findViewById(R.id.tv_title);
        tv_title.setText(titles[0]);//

        viewPager = root.findViewById(R.id.vp);
        viewPager.setAdapter(new MyAdapter());// 设置填充ViewPager页面的适配器
        // 设置一个监听器，当ViewPager中的页面改变时调用
        viewPager.setOnPageChangeListener(new MyPageChangeListener());
        //****************************

        //栅格排列的书籍卡片**************
        GridView mGridView = root.findViewById(R.id.grid_view);
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

        mGridView.setOnItemClickListener((parent, view, position, id) -> {
            // 创建一个Bundle对象
            Bundle data = new Bundle();
            data.putSerializable("name", names[position]);
            data.putSerializable("image", images[position]);
            Intent intent = new Intent(getActivity(), DetailActivity.class);
            intent.putExtras(data);
            startActivity(intent);
        });
        //****************************

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    //图片 线程切换
    public class ScrollTask implements Runnable {
        public void run() {
            synchronized (viewPager) {
                System.out.println("currentItem: " + currentItem);
                currentItem = (currentItem + 1) % imageViews.size();
                handler.obtainMessage().sendToTarget(); // 通过Handler切换图片
            }
        }
    }
    //开始轮播
    @Override
    public void onStart() {
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        // 当Activity显示出来后，每两秒钟切换一次图片显示
        scheduledExecutorService.scheduleAtFixedRate(new ScrollTask(), 1, 2, TimeUnit.SECONDS);
        super.onStart();
    }
    //停止切换
    @Override
    public void onStop() {
        // 当Activity不可见的时候停止切换
        scheduledExecutorService.shutdown();
        super.onStop();
    }
    //图片、ID
    public class MyPageChangeListener implements ViewPager.OnPageChangeListener {
        private int oldPosition = 0;

        /**
         * This method will be invoked when a new page becomes selected.
         * position: Position index of the new selected page.
         */
        public void onPageSelected(int position) {
            currentItem = position;
            tv_title.setText(titles[position]);
            dots.get(oldPosition).setBackgroundResource(R.drawable.dot_normal);
            dots.get(position).setBackgroundResource(R.drawable.dot_focused);
            oldPosition = position;
        }

        public void onPageScrollStateChanged(int arg0) {}

        public void onPageScrolled(int arg0, float arg1, int arg2) {}
    }
    //图片、适配
    public class MyAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return imageResId.length;
        }

        @Override
        public Object instantiateItem(View arg0, int arg1) {
            ((ViewPager) arg0).addView(imageViews.get(arg1));
            return imageViews.get(arg1);
        }

        @Override
        public void destroyItem(View arg0, int arg1, Object arg2) {
            ((ViewPager) arg0).removeView((View) arg2);
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {

        }

        @Override
        public Parcelable saveState() {
            return null;
        }

        @Override
        public void startUpdate(View arg0) {

        }

        @Override
        public void finishUpdate(View arg0) {

        }
    }
}