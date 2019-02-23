package nyamori.moe.tmdbx.frags;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
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
import nyamori.moe.tmdbx.obj.In_theaters;
import nyamori.moe.tmdbx.obj.Movie;
import nyamori.moe.tmdbx.R;
import nyamori.moe.tmdbx.Video;
import nyamori.moe.tmdbx.adapter.ListAdapter;


public class Fragment3 extends Fragment {
    ListAdapter adapter;
    List<Movie> data = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment3, container, false);

        ListView listview = (ListView)rootView.findViewById(R.id.lv);

        adapter = new ListAdapter(getActivity(), data);

        adapter.setOnItemClickListener(new ListAdapter.onItemClickListener() {
            @Override
            public void onClick(View v,int position) {

                Intent intent = new Intent(getActivity(), Video.class);
                Bundle bundle = new Bundle();
                bundle.putInt("id",data.get(position).id);
                bundle.putString("title",data.get(position).t);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

       listview.setAdapter(adapter);

       startUpdateList();



        return rootView;
    }
    public void startUpdateList(){
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if ((networkInfo == null) || !networkInfo.isConnected()) {
            Toast.makeText(getActivity(), "Network connection failed.", Toast.LENGTH_SHORT).show();
            return;
        }
        Observable<String> observable = Observable.create(new ObservableOnSubscribe<String>() {

            @Override
            public void subscribe(ObservableEmitter<String> e){
                try {
                    String str = String.format("https://api-m.mtime.cn/Showtime/LocationMovies.api?locationId=290");
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

                        In_theaters recyclerObj = new Gson().fromJson(jsonString, In_theaters.class);

                       for(int i = 0; i <10; i ++) {

                           Movie m=recyclerObj.getms().get(i);

                           data.add(m);



                        }
                        e.onNext("true");
                    }
                    else {
                        e.onNext("false");
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                    Toast.makeText(getActivity(), "Data load failed.", Toast.LENGTH_LONG).show();
                }
            }

        });


        //一次性观察者，只能订阅一次
        DisposableObserver<String> disposableObserver = new DisposableObserver<String>() {

            @Override
            public void onNext(String value) {
                if(value.equals("false")){
                    Toast.makeText(getActivity(), "Data load failed.", Toast.LENGTH_SHORT).show();
                }
                else{
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
