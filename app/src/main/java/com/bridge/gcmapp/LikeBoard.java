package com.bridge.gcmapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Path;

/**
 * Created by sec on 2016-11-01.
 */
public class LikeBoard extends Activity {


    private static final String TAG = "LikeBoard";

    ListView listView;
    MyListAapter adapter;
    public static final ArrayList<DataVo> lst = new ArrayList<DataVo>();

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    String user_id;

    //------------------------------------GCM start-----------------------------------------------

    // 리스트뷰 떄문에 잠시 주석 GCM
    static public void addList(String title, String contents, String code) {
        lst.add(new DataVo(title, contents, code));

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Grobal.setDone(false);
    }

    //------------------------------------GCM end -------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_activity);

        pref = getSharedPreferences("pref", MODE_PRIVATE);
        editor = pref.edit();
        user_id = pref.getString("user_id", "");

        listView = (ListView) findViewById(R.id.listView);
        adapter = new MyListAapter(this);


        listView.setAdapter(adapter);

        conntectCheck();

        new Thread(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
            }
        }).start();

    }

    public void conntectCheck() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            // fetch data

            DefaultRestClient<RestApiService> defaultRestClient = new DefaultRestClient<>();
            RestApiService restApiService = defaultRestClient.getClient(RestApiService.class);

            Call<ResponseBody> call = restApiService.getLikes(user_id);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    if(response.isSuccessful()) {
                        try {
                            JSONArray objects = new JSONArray(response.body().string());
                            for(int i=0; i<objects.length(); i++) {
                                JSONObject obj = objects.getJSONObject(i);
                                obj = obj.getJSONObject("Post");
                                DataVo dataVo = new DataVo();

                                Log.v("TEST", obj.toString());
                                dataVo.setTitle(obj.getString("post_title"));
                                dataVo.setContents(obj.getString("post_content"));
                                dataVo.setDate(obj.getString("created_at"));
                                dataVo.setCode(obj.getString("id"));

                                System.out.println("2obj.getString(post_title):" + obj.getString("post_title"));
                                System.out.println("2obj.getString(post_content):" + obj.getString("post_content"));
                                System.out.println("2obj.getString(created_at):" + obj.getString("created_at"));

                                adapter.lst.add(dataVo);
//                                adapter.notifyDataSetChanged();
                                listView.invalidateViews();
                            }
                        }catch(Exception e) {
                            e.printStackTrace();
                        }
                    }else {
                        try {
                            JSONObject object = new JSONObject(response.errorBody().string());
                            if(object.has("error")) {
                                Toast.makeText(LikeBoard.this, object.getString("error"), Toast.LENGTH_SHORT).show();
                            }
                        }catch(Exception e) {
                            e.printStackTrace();
                        }

                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    t.printStackTrace();
                }
            });



        } else {
            // display error
            Toast.makeText(this, "네트워크 상태를 확인하십시오", Toast.LENGTH_SHORT).show();
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), DetailActivity.class);

                //TextView title = (TextView) findViewById(R.id.txt_title);
                //TextView content = (TextView) findViewById(R.id.txt_contents);

                DataVo dataVo = (DataVo) adapter.getItem(position);

                intent.putExtra("post_title", dataVo.getTitle());
                intent.putExtra("post_content", dataVo.getContents());
                intent.putExtra("created_at", dataVo.getDate());
                intent.putExtra("code", dataVo.getCode());
//                intent.putExtra("code", dataVo.)
                //-----------------------------------------------------
                lst.remove(id);
                adapter.notifyDataSetChanged();
                //--------------------------------------------------------
                // extras.putString("post_content", content.getText().toString());
                System.out.println("전환 전 받은 값 :" + dataVo.getContents());
                System.out.println("전환 전 받은 값 :" + dataVo.getDate());

                startActivity(intent);
                System.out.println("Position :" + position);
                System.out.println("id:" + id);
                //  Toast.makeText(getApplication(),title.getText().toString(), Toast.LENGTH_SHORT).show();
                //Toast.makeText(getApplication(),content.getText().toString(),Toast.LENGTH_SHORT).show();
            }
        });

        //마이페이지 뷰
        ImageButton myPageView = (ImageButton) findViewById(R.id.btn_mypage);
        myPageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MyPageActivity.class);
                startActivity(intent);

            }
        });

        Button keywordBtn = (Button)findViewById(R.id.btn_keyword);
        keywordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(getApplicationContext(), KeywordActivity.class);
                startActivity(intent);

            }
        });
    }

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.likeboard);
//
//        Intent intent = getIntent();
//    }
}
