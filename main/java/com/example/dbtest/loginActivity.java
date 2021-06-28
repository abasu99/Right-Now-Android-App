package com.example.dbtest;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class loginActivity extends MainActivity{

    EditText usernumber,password;
    Button loginBtn;
    dbHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        db= new dbHelper(this);
        usernumber = (EditText)findViewById(R.id.number);
        password = (EditText)findViewById(R.id.password);
        loginBtn = (Button)findViewById(R.id.loginBtn);
        String phnNumber = getIntent().getStringExtra("phnNumber").toString();
        usernumber.setText(phnNumber);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String unumber = usernumber.getText().toString();
                String pass = password.getText().toString();
                Cursor res = db.getData(unumber, pass);
                String name="",id="";
                while (res.moveToNext()) {
                    id =res.getString(0);
                    name= res.getString(1);
                }
                if(res.getCount()>0) {
                    //db.updateloginStatus(id,name);
                    Toast.makeText(getApplicationContext(), "Welcome "+name, Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(loginActivity.this,homeActivity.class);
                    intent.putExtra("userID",id);
                    startActivity(intent);
                    db.updateloginStatus(id, name);
                }
                else {
                    Toast.makeText(getApplicationContext(), "Invalid Credentials", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}




