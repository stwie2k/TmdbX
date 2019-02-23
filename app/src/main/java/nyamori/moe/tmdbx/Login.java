package nyamori.moe.tmdbx;

import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import nyamori.moe.tmdbx.obj.CurrentUser;
import nyamori.moe.tmdbx.obj.User;
import nyamori.moe.tmdbx.obj.UserLab;

public class Login extends AppCompatActivity {

    UserLab mUserLab;

    Button login;
    TextView loginName;
    TextView loginPassword;
    TextView toRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mUserLab = UserLab.get(getApplicationContext());

        login = (Button)findViewById(R.id.loginButton);
        loginName = (TextView)findViewById(R.id.loginName);
        loginPassword = (TextView)findViewById(R.id.loginPassword);
        toRegister = (TextView)findViewById(R.id.toRegisterButton);

                toRegister.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        android.content.Intent intent = new Intent(Login.this,Register.class);
                        startActivity(intent);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = loginName.getText().toString();
                if(userName == null || userName.equals("")){
                    Toast.makeText(getApplicationContext(),"No username input!",Toast.LENGTH_SHORT).show();
                    return;
                }

                String pw = loginPassword.getText().toString();
                if(pw == null || pw.equals("")){
                    Toast.makeText(getApplicationContext(),"No password input!",Toast.LENGTH_SHORT).show();
                    return;
                }

                User temp = mUserLab.getUserByName(userName);
                if(temp == null){
                    Toast.makeText(getApplicationContext(),"No such user with username:"+userName,Toast.LENGTH_SHORT).show();
                    return;
                } else{
                    String pwMd5 = md5(pw);
                    if(pwMd5.equals(temp.getPasswordMD5())) {
                        //TODO: 跳转到登陆界面
                        CurrentUser.setUser(userName);
                        CurrentUser.setUuid(temp.getUuid());
                        android.content.Intent intent = new Intent(Login.this,MainActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(),"User password incorrect."+userName,Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            }
        });

    }

    public String md5(String string) {
        if (TextUtils.isEmpty(string)) {
            return "";
        }
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest(string.getBytes());
            String result = "";
            for (byte b : bytes) {
                String temp = Integer.toHexString(b & 0xff);
                if (temp.length() == 1) {
                    temp = "0" + temp;
                }
                result += temp;
            }
            return result;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
