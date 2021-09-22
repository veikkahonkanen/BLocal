package com.bteam.blocal.ui.user.store_list;

import android.content.Context;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bteam.blocal.R;
import com.bteam.blocal.data.model.StoreModel;
import com.bteam.blocal.utility.Constants;
import com.bteam.blocal.utility.DistanceCalculator;
import com.bteam.blocal.utility.FirebaseSwipeAdapter;
import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;

public class StoreListAdapter extends
        FirebaseSwipeAdapter<StoreModel, StoreListAdapter.StoreListViewHolder> {
    private Context context;
    private Location lastLocation;

    public StoreListAdapter(FirebaseSwipeAdapter.IItemClickListener listener,
                            @NonNull FirestorePagingOptions<StoreModel> options) {
        super(options, listener);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        // We obtain the context here to use it in the onBindViewHolder
        // Even though it's possible to get it from the item views in the onBindViewHolder function
        // This solution seems to be better
        // Source: https://stackoverflow.com/a/48660869
        context = recyclerView.getContext();
    }

    @NonNull
    @Override
    public StoreListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_store, parent,
                false);
        return new StoreListViewHolder(v, listener);
    }

    @Override
    protected void onBindViewHolder(@NonNull StoreListViewHolder storeListViewHolder, int i,
                                    @NonNull StoreModel storeModel) {
        storeListViewHolder.txtStoreName.setText(storeModel.getName());
        Glide.with(context).load(storeModel.getImageUrl())
                .apply(Constants.getStoreDefaultOptions()).into(storeListViewHolder.imgStoreIcon);

        // Calculate distance to the stores
        Location storeLocation = new Location("store");
        storeLocation.setLatitude(storeModel.getLocation().getLatitude());
        storeLocation.setLongitude(storeModel.getLocation().getLongitude());

        if (null != lastLocation) {
            // Gets distance in meters
            double distance = DistanceCalculator.getDistance(storeLocation, lastLocation);
            storeListViewHolder.txtStoreDistance
                    .setText(String
                            .format(java.util.Locale.US,"%.1f", (distance / 1000)) + " km");
        } else {
            storeListViewHolder.txtStoreDistance.setText("n/a");
        }
    }

    @Override
    protected void onError(@NonNull Exception e) {
        super.onError(e);
        //TODO: Handle error with callback so that snackbar can be shown
    }


    class StoreListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtStoreName, txtStoreOwner, txtStoreDistance;
        ImageView imgStoreIcon;

        IItemClickListener listener;

        public StoreListViewHolder(@NonNull View itemView, IItemClickListener listener) {
            super(itemView);
            this.listener = listener;
            txtStoreName = itemView.findViewById(R.id.txt_store_item_name);
            txtStoreOwner = itemView.findViewById(R.id.txt_store_item_owner);
            txtStoreDistance = itemView.findViewById(R.id.txt_store_item_distance);
            imgStoreIcon = itemView.findViewById(R.id.img_store_item_icon);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if(position != RecyclerView.NO_POSITION && listener != null){
                listener.onItemClick(getItem(position),position);
            }
        }
    }

    public void setLastLocation(Location lastLocation) {
        this.lastLocation = lastLocation;
        notifyDataSetChanged();
    }
}
