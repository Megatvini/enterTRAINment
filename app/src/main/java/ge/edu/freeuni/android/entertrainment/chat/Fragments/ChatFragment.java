package ge.edu.freeuni.android.entertrainment.chat.Fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import ge.edu.freeuni.android.entertrainment.MainActivity;
import ge.edu.freeuni.android.entertrainment.R;
import ge.edu.freeuni.android.entertrainment.chat.Constants;
import ge.edu.freeuni.android.entertrainment.chat.Utils;
import ge.edu.freeuni.android.entertrainment.events.ShareEvent;
import ge.edu.freeuni.android.entertrainment.events.UsernameChangedEvent;


public class ChatFragment extends Fragment {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private View view;
    private GroupChatFragment groupChatFragment;
    private IndividualChatFragment individualChatFragment;
    private OnFragmentInteractionListener mListener;

    public ChatFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        initUsername();
        view =  inflater.inflate(R.layout.fragment_chat, container, false);
        viewPager = (ViewPager) view.findViewById(R.id.chat_viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) view.findViewById(R.id.chat_tabs);
        tabLayout.setupWithViewPager(viewPager);

        EventBus.getDefault().register(this);
        return view;
    }

    @Override
    public void onDestroyView() {
        EventBus.getDefault().unregister(this);
        super.onDestroyView();
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());

        groupChatFragment = new GroupChatFragment();
        individualChatFragment = new IndividualChatFragment();

        adapter.addFrag(groupChatFragment, "Group Chat");
        adapter.addFrag(individualChatFragment, "Individual Chat");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    private void initUsername() {
        String username = Utils.readUsernameFromPreferences(getContext());

        if (username == null) {
            String url = "http://" + Constants.SERVER_ADDRESS + "/webapi/chatname";

            AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
            asyncHttpClient.get(url, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, final JSONObject response) {
                    Utils.runInMain(new Runnable() {
                        @Override
                        public void run() {
                            Utils.saveUsernameInSharedPreferences(getContext(), response);
                            groupChatFragment.usernameUpdated();
                            individualChatFragment.usernameUpdated();
                        }
                    });
                }
            });
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ShareEvent event) {
        if (isVisible()) {
            String data = "Share about chat";
            String title = "chatting with passengers...";
            Utils.shareStringData(getContext(), title, data);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        MainActivity activity = (MainActivity) getActivity();
        activity.getNavigationView().getMenu().findItem(R.id.nav_chat).setChecked(true);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_chat, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_change_username:
                showInputDialog();
                return true;
            default:
                break;
        }

        return false;
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
                        String username = editText.getText().toString();
                        username = username.replace(" ", "");
                        if (!username.equals("")) {
                           try_ask_server(username);
                        } else {
                            Toast.makeText(getContext(), "Enter something non-empty", Toast.LENGTH_SHORT).show();
                        }
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

    private void try_ask_server(final String username) {
        String url = "http://" + Constants.SERVER_ADDRESS + "/webapi/chatname/check/" + username;
        url = url + "?oldname=" + Utils.readUsernameFromPreferences(getContext());
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.get(url, null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (statusCode == 200) {
                    Utils.saveUsernameInSharedPreferences(getContext(), username);
                    EventBus.getDefault().post(new UsernameChangedEvent(username));
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }
}
