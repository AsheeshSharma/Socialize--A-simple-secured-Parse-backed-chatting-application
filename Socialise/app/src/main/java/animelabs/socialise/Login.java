package animelabs.socialise;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseUser;

import animelabs.socialise.Utils.Utils;
import animelabs.socialise.CustomActivitty.CustomActivity;

/**
 * Created by Asheesh on 9/29/2015.
 */
public class Login extends CustomActivity {
    EditText username;
    EditText password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        setTouchNClick(R.id.btnLogin);
        setTouchNClick(R.id.btnReg);
        username=(EditText)findViewById(R.id.user);
        password=(EditText)findViewById(R.id.pwd);

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if(v.getId()==R.id.btnReg)
        {
            startActivityForResult(new Intent(this, Register.class), 10);
        }
        else
        {
            String uname=username.getText().toString();
            String pwd=password.getText().toString();
            if(uname==null||pwd==null)
            {
                Toast.makeText(this,"Please enter the required details",Toast.LENGTH_SHORT);
            }
            final ProgressDialog progressDialog=ProgressDialog.show(this, null, "Loading Server");
            ParseUser.logInInBackground(uname, pwd, new LogInCallback() {
                        @Override
                        public void done(ParseUser parseUser, ParseException e) {
                            if(parseUser!=null)
                            {   progressDialog.dismiss();
                                UserList.user=parseUser;
                                startActivity(new Intent(Login.this, UserList.class));
                            }
                            else
                            {   progressDialog.dismiss();
                                Utils.showDialog(Login.this,getString(R.string.err_login) + " "+ e.getMessage());
                                e.printStackTrace();
                            }
                        }
                    }
            );
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==10&&resultCode==RESULT_OK)
        {
            finish();
        }
    }
}
