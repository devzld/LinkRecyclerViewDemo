package com.zld.demo2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.zld.demo2.adapter.CategoryAdapter;
import com.zld.demo2.adapter.MainAdapter;
import com.zld.demo2.bean.CategoryBean;
import com.zld.demo2.bean.FoodBean;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    RecyclerView rcvCategory;
    RecyclerView rcvMain;

    CategoryAdapter mCategoryAdapter;
    MainAdapter mMainAdapter;
    List<CategoryBean> mCategoryList = new ArrayList<>();
    List<FoodBean> mFoodList = new ArrayList<>();

    String[] mCategoryArr = {"经典小食", "主食汉堡", "汉堡套餐", "中式简餐", "招牌炸鸡", "畅爽饮品", "热饮", "奶茶类", "咖啡类", "特色盖浇饭", "单人套餐", "海鲜"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rcvCategory = findViewById(R.id.rcv_left);
        rcvMain = findViewById(R.id.rcv_main);

        rcvCategory.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        mCategoryAdapter = new CategoryAdapter(this, rcvCategory, mCategoryList);
        rcvCategory.setAdapter(mCategoryAdapter);

        rcvMain.setLayoutManager(new LinearLayoutManager(this));
        mMainAdapter = new MainAdapter(this, rcvMain, mFoodList);
        rcvMain.setAdapter(mMainAdapter);

        initEvent();
        initData();
    }

    private int firstVisibleItemPosition = -1;
    private int lastVisibleItemPosition = -1;

    private void initEvent() {
        rcvMain.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
//                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
//                    Log.e("onScrollStateChanged", "scroll_state_dragging");
//                } else if (newState == RecyclerView.SCROLL_STATE_IDLE) {
//                    Log.e("onScrollStateChanged", "scroll_state_idle");
//
//                } else if (newState == RecyclerView.SCROLL_STATE_SETTLING) {
//                    Log.e("onScrollStateChanged", "scroll_state_settling");
//                }

//                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
//                    firstVisibleItemPosition = ((LinearLayoutManager) rcvMain.getLayoutManager()).findFirstVisibleItemPosition();
//                    Log.e("onScrollStateChanged", "onScrollStateChanged: " + firstVisibleItemPosition);
//                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                Log.e(TAG, "onScrolled: " + dx + " -- " + dy);
                firstVisibleItemPosition = ((LinearLayoutManager) rcvMain.getLayoutManager()).findFirstVisibleItemPosition();
                lastVisibleItemPosition = ((LinearLayoutManager) rcvMain.getLayoutManager()).findLastVisibleItemPosition();

                if(mMainAdapter.isMove()){//第二次滚动
                    //在这里进行第二次滚动

                    //获取要置顶的项在当前屏幕的位置，mIndex是记录的要置顶项在RecyclerView中的位置
                    int n = mMainAdapter.getIndex()-firstVisibleItemPosition;
                    if(0<=n && n<rcvMain.getChildCount()){
                        //获取要置顶的项顶部离RecyclerView顶部的距离
                        int top = rcvMain.getChildAt(n).getTop();
                        //最后的移动
                        rcvMain.scrollBy(0,top);
                    }
                    mMainAdapter.setMove(false);
                }else {         //手动滚动
                    if (dy > 0) {//上滑
                        if (mMainAdapter.getData().get(firstVisibleItemPosition).getGroup() !=
                                mCategoryAdapter.getData().get(mCategoryAdapter.getCheckedItem()).getGroupId()) {
                            mCategoryAdapter.updateCheck(mMainAdapter.getData().get(firstVisibleItemPosition).getGroup());
                        }
                    } else if (dy < 0) {//下滑
                        if (mMainAdapter.getData().get(firstVisibleItemPosition).getGroup() !=
                                mCategoryAdapter.getData().get(mCategoryAdapter.getCheckedItem()).getGroupId()) {
                            mCategoryAdapter.updateCheck(mMainAdapter.getData().get(firstVisibleItemPosition).getGroup());
                        }
                    }
                }
            }
        });

        mCategoryAdapter.setOnItemClickListener(new CategoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                mMainAdapter.updatePosition(mCategoryAdapter.getData().get(position).getGroupId());
            }
        });
    }

    private void initData() {


        for (int i = 0; i < mCategoryArr.length; i++) {
            CategoryBean bean = new CategoryBean();
            bean.setTitle(mCategoryArr[i]);
            bean.setGroupId(i);
            mCategoryList.add(bean);
        }
        mCategoryAdapter.notifyDataSetChanged();

        for (int i = 0; i < mCategoryArr.length; i++) {
            FoodBean groupBean = new FoodBean();
            groupBean.setGroupId(true);
            groupBean.setGroup(i);
            groupBean.setGroupTitle(mCategoryArr[i]);
            mFoodList.add(groupBean);
            for (int j = 0; j < 5; j++) {
                FoodBean bean = new FoodBean();
                bean.setGroup(i);
                bean.setImageId(R.mipmap.ic_launcher);
                bean.setTitle(mCategoryArr[i] + j);
                mFoodList.add(bean);
            }
        }
        mMainAdapter.notifyDataSetChanged();

        mCategoryAdapter.updateCheck(0);
    }
}
