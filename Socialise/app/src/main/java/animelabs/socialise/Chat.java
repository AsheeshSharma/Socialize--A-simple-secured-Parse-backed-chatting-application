package animelabs.socialise;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.DateUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import animelabs.socialise.CustomActivitty.CustomActivity;
import animelabs.socialise.ConversationModel.Conversation;
import animelabs.socialise.Utils.Const;

/**
 * Created by Asheesh on 9/29/2015.
 */
public class Chat extends CustomActivity{
    ListView listView;
    EditText message;
    private ArrayList<Conversation> conversationArrayList;
    private String guest;
    private Date date;
    private Handler handler;
    private boolean isrunning;
    ConversationAdapter adapterconver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat);
        conversationArrayList=new ArrayList<Conversation>();
        adapterconver=new ConversationAdapter();
        listView=(ListView)findViewById(R.id.list);
        listView.setAdapter(adapterconver);
        listView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        listView.setStackFromBottom(true);
        message=(EditText)findViewById(R.id.txt);
        setTouchNClick(R.id.btnSend);
        guest = getIntent().getStringExtra(Const.EXTRA_DATA);
        getActionBar().setTitle(guest);
        handler = new android.os.Handler();

    }

    @Override
    protected void onResume() {
        super.onResume();
        isrunning=true;
        loadAllConversation();
    }
    @Override
    protected void onPause() {
        super.onPause();
        isrunning=false;
    }

    private void loadAllConversation() {
        ParseQuery<ParseObject> objectParseQuery=ParseQuery.getQuery("Chat");
        if(conversationArrayList.size()==0)
        {
            ArrayList<String> dataset=new ArrayList<String>();
            dataset.add(guest);
            dataset.add(UserList.user.getUsername());
            objectParseQuery.whereContainedIn("sender",dataset);
            objectParseQuery.whereContainedIn("receiver",dataset);
        }
        else
        {
            if (date != null)
            objectParseQuery.whereGreaterThan("createdAt", date);
            objectParseQuery.whereEqualTo("sender", guest);
            objectParseQuery.whereEqualTo("receiver", UserList.user.getUsername());
        }
        objectParseQuery.orderByDescending("createdAt");
        objectParseQuery.setLimit(20);
        objectParseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objectlist, ParseException e) {
                if (objectlist != null) {
                    if (objectlist.size() > 0) {
                        for (int i = objectlist.size() - 1; i >= 0; i--) {
                            ParseObject parseObject = objectlist.get(i);
                            Conversation conversation = new Conversation(parseObject.getString("message"), parseObject.getString("sender"), parseObject.getCreatedAt());
                            conversationArrayList.add(conversation);
                            if (date == null || date.before(conversation.getDate()))
                                date = conversation.getDate();
                            adapterconver.notifyDataSetChanged();
                        }

                    }
                }
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (isrunning)
                            loadAllConversation();
                    }
                }, 1000);
            }
        });
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if(v.getId()==R.id.btnSend)
        {
            sendMessage();
        }
    }

    private void sendMessage() {
        if (message.length() == 0)
            return;

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(message.getWindowToken(), 0);

        final String messagedata = message.getText().toString();
        final Conversation conversationNew = new Conversation(messagedata,UserList.user.getUsername(), new Date());
        conversationNew.setStatus(Conversation.STATUS_SENDING);
        conversationArrayList.add(conversationNew);
        adapterconver.notifyDataSetChanged();
        message.setText(null);

        ParseObject po = new ParseObject("Chat");
        po.put("sender", UserList.user.getUsername());
        po.put("receiver", guest);
        po.put("message", messagedata);

        po.saveEventually(new SaveCallback() {

            @Override
            public void done(ParseException e) {
                if (e == null)
                    conversationNew.setStatus(Conversation.STATUS_SENT);
                else
                    conversationNew.setStatus(Conversation.STATUS_FAILED);
                adapterconver.notifyDataSetChanged();
            }
        });
        ParsePush.subscribeInBackground(guest, new SaveCallback() {
            @Override
            public void done(ParseException e) {

                if (e == null) {
                    ParsePush push = new ParsePush();
                    push.setChannel(guest);
                    push.setMessage(messagedata);
                    push.sendInBackground();
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    private class ConversationAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return conversationArrayList.size();
        }

        @Override
        public Conversation getItem(int position) {
            return conversationArrayList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Conversation conversation=getItem(position);
            if(convertView==null)
            {
                if(conversation.isSent())
                    convertView=getLayoutInflater().inflate(R.layout.chat_item_sent,null);
                else
                    convertView=getLayoutInflater().inflate(R.layout.chat_item_rcv,null);
            }
            TextView data=(TextView)convertView.findViewById(R.id.lbl2);
            data.setText(conversation.getMsg());
            TextView datetextview=(TextView)convertView.findViewById(R.id.lbl1);
            datetextview.setText(DateUtils.getRelativeDateTimeString(Chat.this, conversation.getDate().getTime(), DateUtils.SECOND_IN_MILLIS,
                    DateUtils.DAY_IN_MILLIS, 0));
            TextView statustextview=(TextView)convertView.findViewById(R.id.lbl3);
            if(conversation.isSent())
            {
                if(conversation.getStatus()==Conversation.STATUS_SENT)
                    statustextview.setText("Delivered");
                else if(conversation.getStatus()==Conversation.STATUS_SENDING)
                    statustextview.setText("Sending");
                else if(conversation.getStatus()==Conversation.STATUS_FAILED)
                    statustextview.setText("Failed");

            }
            else
                statustextview.setText("");
            return convertView;
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getItemId() == android.R.id.home)
        {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
