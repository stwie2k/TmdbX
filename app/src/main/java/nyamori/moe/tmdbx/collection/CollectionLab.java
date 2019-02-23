package nyamori.moe.tmdbx.collection;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import nyamori.moe.tmdbx.collectionDb.CollectionBaseHelper;
import nyamori.moe.tmdbx.collectionDb.CollectionCursorWrapper;
import nyamori.moe.tmdbx.collectionDb.CollectionDbSchema.CollectionTable;


public class CollectionLab {
    private static CollectionLab sCollectionLab;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static CollectionLab get(Context context) {
        if (sCollectionLab == null) {
            sCollectionLab = new CollectionLab(context);
        }
        return sCollectionLab;
    }

    private CollectionLab(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new CollectionBaseHelper(mContext)
                .getWritableDatabase();
    }

    public List<CollectionObj> getCollections() {
        List<CollectionObj> Collections = new ArrayList<>();

        CollectionCursorWrapper cursor = queryCollections(null, null);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Collections.add(cursor.getCollection());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }


        //now it returns a snapshot of the whole database
        //rather than a reference of mCollections before
        return Collections;
    }

    public List<CollectionObj> getCollectionsOfUser(UUID uuid) {
        List<CollectionObj> Collections = new ArrayList<>();

        CollectionCursorWrapper cursor = queryCollections(
                CollectionTable.Cols.UUID + " = ?",
                new String[]{uuid.toString()}
        );

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Collections.add(cursor.getCollection());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return Collections;
    }

    public CollectionObj getCollection(String movieId) {
        CollectionCursorWrapper cursor = queryCollections(
                CollectionTable.Cols.MOVIEID + " = ?",
                new String[]{movieId}
        );

        try {
            if (cursor.getCount() == 0) {
                return null;
            }
            cursor.moveToFirst();
            return cursor.getCollection();
        } finally {
            cursor.close();
        }

    }

    public void addCollection(CollectionObj c) {
        ContentValues values = getContentValues(c);
        mDatabase.insert(CollectionTable.NAME, null, values);
    }

    public void updateCollection(CollectionObj Collection) {
        String movieId = Collection.getMovieId();
        ContentValues values = getContentValues(Collection);

        mDatabase.update(CollectionTable.NAME, values,
                CollectionTable.Cols.MOVIEID + " = ?",
                new String[]{Collection.getMovieId().toString()});
    }


    private CollectionCursorWrapper queryCollections(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                CollectionTable.NAME,
                null, // Select al the columns
                whereClause,
                whereArgs,
                null, //groupBy
                null, //having
                null //orderBy
        );

        return new CollectionCursorWrapper(cursor);

    }

    private static ContentValues getContentValues(CollectionObj Collection) {
        ContentValues values = new ContentValues();
        values.put(CollectionTable.Cols.MOVIEID, Collection.getMovieId());
        values.put(CollectionTable.Cols.UUID, Collection.getCollectingUserId().toString());
        values.put(CollectionTable.Cols.COLLECTIONTYPE, Collection.getCollectionType());

        return values;
    }

    public void deleteCollection(CollectionObj Collection) {
        mDatabase.delete(CollectionTable.NAME,
                CollectionTable.Cols.MOVIEID + " = ? and "+ CollectionTable.Cols.COLLECTIONTYPE+ " = ? and " + CollectionTable.Cols.UUID+" = ? ",
                new String[]{Collection.getMovieId(),Collection.getCollectionType(),Collection.getCollectingUserId().toString()});
        ;
    }
}