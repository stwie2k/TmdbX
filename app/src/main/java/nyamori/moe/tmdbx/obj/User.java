package nyamori.moe.tmdbx.obj;

import java.util.UUID;

public class User {
    private String mUsername;
    private UUID mUuid;
    private String mPasswordMD5;

    public User(String username, String passwordMD5) {
        mUuid = UUID.randomUUID();
        mUsername = username;
        mPasswordMD5 = passwordMD5;
    }

    public User(UUID uuid, String username, String passwordMD5) {
        mUuid= uuid;
        mUsername = username;
        mPasswordMD5 = passwordMD5;
    }

    public String getUsername() {
        return mUsername;
    }

    public void setUsername(String username) {
        mUsername = username;
    }

    public UUID getUuid() {
        return mUuid;
    }

    public void setUuid(UUID UUID) {
        mUuid= UUID;
    }

    public String getPasswordMD5() {
        return mPasswordMD5;
    }

    public void setPasswordMD5(String passwordMD5) {
        mPasswordMD5 = passwordMD5;
    }


}
