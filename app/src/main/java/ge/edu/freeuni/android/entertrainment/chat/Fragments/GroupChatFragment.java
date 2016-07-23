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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import ge.edu.freeuni.android.entertrainment.R;
import ge.edu.freeuni.android.entertrainment.chat.Constants;
import ge.edu.freeuni.android.entertrainment.chat.Utils;
import ge.edu.freeuni.android.entertrainment.chat.adapters.GroupChatAdapter;
import ge.edu.freeuni.android.entertrainment.chat.helper.VerticalSpaceItemDecoration;
import ge.edu.freeuni.android.entertrainment.chat.model.ChatEntry;
import ge.edu.freeuni.android.entertrainment.chat.model.ChatUpdateListener;
import ge.edu.freeuni.android.entertrainment.chat.model.GroupChatDataSource;

/**
 * A simple {@link Fragment} subclass.
 */
public class GroupChatFragment extends Fragment implements ChatUpdateListener{
    private GroupChatAdapter groupChatAdapter;
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

        initUsername();

        List<ChatEntry> chatEntryList = new ArrayList<>();
        groupChatAdapter = new GroupChatAdapter(getContext(), chatEntryList, username);

        groupChatDataSource = new GroupChatDataSource();
        groupChatDataSource.registerListener(this);

        recyclerView = (RecyclerView) view.findViewById(R.id.group_chat_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new VerticalSpaceItemDecoration(Constants.CHAT_ITEM_DIVIDER_HEIGHT));
        recyclerView.setAdapter(groupChatAdapter);

        ImageView imageView = (ImageView) view.findViewById(R.id.group_chat_btn_send_text);
        final EditText editText = (EditText) view.findViewById(R.id.group_chat_edit_text);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View ignored) {
                if (username == null || username.equals("")) {
                    initUsername();
                }

                if (username == null || username.equals("")) {
                    showInputDialog();
                }

                String text = editText.getText().toString();
                if (text.equals(""))
                    return;

                ChatEntry chatEntry = new ChatEntry(username, text, System.currentTimeMillis());
                groupChatDataSource.sendMessage(chatEntry);
                editText.setText("");
            }
        });

        spinner = (ProgressBar) view.findViewById(R.id.progressBar1);

        return view;
    }

    private void initUsername() {
        if (username == null) {
            username = Utils.readUsernameFromPreferences(getContext());
        }
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        groupChatDataSource.clearListeners();
        groupChatDataSource.closeConnection();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        groupChatDataSource.clearListeners();
        groupChatDataSource.closeConnection();
    }

    @Override
    public void messageReceived(ChatEntry chatEntry) {
        spinner.setVisibility(View.GONE);
        groupChatAdapter.messageReceived(chatEntry);
        recyclerView.smoothScrollToPosition(groupChatAdapter.getItemCount());
    }
}
