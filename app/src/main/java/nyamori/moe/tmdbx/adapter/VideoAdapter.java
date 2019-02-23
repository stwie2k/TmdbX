package nyamori.moe.tmdbx.adapter;





import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import nyamori.moe.tmdbx.R;
import nyamori.moe.tmdbx.Trailer;

public class VideoAdapter extends BaseAdapter {
    private Context mContext;
    private List<Trailer.video> mList = new ArrayList<>();
    ViewHolder viewHolder = null;

    private ListView listview;
    private LruCache<String, BitmapDrawable> mImageCache;
    private int currentItem;

    public void setCurrentItem(int currentItem) {
        this.currentItem = currentItem;
    }

    public VideoAdapter(Context context, List<Trailer.video> list) {
        mContext = context;
        mList = list;

        int maxCache = (int) Runtime.getRuntime().maxMemory();
        int cacheSize = maxCache / 8;
        mImageCache = new LruCache<String, BitmapDrawable>(cacheSize) {
            @Override
            protected int sizeOf(String key, BitmapDrawable value) {

                return value.getBitmap().getByteCount();

            }
        };
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {






        if (listview == null) {
            listview = (ListView) viewGroup;
        }

        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.videolist, null);

            viewHolder.mTitle = view.findViewById(R.id.title);


            viewHolder.mPicture= view.findViewById(R.id.image);


            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.mTitle.setText(mList.get(i).title);


        String urlpath = mList.get(i).image;

        viewHolder.mPicture.setTag(urlpath);

        // 如果本地已有缓存，就从本地读取，否则从网络请求数据
        if (mImageCache.get(urlpath) != null) {
            viewHolder.mPicture.setImageDrawable(mImageCache.get(urlpath));
        } else {
            ImageTask it = new ImageTask();
            it.execute(urlpath);
        }









        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                mOnItemClickListener.onClick(v,i);
            }
        });

        if (currentItem == i) {
            //如果被点击，设置当前TextView被选中
            viewHolder.mTitle.setSelected(true);
        } else {
            //如果没有被点击，设置当前TextView未被选中
            viewHolder.mTitle.setSelected(false);
        }

        return view;
    }


    public interface onItemClickListener {
        void onClick(View v,int i);
    }

    private onItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(onItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public  class ViewHolder {
        TextView mTitle;
        TextView mType;
        TextView mActors;
        ImageView mPicture;
        Bitmap bmp;

    }
    public void addBitmapToMemoryCache(String key, BitmapDrawable drawable) {
        if (getBitmapFromMemoryCache(key) == null ) {
            mImageCache.put(key, drawable);
        }
    }
    public BitmapDrawable getBitmapFromMemoryCache(String key) {
        return mImageCache.get(key);
    }

    class ImageTask extends AsyncTask<String, Void, BitmapDrawable> {

        private String imageUrl;

        @Override
        protected BitmapDrawable doInBackground(String... params) {
            imageUrl = params[0];
            Bitmap bitmap = downloadImage();
            BitmapDrawable db = new BitmapDrawable(
                    bitmap);
            // 如果本地还没缓存该图片，就缓存
            if(bitmap !=null )addBitmapToMemoryCache(imageUrl, db);

            return db;
        }

        @Override
        protected void onPostExecute(BitmapDrawable result) {
            // 通过Tag找到我们需要的ImageView，如果该ImageView所在的item已被移出页面，就会直接返回null
            ImageView iv = (ImageView) listview.findViewWithTag(imageUrl);
            if (iv != null && result != null) {
                iv.setImageDrawable(result);
            }
        }

        /**
         * 根据url从网络上下载图片
         *
         * @return
         */
        private Bitmap downloadImage() {
            HttpURLConnection con = null;
            Bitmap bitmap = null;
            try {
                URL url = new URL(imageUrl);
                con = (HttpURLConnection) url.openConnection();
                con.setConnectTimeout(5 * 1000);
                con.setReadTimeout(10 * 1000);
                bitmap = BitmapFactory.decodeStream(con.getInputStream());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
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
