package br.com.samuelnunes.valorantpassbattle.database.room.dao

import androidx.room.Dao
import androidx.room.Query
import br.com.samuelnunes.valorantpassbattle.model.dto.UserTier
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged

@Dao
interface UserTierDao : BaseDao<UserTier> {
    @Query("SELECT * FROM UserTier")
    fun getAll(): Flow<List<UserTier>>

    @Query("SELECT * FROM UserTier ORDER BY tierCurrent DESC, expCurrent DESC LIMIT 1")
    fun last(): Flow<UserTier?>

    fun getAllDistinctUntilChanged() = getAll().distinctUntilChanged()

    @Query("SELECT * FROM UserTier WHERE id=:id")
    fun getUserTierById(id: Int): Flow<UserTier>

}
