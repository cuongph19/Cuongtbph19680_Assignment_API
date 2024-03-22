package com.example.cuongtbph19680_assignment_api;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Screen_SignUp extends AppCompatActivity {
    FirebaseAuth auth;
    ImageView home;
    EditText txtn, txtp;
    Button btnsign_up;
    Context context = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_sign_up);
        txtn = findViewById(R.id.txtName);
        txtp = findViewById(R.id.txtPass);
        auth = FirebaseAuth.getInstance();

        btnsign_up = findViewById(R.id.btnsign_up);

        home = findViewById(R.id.home);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Screen_SignUp.this, Screen_login.class);
                startActivity(intent);
            }
        });
        btnsign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = txtn.getText().toString();
                String password = txtp.getText().toString();
                // Kiểm tra xem email và password có hợp lệ không
                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Email hoặc mật khẩu không được để trống", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Đăng ký tài khoản với email và password
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener((Activity) context,
                        new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Lấy thông tin người dùng mới tạo
                                    FirebaseUser user = auth.getCurrentUser();
                                    // Thông báo đăng ký thành công
                                    Toast.makeText(getApplicationContext(), "Đăng ký thành công", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(Screen_SignUp.this, Screen_login.class);
                                    startActivity(intent);
                                } else {
                                    // Xảy ra lỗi, hiển thị thông báo lỗi
                                    Toast.makeText(getApplicationContext(), "Đăng ký thất bại: " + task.getException().toString(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

    }
}