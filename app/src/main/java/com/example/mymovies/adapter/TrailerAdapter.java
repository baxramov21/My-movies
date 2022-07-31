package com.example.mymovies.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mymovies.R;
import com.example.mymovies.data.Trailer;

import java.util.ArrayList;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailersViewholder> {

    private OnClickPlayVideo onClickPlayVideo;
    private ArrayList<Trailer> trailers;

    public void setTrailers(ArrayList<Trailer> trailers) {
        this.trailers = trailers;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TrailersViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trailer_activity , parent,false);
        return new TrailersViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrailersViewholder holder, int position) {
        Trailer currentTrailer = trailers.get(position);
        holder.textViewVideoName.setText(currentTrailer.getName());
    }

    @Override
    public int getItemCount() {
        return trailers.size();
    }

    public interface OnClickPlayVideo {
        void onClickPlay(String url);
    }

    class TrailersViewholder extends RecyclerView.ViewHolder {

        private TextView textViewVideoName;

        public TrailersViewholder(@NonNull View itemView) {
            super(itemView);
            textViewVideoName = itemView.findViewById(R.id.textViewTrailerTitle);
            itemView.setOnClickListener(view -> {
                if (onClickPlayVideo != null) {
                    onClickPlayVideo.onClickPlay(trailers.get(getAdapterPosition()).getKey());
                }
            });
        }
    }

    public void setOnClickPlayVideo(OnClickPlayVideo onClickPlayVideo) {
        this.onClickPlayVideo = onClickPlayVideo;
        notifyDataSetChanged();
    }
}
