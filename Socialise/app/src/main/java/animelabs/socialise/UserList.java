package animelabs.socialise;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import animelabs.socialise.Utils.Const;
import animelabs.socialise.CustomActivitty.CustomActivity;

/**
 * Created by Asheesh on 9/29/2015.
 */
public class UserList extends CustomActivity {
    private ArrayList<ParseUser> userArrayList;
    public static ParseUser user;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_list);
        listView=(ListView)findViewById(R.id.list);
        updateUserStatus(true);
    }

    private void updateUserStatus(boolean b) {
        user.put("online",b);
        user.saveEventually();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        updateUserStatus(false);
    }


    @Override
    protected void onResume() {
        super.onResume();
       loadUserList();
    }

    private void loadUserList() {
        final ProgressDialog progressDialog=ProgressDialog.show(this,null,"Loading...");
        ParseUser.getQuery().whereNotEqualTo("username",user.getUsername())
                .findInBackground(new FindCallback<ParseUser>() {
                    @Override
                    public void done(List<ParseUser> list, ParseException e) {
                        progressDialog.dismiss();
                       if(list!=null)
                       {
                           if(list.size()==0)
                           {
                               Toast.makeText(getApplicationContext(),"No Users To Chat With",Toast.LENGTH_SHORT).show();
                           }
                           userArrayList=new ArrayList<ParseUser>(list);
                           listView.setAdapter(new CustomAdapter());
                           listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                               @Override
                               public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                   startActivity(new Intent(UserList.this,Chat.class).putExtra(Const.EXTRA_DATA, userArrayList.get(position).getUsername()));
                               }
                           });
                       }
                    }
                });
    }

    public class CustomAdapter extends BaseAdapter
    {

        @Override
        public int getCount() {
            return userArrayList.size();
        }

        @Override
        public ParseUser getItem(int position) {
            return userArrayList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView==null)
                convertView= getLayoutInflater().inflate(R.layout.chat_item,null);
            ParseUser userParse=getItem(position);
            TextView usernamtext=(TextView)convertView.findViewById(R.id.usertext);
            usernamtext.setText(userParse.getUsername());
            usernamtext.setCompoundDrawablesWithIntrinsicBounds(userParse.getBoolean("online") ? R.drawable.ic_online
                    : R.drawable.ic_offline, 0, R.drawable.arrow, 0);
            return convertView;
        }
    }
}
