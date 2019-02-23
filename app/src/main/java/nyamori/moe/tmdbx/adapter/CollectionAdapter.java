package nyamori.moe.tmdbx.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import nyamori.moe.tmdbx.collection.CollectionObj;
import nyamori.moe.tmdbx.obj.MovieDetail;
import nyamori.moe.tmdbx.R;
import nyamori.moe.tmdbx.obj.TVDetail;

public class CollectionAdapter extends RecyclerView.Adapter<CollectionAdapter.ViewHolder>  {
    private List<CollectionObj> collections;
    private LayoutInflater mInflater;
    Context context;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onClick(int position);
        void onLongClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener _onItemClickListener) {
        this.onItemClickListener = _onItemClickListener;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
        ImageView Cover;
        TextView Title;
    }

    public void setCollections(List<CollectionObj> collections) {
        this.collections = collections;
    }

    public void removeCollection(int position){
        collections.remove(position);
    }

    public CollectionAdapter(Context _context, List<CollectionObj> items) {
        super();
        collections = items;
        mInflater = LayoutInflater.from(_context);
        context=_context;
    }

    @NonNull
    @Override
    public CollectionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.collection_item, viewGroup, false);
        ViewHolder holder = new ViewHolder(view) {
            @Override
            public String toString() {
                return super.toString();
            }
        };
        holder.Cover=view.findViewById(R.id.movie_cover);
        holder.Title = view.findViewById(R.id.movie_title);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final CollectionAdapter.ViewHolder viewHolder, final int i) {

        final int id = Integer.valueOf(collections.get(i).getMovieId());
        if(collections.get(i).getCollectionType().equals("movie")){
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
                public void onNext(final MovieDetail value) {
                    viewHolder.Title.setText(value.getTitle());
                    Observable.create(new ObservableOnSubscribe<Bitmap>() {
                        @Override
                        public void subscribe(ObservableEmitter<Bitmap> emitter) throws Exception {

                            String urlpath = String.format("https://image.tmdb.org/t/p/w500" + value.getBackdropPath());
                            final Bitmap bmp = getInternetPicture(urlpath);
                            emitter.onNext(bmp);
                            emitter.onComplete();
                        }
                    }).observeOn(AndroidSchedulers.mainThread())//回调在主线程
                            .subscribeOn(Schedulers.io())//执行在io线程
                            .subscribe(new io.reactivex.Observer<Bitmap>() {
                                @Override
                                public void onSubscribe(Disposable d) {

                                }

                                @Override
                                public void onNext(Bitmap bm) {
                                    viewHolder.Cover.setImageBitmap(bm);
                                    //viewHolder.bmp=bm;
                                }

                                @Override
                                public void onError(Throwable e) {

                                }

                                @Override
                                public void onComplete() {

                                }
                            });
                }

                @Override
                public void onError(Throwable e) {
                }

                @Override
                public void onComplete() {

                }
            };

            obs.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(dis);

        }
        else{
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
                public void onNext(final TVDetail value) {
                    viewHolder.Title.setText(value.getName());
                    Observable.create(new ObservableOnSubscribe<Bitmap>() {
                        @Override
                        public void subscribe(ObservableEmitter<Bitmap> emitter) throws Exception {

                            String urlpath = String.format("https://image.tmdb.org/t/p/w500" + value.getBackdrop_path());
                            final Bitmap bmp = getInternetPicture(urlpath);
                            emitter.onNext(bmp);
                            emitter.onComplete();
                        }
                    }).observeOn(AndroidSchedulers.mainThread())//回调在主线程
                            .subscribeOn(Schedulers.io())//执行在io线程
                            .subscribe(new io.reactivex.Observer<Bitmap>() {
                                @Override
                                public void onSubscribe(Disposable d) {

                                }

                                @Override
                                public void onNext(Bitmap bm) {
                                    viewHolder.Cover.setImageBitmap(bm);
                                    //viewHolder.bmp=bm;
                                }

                                @Override
                                public void onError(Throwable e) {

                                }

                                @Override
                                public void onComplete() {

                                }
                            });
                }

                @Override
                public void onError(Throwable e) {
                }

                @Override
                public void onComplete() {

                }
            };

            obs.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(dis);

        }
        if (onItemClickListener != null){
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onClick(viewHolder.getAdapterPosition());
                }
            });
            viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    onItemClickListener.onLongClick(viewHolder.getAdapterPosition());
                    return false;
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return collections.size();
    }

    public Bitmap getInternetPicture(String UrlPath) {
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
                File file = new File(context.getCacheDir(), getFileName(urlpath));
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

    public CollectionObj getCollection(int position){
        return collections.get(position);
    }
}
