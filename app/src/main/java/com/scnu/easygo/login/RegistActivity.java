package com.scnu.easygo.login;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.scnu.easygo.MainActivity;
import com.scnu.easygo.R;

public class RegistActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText username;
    private EditText password;
    private EditText email;
    private EditText verification;
    private Button registButton;
    private Button verificationButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);

        username=findViewById(R.id.username_1);
        password=findViewById(R.id.password_1);
        email=findViewById(R.id.email);
        verification=findViewById(R.id.verification_1);
        registButton=findViewById(R.id.registButton);
        registButton.setOnClickListener(this);
        verificationButton=findViewById(R.id.verificationButton);
        verificationButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.registButton:
                regist();
                break;

            case R.id.verificationButton:
                verification();
                break;
        }
    }

    public void regist(){
        final String username_1=username.getText().toString().trim();
        final String password_1=password.getText().toString().trim();
        final String email_1=email.getText().toString().trim();
        final String verification_1=verification.getText().toString().trim();

        new Thread() {
            public void run() {
                if(username_1==null||"".equals(username_1)){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(RegistActivity.this,"昵称不能为空",Toast.LENGTH_SHORT).show();
                        }
                    });
                    return;
                }
                if(password_1==null||"".equals(password_1)){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(RegistActivity.this,"密码不能为空",Toast.LENGTH_SHORT).show();
                        }
                    });
                    return;
                }
                if(email_1==null||"".equals(email_1)){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(RegistActivity.this,"邮箱不能为空",Toast.LENGTH_SHORT).show();
                        }
                    });
                    return;
                }
                if(verification_1==null||"".equals(email_1)){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(RegistActivity.this,"验证码不能为空",Toast.LENGTH_SHORT).show();
                        }
                    });
                    return;
                }

                final String result[]= Regist.registByPost(username_1,password_1,email_1,verification_1).split("&");
                if("1".equals(result[0])){
                    //线程加入
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(RegistActivity.this,result[1],Toast.LENGTH_SHORT).show();
                        }
                    });
                    Intent starter = new Intent(RegistActivity.this, LoginActivity.class);
                    startActivity(starter);
                }else{
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(RegistActivity.this,result[1],Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        } .start();

    }
    public void verification(){
        final  String email_1=email.getText().toString().trim();
        new Thread() {
            public void run() {
                //线程加入
                if(email_1==null||"".equals(email_1)){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(RegistActivity.this,"邮箱不能为空",Toast.LENGTH_SHORT).show();
                        }
                    });
                    return;
                }
                if (!email_1.matches("^\\w+@\\w+(\\.\\w+)+$")) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(RegistActivity.this,"邮箱格式不正确",Toast.LENGTH_SHORT).show();
                        }
                    });
                    return;
                }
                boolean send= Verification.verificationByGet(email_1);
                if(send){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(RegistActivity.this,"发送成功",Toast.LENGTH_SHORT).show();
                        }
                    });
                }else{
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(RegistActivity.this,"发送失败",Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        } .start();
    }
}