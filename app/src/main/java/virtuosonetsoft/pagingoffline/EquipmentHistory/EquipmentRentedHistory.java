package virtuosonetsoft.pagingoffline.EquipmentHistory;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.List;
import java.util.concurrent.Executors;

import butterknife.OnClick;
import virtuosonetsoft.pagingoffline.R;
import virtuosonetsoft.pagingoffline.authkey.ApplicationMain;

import virtuosonetsoft.pagingoffline.dbdatabase.GithubLocalCache;
import virtuosonetsoft.pagingoffline.dbdatabase.HistoryFroimDb;
import virtuosonetsoft.pagingoffline.dbdatabase.RepoDatabase;
import virtuosonetsoft.pagingoffline.interfaceAdapter.AdapterNoValue;
import virtuosonetsoft.pagingoffline.pagingLibrary.adapter.PastBookingAdapter;
import virtuosonetsoft.pagingoffline.pagingLibrary.adapter.RetryCallback;
import virtuosonetsoft.pagingoffline.pagingLibrary.data.NetworkState;
import virtuosonetsoft.pagingoffline.pagingLibrary.data.Status;
import virtuosonetsoft.pagingoffline.pagingLibrary.ui.PastDataModel;

public class EquipmentRentedHistory extends AppCompatActivity implements RetryCallback,AdapterNoValue {
    private PastDataModel pastDataModel;
    RecyclerView pastbookingRecycler;
    private PastBookingAdapter pastbookAdapter;
    Button retryLoadingButton;
    TextView errorMessageTextView,noHistory;
    ProgressBar loadingProgressBar;
    SwipeRefreshLayout pastSwipeRefreshLayout;
    LinearLayout linear;
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equipment_rented_history2);
        retryLoadingButton=(Button)findViewById(R.id.retryLoadingButton);
        errorMessageTextView=(TextView)findViewById(R.id.errorMessageTextView);
        loadingProgressBar=(ProgressBar)findViewById(R.id.loadingProgressBar);
        imageView=(ImageView) findViewById(R.id.imageView1);
        noHistory = (TextView) findViewById(R.id.textView122);
        noHistory.setVisibility(View.GONE);
        linear=(LinearLayout)findViewById(R.id.linear);

        linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ApplicationMain.getInstance().getstoreSharedpref().storeSharedValue(Cons.booking_steps, "0");
                ApplicationMain.getInstance().getstoreSharedpref().storeSharedValue(Cons.ismessageReceived,"false");
                finish();
               // BackActivityWithFinish(DashboardActivityChanges.class);
               }
        });
        pastbookingRecycler=(RecyclerView)findViewById(R.id.recenthistoryRecycler);
        pastSwipeRefreshLayout=(SwipeRefreshLayout)findViewById(R.id.usersSwipeRefreshLayout);
        Cons.adapterNoValue = this;
        Cons.context=getApplicationContext();
        new LongOperation().execute();

}
private class LongOperation extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            List<HistoryFroimDb> integerHistoryFroimDbFactory = provideCache(Cons.context).loaddata();
            ApplicationMain.getInstance().createLog("integerHistoryFroimDbFactory" + integerHistoryFroimDbFactory.size());
            try {
                if (integerHistoryFroimDbFactory.size() <= 0) {
                    ApplicationMain.getInstance().createLog("if integerHistoryFroimDbFactory" + integerHistoryFroimDbFactory.size());
                    Cons.datastored = "notsave";

                } else {
                    ApplicationMain.getInstance().createLog("elseif integerHistoryFroimDbFactory" + integerHistoryFroimDbFactory.size());
                    Cons.datastored = "save";
                }
            } catch (NullPointerException e) {
                ApplicationMain.getInstance().createLog("null if integerHistoryFroimDbFactory" + integerHistoryFroimDbFactory.size());
                Cons.datastored = "notsave";
            }
            return "Executed";
        }
        @Override
        protected void onPostExecute(String result) {
            pastDataModel = ViewModelProviders.of((FragmentActivity) EquipmentRentedHistory.this).get(PastDataModel.class);
            initAdapter();
            initSwipeToRefresh();
        }
    }
    @NonNull
    private static GithubLocalCache provideCache(Context context) {
        RepoDatabase repoDatabase = RepoDatabase.getInstance(context);
        return new GithubLocalCache(repoDatabase.reposDao(), Executors.newSingleThreadExecutor());
    }
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(ApplicationMain.onAttach(base));
    }
    private void initAdapter() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(EquipmentRentedHistory.this,
                LinearLayoutManager.VERTICAL, false);
        pastbookAdapter = new PastBookingAdapter(this);
        pastbookingRecycler.setLayoutManager(linearLayoutManager);
        pastbookingRecycler.setAdapter(pastbookAdapter);
        try {
            pastDataModel.userList.observe(this, pastbookAdapter::submitList);
            }
        catch (NullPointerException e)
        {

        }
        pastDataModel.getNetworkState().observe(this, pastbookAdapter::setNetworkState);
    }
    private void initSwipeToRefresh() {
        pastDataModel.getRefreshState().observe(this, networkState -> {
            if (networkState != null) {
                if (pastbookAdapter.getCurrentList() != null) {
                    if (pastbookAdapter.getCurrentList().size() > 0) {
                        pastSwipeRefreshLayout.setRefreshing(
                                networkState.getStatus() == NetworkState.LOADING.getStatus());
                    } else {
                        setInitialLoadingState(networkState);
                    }
                } else {
                    setInitialLoadingState(networkState);
                }
            }
        });
        pastSwipeRefreshLayout.setOnRefreshListener(() -> pastDataModel.refresh());
    }

    /**
     * Show the current network state for the first load when the user list
     * in the adapter is empty and disable swipe to scroll at the first loading
     *
     * @param networkState the new network state
     */
    private void setInitialLoadingState(NetworkState networkState) {
        //error message
        errorMessageTextView.setVisibility(View.GONE);
        if (networkState.getMessage() != null) {
            errorMessageTextView.setText(networkState.getMessage());
        }

        //loading and retry
        retryLoadingButton.setVisibility(View.GONE);
        loadingProgressBar.setVisibility(networkState.getStatus() == Status.RUNNING ? View.VISIBLE : View.GONE);

        pastSwipeRefreshLayout.setEnabled(networkState.getStatus() == Status.SUCCESS);
    }

    @OnClick(R.id.retryLoadingButton)
    void retryInitialLoading() {
        pastDataModel.retry();
    }

    @Override
    public void retry() {
        pastDataModel.retry();
    }
    @Override
    public void onBackPressed() {
        ApplicationMain.getInstance().getstoreSharedpref().storeSharedValue(Cons.booking_steps, "0");
        super.onBackPressed();
        ApplicationMain.getInstance().getstoreSharedpref().storeSharedValue(Cons.messageReceived,"false");
        ApplicationMain.getInstance().getstoreSharedpref().storeSharedValue(Cons.ismessageReceived,"false");

     //finish();
    }
    public void BackActivityWithFinish(Class cls) {

        Intent i = new Intent(getApplicationContext(), cls);
        finish();
        startActivity(i);
        overridePendingTransition(R.anim.slide_out, R.anim.slide_in);
    }

    @Override
    public void onMethodNoValueCallback(String v) {
        ApplicationMain.getInstance().createLog("All Adapter Callback from Fragment");
        if (v.equalsIgnoreCase("no")) {
            noHistory.setVisibility(View.VISIBLE);
            pastbookingRecycler.setVisibility(View.GONE);


            // initSwipeToRefresh();
            //getFragmentManager().beginTransaction().detach(this).attach(this).commit();
            // ApplicationMain.getInstance().createLog("Adapter Callback" + v);
        } else if (v.equalsIgnoreCase("yes")) {
            noHistory.setVisibility(View.GONE);
            pastbookingRecycler.setVisibility(View.VISIBLE);


            // initSwipeToRefresh();
            ApplicationMain.getInstance().createLog("All Adapter Callback" + v);
        }
    }
}
