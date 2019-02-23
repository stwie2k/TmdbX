package nyamori.moe.tmdbx.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import nyamori.moe.tmdbx.R;
import nyamori.moe.tmdbx.obj.TVObj;

public class TVAdapter extends RecyclerView.Adapter<TVAdapter.MViewHolder>{

    class MViewHolder extends RecyclerView.ViewHolder{
        public ImageView videoItemPoster;
        public TextView videoItemName;
        public View view;

        public MViewHolder(View _view){
            super(_view);
            view = _view;
            videoItemName = (TextView)_view.findViewById(R.id.videoItemName);
            videoItemPoster = (ImageView)_view.findViewById(R.id.videoItemPoster);
        }
    }
    private List<TVObj.TVResult> tvs;
    //图片缓存
    private LruCache<String, BitmapDrawable> mMemoryCache;

    private RecyclerView recyclerView;


    private OnItemClickListener onItemClickListener;

    public TVAdapter(List<TVObj.TVResult> _tvs){
        tvs = _tvs;
        // 获取应用程序最大可用内存
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int cacheSize = maxMemory / 8;
        mMemoryCache = new LruCache<String, BitmapDrawable>(cacheSize) {
            @Override
            protected int sizeOf(String key, BitmapDrawable drawable) {
                return drawable.getBitmap().getByteCount();
            }
        };
    }

    @Override
    public int getItemCount(){
        return tvs.size();
    }

    @Override
    public MViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(recyclerView == null){
            recyclerView = (RecyclerView)parent;
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_item,parent,false);
        MViewHolder holder = new MViewHolder(view);
        return holder;
    }

    /**
     * 将一张图片存储到LruCache中。
     *
     * @param key
     *            LruCache的键，这里传入图片的URL地址。
     * @param drawable
     *            LruCache的值，这里传入从网络上下载的BitmapDrawable对象。
     */
    public void addBitmapToMemoryCache(String key, BitmapDrawable drawable) {
        if (getBitmapFromMemoryCache(key) == null) {
            mMemoryCache.put(key, drawable);
        }
    }

    /**
     * 从LruCache中获取一张图片，如果不存在就返回null。
     *
     * @param key
     *            LruCache的键，这里传入图片的URL地址。
     * @return 对应传入键的BitmapDrawable对象，或者null。
     */
    public BitmapDrawable getBitmapFromMemoryCache(String key) {
        return mMemoryCache.get(key);
    }

    /**
     * 异步下载图片的任务。
     */
    class BitmapWorkerTask extends AsyncTask<String, Void, BitmapDrawable> {

        private String imageUrl;

        public BitmapWorkerTask(String _imageUrl) {
            imageUrl = _imageUrl;
        }

        @Override
        protected BitmapDrawable doInBackground(String... params) {
            // 在后台开始下载图片
            Bitmap bitmap = downloadBitmap(imageUrl);
            BitmapDrawable drawable = new BitmapDrawable(bitmap);
            if(bitmap != null)addBitmapToMemoryCache(imageUrl, drawable);
            return drawable;
        }

        @Override
        protected void onPostExecute(BitmapDrawable drawable) {
            ImageView imageView = (ImageView)recyclerView.findViewWithTag(imageUrl);
            if (imageView != null) {
                if(drawable != null){
                    imageView.setImageDrawable(drawable);
                }
                else{
                    imageView.setImageResource(R.drawable.defaultprofile);
                }
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


    public void onBindViewHolder(final MViewHolder holder, int position) {
        final TVObj.TVResult tv = tvs.get(position);
        holder.videoItemName.setText(tv.getName());
        if(tv.getPoster_path() != null && tv.getPoster_path() != "") {
            String imageUrl = String.format("https://image.tmdb.org/t/p/w200" + tv.getPoster_path());
            //
            holder.videoItemPoster.setTag(imageUrl);
            BitmapDrawable drawable = getBitmapFromMemoryCache(imageUrl);
            if (drawable != null) {
                holder.videoItemPoster.setImageDrawable(drawable);
            } else {
                BitmapWorkerTask task = new BitmapWorkerTask(imageUrl);
                task.execute();
            }
        }
        else {
            holder.videoItemPoster.setImageResource(R.drawable.defaultposter);
        }
        if (onItemClickListener != null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onClick(holder.getAdapterPosition());
                }
            });
        }
    }

    public void addTV(TVObj.TVResult tv){
        tvs.add(tv);
    }

    public void setTV(List<TVObj.TVResult> tvs) {
        this.tvs = tvs;
    }

    public TVObj.TVResult getTV(int position){
        return tvs.get(position);
    }

    public interface OnItemClickListener {
        void onClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener _onItemClickListener) {
        this.onItemClickListener = _onItemClickListener;
    }

}
