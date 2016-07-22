package ge.edu.freeuni.android.entertrainment.chat.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
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
        this.chatEntryList = aggregateSameNames(chatEntryList);
    }

    private List<ChatEntry> aggregateSameNames(List<ChatEntry> chatEntryList) {
        List<ChatEntry> res = new ArrayList<>();
        for (int i=0; i<chatEntryList.size(); i++) {
            ChatEntry curEntry = chatEntryList.get(i);
            if (res.isEmpty())
                res.add(curEntry);
            else {
                ChatEntry lastEntry = res.get(res.size() - 1);
                if (lastEntry.getUsername().equals(curEntry.getUsername())) {
                    lastEntry.setText(lastEntry.getText() + "\n" + curEntry.getText());
                    lastEntry.setTimestamp(curEntry.getTimestamp());
                } else {
                    res.add(curEntry);
                }
            }
        }
        return res;
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
        if (chatEntryList.isEmpty()) {
            chatEntryList.add(chatEntry);
        } else {
            ChatEntry lastEntry = chatEntryList.get(chatEntryList.size() - 1);
            lastEntry.setText(lastEntry.getText() + "\n" + chatEntry.getText());
            lastEntry.setTimestamp(chatEntry.getTimestamp());
        }
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
