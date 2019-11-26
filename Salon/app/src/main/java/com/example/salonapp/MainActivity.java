package com.example.salonapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.salonapp.Common.Common;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {/*

    Context context;

    private static int APP_REQUEST_CODE = 7117;


    @BindView(R.id.register)
    Button register;




    @BindView(R.id.txt_skip)
    TextView txt_skip;


    @OnClick(R.id.register)
    void loginUser()
    {
        final Intent intent = new Intent(this, AccountKitActivity.class);
        AccountKitConfiguration.AccountKitConfigurationBuilder configurationBuilder =
                new  AccountKitConfiguration.AccountKitConfigurationBuilder(LoginType.PHONE,
                        AccountKitActivity.ResponseType.TOKEN);
        intent.putExtra(AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION,configurationBuilder.build());

        startActivityForResult(intent,APP_REQUEST_CODE);
    }

    @OnClick(R.id.txt_skip)
    void skipLoginJustGoHome(){
        Intent intent = new Intent(this,GridsLayoutScreen.class);
        intent.putExtra(Common.IS_LOGIN,false);
        startActivity(intent);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == APP_REQUEST_CODE){
            assert data != null;
            AccountKitLoginResult loginResult = data.getParcelableExtra(AccountKitLoginResult.RESULT_KEY);
            if(loginResult != null && loginResult.getError()!=null){
                Toast.makeText(this,""+loginResult.getError().getErrorType().getMessage(),Toast.LENGTH_SHORT).show();
            }else if (loginResult != null && loginResult.wasCancelled()){
                Toast.makeText(this,"Login Cancelled",Toast.LENGTH_SHORT).show();
            }else
            {
                Intent intent = new Intent(this,GridsLayoutScreen.class);
                intent.putExtra(Common.IS_LOGIN,false);
                startActivity(intent);
                finish();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;

        AccessToken accessToken = AccountKit.getCurrentAccessToken();
        if(accessToken!=null)
        {
            Intent intent = new Intent(this,GridsLayoutScreen.class);
            intent.putExtra(Common.IS_LOGIN,false);
            startActivity(intent);
            finish();
        }else
        {
            setContentView(R.layout.activity_main);
            ButterKnife.bind(MainActivity.this);
        }
    }

*/
}

