package it.polito.mad.sharenbook.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import it.polito.mad.sharenbook.ChatActivity;
import it.polito.mad.sharenbook.R;
import it.polito.mad.sharenbook.model.Conversation;
import it.polito.mad.sharenbook.utils.UserInterface;

public class ConversationAdapter extends BaseAdapter {

    private List<Conversation> conversations = new ArrayList<>();
    private boolean modification = false;
    private long withoutIncomingMessagesCounter = 0;
    private Context context;


    public long getWithoutIncomingMessagesCounter() {
        return withoutIncomingMessagesCounter;
    }

    public void setWithoutIncomingMessagesCounter(long withoutIncomingMessagesCounter) {
        this.withoutIncomingMessagesCounter = withoutIncomingMessagesCounter;
    }

    public ConversationAdapter(Context context){
        this.context = context;
    }

    public void addConversation(Conversation conversation) {

        if(this.conversations.size()==(int)this.withoutIncomingMessagesCounter){
            this.conversations.add(0,conversation);
            Log.d("Conversation after:","size:"+conversations.size()+"chats"+withoutIncomingMessagesCounter);
            notifyDataSetChanged();
        }
        else {
            this.conversations.add(conversation);
            sortAfterInsertNewElement();
            Log.d("Conversation during:","size:"+conversations.size()+"chats"+withoutIncomingMessagesCounter);
            if (this.conversations.size() == this.withoutIncomingMessagesCounter)
                notifyDataSetChanged();
        }

    }

    private void sortAfterInsertNewElement() {
        // The last object is the only one that is potentially not in the right
        // position

        Conversation swap;
            for (int pos = conversations.size() - 1; pos > 0
                    && conversations.get(pos).compareTo(conversations.get(pos - 1)) > 0; pos--) {
                Collections.swap(conversations,pos,pos-1);
            }

    }

    public void modifyConversation(Conversation conversation){

        Conversation upFront = null;
        int index = 0;
        for(Conversation conv : conversations){
            if(conv.getConversationCounterpart().equals(conversation.getConversationCounterpart())) {
                conv.setMessageReceived(conversation.getMessageReceived());
                conv.setNewInboxMessageCounter(conv.getNewInboxMessageCounter() + 1);
                conv.setProfilePicRef(conversation.getProfilePicRef());
                if(conversation.getMessageReceived().getUsername().equals(conversation.getConversationCounterpart())) {
                    modification = true;
                    index = conversations.indexOf(conv);
                    Log.d("conversation","mod at pos ->"+index);
                    upFront = new Conversation(conversation);
                    }
                break;
            }

        }
        if(upFront!=null && index!=0){
            conversations.remove(index);
            addConversation(upFront);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return conversations.size();
    }

    @Override
    public Object getItem(int position) {
        return conversations.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {




        ConversationViewHolder holder = new ConversationViewHolder();
        Conversation conversation = conversations.get(position);
        if(convertView == null) {
            LayoutInflater conversationInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = conversationInflater.inflate(R.layout.my_chats_item_layout,parent,false);
        }
        holder.username = convertView.findViewById(R.id.mychats_username);
        holder.lastMessageBody = convertView.findViewById(R.id.mychats_message_body);
        holder.date = convertView.findViewById(R.id.mychats_date);
        holder.inboxCounter = convertView.findViewById(R.id.mychats_inboxCounter);
        holder.avatar = convertView.findViewById(R.id.mychats_avatar);
        holder.conversation = convertView.findViewById(R.id.conversation);

        UserInterface.showGlideImage(context, conversation.getProfilePicRef(), holder.avatar, 0);
        holder.username.setText(conversation.getConversationCounterpart());
        holder.lastMessageBody.setText(conversation.getMessageReceived().getMessage());
        holder.date.setText(conversation.getMessageReceived().getTimeStampAsString(conversation.getMessageReceived().getTimestamp()));
        if(conversation.getNewInboxMessageCounter() == -1)
            holder.inboxCounter.setVisibility(View.GONE);
        //else
          //  holder.inboxCounter.setText(conversation.getNewInboxMessageCounter());

        convertView.setTag(holder);


        holder.conversation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent chatActivity = new Intent(context, ChatActivity.class);
                SharedPreferences userPreferences =context.getSharedPreferences(context.getString(R.string.username_preferences), Context.MODE_PRIVATE);
                chatActivity.putExtra("recipientUsername",conversation.getConversationCounterpart() );
                chatActivity.putExtra("recipientUID", userPreferences.getString(conversation.getConversationCounterpart(),"void"));
                context.startActivity(chatActivity);
                conversations.get(position).setNewInboxMessageCounter(-1);
                notifyDataSetChanged();

            }
        });
/*
        if(modification) {
            Log.d("conversation", "true");
            modification = false;
            convertView.bringToFront();
        }
        else
            Log.d("conversation","false");
*/
        return convertView;
    }

    public class ConversationViewHolder{

        CircularImageView avatar;
        TextView username;
        TextView lastMessageBody;
        TextView date;
        TextView inboxCounter;
        RelativeLayout conversation;
    }
}
