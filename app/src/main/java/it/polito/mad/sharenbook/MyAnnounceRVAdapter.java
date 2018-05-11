package it.polito.mad.sharenbook;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import it.polito.mad.sharenbook.model.Book;
import it.polito.mad.sharenbook.utils.UserInterface;


/**
 * Created by claudiosava on 19/04/18.
 */

public class MyAnnounceRVAdapter extends RecyclerView.Adapter<MyAnnounceRVAdapter.AnnounceViewHolder>{

    private static List<Book> announcements;
    private static Book underModification;
    private static int positionUnderModificaiton;
    private Context context;
    private LinearLayoutManager llm;
    private StorageReference mBookImagesStorage;

    MyAnnounceRVAdapter(List<Book> announcements, Context context, LinearLayoutManager llm, StorageReference bookImagesStorage) {
        MyAnnounceRVAdapter.announcements = announcements;
        this.context = context;
        this.llm = llm;
        this.mBookImagesStorage = bookImagesStorage;
        underModification = null;
    }

    @NonNull
    @Override
    public MyAnnounceRVAdapter.AnnounceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.mybook_item, parent, false);
        AnnounceViewHolder announceViewHolder = new AnnounceViewHolder(v);
        return announceViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyAnnounceRVAdapter.AnnounceViewHolder holder, int position) {

        if(!announcements.isEmpty()) {
            // UserInterface.TextViewFontResize(announcements.get(position).getTitle().length(),(WindowManager)context.getSystemService(Context.WINDOW_SERVICE) , holder.bookTitle);

            // Get Book Image
            StorageReference photoRef = mBookImagesStorage.child(announcements.get(position).getBookId() + "/" + announcements.get(position).getPhotosName().get(0));
            Glide.with(context).load(photoRef).into(holder.bookPhoto);

            holder.bookTitle.setText(announcements.get(position).getTitle());
            holder.bookAuthors.setText(announcements.get(position).getAuthorsAsString());
            holder.bookCreationTime.setText(announcements.get(position).getCreationTimeAsString(context));

            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
            List<Address> place = new ArrayList<>();
            try {
                place.addAll(geocoder.getFromLocation(Double.parseDouble(announcements.get(position).getLocation_lat()), Double.parseDouble(announcements.get(position).getLocation_long()), 1));
            }catch (IOException e) {
                e.printStackTrace();
            }

            if(!place.isEmpty())
                holder.bookLocation.setText(place.get(0).getLocality() + ", " + place.get(0).getCountryName());
            else
                holder.bookLocation.setText(R.string.unknown_place);

            holder.editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, EditBookActivity.class);
                    i.putExtra("book", announcements.get(position));
                    context.startActivity(i);
                    underModification = announcements.get(position);
                    positionUnderModificaiton = position;

                }
            });
            holder.cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, ShowBookActivity.class);
                    i.putExtra("book",announcements.get(position));
                    context.startActivity(i); // start activity without finishing in order to return back with back pressed

                }
            });
        }

    }


    @Override
    public int getItemCount() {
        return announcements.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static List<Book> getAnnouncementsModel(){
        return announcements;
    }

    public static int getPositionUnderModificaiton() {
        return positionUnderModificaiton;
    }

    public static void setPositionUnderModificaiton(int positionUnderModificaiton) {
        MyAnnounceRVAdapter.positionUnderModificaiton = positionUnderModificaiton;
    }

    public static Book getUnderModification() {
        return underModification;
    }

    public static void setUnderModification(Book underModification) {
        MyAnnounceRVAdapter.underModification = underModification;
    }

    public void addMultipleItems(int startPosition, List<Book> announces){
        if(!announcements.containsAll(announces)) {
            announcements.addAll(startPosition, announces);
        }
            this.notifyItemRangeInserted(startPosition, announces.size());

    }

    public void addItem(int position, Book announce){

        if(!announcements.contains(announce))
            announcements.add(position, announce);
        this.notifyItemInserted(position);


    }

    public void removeItem(int position){
        announcements.remove(position);
        notifyItemRemoved(position);
    }




    public class AnnounceViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView bookTitle;
        TextView bookAuthors;
        TextView bookCreationTime;
        TextView bookLocation;
        ImageView bookPhoto;
        Button editButton;

        AnnounceViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.mybook_item);
            bookTitle = (TextView)itemView.findViewById(R.id.book_title);
            bookAuthors = (TextView)itemView.findViewById(R.id.book_authors);
            bookCreationTime = (TextView)itemView.findViewById(R.id.book_creationTime);
            bookLocation = (TextView)itemView.findViewById(R.id.book_location);
            bookPhoto = (ImageView)itemView.findViewById(R.id.book_photo);
            editButton = itemView.findViewById(R.id.edit_button);
        }
    }

}
