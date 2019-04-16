package virtuosonetsoft.pagingoffline.pagingLibrary.data.datasource;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.ItemKeyedDataSource;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.util.Log;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;
import virtuosonetsoft.pagingoffline.EquipmentHistory.Cons;
import virtuosonetsoft.pagingoffline.authkey.ApplicationMain;

import virtuosonetsoft.pagingoffline.dbdatabase.GithubLocalCache;
import virtuosonetsoft.pagingoffline.dbdatabase.HistoryFroimDb;
import virtuosonetsoft.pagingoffline.interfaceAdapter.AdapterNoValue;
import virtuosonetsoft.pagingoffline.pagingLibrary.api.GithubService;
import virtuosonetsoft.pagingoffline.pagingLibrary.data.NetworkState;



public class PastBookingDataSource extends ItemKeyedDataSource<Long, HistoryFroimDb> {
    private GithubService githubService;
    private CompositeDisposable compositeDisposable;
    private MutableLiveData<NetworkState> networkState = new MutableLiveData<>();
    private MutableLiveData<NetworkState> initialLoad = new MutableLiveData<>();
    private GithubLocalCache localCache;
    /**
     * Keep Completable reference for the retry event
     */
    AdapterNoValue adapterNoValue;
    private Completable retryCompletable;

    PastBookingDataSource(CompositeDisposable compositeDisposable, AdapterNoValue adapterNoValue,GithubLocalCache localCache) {
        this.githubService = GithubService.getService();
        this.compositeDisposable = compositeDisposable;
        this.adapterNoValue = adapterNoValue;
        this.localCache=localCache;
    }

    public void retry() {
        if (retryCompletable != null) {
            compositeDisposable.add(retryCompletable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(() -> {
                    }, throwable -> Timber.e(throwable.getMessage())));
        }
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Long> params, @NonNull LoadInitialCallback<HistoryFroimDb> callback) {
        // update network states.
        // we also provide an initial load state to the listeners so that the UI can know when the
        // very first list is loaded.
        networkState.postValue(NetworkState.LOADING);
        initialLoad.postValue(NetworkState.LOADING);
        // createLog("loadInitial params.key"+params.key);
        createLog("loadInitial params.requestedLoadSize" + params.requestedLoadSize);
        String auth = "0N0UV0e7O6T9RJ94";
        createLog(" loadInitial auth" + auth);
        createLog(" loadInitial keyid" + String.valueOf(0));
        //get the initial users from the api
        compositeDisposable.add(githubService.showPastBookingApi(auth, String.valueOf(0), String.valueOf(params.requestedLoadSize)).subscribe(earningModalClassList -> {
                    // clear retry since last request succeeded
                    setRetry(null);
                    networkState.postValue(NetworkState.LOADED);
                    initialLoad.postValue(NetworkState.LOADED);
                    // Constants.earningModalClasses.addAll(earningModalClassList);
                    localCache.insert(earningModalClassList, () -> {
                        //Updating the last requested page number when the request was successful
                        //and the results were inserted successfully
                        String.valueOf(0);
                        //Marking the request progress as completed

                    });
                    callback.onResult(earningModalClassList);
                    try {


                        if (earningModalClassList.size() <= 0) {
                            ApplicationMain.getInstance().createLog("No Value in List");
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    ApplicationMain.getInstance().getstoreSharedpref().storeSharedValue(Cons.valueinlist, "true");
                                    adapterNoValue.onMethodNoValueCallback("no");
                                }
                            });
                        } else {
                            ApplicationMain.getInstance().createLog("Value in List");
                            ApplicationMain.getInstance().getstoreSharedpref().storeSharedValue(Cons.valueinlist, "false");
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    ApplicationMain.getInstance().getstoreSharedpref().storeSharedValue(Cons.valueinlist, "true");
                                    adapterNoValue.onMethodNoValueCallback("yes");
                                }
                            });

                        }
                    } catch (Exception e) {
                        ApplicationMain.getInstance().createLog("Null Exception");
                    }
                },
                throwable -> {
                    // keep a Completable for future retry
                    setRetry(() -> loadInitial(params, callback));
                    NetworkState error = NetworkState.error(throwable.getMessage());
                    // publish the error
                    networkState.postValue(error);
                    initialLoad.postValue(error);
                }));

    }

    public void createLog(String s) {
        Log.i("Pagingwithkey", s.toString());
    }

    @Override
    public void loadAfter(@NonNull LoadParams<Long> params, @NonNull LoadCallback<HistoryFroimDb> callback) {
        // set network value to loading.
        networkState.postValue(NetworkState.LOADING);
        createLog("loadAfter params.key" + params.key);
        createLog("loadAfter params.requestedLoadSize" + params.requestedLoadSize);
        String auth ="0N0UV0e7O6T9RJ94";

        createLog(" loadInitial keyid" + String.valueOf(params.key));
        //get the users from the api after id
        compositeDisposable.add(githubService.showPastBookingApi(auth, String.valueOf(params.key), String.valueOf(params.requestedLoadSize)).subscribe(earningModalClassList -> {
                    // clear retry since last request succeeded
                    setRetry(null);
                    networkState.postValue(NetworkState.LOADED);
                    localCache.insert(earningModalClassList, () -> {
                        //Updating the last requested page number when the request was successful
                        //and the results were inserted successfully
                        String.valueOf(0);
                        //Marking the request progress as completed

                    });
                    callback.onResult(earningModalClassList);
                },
                throwable -> {
                    // keep a Completable for future retry
                    setRetry(() -> loadAfter(params, callback));
                    // publish the error
                    networkState.postValue(NetworkState.error(throwable.getMessage()));
                }));
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Long> params, @NonNull LoadCallback<HistoryFroimDb> callback) {
        // ignored, since we only ever append to our initial load
    }

    /**
     * The id field is a unique identifier for users.
     */
    @NonNull
    @Override
    public Long getKey(@NonNull HistoryFroimDb item) {
        // networkState.postValue(NetworkState.RUNNING);
        createLog("getKey" + item.getId());
        return Long.valueOf(item.getId());
    }

    @NonNull
    public MutableLiveData<NetworkState> getNetworkState() {
        return networkState;
    }

    @NonNull
    public MutableLiveData<NetworkState> getInitialLoad() {
        return initialLoad;
    }

    private void setRetry(final Action action) {
        if (action == null) {
            this.retryCompletable = null;
        } else {
            this.retryCompletable = Completable.fromAction(action);
        }
    }

}
