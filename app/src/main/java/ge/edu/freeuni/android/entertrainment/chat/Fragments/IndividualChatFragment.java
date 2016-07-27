package ge.edu.freeuni.android.entertrainment.chat.Fragments;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import ge.edu.freeuni.android.entertrainment.chat.model.RandomChatDataSource;
import ge.edu.freeuni.android.entertrainment.events.UsernameChangedEvent;

/**
 * A simple {@link Fragment} subclass.
 */
public class IndividualChatFragment extends Fragment implements ChatUpdateListener {
    private RandomChatDataSource randomChatDataSource;
    private String username;
    private View view;
    private RecyclerView recyclerView;
    private Button findPairButton;
    private ChatAdapter chatAdapter;
    private ProgressBar spinner;
    private LinearLayout chatInputLayout;



    public IndividualChatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_individual_chat, container, false);

        initUsername();

        List<ChatEntry> chatEntryList = new ArrayList<>();
        chatAdapter = new ChatAdapter(getContext(), chatEntryList, username);

        randomChatDataSource = new RandomChatDataSource();
        randomChatDataSource.registerListener(this);

        recyclerView = (RecyclerView) view.findViewById(R.id.random_chat_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new VerticalSpaceItemDecoration(Constants.CHAT_ITEM_DIVIDER_HEIGHT));
        recyclerView.setAdapter(chatAdapter);

        ImageView imageView = (ImageView) view.findViewById(R.id.random_chat_btn_send_text);
        final EditText editText = (EditText) view.findViewById(R.id.random_chat_edit_text);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View ignored) {
                if (username == null || username.equals("")) {
                    initUsername();
                    return;
                }

                String text = editText.getText().toString();
                if (text.equals("") ||
                        findPairButton.getVisibility() == View.VISIBLE ||
                        spinner.getVisibility() == View.VISIBLE)
                    return;

                ChatEntry chatEntry = new ChatEntry(username, text, System.currentTimeMillis());
                randomChatDataSource.sendMessage(chatEntry);
                editText.setText("");
            }
        });

        spinner = (ProgressBar) view.findViewById(R.id.progressBar2);

        findPairButton = (Button) view.findViewById(R.id.random_chat_find_btn);
        findPairButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                randomChatDataSource.initWebSocketConnection();
                findPairButton.setVisibility(View.GONE);
                spinner.setVisibility(View.VISIBLE);
            }
        });

        view.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
                recyclerView.smoothScrollToPosition(chatAdapter.getItemCount());
            }
        });

        chatInputLayout = (LinearLayout) view.findViewById(R.id.random_chat_input_layout);

        EventBus.getDefault().register(this);

        return view;
    }

    protected void showInputDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View promptView = layoutInflater.inflate(R.layout.username_input_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setView(promptView);

        final EditText editText = (EditText) promptView.findViewById(R.id.edittext);
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        username = editText.getText().toString();
                        if (username.replace(" ", "").equals(""))
                            username = null;
                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    private void initUsername() {
        if (username == null) {
            username = Utils.readUsernameFromPreferences(getContext());
            if (chatAdapter != null)
                chatAdapter.setUsername(username);
        }
    }

    @Override
    public void messageReceived(ChatEntry chatEntry) {
        chatAdapter.messageReceived(chatEntry);
        recyclerView.smoothScrollToPosition(chatAdapter.getItemCount());
    }

    @Override
    public void noMessages() {
        spinner.setVisibility(View.GONE);
    }

    @Override
    public void connectionClosed() {
        spinner.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        chatAdapter.clearEntries();
        findPairButton.setVisibility(View.VISIBLE);
        chatInputLayout.setVisibility(View.GONE);
        Utils.hideSoftKeyboard(getActivity());
        Toast.makeText(getContext(), "Peer closed connection...", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void connectionFound() {
        spinner.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        Toast.makeText(getContext(), "Connection found", Toast.LENGTH_SHORT).show();
        chatInputLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDestroyView() {
        if (randomChatDataSource != null) {
            randomChatDataSource.clearListeners();
            randomChatDataSource.closeConnection();
        }
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        if (randomChatDataSource != null) {
            randomChatDataSource.clearListeners();
            randomChatDataSource.closeConnection();
        }
        super.onDestroy();
    }

    public void usernameUpdated() {
        initUsername();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(UsernameChangedEvent event) {
        username = event.getNewUsername();
        chatAdapter.setUsername(username);
        spinner.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        chatAdapter.clearEntries();
        findPairButton.setVisibility(View.VISIBLE);
        chatInputLayout.setVisibility(View.GONE);
        Utils.hideSoftKeyboard(getActivity());
    }
}
