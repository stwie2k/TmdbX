package nyamori.moe.tmdbx.collectionDb;

import android.database.Cursor;
import android.database.CursorWrapper;

import java.util.UUID;

import nyamori.moe.tmdbx.collection.CollectionObj;
import nyamori.moe.tmdbx.collectionDb.CollectionDbSchema.CollectionTable;


public class CollectionCursorWrapper extends CursorWrapper {
    public CollectionCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public CollectionObj getCollection() {
        String uuidString = getString(getColumnIndex(CollectionTable.Cols.UUID));
        String movieId = getString(getColumnIndex(CollectionTable.Cols.MOVIEID));
        String collectionType = getString(getColumnIndex(CollectionTable.Cols.COLLECTIONTYPE));

//        User hero = new User(UUID.fromString(uuidString));
        CollectionObj collection = new CollectionObj(movieId, UUID.fromString(uuidString),collectionType);

        return collection;
    }
}
