package com.robert.foursquareofficial;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Robert on 10/23/2017.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{

    public List<AllLocation> locationList;
    private AdapterView.OnItemClickListener onItemClickListener;
    private Context context;

    public MyAdapter(List<AllLocation> listItems, AdapterView.OnItemClickListener onItemClickListener, Context context) {
        this.locationList = listItems;
        this.context = context;
        this.onItemClickListener = onItemClickListener;

    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.locationcard,parent,false);

        return new ViewHolder(v);
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        AllLocation individualLocation = locationList.get(position);

        holder.locationN.setText(individualLocation.getLocation());
        holder.locationD.setText(individualLocation.getDate());

        if(individualLocation.getLocationDetermined() == false){
            holder.mCardView.setCardBackgroundColor(Color.RED);
        }else{
            if(individualLocation.booleanSearch == true){
                holder.mCardView.setCardBackgroundColor(Color.BLUE);
            }else{
                holder.mCardView.setCardBackgroundColor(Color.WHITE);
            }
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView locationN;
        public TextView locationD;
        public CardView mCardView = (CardView) itemView.findViewById(R.id.cardView);

        public ViewHolder(View itemView) {
            super(itemView);

            locationN = (TextView) itemView.findViewById(R.id.locationName);
            locationD = (TextView) itemView.findViewById(R.id.locationDate);

            locationN.setOnClickListener(this);
            locationD.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            //passing the clicked position to the parent class
            onItemClickListener.onItemClick(null, view, getAdapterPosition(), view.getId());
        }
    }
    public AllLocation getItem(int position) {
        return locationList.get(position);
    }
    public void removeAt(int position) {
        locationList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, locationList.size());
    }
    public void resetColors(){
        for(AllLocation i : locationList){
            i.booleanSearch = false;
        }
    }

    public void addColor(ArrayList<Integer> aList){
        resetColors();
        for(Integer i : aList){
            locationList.get(i).booleanSearch = true;
        }

        notifyDataSetChanged();
    }

    public int getItemCount() {
        return locationList.size();
    }
    public void setLocation(int cardLocation, int positionSelection){
        AllLocation allL = locationList.get(cardLocation);
        allL.selectfinal(positionSelection);
        notifyDataSetChanged();
    }





}
