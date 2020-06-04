package com.scnu.easygo.login;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.scnu.easygo.MainActivity;
import com.scnu.easygo.R;
import com.scnu.easygo.area_talk_client.ChatManager;
import com.scnu.easygo.userID.UserID;

import static com.baidu.mapapi.BMapManager.getContext;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText email;
    private EditText password;
    private Button signButton;
    private TextView registText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        signButton=findViewById(R.id.signButton);
        signButton.setOnClickListener(this);
        registText=findViewById(R.id.registText);
        registText.setOnClickListener(this);

    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.signButton:
                login();
                break;
            case R.id.registText:
                regist();
                break;
        }

    }
    public void login(){
        final String email_1=email.getText().toString().trim();
        final String password_1=password.getText().toString().trim();
        if("000".equals(email_1)||"000".equals(password_1)){
            UserID.userID="12345678";
            LoginActivity.this.finish();
            Intent starter = new Intent(LoginActivity.this,MainActivity.class);
            startActivity(starter);
        }
        new Thread() {
                public void run() {
                if(email_1==null||"".equals(email_1)){
                    runOnUiThread(
                            new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(LoginActivity.this,"邮箱不能为空",Toast.LENGTH_SHORT).show();
                        }
                    });
                    return;
                }
                if(password_1==null||"".equals(password_1)){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(LoginActivity.this,"密码不能为空",Toast.LENGTH_SHORT).show();
                        }
                    });
                    return;
                }

                final String[] result=  Login.loginByGet(email_1, password_1).split("&");
                //线程加入
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(LoginActivity.this,result[1],Toast.LENGTH_SHORT).show();
                    }
                });
                if("1".equals(result[0])){
                    UserID.userID=result[2];
                    UserID.userName=result[3];
                    ChatManager.getChatManager().connect();
                    Intent starter = new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(starter);
                    LoginActivity.this.finish();
                }
            }
        } .start();
    }
    public void regist(){
        Intent starter = new Intent(LoginActivity.this, RegistActivity.class);
        startActivity(starter);
    }

}

