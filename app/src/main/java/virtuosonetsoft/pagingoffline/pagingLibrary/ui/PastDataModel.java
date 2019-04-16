package virtuosonetsoft.pagingoffline.pagingLibrary.ui;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.arch.paging.DataSource;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;
import android.content.Context;
import android.support.annotation.NonNull;

import java.util.concurrent.Executors;

import io.reactivex.disposables.CompositeDisposable;
import virtuosonetsoft.pagingoffline.EquipmentHistory.Cons;
import virtuosonetsoft.pagingoffline.authkey.ApplicationMain;

import virtuosonetsoft.pagingoffline.dbdatabase.GithubLocalCache;
import virtuosonetsoft.pagingoffline.dbdatabase.HistoryFroimDb;
import virtuosonetsoft.pagingoffline.dbdatabase.RepoDatabase;
import virtuosonetsoft.pagingoffline.pagingLibrary.data.NetworkState;

import virtuosonetsoft.pagingoffline.pagingLibrary.data.datasource.PastBookingDataSource;
import virtuosonetsoft.pagingoffline.pagingLibrary.data.datasource.PastBookingDataSourceFactory;



public class PastDataModel extends ViewModel {
    public LiveData<PagedList<HistoryFroimDb>> userList;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private static final int pageSize = 15;
    DataSource.Factory<Integer, HistoryFroimDb> reposByName;
    private PastBookingDataSourceFactory usersDataSourceFactory;
    @NonNull
    private static GithubLocalCache provideCache(Context context) {
        RepoDatabase repoDatabase = RepoDatabase.getInstance(context);
        return new GithubLocalCache(repoDatabase.reposDao(), Executors.newSingleThreadExecutor());
    }
    public PastDataModel() {
        usersDataSourceFactory = new PastBookingDataSourceFactory(compositeDisposable, Cons.adapterNoValue,Cons.context);
        PagedList.Config config = new PagedList.Config.Builder()
                .setPageSize(pageSize)
                .setInitialLoadSizeHint(pageSize * 2)
                .setEnablePlaceholders(false)
                .build();

       if(Cons.datastored.equalsIgnoreCase("notsave"))
       {
           ApplicationMain.getInstance().createLog("notsave");
           userList = new LivePagedListBuilder<>(usersDataSourceFactory, config).build();

       }
       else
       {
           ApplicationMain.getInstance().createLog("save");
           reposByName = provideCache(Cons.context).reposByName();
           userList = new LivePagedListBuilder<>(reposByName, config).build();

       }
    }

    public void retry() {
        usersDataSourceFactory.getRide_listsDataSourceLiveData().getValue().retry();
    }

    public void refresh() {
        try {
            usersDataSourceFactory.getRide_listsDataSourceLiveData().getValue().invalidate();
        } catch (Exception e) {
        }
    }

    public LiveData<NetworkState> getNetworkState() {
        return Transformations.switchMap(usersDataSourceFactory.getRide_listsDataSourceLiveData(), PastBookingDataSource::getNetworkState);
    }

    public LiveData<NetworkState> getRefreshState() {
        return Transformations.switchMap(usersDataSourceFactory.getRide_listsDataSourceLiveData(), PastBookingDataSource::getInitialLoad);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }
}
