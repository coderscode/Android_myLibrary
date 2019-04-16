package virtuosonetsoft.pagingoffline.dbdatabase;

import android.arch.paging.DataSource;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;


import java.util.List;


/**
 * Room data access object for accessing the {@link HistoryFroimDb} table.
 *
 * @author Kaushik N Sanji
 */
@Dao
public interface RepoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<HistoryFroimDb> posts);

    // Do a similar query as the search API:
    // Look for repos that contain the query string in the name or in the description
    // and order those results descending, by the number of stars and then by name ascending
    @Query("SELECT * FROM history")
//            WHERE (name LIKE :queryString) OR (description LIKE " +
//            ":queryString) ORDER BY stars DESC, name ASC"
    DataSource.Factory<Integer, HistoryFroimDb> reposByName();

    @Query("SELECT * FROM history")
    public List<HistoryFroimDb> findUserWithName();
}
