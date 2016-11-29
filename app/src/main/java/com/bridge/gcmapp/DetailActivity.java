package com.bridge.gcmapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sec on 2016-05-24.
 */
public class DetailActivity extends Activity {
   /* static final String[] CONTENTS = new String[] {
            "사용자UI에 관해서는 이 책보다 나은 책을 본 적은 없는 듯 합니다. 디자인 부분 뿐만 아니라 사용자가 이벤트 처리 그리고 여러가지 view들의 사용법들... UI의 기초와 풍부한 지식을 쌓기에는 유용했던 책이며 상당히 수준있는 예제들이 존재해서 그대로 본인 앱에 인용해도 될 만한 내용들도 많이 존재합니다.",
            "이 책은 사실은 전체적으로 첨부터 끝까지 읽어본 건 아니어서 여기서 제가 뭐라 말하기는 좀 그런 면도 있지만 이 책은 문제 발생시 그 부분에 대한 포커스를 맞추고 해결하는 방식으로 적혀 있기 때문에 앱개발시 막히는 문제가 생기면 찾아보게되는 책입니다.",
            "일단 제가 본 국내 안드로이드 서적중에는 가장 깊이 있고 풍부한 예제와 앱제작 프로그램시에 가장 도움을 많이 받았던 책입니다. 두권으로 구성되어있는데 첫권은 주로 view나 기본적인 컴포넌트에 대한 이야기가 많고 2권은 깊이있는 네트워크,지도,센서...등등 이야기로 구성되어 있습니다.쉽지는 않고 방대한 양을 다루다보니 처음 공부하시분에게는 바로 권하기 보단 다른 초급서적을 먼저 본 후에 이 책을 읽고 개발시에는 항상 옆에 두고 보면서 문제를 해결해 나가기에 좋은 책이라는 의견입니다.\n 또한 이 블로그에 많은 내용의 소스들이나 쓰는 내용이 이 책에서 가장 많이 인용됩니다."
    };*/

    String code;
    String user_id;
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail);

   /*     TextView tvTitle = (TextView)findViewById(R.id.textView1);
        TextView tvArtist = (TextView)findViewById(R.id.textView2);



        Intent intent = getIntent(); // 보내온 Intent를 얻는다
        tvTitle.setText(intent.getStringExtra("title"));
        tvArtist.setText(intent.getStringExtra("artist"));*/

        pref = getSharedPreferences("pref", MODE_PRIVATE);
        editor = pref.edit();

        user_id = pref.getString("user_id", "");

        TextView title = (TextView)findViewById(R.id.txt_title);
        TextView content = (TextView)findViewById(R.id.txt_contents);
        content.setMovementMethod(new ScrollingMovementMethod());


        Intent intent = getIntent();
        title.setText(intent.getStringExtra("post_title"));
        content.setText(intent.getStringExtra("post_content"));
        code = intent.getStringExtra("code");

        System.out.println("전환된 후 받은 값 :" + intent.getStringExtra("post_title"));
        System.out.println("전환된 후 받은 값 :" +intent.getStringExtra("post_content"));

        Button btn_home = (Button)findViewById(R.id.btn_home);

        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SecondActivity.class);
                startActivity(intent);
            }
        });

        Button btn_like = (Button)findViewById(R.id.btn_like);
        btn_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doLike();
            }
        });
    } // end of onCreate

    private void doLike() {
        DefaultRestClient<RestApiService> defaultRestClient = new DefaultRestClient<>();
        RestApiService restApiService = defaultRestClient.getClient(RestApiService.class);

        Call<ResponseBody> call = restApiService.like(user_id, code);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if(response.isSuccessful()) {
                    try {
                        JSONObject object = new JSONObject(response.body().string());

                        if(object.has("error")) {
                            Toast.makeText(DetailActivity.this, object.getString("error"), Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(DetailActivity.this, "좋아요 하였습니다", Toast.LENGTH_SHORT).show();
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
    }

/*    public void onScroll(MyListAapter view, int firstVisibleItem, int visibleItemCount, int totalItemCount){
        if((firstVisibleItem + visibleItemCount)==(totalItemCount)){
            page++
        }*/
}



