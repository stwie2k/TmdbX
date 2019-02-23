package nyamori.moe.tmdbx;

import android.app.Fragment;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import java.util.ArrayList;
import java.util.List;

import nyamori.moe.tmdbx.adapter.MainAdapter;
import nyamori.moe.tmdbx.frags.Fragment1;
import nyamori.moe.tmdbx.frags.Fragment2;
import nyamori.moe.tmdbx.frags.Fragment3;
import nyamori.moe.tmdbx.obj.MovieObj;

public class MainActivity extends AppCompatActivity {

    private int page = 1;
    private String language = "";
    private String Mgenre = "";
    private String releaseYear = "";
    private String sortBy = "&sort_by=popularity.desc";
    private List<MovieObj.Result> list = new ArrayList<MovieObj.Result>();
    private MainAdapter mainAdapter = new MainAdapter(list);

    BottomBar bottomBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        bottomBar = (BottomBar) findViewById(R.id.bottomBar);

        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {

                Object ob=null;
                if (tabId == R.id.tab_favorites) {
                    // 选择指定 id 的标签
                    ob  = new Fragment1();

                }else if(tabId == R.id.tab_nearby){
                    ob  = new Fragment3();
                }else if(tabId == R.id.tab_friends){
                    ob  = new Fragment2();
                }


                getFragmentManager().beginTransaction().replace(R.id.fragment_container,(Fragment) ob).commit();
            }
        });



    }
}
