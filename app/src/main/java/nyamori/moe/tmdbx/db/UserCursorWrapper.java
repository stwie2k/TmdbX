package nyamori.moe.tmdbx.db;

import android.database.Cursor;
import android.database.CursorWrapper;

import nyamori.moe.tmdbx.obj.User;
import nyamori.moe.tmdbx.db.UserDbSchema.UserTable;

import java.util.UUID;

public class UserCursorWrapper extends CursorWrapper {
    public UserCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public User getUser() {
        String uuidString = getString(getColumnIndex(UserTable.Cols.UUID));
        String passwordmd5 = getString(getColumnIndex(UserTable.Cols.PASSWORDMD5));
        String username = getString(getColumnIndex(UserTable.Cols.USERNAME));

//        User hero = new User(UUID.fromString(uuidString));
        User user = new User(UUID.fromString(uuidString), username, passwordmd5);

        return user;
    }
}
