package nyamori.moe.tmdbx.adapter;



import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import nyamori.moe.tmdbx.obj.Movie;
import nyamori.moe.tmdbx.R;

public class ListAdapter extends BaseAdapter {
    private Context mContext;
    private List<Movie> mList = new ArrayList<>();
    ViewHolder viewHolder = null;

    private ListView listview;
    private LruCache<String, BitmapDrawable> mImageCache;


    public ListAdapter(Context context, List<Movie> list) {
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
            view = LayoutInflater.from(mContext).inflate(R.layout.onscreen, null);
            viewHolder.mTitle = view.findViewById(R.id.title);
            viewHolder.mType =  view.findViewById(R.id.type);
            viewHolder.mActors = view.findViewById(R.id.actors);
            viewHolder.mCommon=view.findViewById(R.id.common);

            viewHolder.mPicture= view.findViewById(R.id.image);


            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.mTitle.setText(mList.get(i).t);
        viewHolder.mType.setText(mList.get(i).movieType);
        viewHolder.mCommon.setText(mList.get(i).commonSpecial);
        viewHolder.mActors.setText(mList.get(i).actors);

        String urlpath = mList.get(i).img;

        viewHolder.mPicture.setTag(urlpath);
        // 如果本地已有缓存，就从本地读取，否则从网络请求数据
        if (mImageCache.get(urlpath) != null) {
            viewHolder.mPicture.setImageDrawable(mImageCache.get(urlpath));
        } else {
            ImageTask it = new ImageTask();
            it.execute(urlpath);
        }








        viewHolder.mPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                mOnItemClickListener.onClick(v,i);
            }
        });
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
        TextView mCommon;
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



    Bitmap getInternetPicture(String UrlPath) {
        Bitmap bm = null;
        // 1、确定网址
        // http://pic39.nipic.com/20140226/18071023_164300608000_2.jpg
        String urlpath = UrlPath;
        // 2、获取Uri
        try {
            URL uri = new URL(urlpath);

            // 3、获取连接对象、此时还没有建立连接
            HttpURLConnection connection = (HttpURLConnection) uri.openConnection();
            // 4、初始化连接对象
            // 设置请求的方法，注意大写
            connection.setRequestMethod("GET");
            // 读取超时
            connection.setReadTimeout(5000);
            // 设置连接超时
            connection.setConnectTimeout(5000);
            // 5、建立连接
            connection.connect();

            // 6、获取成功判断,获取响应码
            if (connection.getResponseCode() == 200) {
                // 7、拿到服务器返回的流，客户端请求的数据，就保存在流当中
                InputStream is = connection.getInputStream();
                // 8、开启文件输出流，把读取到的字节写到本地缓存文件
                File file = new File(mContext.getCacheDir(), getFileName(urlpath));
                FileOutputStream fos = new FileOutputStream(file);
                int len = 0;
                byte[] b = new byte[1024];
                while ((len = is.read(b)) != -1) {
                    fos.write(b, 0, len);
                }
                fos.close();
                is.close();
                //9、 通过图片绝对路径，创建Bitmap对象

                bm = BitmapFactory.decodeFile(file.getAbsolutePath());

                Log.i("", "网络请求成功");

            } else {
                Log.v("tag", "网络请求失败");
                bm = null;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bm;

    }

    public String getFileName(String path) {
        int index = path.lastIndexOf("/");
        return path.substring(index + 1);
    }

}
