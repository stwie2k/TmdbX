package nyamori.moe.tmdbx;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import nyamori.moe.tmdbx.adapter.VideoAdapter;

public class Video extends AppCompatActivity {
    int movieid;
    VideoAdapter adapter;
    List<Trailer.video> videos = new ArrayList<>();
    VideoView videoView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        String title=bundle.getString("title");
        movieid=bundle.getInt("id");


        ListView lv=findViewById(R.id.vlv);
        adapter = new VideoAdapter(this, videos);
        adapter.setOnItemClickListener(new VideoAdapter.onItemClickListener() {
            @Override
            public void onClick(View v, int i) {
                videoView.setVideoURI(Uri.parse(videos.get(i).url));
                videoView.start();


                adapter.setCurrentItem(i);
                //通知ListView改变状态
                adapter.notifyDataSetChanged();
           }
        });


        lv.setAdapter(adapter);




         videoView = (VideoView) findViewById(R.id.videoView);

        videoView.setMediaController(new MediaController(this));




        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Toast.makeText(Video.this, "播放完成了", Toast.LENGTH_SHORT).show();
            }
        });


        startUpdateList();

    }

    public void startUpdateList(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if ((networkInfo == null) || !networkInfo.isConnected()) {
            Toast.makeText(Video.this, "Network connection failed.", Toast.LENGTH_SHORT).show();
            return;
        }
        Observable<String> observable = Observable.create(new ObservableOnSubscribe<String>() {

            @Override
            public void subscribe(ObservableEmitter<String> e){
                try {
                    String str = String.format("https://api-m.mtime.cn/Movie/Video.api?pageIndex=1&movieId="+movieid);
                    URL url = new URL(str);
                    HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(5000);
                    if(conn.getResponseCode()==200) {
                        InputStream inputStream = conn.getInputStream();
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        byte[] buffer = new byte[1024];
                        int len = 0;
                        while ((len = inputStream.read(buffer)) != -1) { baos.write(buffer, 0, len);}
                        String jsonString = baos.toString();
                        baos.close();
                        inputStream.close();

                        Trailer recyclerObj = new Gson().fromJson(jsonString, Trailer.class);

                        for(int i = 0; i <recyclerObj.videoList.size(); i ++) {

                            Trailer.video v= recyclerObj.videoList.get(i);

                            videos.add(v);



                        }
                        e.onNext("true");
                    }
                    else {
                        e.onNext("false");
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                    Toast.makeText(Video.this, "Data load failed.", Toast.LENGTH_LONG).show();
                }
            }

        });


        //一次性观察者，只能订阅一次
        DisposableObserver<String> disposableObserver = new DisposableObserver<String>() {

            @Override
            public void onNext(String value) {
                if(value.equals("false")){
                    Toast.makeText(Video.this, "Data load failed.", Toast.LENGTH_SHORT).show();
                }
                else{
                    videoView.setVideoURI(Uri.parse(videos.get(0).url));
                    videoView.start();
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onComplete() {

            }
        };

        //新线程进行网络访问

        observable.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(disposableObserver);
    }
}
