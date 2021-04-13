package app.melon.data.di

import android.content.Context
import androidx.room.Room
import app.melon.data.MelonDatabase
import app.melon.data.MelonRoomDatabase
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(
    includes = [
        RoomDatabaseModule::class,
        DatabaseDaoModule::class,
        DatabaseModuleBinds::class
    ]
)
class DatabaseModule

@Module
class RoomDatabaseModule {
    @Singleton
    @Provides
    fun provideDatabase(context: Context): MelonRoomDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            MelonRoomDatabase::class.java, "Melon.db"
        ).fallbackToDestructiveMigration().build()
    }
}

@Module
class DatabaseDaoModule {
    @Provides
    fun provideFeedDao(db: MelonDatabase) = db.feedDao()

    @Provides
    fun provideRecommendDao(db: MelonDatabase) = db.recommendedDao()

    @Provides
    fun provideFollowingDao(db: MelonDatabase) = db.followingDao()

    @Provides
    fun provideSchoolFeedsDao(db: MelonDatabase) = db.ANSchoolDao()

    @Provides
    fun provideExploreFeedsDao(db: MelonDatabase) = db.ANExploreDao()

    @Provides
    fun provideTrendingFeedsDao(db: MelonDatabase) = db.ANTrendingDao()

    @Provides
    fun provideEventDao(db: MelonDatabase) = db.eventDao()

    @Provides
    fun provideOrganisedEventDao(db: MelonDatabase) = db.organisedEventDao()
}

@Module
abstract class DatabaseModuleBinds {
    @Binds
    abstract fun bindDatabase(database: MelonRoomDatabase): MelonDatabase
}