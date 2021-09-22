package com.bteam.blocal.ui.user.dashboard;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bteam.blocal.R;
import com.bteam.blocal.data.model.DashboardButtonModel;

import java.util.List;

public class DashboardAdapter extends RecyclerView.Adapter<DashboardAdapter.DashboardViewHolder> {
    private static final String TAG = "DashboardAdapter";

    private List<DashboardButtonModel> dashboardItems;
    private IItemClickListener listener;

    public DashboardAdapter(IItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public DashboardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dashboard, parent,
                false);
        return new DashboardViewHolder(v, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull DashboardViewHolder holder, int position) {
        DashboardButtonModel dim = dashboardItems.get(position);
        holder.itemImage.setImageResource(dim.getIconId());
        holder.itemName.setText(dim.getTextId());
    }

    @Override
    public int getItemCount() {
        if (null != dashboardItems)
            return dashboardItems.size();
        else
            return 0;
    }

    public void updateItemsList(List<DashboardButtonModel> items) {
        dashboardItems = items;
        Log.d(TAG, "updateItemsList: " + items);
        notifyDataSetChanged();
    }

    public interface IItemClickListener {
        void onItemClick(int index);
    }

    public class DashboardViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        ImageView itemImage;
        TextView itemName;

        IItemClickListener listener;

        public DashboardViewHolder(@NonNull View itemView, IItemClickListener listener) {
            super(itemView);
            this.listener = listener;
            itemImage = itemView.findViewById(R.id.dashboard_item_image);
            itemName = itemView.findViewById(R.id.dashboard_item_name);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onItemClick(getAdapterPosition());
        }
    }
}
