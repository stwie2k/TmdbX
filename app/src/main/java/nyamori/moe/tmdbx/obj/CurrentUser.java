package nyamori.moe.tmdbx.obj;

import java.util.UUID;

public class CurrentUser{
    private static String user ="";
    private static UUID uuid;

    public static UUID getUuid() {
        return uuid;
    }

    public static void setUuid(UUID uuid) {
        CurrentUser.uuid = uuid;
    }

    public static String getUser() {
        return user;
    }

    public static void setUser(String user) {
        CurrentUser.user = user;
    }
}
