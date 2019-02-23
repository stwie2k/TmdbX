package nyamori.moe.tmdbx.obj;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import nyamori.moe.tmdbx.db.UserBaseHelper;
import nyamori.moe.tmdbx.db.UserCursorWrapper;
import nyamori.moe.tmdbx.db.UserDbSchema.UserTable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class UserLab {
    private static UserLab sUserLab;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static UserLab get(Context context) {
        if (sUserLab == null) {
            sUserLab = new UserLab(context);
        }
        return sUserLab;
    }

    private UserLab(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new UserBaseHelper(mContext)
                .getWritableDatabase();
    }

    public List<User> getUsers() {
        List<User> Users = new ArrayList<>();

        UserCursorWrapper cursor = queryUsers(null, null);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Users.add(cursor.getUser());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }


        //now it returns a snapshot of the whole database
        //rather than a reference of mUsers before
        return Users;
    }

    public User getUser(UUID uuid) {
        UserCursorWrapper cursor = queryUsers(
                UserTable.Cols.UUID + " = ?",
                new String[]{uuid.toString()}
        );

        try {
            if (cursor.getCount() == 0) {
                return null;
            }
            cursor.moveToFirst();
            return cursor.getUser();
        } finally {
            cursor.close();
        }

    }

    public User getUserByName(String name){
        UserCursorWrapper cursor = queryUsers(
                UserTable.Cols.USERNAME + " = ?",
                new String[]{name}
        );

        try {
            if (cursor.getCount() == 0) {
                return null;
            }
            cursor.moveToFirst();
            return cursor.getUser();
        } finally {
            cursor.close();
        }

    }

    public void addUser(User c) {
        ContentValues values = getContentValues(c);
        mDatabase.insert(UserTable.NAME, null, values);
    }

    public void updateUser(User User) {
        String name = User.getUsername();
        ContentValues values = getContentValues(User);

        mDatabase.update(UserTable.NAME, values,
                UserTable.Cols.UUID + " = ?",
                new String[]{User.getUuid().toString()});
    }


    private UserCursorWrapper queryUsers(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                UserTable.NAME,
                null, // Select al the columns
                whereClause,
                whereArgs,
                null, //groupBy
                null, //having
                null //orderBy
        );

        return new UserCursorWrapper(cursor);

    }

    private static ContentValues getContentValues(User User) {
        ContentValues values = new ContentValues();
        values.put(UserTable.Cols.UUID, User.getUuid().toString());
        values.put(UserTable.Cols.USERNAME, User.getUsername());
        values.put(UserTable.Cols.PASSWORDMD5, User.getPasswordMD5());


        return values;
    }

    public void deleteUser(User User) {
        mDatabase.delete(UserTable.NAME,
                UserTable.Cols.UUID + " = ? ",
                new String[]{User.getUuid().toString()});
        ;
    }
}