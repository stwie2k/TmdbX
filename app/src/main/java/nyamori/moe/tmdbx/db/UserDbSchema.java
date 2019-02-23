package nyamori.moe.tmdbx.db;

public class UserDbSchema {

    public static final class UserTable {
        public static final String NAME = "users";

        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String USERNAME = "username";
            public static final String PASSWORDMD5 = "passwordmd5";
        }
    }

}
