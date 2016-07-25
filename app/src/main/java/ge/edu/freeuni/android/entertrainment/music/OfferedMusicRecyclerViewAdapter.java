package ge.edu.freeuni.android.entertrainment.music;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

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
        holder.song = mValues.get(position);
        Context context = holder.mView.getContext();

        if(!holder.song.getImage().equals("")){
            Picasso.with(context).load(holder.song.getImage()).into(holder.songImage);
        }

        holder.name.setText(holder.song.getName());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.song);
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
        public final ImageView songImage;
        public final TextView name;
        public Song song;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            songImage = (ImageView) view.findViewById(R.id.song_image);
            name = (TextView) view.findViewById(R.id.song_name);
        }

    }
}
