package nyamori.moe.tmdbx.collection;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import nyamori.moe.tmdbx.obj.CurrentUser;
import nyamori.moe.tmdbx.DetailActivity;
import nyamori.moe.tmdbx.R;
import nyamori.moe.tmdbx.obj.TVDetailActivity;
import nyamori.moe.tmdbx.adapter.CollectionAdapter;

public class CollectionList extends AppCompatActivity {

    RecyclerView rv;
    CollectionLab collectionLab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection_list);
        rv = (RecyclerView) findViewById(R.id.collectioRecyclerview);

        collectionLab = CollectionLab.get(getApplicationContext());

        final CollectionAdapter collectionAdapter = new CollectionAdapter(getApplicationContext(),collectionLab.getCollectionsOfUser(CurrentUser.getUuid()));
        collectionAdapter.setOnItemClickListener(new CollectionAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                CollectionObj collectionObj = collectionAdapter.getCollection(position);
                if(collectionObj.getCollectionType().equals("movie")){
                    Intent intent = new Intent(CollectionList.this, DetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("id", Long.valueOf(collectionObj.getMovieId()));
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                else{
                    Intent intent = new Intent(CollectionList.this, TVDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("id", Long.valueOf(collectionObj.getMovieId()));
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }

            @Override
            public void onLongClick(final int position) {
                final CollectionObj collectionObj = collectionAdapter.getCollection(position);
                AlertDialog.Builder dialog = new AlertDialog.Builder(CollectionList.this);

                dialog.setPositiveButton("YES",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        collectionLab.deleteCollection(collectionObj);
                        collectionAdapter.removeCollection(position);
                        collectionAdapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setMessage("Delete or not?");
            }
        });
        rv.setAdapter(collectionAdapter);
    LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(layoutManager);

}
}
