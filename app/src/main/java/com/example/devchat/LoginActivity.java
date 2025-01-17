package com.example.devchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private ProgressDialog loadingbar;

    private Button LoginButton;
    private EditText UserEmail, UserPassword;
    private TextView CreateAccountLink, ForgotPasswordLink;

    private DatabaseReference UsersRef;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        UsersRef= FirebaseDatabase.getInstance().getReference().child("Users");

        InitializeFields();

        CreateAccountLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendUserToRegisterActivity();

            }
        });

        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AllowUserToLogin();
            }
        });

    }

    private void AllowUserToLogin() {
        String email = UserEmail.getText().toString();
        String password = UserPassword.getText().toString();

        if(TextUtils.isEmpty(email))
        {
            Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
        }
        else
        {
            loadingbar.setTitle("Signing In....");
            loadingbar.setMessage("Please wait.....");
            loadingbar.setCanceledOnTouchOutside(true);
            loadingbar.show();

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(task.isSuccessful())
                            {
                                String currentUserID = mAuth.getCurrentUser().getUid();
                                String deviceToken = FirebaseInstanceId.getInstance().getToken();

                                UsersRef.child(currentUserID).child("device_token")
                                        .setValue(deviceToken)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful())
                                                {
                                                    SendUserToMainActivity();
                                                    Toast.makeText(LoginActivity.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                                                    loadingbar.dismiss();
                                                }
                                            }
                                        });


                            }
                            else{
                                String message = task.getException().toString();
                                Toast.makeText(LoginActivity.this, "Error :"+ message, Toast.LENGTH_SHORT).show();
                                loadingbar.dismiss();
                            }
                        }
                    });
        }
    }

    private void InitializeFields() {

        LoginButton = (Button) findViewById(R.id.login_button);
        UserEmail = (EditText) findViewById(R.id.login_email);
        UserPassword = (EditText) findViewById(R.id.login_password);
        CreateAccountLink = (TextView) findViewById(R.id.create_account_link);
        ForgotPasswordLink = (TextView) findViewById(R.id.forgot_password_link);

        loadingbar = new ProgressDialog(this);
    }



    private void SendUserToMainActivity() {
        Intent mainIntent = new Intent (LoginActivity.this, MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();

    }
    private void SendUserToRegisterActivity() {
        Intent registerIntent = new Intent (LoginActivity.this, RegisterActivity.class);
        startActivity(registerIntent);
    }
}
