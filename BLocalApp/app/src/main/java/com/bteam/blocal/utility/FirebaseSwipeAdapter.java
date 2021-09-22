package com.bteam.blocal.utility;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bteam.blocal.ui.shared.item_list.ItemListAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.firebase.ui.firestore.paging.LoadingState;
import com.google.firebase.firestore.DocumentSnapshot;

public abstract class FirebaseSwipeAdapter<T,K extends RecyclerView.ViewHolder>
        extends FirestorePagingAdapter<T, K> {
    public interface IItemClickListener {
        void onItemClick(DocumentSnapshot document, int index);
    }

    private SwipeRefreshLayout _swipeRefreshLayout;
    protected ItemListAdapter.IItemClickListener listener;

    public FirebaseSwipeAdapter(@NonNull FirestorePagingOptions<T> options,
                                ItemListAdapter.IItemClickListener listener) {
        super(options);
        this.listener = listener;

    }
    @Override
    protected void onLoadingStateChanged(@NonNull LoadingState state) {
        switch (state){
            case LOADING_INITIAL:
            case LOADING_MORE:
                swipeRefreshIsReloading(true);
                break;
            case LOADED:
            case FINISHED:
                swipeRefreshIsReloading(false);
                break;
            case ERROR:
                //TODO: Handle error
                swipeRefreshIsReloading(false);
                break;
        }
    }

    public void bindSwipeRefresh(SwipeRefreshLayout swipeRefreshLayout){
        _swipeRefreshLayout = swipeRefreshLayout;
    }

    private void swipeRefreshIsReloading(boolean isRefreshing){
        if(_swipeRefreshLayout != null){
            _swipeRefreshLayout.setRefreshing(isRefreshing);
        }
    }
}
