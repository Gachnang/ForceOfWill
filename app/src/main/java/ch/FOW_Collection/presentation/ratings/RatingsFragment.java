package ch.FOW_Collection.presentation.ratings;

import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import ch.FOW_Collection.R;
import ch.FOW_Collection.domain.models.Rating;
import ch.FOW_Collection.domain.models.User;
import ch.FOW_Collection.domain.models.Wish;
import ch.FOW_Collection.presentation.MainViewModel;
import ch.FOW_Collection.presentation.cardDetails.CardDetailsActivity;
import lombok.val;

public class RatingsFragment extends Fragment
        implements OnRatingsItemInteractionListener, SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "FeedFragment";

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    private RatingsRecyclerViewAdapter adapter;
    private MainViewModel model;

    public RatingsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View rootView = inflater.inflate(R.layout.fragment_ratings, container, false);
        ButterKnife.bind(this, rootView);

        model = ViewModelProviders.of(this).get(MainViewModel.class);
        /// model.getAllRatingsWithWishes().observe(this, this::updateRatings);
        model.getCurrentUser().observe(this, this::updateUser);

        val layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);


        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), layoutManager.getOrientation()));



        swipeRefreshLayout.setOnRefreshListener(this);

        return rootView;
    }

    private void updateRatings(List<Pair<Rating, Wish>> ratings) {
        if (ratings != null) {
            adapter.submitList(new ArrayList<>(ratings));
        }
    }

    private void updateUser(User user) {
        if (user != null) {
            adapter = new RatingsRecyclerViewAdapter(this, this, user);
            recyclerView.setAdapter(adapter);
        }
    }

    @Override
    public void onRatingLikedListener(Rating rating) {
        /// model.toggleLike(rating);
    }

    @Override
    public void onMoreClickedListener(Rating rating) {
        Intent intent = new Intent(getActivity(), CardDetailsActivity.class);
        intent.putExtra(CardDetailsActivity.ITEM_ID, rating.getCardId());
        startActivity(intent);
    }

    @Override
    public void onWishClickedListener(Rating item) {
        /// model.toggleItemInWishlist(item.getBeerId());
    }

    @Override
    public void onRefresh() {
        /// updateRatings(model.getAllRatingsWithWishes().getValue());
        swipeRefreshLayout.setRefreshing(false);
        adapter.notifyDataSetChanged();
    }
}