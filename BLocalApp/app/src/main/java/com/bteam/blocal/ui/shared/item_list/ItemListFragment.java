package com.bteam.blocal.ui.shared.item_list;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.IdRes;
import androidx.annotation.NavigationRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.NavInflater;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bteam.blocal.R;
import com.bteam.blocal.data.model.ItemModel;
import com.bteam.blocal.utility.SizeUtils;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.google.firebase.firestore.DocumentSnapshot;

public abstract class ItemListFragment extends Fragment
        implements ItemListAdapter.IItemClickListener {

    protected ItemListViewModel itemListViewModel;
    private ItemListAdapter adapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        itemListViewModel =
                new ViewModelProvider(this).get(ItemListViewModel.class);

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_item_list, container, false);

        adapter = new ItemListAdapter(this, new FirestorePagingOptions.Builder<ItemModel>()
                .setLifecycleOwner(this)
                .setQuery(itemListViewModel.getQuery(), itemListViewModel.getPagingConfig(),
                        ItemModel.class)
                .build());


        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Set the adapter

        SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.swiperefresh_store);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            // Clear items and obtain data again
            adapter.refresh();
        });
        RecyclerView recyclerView = view.findViewById(R.id.list_items);
        Context context = view.getContext();
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        adapter.bindSwipeRefresh(swipeRefreshLayout);

        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public void onItemClick(DocumentSnapshot document, int index) {
        String uid = document.toObject(ItemModel.class).getUid();
        if(!SizeUtils.isTablet(getContext())){
            navigateToDetail(uid);
        }
        else{
            swapDetailFragment(uid);
        }

    }

    private void swapDetailFragment(String uid) {
        NavHostFragment hostFragment = ((NavHostFragment) getChildFragmentManager()
                .findFragmentById(R.id.detail_host));
        if(hostFragment != null){
            NavController controller = hostFragment.getNavController();
            NavInflater inflater = controller.getNavInflater();
            NavGraph graph = inflater.inflate(getNavigationGraphId());
            graph.setStartDestination(getDetailNavigationId());
            //Override the arguments here with whatever we need to produce this detail fragment
            Bundle args = getDetailArgsBundle(uid);
            controller.setGraph(graph, args);
        }
    }

    protected abstract @NavigationRes int getNavigationGraphId();

    protected abstract Bundle getDetailArgsBundle(String itemUid);

    protected abstract @IdRes int getDetailNavigationId();

    protected abstract void navigateToDetail(String uid);
}