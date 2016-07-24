package ge.edu.freeuni.android.entertrainment.music;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ge.edu.freeuni.android.entertrainment.R;
import ge.edu.freeuni.android.entertrainment.music.OfferedMusicsFragment.OnListFragmentInteractionListener;
import ge.edu.freeuni.android.entertrainment.music.data.Song;

import java.util.List;

public class OfferedMusicRecyclerViewAdapter extends RecyclerView.Adapter<OfferedMusicRecyclerViewAdapter.ViewHolder> implements MusicAdapter {

    private List<Song> mValues;
    private final OnListFragmentInteractionListener mListener;

    public OfferedMusicRecyclerViewAdapter(List<Song> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.offered_music_fragment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).id);


        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    @Override
    public void setData(List<Song> songs) {
        this.mValues = songs;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public Song mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.id);
            mContentView = (TextView) view.findViewById(R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
