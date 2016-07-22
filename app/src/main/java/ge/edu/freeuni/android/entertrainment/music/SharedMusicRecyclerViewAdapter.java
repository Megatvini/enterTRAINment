package ge.edu.freeuni.android.entertrainment.music;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import ge.edu.freeuni.android.entertrainment.R;
import ge.edu.freeuni.android.entertrainment.music.SharedMusicFragment.OnListFragmentInteractionListener;
import ge.edu.freeuni.android.entertrainment.music.data.MusicProvider.Song;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Song} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class SharedMusicRecyclerViewAdapter extends RecyclerView.Adapter<SharedMusicRecyclerViewAdapter.ViewHolder> {

    private final List<Song> mValues;
    private final OnListFragmentInteractionListener mListener;

    public SharedMusicRecyclerViewAdapter(List<Song> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_sharedmusic, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.song = mValues.get(position);
        holder.songName.setText(mValues.get(position).name);
        holder.songRating.setText(mValues.get(position).rating);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onListFragmentInteraction(holder.song);
                }
            }
        });
    }

    @Override
    public int getItemCount() {

        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView songImage;
        public final TextView songName;
        public final TextView songRating;
        public Song song;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            songImage = (ImageView) view.findViewById(R.id.song_image);
            songName = (TextView) view.findViewById(R.id.song_name);
            songRating = (TextView) view.findViewById(R.id.song_rating);
        }

    }
}
