package ge.edu.freeuni.android.entertrainment.chat;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import ge.edu.freeuni.android.entertrainment.R;
import ge.edu.freeuni.android.entertrainment.chat.adapters.GroupChatAdapter;
import ge.edu.freeuni.android.entertrainment.chat.model.ChatEntry;

/**
 * A simple {@link Fragment} subclass.
 */
public class GroupChatFragment extends Fragment {
    private GroupChatAdapter groupChatAdapter;

    public GroupChatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_group_chat, container, false);


        List<ChatEntry> chatEntryList = new ArrayList<>();
        chatEntryList.add(new ChatEntry("Nika", "teqsti", 1469183621175L));
        chatEntryList.add(new ChatEntry("Nika", "teqasdasddjhbahbjsdahdsasti", 1469183623175L));
        chatEntryList.add(new ChatEntry("Megatvini", "teqsasdfdasfdfaadfsdti", 1469183625175L));
        chatEntryList.add(new ChatEntry("Alo", "teasdfdasfsdaasfdfasqsti", 1469183628175L));
        chatEntryList.add(new ChatEntry("Alo", "teasdfdasfsdaasfdfasqsti", 1469183628175L));
        chatEntryList.add(new ChatEntry("Alo", "teasdfdasfsdaasfdfasqsti", 1469183628175L));
        chatEntryList.add(new ChatEntry("Alo", "teasdfdasfsdaasfdfasqsti", 1469183628175L));
        chatEntryList.add(new ChatEntry("Alo", "teasdfdasfsdaasfdfasqsti", 1469183628175L));
        chatEntryList.add(new ChatEntry("Alo", "teasdfdasfsdaasfdfasqsti", 1469183628175L));
        chatEntryList.add(new ChatEntry("Alo", "teasdfdasfsdaasfdfasqsti", 1469183628175L));
        chatEntryList.add(new ChatEntry("Alo", "teasdfdasfsdaasfdfasqsti", 1469183628175L));
        chatEntryList.add(new ChatEntry("Alo", "teasdfdasfsdaasfdfasqsti", 1469183628175L));
        chatEntryList.add(new ChatEntry("Alo", "teasdfdasfsdaasfdfasqsti", 1469183628175L));
        chatEntryList.add(new ChatEntry("Alo", "teasdfdasfsdaasfdfasqsti", 1469183628175L));
        chatEntryList.add(new ChatEntry("Alo", "teasdfdasfsdaasfdfasqsti", 1469183628175L));
        chatEntryList.add(new ChatEntry("Alo", "teasdfdasfsdaasfdfasqsti", 1469183628175L));
        chatEntryList.add(new ChatEntry("Alo", "teasdfdasfsdaasfdfasqsti", 1469183628175L));
        groupChatAdapter = new GroupChatAdapter(getContext(), chatEntryList);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.group_chat_recycler_view);
        recyclerView.setAdapter(groupChatAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        return view;
    }

}
