package ge.edu.freeuni.android.entertrainment.chat.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.loopj.android.http.AsyncHttpClient;
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
import ge.edu.freeuni.android.entertrainment.ShareEvent;
import ge.edu.freeuni.android.entertrainment.chat.Constants;
import ge.edu.freeuni.android.entertrainment.chat.Utils;


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
}
