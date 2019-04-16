package virtuosonetsoft.pagingoffline.pagingLibrary.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;



import butterknife.BindView;
import butterknife.ButterKnife;
import virtuosonetsoft.pagingoffline.R;
import virtuosonetsoft.pagingoffline.pagingLibrary.data.NetworkState;
import virtuosonetsoft.pagingoffline.pagingLibrary.data.Status;

/**
 * Created by Ahmed Abd-Elmeged on 2/20/2018.
 */
public class NetworkStateViewHolder extends RecyclerView.ViewHolder {


    @BindView(R.id.errorMessageTextView)
    TextView errorMessageTextView;


    @BindView(R.id.retryLoadingButton)
    Button retryLoadingButton;

    @BindView(R.id.loadingProgressBar)
    ProgressBar loadingProgressBar;

    private NetworkStateViewHolder(View itemView, RetryCallback retryCallback) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        retryLoadingButton.setOnClickListener(v -> retryCallback.retry());
    }

    public void bindTo(NetworkState networkState) {
        //error message
        errorMessageTextView.setVisibility(View.INVISIBLE);
        retryLoadingButton.setVisibility(View.INVISIBLE);
       // errorMessageTextView.setVisibility(networkState.getMessage() != null ? View.VISIBLE : View.GONE);
        if (networkState.getMessage() != null) {
            errorMessageTextView.setText(networkState.getMessage());
        }

        //loading and retry
       // retryLoadingButton.setVisibility(networkState.getStatus() == Status.FAILED ? View.VISIBLE : View.GONE);
        loadingProgressBar.setVisibility(networkState.getStatus() == Status.RUNNING ? View.VISIBLE : View.GONE);

        if (loadingProgressBar.getVisibility() == View.VISIBLE) {
          createLog(loadingProgressBar.getVisibility()+"ifvisible");
        } else {
            createLog(loadingProgressBar.getVisibility()+"notvisible");
        }

    }
    public void createLog(String s) {
        Log.i("Pagingwithkey", s.toString());
    }

    public static NetworkStateViewHolder create(ViewGroup parent, RetryCallback retryCallback) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_network_state, parent, false);
        return new NetworkStateViewHolder(view, retryCallback);
    }

}
