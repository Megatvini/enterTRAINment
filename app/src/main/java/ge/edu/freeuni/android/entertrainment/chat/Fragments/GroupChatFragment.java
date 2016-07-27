package ge.edu.freeuni.android.entertrainment.chat.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import ge.edu.freeuni.android.entertrainment.R;
import ge.edu.freeuni.android.entertrainment.chat.Constants;
import ge.edu.freeuni.android.entertrainment.chat.Utils;
import ge.edu.freeuni.android.entertrainment.chat.adapters.ChatAdapter;
import ge.edu.freeuni.android.entertrainment.chat.helper.VerticalSpaceItemDecoration;
import ge.edu.freeuni.android.entertrainment.chat.model.ChatEntry;
import ge.edu.freeuni.android.entertrainment.chat.model.ChatUpdateListener;
import ge.edu.freeuni.android.entertrainment.chat.model.GroupChatDataSource;
import ge.edu.freeuni.android.entertrainment.events.UsernameChangedEvent;

/**
 * A simple {@link Fragment} subclass.
 */
public class GroupChatFragment extends Fragment implements ChatUpdateListener{
    private ChatAdapter chatAdapter;
    private String username;
    private GroupChatDataSource groupChatDataSource;
    private RecyclerView recyclerView;
    private ProgressBar spinner;



    public GroupChatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_group_chat, container, false);

        groupChatDataSource = new GroupChatDataSource();
        groupChatDataSource.registerListener(this);
        groupChatDataSource.initWebSocketConnection();

        initUsername();

        List<ChatEntry> chatEntryList = new ArrayList<>();
        chatAdapter = new ChatAdapter(getContext(), chatEntryList, username);

        recyclerView = (RecyclerView) view.findViewById(R.id.group_chat_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new VerticalSpaceItemDecoration(Constants.CHAT_ITEM_DIVIDER_HEIGHT));
        recyclerView.setAdapter(chatAdapter);

        final ImageView imageView = (ImageView) view.findViewById(R.id.group_chat_btn_send_text);
        final EditText editText = (EditText) view.findViewById(R.id.group_chat_edit_text);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View ignored) {
                if (username == null || username.equals("")) {
                    initUsername();
                    return;
                }

                String text = editText.getText().toString();
                if (text.equals(""))
                    return;

                Utils.animateViewScale(imageView);

                ChatEntry chatEntry = new ChatEntry(username, text, System.currentTimeMillis());
                groupChatDataSource.sendMessage(chatEntry);
                editText.setText("");
            }
        });

        view.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
                recyclerView.smoothScrollToPosition(chatAdapter.getItemCount());
            }
        });

        spinner = (ProgressBar) view.findViewById(R.id.progressBar1);

        EventBus.getDefault().register(this);

        return view;
    }

    private void initUsername() {
        if (username == null) {
            username = Utils.readUsernameFromPreferences(getContext());
            if (chatAdapter != null)
                chatAdapter.setUsername(username);
        }
    }

    @Override
    public void onDestroyView() {
        if (groupChatDataSource != null) {
            groupChatDataSource.clearListeners();
            groupChatDataSource.closeConnection();
        }
        EventBus.getDefault().unregister(this);
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        if (groupChatDataSource != null) {
            groupChatDataSource.clearListeners();
            groupChatDataSource.closeConnection();
        }
        super.onDestroy();
    }

    @Override
    public void messageReceived(ChatEntry chatEntry) {
        spinner.setVisibility(View.GONE);
        chatAdapter.messageReceived(chatEntry);
        recyclerView.smoothScrollToPosition(chatAdapter.getItemCount());
    }

    @Override
    public void noMessages() {
        spinner.setVisibility(View.GONE);
    }

    @Override
    public void connectionClosed() {
        groupChatDataSource.initWebSocketConnection();
        chatAdapter.clearEntries();
        Toast.makeText(getContext(), "Reinitializing Connection ...", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void connectionFound() {

    }


    public void usernameUpdated() {
        initUsername();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(UsernameChangedEvent event) {
        username = event.getNewUsername();
        chatAdapter.setUsername(username);
        groupChatDataSource.initWebSocketConnection();
        chatAdapter.clearEntries();
    }
}
