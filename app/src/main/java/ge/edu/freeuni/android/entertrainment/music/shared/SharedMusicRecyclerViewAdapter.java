package ge.edu.freeuni.android.entertrainment.music.shared;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import ge.edu.freeuni.android.entertrainment.R;
import ge.edu.freeuni.android.entertrainment.music.PlayerService;
import ge.edu.freeuni.android.entertrainment.music.shared.SharedMusicFragment.OnListFragmentInteractionListener;
import ge.edu.freeuni.android.entertrainment.music.VoteListener;
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
        Context context = holder.mView.getContext();
        holder.song = mValues.get(position);
        String name = mValues.get(position).name;

        PlayerService.NameInfo nameInfo = new PlayerService.NameInfo(name).invoke();

        holder.songName.setText(nameInfo.getTitle());
        holder.songAuthor.setText(nameInfo.getArtist());


        holder.songRating.setText(mValues.get(position).rating+"");

        String path = holder.song.getImage();
        if (!path.equals(""))
            Picasso.with(holder.mView.getContext()).load(path).into(holder.songImage);

        setTint(holder.downvote, context, R.color.cardview_shadow_start_color);
        setTint(holder.upvote,context, R.color.cardview_shadow_start_color);

        if (holder.song.getVoted().equals("up")){
            setTint(holder.upvote, context, R.color.colorAccent);
            setTint(holder.downvote, context, R.color.cardview_shadow_start_color);
        }
        if (holder.song.getVoted().equals("down")){
            setTint(holder.downvote,context, R.color.colorAccent);
            setTint(holder.upvote,context, R.color.cardview_shadow_start_color);
        }

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

    private void setTint(ImageButton button, Context context, int color) {
        Drawable mWrappedDrawable = button.getDrawable().mutate();
        mWrappedDrawable = DrawableCompat.wrap(mWrappedDrawable);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            DrawableCompat.setTint(mWrappedDrawable, context.getResources().getColor(color,context.getTheme()));
        }else {
            DrawableCompat.setTint(mWrappedDrawable, context.getResources().getColor(color));
        }
        DrawableCompat.setTintMode(mWrappedDrawable, PorterDuff.Mode.SRC_IN);
    }

    @Override
    public int getItemCount() {

        return mValues.size();
    }

    public void setData(List<Song> songs) {
        this.mValues.clear();
        this.mValues.addAll(songs);
//        notifyDataSetChanged();

//        notifyItemInserted(mValues.size());



    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView songImage;
        public final TextView songName;
        public final TextView songAuthor;
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

            downvote = (ImageButton) view.findViewById(R.id.downvote);
            upvote = (ImageButton) view.findViewById(R.id.upvote);
            songAuthor = (TextView) view.findViewById(R.id.song_author);
        }

    }

    public void setVoteListener(VoteListener voteListener) {
        this.voteListener = voteListener;
    }
}
