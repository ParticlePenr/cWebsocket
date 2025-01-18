package br.com.battlepassCalculatorValorant.di

import android.content.Context
import androidx.room.Room
import br.com.battlepassCalculatorValorant.database.room.AppDB
import br.com.battlepassCalculatorValorant.model.battlePass.BattlePassFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun database(@ApplicationContext context: Context): AppDB {
        return Room.databaseBuilder(context, AppDB::class.java, "valorantCalculator.db")
            .enableMultiInstanceInvalidation()
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun battlePass(@ApplicationContext context: Context) =
        BattlePassFactory(context).getBattlePass()

}