package ge.edu.freeuni.android.entertrainment.chat.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ge.edu.freeuni.android.entertrainment.R;
import ge.edu.freeuni.android.entertrainment.chat.model.ChatEntry;
import ge.edu.freeuni.android.entertrainment.chat.model.ChatUpdateListener;

/**
 * Created by Nika Doghonadze.
 */
public class GroupChatAdapter extends RecyclerView.Adapter<GroupChatAdapter.ViewHolder> implements ChatUpdateListener {
    private Context context;
    private List<ChatEntry> chatEntryList;

    public GroupChatAdapter(Context context, List<ChatEntry> chatEntryList) {
        this.context = context;
        this.chatEntryList = chatEntryList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.group_chat_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ChatEntry chatEntry = chatEntryList.get(position);
        holder.username.setText(chatEntry.getUsername());
        holder.userText.setText(chatEntry.getText());
        holder.date.setText(chatEntry.getTimeText());
    }

    @Override
    public int getItemCount() {
        return chatEntryList.size();
    }

    @Override
    public void messageReceived(ChatEntry chatEntry) {
        chatEntryList.add(chatEntry);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView username, userText, date;

        public ViewHolder(View itemView) {
            super(itemView);
            username = (TextView) itemView.findViewById(R.id.group_chat_item_username);
            userText = (TextView) itemView.findViewById(R.id.group_chat_item_text);
            date = (TextView) itemView.findViewById(R.id.group_chat_item_date);
        }
    }
}
