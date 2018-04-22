package it.polito.mad.sharenbook;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;


/**
 * Created by claudiosava on 19/04/18.
 */

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.AnnounceViewHolder>{

    public static class AnnounceViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView bookTitle;
        TextView bookSubtitle;
        ImageView bookPhoto;

        AnnounceViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.mybook_item);
            bookTitle = (TextView)itemView.findViewById(R.id.book_title);
            bookSubtitle = (TextView)itemView.findViewById(R.id.book_subtitle);
            bookPhoto = (ImageView)itemView.findViewById(R.id.book_photo);
        }
    }

    private List<Book> announcements;
    private Context context;

    RVAdapter(List<Book> announcements, Context context){
        this.announcements = announcements;
        this.context = context;
    }

    @NonNull
    @Override
    public RVAdapter.AnnounceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.mybook_item, parent, false);
        AnnounceViewHolder announceViewHolder = new AnnounceViewHolder(v);
        return announceViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RVAdapter.AnnounceViewHolder holder, int position) {

        holder.bookTitle.setText(announcements.get(position).getTitle());
        holder.bookSubtitle.setText(announcements.get(position).getBookConditions());
        //holder.bookPhoto.setImageURI(Uri.parse(announcements.get(position).getThumbnail()));
        Glide.with(context).load(announcements.get(position).getThumbnail()).into(holder.bookPhoto);
        Log.d("Book:",announcements.get(position).getThumbnail());

    }

    @Override
    public int getItemCount() {
        return announcements.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

}