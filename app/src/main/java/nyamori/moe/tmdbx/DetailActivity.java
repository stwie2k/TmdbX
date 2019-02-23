package nyamori.moe.tmdbx;

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
import nyamori.moe.tmdbx.adapter.ActorAdapter;
import nyamori.moe.tmdbx.collection.CollectionLab;
import nyamori.moe.tmdbx.collection.CollectionObj;
import nyamori.moe.tmdbx.obj.CurrentUser;
import nyamori.moe.tmdbx.obj.MovieDetail;

public class DetailActivity extends AppCompatActivity {

    private long id;
    private List<MovieDetail.Cast> list = new ArrayList<MovieDetail.Cast>();
    private ActorAdapter actorAdapter = new ActorAdapter(list);
    private CollectionLab mCollectionLab;
    private boolean flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Bundle b = getIntent().getExtras();
        id = (long)b.get("id");

        final ImageView backdrop = findViewById(R.id.backdrop);
        final TextView originaltitle = findViewById(R.id.originaltitle);
        final TextView title = findViewById(R.id.title);
        final TextView origianallanguage = findViewById(R.id.originallanguage);
        final TextView spokenlanguage = findViewById(R.id.spokenlanguage);
        final TextView genres = findViewById(R.id.genres);
        final TextView productioncountries = findViewById(R.id.productioncountries);
        final TextView productioncompanies = findViewById(R.id.productioncompanies);
        final TextView releasedate = findViewById(R.id.releasedate);
        final TextView runtime = findViewById(R.id.runtime);
        final TextView budget = findViewById(R.id.budget);
        final TextView revenue = findViewById(R.id.revenue);
        final TextView score = findViewById(R.id.score);
        final TextView homepage = findViewById(R.id.homepage);
        final TextView overview = findViewById(R.id.overview);
        final FloatingActionButton fab = findViewById(R.id.fab);
        mCollectionLab = CollectionLab.get(getApplicationContext());

        Observable<MovieDetail> obs = Observable.create(new ObservableOnSubscribe<MovieDetail>() {

            @Override
            public void subscribe(ObservableEmitter<MovieDetail> e){

                try {
                    String str = String.format("https://api.themoviedb.org/3/movie/%d?api_key=7888f0042a366f63289ff571b68b7ce0&append_to_response=casts",id);
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
                        MovieDetail detail = new Gson().fromJson(jsonString, MovieDetail.class);
                        e.onNext(detail);
                    }
                }
                catch (Exception ex){
                    ex.printStackTrace();
                }
            }

        });


        //一次性观察者，只能订阅一次
        DisposableObserver<MovieDetail> dis = new DisposableObserver<MovieDetail>() {

            @Override
            public void onNext(MovieDetail value) {

                String temp = "";
                originaltitle.setText(value.getOriginalTitle());
                title.setText(value.getTitle());
                origianallanguage.setText(value.getOriginalLanguage());
                for(int i = 0; i < value.getSpokenLanguages().size(); i ++){
                    temp += " | " + value.getSpokenLanguages().get(i).getName();
                }
                if(temp != ""){
                    temp = temp.substring(3);
                }
                spokenlanguage.setText(temp);
                temp = "";
                for(int i = 0; i < value.getGenres().size(); i++){
                    temp += " | " + value.getGenres().get(i).getName();
                }
                if(temp != ""){
                    temp = temp.substring(3);
                }
                genres.setText(temp);
                temp = "";
                for(int i = 0; i < value.getProductionCountries().size(); i++){
                    temp += value.getProductionCountries().get(i).getName() + "\n";
                }
                productioncountries.setText(temp);
                temp = "";
                for(int i = 0; i < value.getProductionCompanies().size(); i++){
                    temp += value.getProductionCompanies().get(i).getName() + "\n";
                }
                productioncompanies.setText(temp);
                releasedate.setText(value.getReleaseDate());
                runtime.setText(String.format("%d minutes",value.getRuntime()));
                budget.setText(String.format("%d dollars",value.getBudget()));
                revenue.setText(String.format("%d dollars",value.getRevenue()));
                score.setText(String.format("%f points / %d participants",value.getVoteAverage(),value.getVoteCount()));
                homepage.setText(value.getHomepage());
                overview.setText(value.getOverview());
                String imageUrl = String.format("https://image.tmdb.org/t/p/w500" + value.getBackdropPath());
                BitmapWorkerTask task = new BitmapWorkerTask(backdrop);
                task.execute(imageUrl);
                actorAdapter.setActors(value.getCasts().getCast());
                actorAdapter.notifyDataSetChanged();


            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onComplete() {

            }
        };

        obs.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(dis);

        RecyclerView recyclerView = findViewById(R.id.actorRecyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(DetailActivity.this,3));
/*
        actorAdapter.setOnItemClickListener(new MainAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                MovieDetail.Cast actor = actorAdapter.getMovie(position);
                Intent intent = new Intent(DetailActivity.this,DetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("id",movie.getId());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
*/

        recyclerView.setAdapter(actorAdapter);
        recyclerView.setHasFixedSize(true);


        //TODO 判断用户是否收藏了该电影，电视
        flag = false;
        if(CurrentUser.getUuid() != null){

            List<CollectionObj> mcollection = mCollectionLab.getCollectionsOfUser(CurrentUser.getUuid());
            for(int i = 0; i < mcollection.size(); i ++){
                if(mcollection.get(i).getCollectionType().equals("movie") && mcollection.get(i).getMovieId().equals(String.valueOf(id))){
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
                    Toast.makeText(DetailActivity.this, "Please login first.", Toast.LENGTH_SHORT).show();
                    return;
                }
                //用户已经收藏了该视频

                if(flag){
                    fab.setImageResource(R.drawable.empty_star);
                    flag = false;
                    CollectionObj temp = new CollectionObj(String.valueOf(id),CurrentUser.getUuid(),"movie");
                    mCollectionLab.deleteCollection(temp);
                    Toast.makeText(DetailActivity.this, "Remove from favorites successfully.", Toast.LENGTH_SHORT).show();

                }
                //用户没有收藏该视频

                else {
                    fab.setImageResource(R.drawable.full_star);
                    flag = true;
                    CollectionObj temp = new CollectionObj(String.valueOf(id),CurrentUser.getUuid(),"movie");
                    mCollectionLab.addCollection(temp);
                    Toast.makeText(DetailActivity.this, "Add to favorites successfully.", Toast.LENGTH_SHORT).show();
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
