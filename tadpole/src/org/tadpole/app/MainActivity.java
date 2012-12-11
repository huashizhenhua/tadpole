package org.tadpole.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.tadpole.adapter.BoardPagedAdapter;
import org.tadpole.aidl.TestServiceConnect;
import org.tadpole.widget.PagedView;
import org.tadpole.zenip.BoardPageData;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class MainActivity extends Activity {
    private static final String TAG = "MainActivity";
    private static final String SERVICE_NAME = "org.tadpole.service.testservices";

    private PagedView boardPagedView;
    private ArrayList<View> pageViews;

    private ServiceConnection mServiceConnection;
    private TestServiceConnect mTestServiceConnect;
    private Boolean mConnectComplete;
    private Context mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.board);

        boardPagedView = (PagedView) this.findViewById(R.id.page_view_board);
        pageViews = new ArrayList<View>();
        mContext = this;

        initPage();
        //		initServices();
        //		connectService();
    }

    private void initServices() {
        Log.i(TAG, "initServices");
        mServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceDisconnected(ComponentName name) {
                // TODO Auto-generated method stub
                Log.i(TAG, "onServiceDisconnected");

            }

            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                // TODO Auto-generated method stub
                Log.i(TAG, "onServiceConnected");
                mTestServiceConnect = TestServiceConnect.Stub.asInterface(service);
                if (mTestServiceConnect != null) {
                    try {
                        mTestServiceConnect.test();
                    } catch (RemoteException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        };
        mConnectComplete = false;
        mTestServiceConnect = null;
    }

    private boolean connectService() {
        if (mConnectComplete == true) {
            return true;
        }
        Intent intent = new Intent(SERVICE_NAME);
        if (mContext != null) {
            Log.i(TAG, "begin to connectService	");
            mContext.bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
            mContext.startService(intent);
            mConnectComplete = true;
            return true;
        }
        return false;
    }

    public void disConnectedService() {
        if (mConnectComplete) {
            Intent intent = new Intent(SERVICE_NAME);
            mContext.unbindService(mServiceConnection);
            mContext.stopService(intent);
            mTestServiceConnect = null;
            mConnectComplete = false;

        }
    }

    @Override
    protected void onDestroy() {
        disConnectedService();

        super.onDestroy();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mTestServiceConnect != null) {
            try {
                mTestServiceConnect.test();
            } catch (RemoteException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return super.onKeyDown(keyCode, event);
    }


    //----------------ui logic-----------------

    private ArrayList<BoardPageData> loadBoardPageData() {
        String[] titles = { "weico", "tecent_weibo", "qq", "dear qin" };
        ArrayList<BoardPageData> dataList = new ArrayList<BoardPageData>();
        for (int i = 0, len = titles.length; i < len; i++) {
            BoardPageData data = new BoardPageData();
            data.title = titles[i];
            dataList.add(data);
        }
        return dataList;
    }

    private void initPage() {
        ArrayList<BoardPageData> dataList = loadBoardPageData();
        Iterator<BoardPageData> dataIter = dataList.iterator();
        while (dataIter.hasNext()) {
            BoardPageData data = dataIter.next();
            pageViews.add(buildPageView(data));
        }
        boardPagedView.setAdapter(new BoardPagedAdapter(pageViews));
    }

    private View buildPageView(BoardPageData data) {
        LinearLayout.LayoutParams viewParams = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);

        LinearLayout rl = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.board_page, null);
        GridView gridView = (GridView) rl.findViewById(R.id.grid_view_board_page);

        //生成动态数组，并且转入数据  
        ArrayList<HashMap<String, Object>> lstImageItem = new ArrayList<HashMap<String, Object>>();
        for (int i = 0; i < 10; i++) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("ItemImage", R.drawable.ic_action_search);//添加图像资源的ID  
            map.put("ItemText", "NO." + String.valueOf(i));//按序号做ItemText  
            lstImageItem.add(map);
        }
        //生成适配器的ImageItem <====> 动态数组的元素，两者一一对应  
        SimpleAdapter saImageItems = new SimpleAdapter(this, //没什么解释  
                lstImageItem,//数据来源   
                R.layout.board_page_griditem,//night_item的XML实现  
                //动态数组与ImageItem对应的子项          
                new String[] { "ItemImage", "ItemText" },
                //ImageItem的XML文件里面的一个ImageView,两个TextView ID  
                new int[] { R.id.ItemImage, R.id.gridItemText });

        gridView.setAdapter(saImageItems);
        
        rl.setLayoutParams(viewParams);
//        TextView pageView = new TextView(this);
//        pageView.setLayoutParams(viewParams);
//        pageView.setBackgroundColor(Color.RED);
//        pageView.setText(data.title);
//        pageView.setBackgroundColor(Color.WHITE);
        return rl;
    }
}
