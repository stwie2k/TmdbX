package nyamori.moe.tmdbx.frags;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;
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
import nyamori.moe.tmdbx.DetailActivity;
import nyamori.moe.tmdbx.obj.MovieObj;
import nyamori.moe.tmdbx.OnSwipeTouchListener;
import nyamori.moe.tmdbx.R;
import nyamori.moe.tmdbx.SearchResultActivity;
import nyamori.moe.tmdbx.obj.TVDetailActivity;
import nyamori.moe.tmdbx.obj.TVObj;
import nyamori.moe.tmdbx.adapter.MainAdapter;
import nyamori.moe.tmdbx.adapter.TVAdapter;

public class Fragment1 extends Fragment {

    //0 for movie,1 for tv
    private int type = 0;
    private int page = 1;
    private String language = "";
    private String Mgenre = "";
    private String Tgenre = "";
    private String releaseYear = "";
    private String sortBy = "&sort_by=popularity.desc";
    private List<MovieObj.Result> list = new ArrayList<MovieObj.Result>();
    private MainAdapter mainAdapter = new MainAdapter(list);
    private List<TVObj.TVResult> mlist = new ArrayList<TVObj.TVResult>();
    private TVAdapter tvAdapter = new TVAdapter(mlist);

    private Spinner mSpinnerType;
    private Spinner mSpinnerMovieGenre;
    private Spinner mSpinnerTVGenre;
    private Spinner mSpinnerYears;
    private Spinner mSpinnerLanguage;

    private LinearLayout movie_layout;
    private LinearLayout tv_layout;

    private Button searchButton;
    private EditText searchEditText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment1, container, false);


        final RecyclerView recyclerView = rootView.findViewById(R.id.mainRecyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));

        movie_layout = rootView.findViewById(R.id.movie_layout);
        tv_layout = rootView.findViewById(R.id.tv_layout);

        mSpinnerType = rootView.findViewById(R.id.spinner_type);
        mSpinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                page = 1;
                switch (position){
                    case 0:
                        tv_layout.setVisibility(View.GONE);
                        movie_layout.setVisibility(View.VISIBLE);
                        type = 0;
                        recyclerView.setAdapter(mainAdapter);
                        break;

                    case 1:
                        tv_layout.setVisibility(View.VISIBLE);
                        movie_layout.setVisibility(View.GONE);
                        type = 1;
                        recyclerView.setAdapter(tvAdapter);
                        break;
                }
                startUpdateList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mSpinnerTVGenre = rootView.findViewById(R.id.spinner_tv_genre);
        mSpinnerTVGenre.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                page = 1;
                switch (position)
                {
                    case 0:
                        Tgenre = "";
                        break;
                    case 1:
                        Tgenre = "&with_genres=10759";
                        break;
                    case 2:
                        Tgenre = "&with_genres=16";
                        break;
                    case 3:
                        Tgenre = "&with_genres=35";
                        break;
                    case 4:
                        Tgenre = "&with_genres=80";
                        break;
                    case 5:
                        Tgenre = "&with_genres=99";
                        break;
                    case 6:
                        Tgenre = "&with_genres=18";
                        break;
                    case 7:
                        Tgenre = "&with_genres=10751";
                        break;
                    case 8:
                        Tgenre = "&with_genres=10762";
                        break;
                    case 9:
                        Tgenre = "&with_genres=9648";
                        break;
                    case 10:
                        Tgenre = "&with_genres=10763";
                        break;
                    case 11:
                        Tgenre = "&with_genres=10764";
                        break;
                    case 12:
                        Tgenre = "&with_genres=10765";
                        break;
                    case 13:
                        Tgenre = "&with_genres=10766";
                        break;
                    case 14:
                        Tgenre = "&with_genres=10767";
                        break;
                    case 15:
                        Tgenre = "&with_genres=10768";
                        break;
                    case 16:
                        Tgenre = "&with_genres=37";
                        break;
                }
                startUpdateList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Tgenre = "";
            }
        });

        mSpinnerMovieGenre = rootView.findViewById(R.id.spinner_movie_genre);
        mSpinnerMovieGenre.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                page = 1;
                switch (pos){
                    case  0:
                        Mgenre = "";
                        break;
                    case 1:
                        Mgenre = "&with_genres=28";
                        break;
                    case 2:
                        Mgenre = "&with_genres=12";
                        break;
                    case 3:
                        Mgenre = "&with_genres=16";
                        break;
                    case 4:
                        Mgenre = "&with_genres=35";
                        break;
                    case 5:
                        Mgenre = "&with_genres=80";
                        break;
                    case 6:
                        Mgenre = "&with_genres=99";
                        break;
                    case 7:
                        Mgenre = "&with_genres=18";
                        break;
                    case 8:
                        Mgenre = "&with_genres=10751";
                        break;
                    case 9:
                        Mgenre = "&with_genres=14";
                        break;
                    case 10:
                        Mgenre = "&with_genres=36";
                        break;
                    case 11:
                        Mgenre = "&with_genres=27";
                        break;
                    case 12:
                        Mgenre = "&with_genres=10402";
                        break;
                    case 13:
                        Mgenre = "&with_genres=9648";
                        break;
                    case 14:
                        Mgenre = "&with_genres=10749";
                        break;
                    case 15:
                        Mgenre = "&with_genres=878";
                        break;
                    case 16:
                        Mgenre = "&with_genres=10770";
                        break;
                    case 17:
                        Mgenre = "&with_genres=53";
                        break;
                    case 18:
                        Mgenre = "&with_genres=10752";
                        break;
                    case 19:
                        Mgenre = "&with_genres=37";
                        break;

                }
                startUpdateList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Mgenre = "";
            }
        });
        /*
        mSpinnerYears = rootView.findViewById(R.id.spinner_years);
        mSpinnerYears.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                page = 1;
                switch (pos)
                {
                    case 0:
                        releaseYear = "";
                        break;
                    case 1:
                        releaseYear = "&primary_release_date.gte=2019-01-01&primary_release_date.lte=2019-12-31";
                        break;
                    case 2:
                        releaseYear = "&primary_release_date.gte=2018-01-01&primary_release_date.lte=2018-12-31";
                        break;
                    case 3:
                        releaseYear = "&primary_release_date.gte=2017-01-01&primary_release_date.lte=2017-12-31";
                        break;
                    case 4:
                        releaseYear = "&primary_release_date.gte=2016-01-01&primary_release_date.lte=2016-12-31";
                        break;
                    case 5:
                        releaseYear = "&primary_release_date.gte=2015-01-01&primary_release_date.lte=2015-12-31";
                        break;
                    case 6:
                        releaseYear = "&primary_release_date.gte=2014-01-01&primary_release_date.lte=2014-12-31";
                        break;
                    case 7:
                        releaseYear = "&primary_release_date.gte=2013-01-01&primary_release_date.lte=2013-12-31";
                        break;
                    case 8:
                        releaseYear = "&primary_release_date.gte=2012-01-01&primary_release_date.lte=2012-12-31";
                        break;
                    case 9:
                        releaseYear = "&primary_release_date.gte=2011-01-01&primary_release_date.lte=2011-12-31";
                        break;
                    case 10:
                        releaseYear = "&primary_release_date.gte=2010-01-01&primary_release_date.lte=2010-12-31";
                        break;
                    case 11:
                        releaseYear = "&primary_release_date.lte=2009-12-31";
                        break;
                }
                startUpdateList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                releaseYear = "";
            }
        });
        */
        mSpinnerLanguage = rootView.findViewById(R.id.spinner_language);
        mSpinnerLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                page = 1;
                switch (pos)
                {
                    case 0:
                        language = "";
                        break;
                    case 1:
                        language = "&with_original_language=zh";
                        break;
                    case 2:
                        language = "&with_original_language=ja";
                        break;
                    case 3:
                        language = "&with_original_language=de";
                        break;
                }
                startUpdateList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                language = "";
            }

        });

//        addGenreForSpinner();
//        Adapter adapter = new ArrayAdapter<String>(getContext(),
//                R.layout.genre_list_item, mMovieGenreList);


        mainAdapter.setOnItemClickListener(new MainAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                MovieObj.Result movie = mainAdapter.getMovie(position);
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("id", movie.getId());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        tvAdapter.setOnItemClickListener(new TVAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                TVObj.TVResult tv = tvAdapter.getTV(position);
                Intent intent = new Intent(getActivity(), TVDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("id", tv.getId());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });


        recyclerView.setAdapter(mainAdapter);
        recyclerView.setHasFixedSize(true);
        startUpdateList();

        recyclerView.setOnTouchListener(new OnSwipeTouchListener(getActivity()){
            public void onSwipeTop() {

            }
            public void onSwipeRight() {
                if(page > 1){
                    page --;
                    startUpdateList();
                }
                else{
                    Toast.makeText(getActivity(), "This is the first page.", Toast.LENGTH_SHORT).show();
                }
            }
            public void onSwipeLeft() {
                page++;
                startUpdateList();

            }
            public void onSwipeBottom() {

            }
        });

        searchButton = rootView.findViewById(R.id.searchButton);
        searchEditText = rootView.findViewById(R.id.searchEditText);
        searchEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                searchEditText.setFocusable(true);
                searchEditText.setFocusableInTouchMode(true);
                return false;
            }
        });
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String keyword = searchEditText.getText().toString();
                Intent intent = new Intent(getActivity(),SearchResultActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("keyword",keyword);
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });


        return rootView;
    }

//    private void addGenreForSpinner() {
//        mMovieGenreList.add("Action");
//        mMovieGenreList.add("Adventure");
//        mMovieGenreList.add("Animation");
//        mMovieGenreList.add("Comedy");
//        mMovieGenreList.add("Crime");
//    }

    public void startUpdateList() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if ((networkInfo == null) || !networkInfo.isConnected()) {
            Toast.makeText(getActivity(), "Network connection failed.", Toast.LENGTH_SHORT).show();
            return;
        }
        Observable<String> observable = Observable.create(new ObservableOnSubscribe<String>() {

            @Override
            public void subscribe(ObservableEmitter<String> e){
                if(type == 0){
                    try {
                        String str = String.format("https://api.themoviedb.org/3/discover/movie?api_key=7888f0042a366f63289ff571b68b7ce0&include_adult=false%s&page=%d%s%s%s", language, page, Mgenre, releaseYear, sortBy);
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
                            mainAdapter.setMovies(recyclerObj.getResults());
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
                else {
                    try {
                        String str = String.format("https://api.themoviedb.org/3/discover/tv?api_key=7888f0042a366f63289ff571b68b7ce0&include_adult=false%s&page=%d%s%s", language, page, Tgenre, sortBy);
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
                            tvAdapter.setTV(recyclerObj.getResults());
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
                    mainAdapter.notifyDataSetChanged();
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
