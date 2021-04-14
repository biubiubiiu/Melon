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
    fun provideFeedEntryDao(db: MelonDatabase) = db.feedEntryDao()

    @Provides
    fun provideUserDao(db: MelonDatabase) = db.userDao()

    @Provides
    fun provideEventDao(db: MelonDatabase) = db.eventDao()

    @Provides
    fun provideEventEntryDao(db: MelonDatabase) = db.eventEntryDao()

    @Provides
    fun provideAttendEventDao(db: MelonDatabase) = db.attendEventDao()

    @Provides
    fun provideGroupDao(db: MelonDatabase) = db.groupDao()

    @Provides
    fun provideGroupEntryDao(db: MelonDatabase) = db.groupEntryDao()

    @Provides
    fun provideJoinGroupDao(db: MelonDatabase) = db.joinGroupDao()
}

@Module
abstract class DatabaseModuleBinds {
    @Binds
    abstract fun bindDatabase(database: MelonRoomDatabase): MelonDatabase
}