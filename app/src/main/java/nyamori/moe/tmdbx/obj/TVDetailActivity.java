package nyamori.moe.tmdbx.obj;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
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
import nyamori.moe.tmdbx.R;
import nyamori.moe.tmdbx.adapter.SeasonAdapter;
import nyamori.moe.tmdbx.collection.CollectionLab;
import nyamori.moe.tmdbx.collection.CollectionObj;

public class TVDetailActivity extends AppCompatActivity {

    private long id;
    private List<TVDetail.Season> list = new ArrayList<TVDetail.Season>();
    private SeasonAdapter seasonAdapter = new SeasonAdapter(list);
    private boolean flag;
    private CollectionLab mCollectionLab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_tv);

        Bundle b = getIntent().getExtras();
        id = (long)b.get("id");

        final ImageView backdrop = findViewById(R.id.backdrop);
        final TextView originalname = findViewById(R.id.originalname);
        final TextView name = findViewById(R.id.name);
        final TextView origianallanguage = findViewById(R.id.originallanguage);
        final TextView languages = findViewById(R.id.languages);
        final TextView genres = findViewById(R.id.genres);
        final TextView origincountries = findViewById(R.id.origincountries);
        final TextView productioncompanies = findViewById(R.id.productioncompanies);
        final TextView firstairdate = findViewById(R.id.firstairdate);
        final TextView lastairdate = findViewById(R.id.lastairdate);
        final TextView status = findViewById(R.id.status);
        final TextView score = findViewById(R.id.score);
        final TextView homepage = findViewById(R.id.homepage);
        final TextView creators = findViewById(R.id.creators);
        final TextView overview = findViewById(R.id.overview);
        final FloatingActionButton fab = findViewById(R.id.fab);

        mCollectionLab = CollectionLab.get(getApplicationContext());

        Observable<TVDetail> obs = Observable.create(new ObservableOnSubscribe<TVDetail>() {

            @Override
            public void subscribe(ObservableEmitter<TVDetail> e){

                try {
                    String str = String.format("https://api.themoviedb.org/3/tv/%d?api_key=7888f0042a366f63289ff571b68b7ce0&append_to_response=casts",id);
                    URL url = new URL(str);
                    HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(5000);
                    if(conn.getResponseCode()==200) {
                        InputStream inputStream = conn.getInputStream();
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        byte[] buffer = new byte[1024];
                        int len = 0;
                        while ((len = inputStream.read(buffer)) != -1) {
                            baos.write(buffer, 0, len);
                        }
                        String jsonString = baos.toString();
                        baos.close();
                        inputStream.close();
                        TVDetail detail = new Gson().fromJson(jsonString, TVDetail.class);
                        e.onNext(detail);
                    }
                }
                catch (Exception ex){
                    ex.printStackTrace();
                }
            }

        });


        //一次性观察者，只能订阅一次
        DisposableObserver<TVDetail> dis = new DisposableObserver<TVDetail>() {

            @Override
            public void onNext(TVDetail value) {

                String temp = "";
                originalname.setText(value.getOriginal_name());
                name.setText(value.getName());
                origianallanguage.setText(value.getOriginal_language());
                for(int i = 0; i < value.getLanguages().size(); i ++){
                    temp += " | " + value.getLanguages().get(i);
                }
                if(temp != ""){
                    temp = temp.substring(3);
                }
                languages.setText(temp);
                temp = "";
                for(int i = 0; i < value.getGenres().size(); i++){
                    temp += " | " + value.getGenres().get(i).getName();
                }
                if(temp != ""){
                    temp = temp.substring(3);
                }
                genres.setText(temp);
                temp = "";
                for(int i = 0; i < value.getOrigin_country().size(); i++){
                    temp += value.getOrigin_country().get(i) + "\n";
                }
                origincountries.setText(temp);
                temp = "";
                for(int i = 0; i < value.getProduction_companies().size(); i++){
                    temp += value.getProduction_companies().get(i).getName() + "\n";
                }
                productioncompanies.setText(temp);
                firstairdate.setText(value.getFirst_air_date());
                lastairdate.setText(value.getLast_air_date());
                status.setText(value.getStatus());
                score.setText(String.format("%f points / %d participants",value.getVote_average(),value.getVote_count()));
                homepage.setText(value.getHomepage());
                temp = "";
                for(int i = 0; i < value.getCreated_by().size(); i++){
                    temp += value.getCreated_by().get(i).getName() + "\n";
                }
                creators.setText(temp);
                overview.setText(value.getOverview());
                String imageUrl = String.format("https://image.tmdb.org/t/p/w500" + value.getBackdrop_path());
                BitmapWorkerTask task = new BitmapWorkerTask(backdrop);
                task.execute(imageUrl);
                seasonAdapter.setSeasons(value.getSeasons());
                seasonAdapter.notifyDataSetChanged();


            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onComplete() {

            }
        };

        obs.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(dis);

        RecyclerView recyclerView = findViewById(R.id.seasonsRecyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(TVDetailActivity.this,3));
        recyclerView.setAdapter(seasonAdapter);
        recyclerView.setHasFixedSize(true);


        //TODO 判断用户是否收藏了该电影，电视
        flag = false;
        if(CurrentUser.getUuid() != null){

            List<CollectionObj> mcollection = mCollectionLab.getCollectionsOfUser(CurrentUser.getUuid());
            for(int i = 0; i < mcollection.size(); i ++){
                if(mcollection.get(i).getCollectionType().equals("tv") && mcollection.get(i).getMovieId().equals(String.valueOf(id))){
                    flag = true;
                    break;
                }
            }
            if(!flag){
                fab.setImageResource(R.drawable.empty_star);
            }
            else{
                fab.setImageResource(R.drawable.full_star);
            }

        }

        //TODO 点击收藏按钮响应事件，注意改变按钮的样式
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //没有用户登录的情况
                if(CurrentUser.getUser().equals("")){
                    Toast.makeText(TVDetailActivity.this, "Please login first.", Toast.LENGTH_SHORT).show();
                    return;
                }
                //用户已经收藏了该视频

                if(flag){
                    fab.setImageResource(R.drawable.empty_star);
                    flag = false;
                    CollectionObj temp = new CollectionObj(String.valueOf(id),CurrentUser.getUuid(),"tv");
                    mCollectionLab.deleteCollection(temp);
                    Toast.makeText(TVDetailActivity.this, "Remove from favorites successfully.", Toast.LENGTH_SHORT).show();

                }
                //用户没有收藏该视频

                else {
                    fab.setImageResource(R.drawable.full_star);
                    flag = true;
                    CollectionObj temp = new CollectionObj(String.valueOf(id),CurrentUser.getUuid(),"tv");
                    mCollectionLab.addCollection(temp);
                    Toast.makeText(TVDetailActivity.this, "Add to favorites successfully.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    class BitmapWorkerTask extends AsyncTask<String, Void, BitmapDrawable> {

        private ImageView imageView;

        public BitmapWorkerTask(ImageView _imageView) {
            imageView = _imageView;
        }

        @Override
        protected BitmapDrawable doInBackground(String... params) {
            String imageUrl = params[0];
            // 在后台开始下载图片
            Bitmap bitmap = downloadBitmap(imageUrl);
            BitmapDrawable drawable = new BitmapDrawable(bitmap);
            return drawable;
        }

        @Override
        protected void onPostExecute(BitmapDrawable drawable) {
            if (imageView != null && drawable != null) {
                imageView.setImageDrawable(drawable);
            }
        }

        /**
         * 建立HTTP请求，并获取Bitmap对象。
         *
         * @param imageUrl
         *            图片的URL地址
         * @return 解析后的Bitmap对象
         */
        private Bitmap downloadBitmap(String imageUrl) {
            Bitmap bitmap = null;
            HttpURLConnection con = null;
            try {
                URL url = new URL(imageUrl);
                con = (HttpURLConnection) url.openConnection();
                con.setConnectTimeout(5 * 1000);
                con.setReadTimeout(10 * 1000);
                bitmap = BitmapFactory.decodeStream(con.getInputStream());
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (con != null) {
                    con.disconnect();
                }
            }
            return bitmap;
        }

    }
}
