package ge.edu.freeuni.android.entertrainment.music;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ge.edu.freeuni.android.entertrainment.R;
import ge.edu.freeuni.android.entertrainment.music.SharedMusicFragment.OnListFragmentInteractionListener;
import ge.edu.freeuni.android.entertrainment.music.data.Song;

public class SharedMusicRecyclerViewAdapter extends RecyclerView.Adapter<SharedMusicRecyclerViewAdapter.ViewHolder>{

    private VoteListener voteListener;

    private List<Song> mValues;
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
        System.out.println("re layouting");
        holder.song = mValues.get(position);
        holder.songName.setText(mValues.get(position).name);
        holder.songRating.setText(mValues.get(position).rating+"");

        holder.upvote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("upvote clicked");
                SharedMusicRecyclerViewAdapter.this.voteListener.upvote(holder.song.getId());
            }
        });
        holder.downvote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedMusicRecyclerViewAdapter.this.voteListener.downvote(holder.song.getId());
            }
        });
    }

    @Override
    public int getItemCount() {

        int size = mValues.size();
        System.out.println("size: "+size);
        return size;
    }

    public void setData(List<Song> songs) {
        System.out.println("l1:"+songs.size());
        this.mValues.clear();
        this.mValues.addAll(songs);
        System.out.println("l2:"+mValues.size());
//        notifyDataSetChanged();

//        notifyItemInserted(mValues.size());



    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView songImage;
        public final TextView songName;
        public final TextView songRating;
        public final ImageButton upvote;
        public final ImageButton downvote;
        public Song song;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            songImage = (ImageView) view.findViewById(R.id.song_image);
            songName = (TextView) view.findViewById(R.id.song_name);
            songRating = (TextView) view.findViewById(R.id.song_rating);

            downvote = (ImageButton)view.findViewById(R.id.downvote);
            upvote = (ImageButton)view.findViewById(R.id.upvote);
        }

    }

    public void setVoteListener(VoteListener voteListener) {
        this.voteListener = voteListener;
    }
}
