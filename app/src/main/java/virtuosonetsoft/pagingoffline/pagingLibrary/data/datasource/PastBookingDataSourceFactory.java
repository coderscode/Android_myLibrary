package virtuosonetsoft.pagingoffline.pagingLibrary.data.datasource;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.DataSource;
import android.content.Context;
import android.support.annotation.NonNull;

import java.util.concurrent.Executors;

import io.reactivex.disposables.CompositeDisposable;
import virtuosonetsoft.pagingoffline.dbdatabase.GithubLocalCache;
import virtuosonetsoft.pagingoffline.dbdatabase.HistoryFroimDb;
import virtuosonetsoft.pagingoffline.dbdatabase.RepoDatabase;
import virtuosonetsoft.pagingoffline.interfaceAdapter.AdapterNoValue;



public class PastBookingDataSourceFactory  implements DataSource.Factory<Long, HistoryFroimDb> {

    private CompositeDisposable compositeDisposable;
    AdapterNoValue adapterNoValue;
    Context context;
    private MutableLiveData<PastBookingDataSource> usersDataSourceLiveData = new MutableLiveData<>();

    public PastBookingDataSourceFactory(CompositeDisposable compositeDisposable, AdapterNoValue adapterNoValue,Context d) {
        this.compositeDisposable = compositeDisposable;
        this.adapterNoValue=adapterNoValue;
        this.context=d;
    }
    @NonNull
    private static GithubLocalCache provideCache(Context context) {
        RepoDatabase repoDatabase = RepoDatabase.getInstance(context);
        return new GithubLocalCache(repoDatabase.reposDao(), Executors.newSingleThreadExecutor());
    }

    @Override
    public DataSource<Long, HistoryFroimDb> create() {
        PastBookingDataSource usersDataSource = new PastBookingDataSource(compositeDisposable,adapterNoValue, provideCache(context));
        usersDataSourceLiveData.postValue(usersDataSource);
        return usersDataSource;
    }

    @NonNull
    public MutableLiveData<PastBookingDataSource> getRide_listsDataSourceLiveData() {
        return usersDataSourceLiveData;
    }

}
