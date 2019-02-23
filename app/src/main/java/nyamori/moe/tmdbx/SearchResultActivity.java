package nyamori.moe.tmdbx;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

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
import nyamori.moe.tmdbx.adapter.MainAdapter;
import nyamori.moe.tmdbx.adapter.TVAdapter;
import nyamori.moe.tmdbx.obj.MovieObj;
import nyamori.moe.tmdbx.obj.TVDetailActivity;
import nyamori.moe.tmdbx.obj.TVObj;

public class SearchResultActivity extends AppCompatActivity {

    private String keyword;
    private List<MovieObj.Result> list = new ArrayList<MovieObj.Result>();
    private MainAdapter searchAdapter = new MainAdapter(list);

    private List<TVObj.TVResult> mlist = new ArrayList<TVObj.TVResult>();
    private TVAdapter tvAdapter = new TVAdapter(mlist);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Bundle b = getIntent().getExtras();
        keyword = (String) b.get("keyword");

        RecyclerView recyclerView = findViewById(R.id.searchRecyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(SearchResultActivity.this,3));

        RecyclerView tvRecyclerView = findViewById(R.id.tvRecyclerView);
        tvRecyclerView.setLayoutManager(new GridLayoutManager(SearchResultActivity.this,3));

        searchAdapter.setOnItemClickListener(new MainAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                MovieObj.Result movie = searchAdapter.getMovie(position);
                Intent intent = new Intent(SearchResultActivity.this,DetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("id",movie.getId());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        tvAdapter.setOnItemClickListener(new TVAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                TVObj.TVResult tv = tvAdapter.getTV(position);
                Intent intent = new Intent(SearchResultActivity.this, TVDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("id", tv.getId());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });


        recyclerView.setAdapter(searchAdapter);
        recyclerView.setHasFixedSize(true);

        tvRecyclerView.setAdapter(tvAdapter);
        tvRecyclerView.setHasFixedSize(true);

        startUpdateList();
    }

    public void startUpdateList(){
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if ((networkInfo == null) || !networkInfo.isConnected()) {
            Toast.makeText(SearchResultActivity.this, "Network connection failed.", Toast.LENGTH_SHORT).show();
            return;
        }
        Observable<String> observable = Observable.create(new ObservableOnSubscribe<String>() {

            @Override
            public void subscribe(ObservableEmitter<String> e){
                try {
                    for(int p = 0; p < 5; p ++){
                        String str = String.format("https://api.themoviedb.org/3/search/movie?api_key=7888f0042a366f63289ff571b68b7ce0&query=%s&page=%d", keyword,p);
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

                            MovieObj recyclerObj = new Gson().fromJson(jsonString, MovieObj.class);
                            for(int i = 0; i < recyclerObj.getResults().size(); i ++) {
                                searchAdapter.addMovie(recyclerObj.getResults().get(i));
                            }
                            //e.onNext("true");
                        }
                        else {
                            //e.onNext("false");
                        }
                    }
                    for(int p = 0; p < 5; p ++){
                        String str = String.format("https://api.themoviedb.org/3/search/tv?api_key=7888f0042a366f63289ff571b68b7ce0&query=%s&page=%d", keyword,p);
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

                            TVObj recyclerObj = new Gson().fromJson(jsonString, TVObj.class);
                            for(int i = 0; i < recyclerObj.getResults().size(); i ++) {
                                tvAdapter.addTV(recyclerObj.getResults().get(i));
                            }
                            //e.onNext("true");
                        }
                        else {
                            //e.onNext("false");
                        }
                    }
                    e.onNext("true");
                } catch (Exception ex) {
                    ex.printStackTrace();
                    Toast.makeText(SearchResultActivity.this, "Data load failed.", Toast.LENGTH_LONG).show();
                }
            }

        });


        //一次性观察者，只能订阅一次
        DisposableObserver<String> disposableObserver = new DisposableObserver<String>() {

            @Override
            public void onNext(String value) {
                if(value.equals("false")){
                    Toast.makeText(SearchResultActivity.this, "Data load failed.", Toast.LENGTH_SHORT).show();
                }
                else{
                    searchAdapter.notifyDataSetChanged();
                    tvAdapter.notifyDataSetChanged();
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
