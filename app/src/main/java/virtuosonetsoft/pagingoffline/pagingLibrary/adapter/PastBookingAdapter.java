package virtuosonetsoft.pagingoffline.pagingLibrary.adapter;

import android.annotation.TargetApi;
import android.arch.paging.PagedListAdapter;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ViewGroup;

import java.util.Objects;

import virtuosonetsoft.pagingoffline.R;
import virtuosonetsoft.pagingoffline.dbdatabase.HistoryFroimDb;
import virtuosonetsoft.pagingoffline.pagingLibrary.data.NetworkState;


public class PastBookingAdapter extends PagedListAdapter<HistoryFroimDb, RecyclerView.ViewHolder> {

    private NetworkState networkState;

    private RetryCallback retryCallback;

    public PastBookingAdapter(RetryCallback retryCallback) {
        super(Ride_listDiffCallback);
        this.retryCallback = retryCallback;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case R.layout.item_rented_equip_history:
                return PastBookingViewHolder.create(parent);
            case R.layout.item_network_state:
                return NetworkStateViewHolder.create(parent, retryCallback);
            default:
                throw new IllegalArgumentException("unknown view type");
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case R.layout.item_rented_equip_history:
                ((PastBookingViewHolder) holder).bindTo(getItem(position));

                break;
            case R.layout.item_network_state:
                ((NetworkStateViewHolder) holder).bindTo(networkState);
                break;
        }
    }

    private boolean hasExtraRow() {
        return networkState != null && networkState != NetworkState.LOADED;
    }

    @Override
    public int getItemViewType(int position) {
        if (hasExtraRow() && position == getItemCount() - 1) {
            createLog("network"+"adapter");
            return R.layout.item_network_state;
        } else {
            createLog("item_user"+"adapter");
            return R.layout.item_rented_equip_history;
        }
    }
    public void createLog(String s) {
        Log.i("Pagingwithkey", s.toString());
    }
    @Override
    public int getItemCount() {
        return super.getItemCount() + (hasExtraRow() ? 1 : 0);
    }

    /**
     * Set the current network state to the adapter
     * but this work only after the initial load
     * and the adapter already have list to add new loading raw to it
     * so the initial loading state the activity responsible for handle it
     *
     * @param newNetworkState the new network state
     */
    public void setNetworkState(NetworkState newNetworkState) {
        if (getCurrentList() != null) {
            if (getCurrentList().size() != 0) {
                NetworkState previousState = this.networkState;
                boolean hadExtraRow = hasExtraRow();
                this.networkState = newNetworkState;
                boolean hasExtraRow = hasExtraRow();
                if (hadExtraRow != hasExtraRow) {
                    if (hadExtraRow) {
                        notifyItemRemoved(super.getItemCount());
                    } else {
                        notifyItemInserted(super.getItemCount());
                    }
                } else if (hasExtraRow && previousState != newNetworkState) {
                    notifyItemChanged(getItemCount() - 1);
                }
            }
        }
    }

    private static DiffUtil.ItemCallback<HistoryFroimDb> Ride_listDiffCallback = new DiffUtil.ItemCallback<HistoryFroimDb>() {
        @Override
        public boolean areItemsTheSame(@NonNull HistoryFroimDb oldItem, @NonNull HistoryFroimDb newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @TargetApi(Build.VERSION_CODES.KITKAT)
        @Override
        public boolean areContentsTheSame(@NonNull HistoryFroimDb oldItem, @NonNull HistoryFroimDb newItem) {
            return Objects.equals(oldItem, newItem);
        }
    };

}