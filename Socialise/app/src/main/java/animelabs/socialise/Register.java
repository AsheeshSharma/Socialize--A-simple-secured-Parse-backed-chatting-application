package animelabs.socialise;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import animelabs.socialise.Utils.Utils;
import animelabs.socialise.CustomActivitty.CustomActivity;

/**
 * Created by Asheesh on 9/29/2015.
 */
public class Register extends CustomActivity {
    EditText username,email,password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        setTouchNClick(R.id.btnReg);
        username=(EditText)findViewById(R.id.user);
        password=(EditText)findViewById(R.id.pwd);
        email=(EditText)findViewById(R.id.email);
    }
    @Override
    public void onClick(View v) {
        super.onClick(v);
        if(v.getId()==R.id.btnReg)
        {
            String uname=username.getText().toString();
            String pword=password.getText().toString();
            String eil=email.getText().toString();
            if(uname==null||pword==null||eil==null)
            {
                Utils.showDialog(this,"Please fill in empty details");
                return;
            }
            final ProgressDialog progressDialog=ProgressDialog.show(this,null,"Registering");
            final ParseUser parseUser=new ParseUser();
            parseUser.setEmail(eil);
            parseUser.setPassword(pword);
            parseUser.setUsername(uname);
            parseUser.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                    progressDialog.dismiss();
                    if(e==null)
                    {
                        UserList.user=parseUser;
                        ParseInstallation parseInstallation=new ParseInstallation();
                        parseInstallation.put("username",parseUser.getUsername());
                        parseInstallation.saveInBackground();
                        startActivity(new Intent(Register.this,UserList.class));
                        setResult(RESULT_OK);
                        finish();
                    }
                    else
                    {
                        Utils.showDialog(
                                Register.this,
                                "Error while Registering" + " "
                                        + e.getMessage());
                        e.printStackTrace();
                    }
                }
            });
        }
    }

}
