package com.example.ebook.ui.notifications;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.ebook.MainActivity;
import com.example.ebook.R;
import com.example.ebook.databinding.FragmentNotificationsBinding;
import com.example.ebook.ui.login.LoginActivity;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class NotificationsFragment extends Fragment {

    private FragmentNotificationsBinding binding;
    private View root;

    String[] data1 = { "个人信息", "我的地址", "我的钱包", "我的评价", "设置" }; // 定义文字数据
    String[] data2 = { ">", ">", ">", ">", ">" }; // 定义文字数据
    ArrayList<Map<String, Object>> list; // 定义ArrayList
    Map<String, Object> map;

    // 记录蝴蝶ImageView当前的位置
    private static float curX;
    private static float curY = 30f;
    // 记录蝴蝶ImageView下一个位置的坐标
    private static float nextX;
    private static float nextY;
    private static ImageView imageView;
    private static float screenWidth;

    static class MyHandler extends Handler
    {
        private WeakReference<NotificationsFragment> fragment;
        MyHandler(WeakReference<NotificationsFragment> fragment)
        {
            this.fragment = fragment;
        }
        @Override
        public void handleMessage(Message msg)
        {
            if (msg.what == 0x123)
            {
                // 横向上一直向右飞
                if (nextX > screenWidth)
                {
                    nextX = 0f;
                    curX = nextX;
                }
                else
                {
                    nextX += 8f;
                }
                // 纵向上可以随机上下
                nextY = curY + (float)(Math.random() * 10 - 5);
                // 设置显示蝴蝶的ImageView发生位移改变
                TranslateAnimation anim = new TranslateAnimation(curX, nextX, curY, nextY);
                curX = nextX;
                curY = nextY;
                anim.setDuration(200);
                // 开始位移动画
                imageView.startAnimation(anim);  // ①
            }
        }
    }
    private Handler handler = new MyHandler(new WeakReference<NotificationsFragment>(this));
    
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        root = binding.getRoot();

        ListView person_list = (ListView) root.findViewById(R.id.personlist);
        list = new ArrayList<Map<String, Object>>(); // 定义ArrayList集合
        for (int i = 0; i < data1.length; i++) { // 把原始数据放放进map集合里
            map = new HashMap<String, Object>(); // 定义map集合(数组），一个map集合对应ListView的一栏。
            map.put("option", data1[i]); // 添加数据给map集合
            map.put("icon", data2[i]);
            list.add(map); // 把map集合放进list集合里
        }
        // 调用SimpleAdapter适配器
        SimpleAdapter adapter = new SimpleAdapter(getActivity(), list,
                R.layout.simple_adapter, // 与数据匹配的布局
                new String[] { "option", "icon" }, // 字符串数组，里面放参数名。
                new int[] { R.id.tv_person, R.id.tv_icon });
        person_list.setAdapter(adapter);

        root.findViewById(R.id.loginLy).setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                // 启动指定Activity并等待返回的结果，其中0是请求码，用于标识该请求
                startActivityForResult(intent, 0);
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        // 当requestCode、resultCode同时为0时，也就是处理特定的结果
        if (requestCode == 0 && resultCode == 0)
        {
            // 取出Intent里的Extras数据
            Bundle data = intent.getExtras();
            // 取出Bundle中的数据
            String loggedUser = data.getString("loggedUserView");
            // 修改用户名文本框的内容
            TextView textView = root.findViewById(R.id.textView1);
            textView.setText(loggedUser);
            ImageView image = root.findViewById(R.id.imageView1);
            image.setImageResource(R.drawable.logged);

            // 获取屏幕宽度
            Point p = new Point();
            ((Activity) getContext()).getWindowManager().getDefaultDisplay().getSize(p);
            screenWidth = p.x;
            // 获取显示蝴蝶的ImageView组件
            imageView = root.findViewById(R.id.butterfly);
            imageView.setBackgroundResource(R.drawable.butterfly);
            AnimationDrawable butterfly = (AnimationDrawable) imageView.getBackground();
            // 开始播放蝴蝶振翅的逐帧动画
            butterfly.start();  // ②
            // 通过定时器控制每0.2秒运行一次TranslateAnimation动画
            new Timer().schedule(new TimerTask()
            {
                @Override public void run()
                {
                    handler.sendEmptyMessage(0x123);
                }
            }, 0, 200);
        }
    }
}