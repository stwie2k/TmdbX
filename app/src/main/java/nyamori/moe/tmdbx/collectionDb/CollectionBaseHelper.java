package nyamori.moe.tmdbx.collectionDb;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import nyamori.moe.tmdbx.collectionDb.CollectionDbSchema.CollectionTable;

public class CollectionBaseHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "collectionBase.db";

    public CollectionBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table " + CollectionTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                CollectionTable.Cols.UUID + ", " +
                CollectionTable.Cols.MOVIEID + ", " +
                CollectionTable.Cols.COLLECTIONTYPE +
                ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
