package com.example.dbtest;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText username,email,usernumber,password;
    Button signupBtn,cancelBtn;
    dbHelper db;
    String name="",id="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        db=new dbHelper(this);
        try {
            Cursor result = db.checkLoginStatus();
            while (result.moveToNext()) {
                id = result.getString(0);
                name = result.getString(1);
            }
            if (result.getCount() > 0) {
                Toast.makeText(getApplicationContext(), "Welcome " + name, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(MainActivity.this, homeActivity.class);
                intent.putExtra("userID", id);
                intent.putExtra("userName", name);
                startActivity(intent);
                MainActivity.this.finish();
            }
        }
        catch(Exception e){
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        username=(EditText)findViewById(R.id.username);
        email=(EditText)findViewById(R.id.email);
        usernumber=(EditText)findViewById(R.id.usernumber);
        password=(EditText)findViewById(R.id.password);
        signupBtn=(Button) findViewById(R.id.signupBtn);
        cancelBtn=(Button) findViewById(R.id.cancelBtn);

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (username.getText().toString().isEmpty() || email.getText().toString().isEmpty() || usernumber.getText().toString().isEmpty()
                ||  password.getText().toString().isEmpty())
                {
                    LayoutInflater inflater=getLayoutInflater();
                    View layout=inflater.inflate(R.layout.customtoast,(ViewGroup)findViewById(R.id.toast_custom));

                    Toast toast = new Toast(getApplicationContext());
                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                    toast.setView(layout);//setting the view of custom toast layout
                    toast.show();
                }
                else {
                    String uname = username.getText().toString();
                    String uemail = email.getText().toString();
                    String unumber = usernumber.getText().toString();
                    String pass = password.getText().toString();
                    Cursor result = db.validateNumber(unumber);

                    if (result.getCount() > 0) {
                        Toast.makeText(getApplicationContext(), "User Already Signed up", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(MainActivity.this, loginActivity.class);
                        intent.putExtra("phnNumber", unumber);
                        startActivity(intent);
                    } else {
                        Boolean res = db.insertData(uname, uemail, unumber, pass);
                        if (res) {
                            Toast.makeText(getApplicationContext(), "Sign up is successful !!", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(MainActivity.this, loginActivity.class);
                            intent.putExtra("phnNumber", unumber);
                            startActivity(intent);

                        } else {
                            Toast.makeText(getApplicationContext(), "Sign up is unsuccessful !!", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
        });

       cancelBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               username.setText("");
               email.setText("");
               usernumber.setText("");
               password.setText("");
           }
       });
    }
}
