package nyamori.moe.tmdbx.collectionDb;

public class CollectionDbSchema {

    public static final class CollectionTable {
        public static final String NAME = "collections";

        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String MOVIEID = "movieid";
            public static final String COLLECTIONTYPE = "collectiontype";
        }
    }

}
