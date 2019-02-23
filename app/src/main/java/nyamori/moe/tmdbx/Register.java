package nyamori.moe.tmdbx;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import nyamori.moe.tmdbx.obj.User;
import nyamori.moe.tmdbx.obj.UserLab;

public class Register extends AppCompatActivity {

    Button register;
    TextView registerName;
    TextView registerPassword;
    TextView confirmPassword;
    TextView toLogin;

    UserLab mUserLab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mUserLab = UserLab.get(getApplicationContext());

        register = (Button)findViewById(R.id.registerButton);
        registerName = (TextView)findViewById(R.id.registerName);
        registerPassword = (TextView)findViewById(R.id.registerPassword);
        confirmPassword = (TextView)findViewById(R.id.confirmPassword);
        toLogin = (TextView) findViewById(R.id.toLoginText);

        toLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Register.this,Login.class);
                startActivity(intent);
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = registerName.getText().toString();
                if(userName == null || userName.equals("")){
                    Toast.makeText(getApplicationContext(),"No username input!",Toast.LENGTH_SHORT).show();
                    return;
                }

                String pw = registerPassword.getText().toString();
                if(pw == null || pw.equals("")){
                    Toast.makeText(getApplicationContext(),"No password input!",Toast.LENGTH_SHORT).show();
                    return;
                }

                String conpw = confirmPassword.getText().toString();
                if(conpw == null || conpw.equals("")){
                    Toast.makeText(getApplicationContext(),"No confirm password input!",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!conpw.equals(pw)){
                    Toast.makeText(getApplicationContext(),"Two password input does not match!",Toast.LENGTH_SHORT).show();
                    return;
                }

                User temp = mUserLab.getUserByName(userName);
                if(temp != null){
                    Toast.makeText(getApplicationContext(),userName+" already exits.",Toast.LENGTH_SHORT).show();
                    return;
                } else{
                    String pwMd5 = md5(pw);
                    User mUser = new User(userName,pwMd5);
                    mUserLab.addUser(mUser);
                    Toast.makeText(getApplicationContext(),userName+" sign up success.",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Register.this,Login.class);
                    startActivity(intent);
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
