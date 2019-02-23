package nyamori.moe.tmdbx.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import nyamori.moe.tmdbx.db.UserDbSchema.UserTable;

public class UserBaseHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "userBase.db";

    public UserBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table " + UserTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                UserTable.Cols.UUID + ", " +
                UserTable.Cols.USERNAME + ", " +
                UserTable.Cols.PASSWORDMD5 +
                ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
