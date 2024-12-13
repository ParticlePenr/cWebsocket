package br.com.battlepassCalculatorValorant.database.room.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserTier(
    val tierCurrent: Int,
    var tierExpMissing: Int,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
) {
    constructor() : this(15, 0, -1)

    override fun toString(): String {
        return "UserTier ${super.toString()}:\n\tId: $id\n\tTierCurrent: $tierCurrent\n\tTierExpMissing: $tierExpMissing"
    }
}

