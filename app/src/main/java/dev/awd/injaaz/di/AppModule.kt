package dev.awd.injaaz.di

import android.content.Context
import androidx.room.Room
import com.google.firebase.auth.FirebaseAuth
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.awd.injaaz.data.local.InjaazDb
import dev.awd.injaaz.data.local.dao.NotesDao
import dev.awd.injaaz.data.local.dao.TasksDao
import dev.awd.injaaz.data.repository.NotesRepoImpl
import dev.awd.injaaz.data.repository.TasksRepoImpl
import dev.awd.injaaz.domain.repository.NotesRepository
import dev.awd.injaaz.domain.repository.TasksRepository
import dev.awd.injaaz.presentation.auth.AuthUiClient
import dev.awd.injaaz.presentation.auth.GoogleAuthUiClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface AppModule {
    @Binds
    fun bindsGoogleAuthUiClient(authUiClient: GoogleAuthUiClient): AuthUiClient

    @Binds
    fun bindsTasksRepo(tasksRepo: TasksRepoImpl): TasksRepository

    @Binds
    fun bindsNotesRepo(notesRepoImpl: NotesRepoImpl): NotesRepository

    companion object {
        @Singleton
        @Provides
        fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

        @Singleton
        @Provides
        fun provideTasksDb(@ApplicationContext context: Context): InjaazDb = Room.databaseBuilder(
            context,
            InjaazDb::class.java,
            "injaaz-db"
        ).build()

        @Singleton
        @Provides
        fun provideTasksDao(database: InjaazDb): TasksDao = database.tasksDao

        @Singleton
        @Provides
        fun provideNotesDao(database: InjaazDb): NotesDao = database.notesDao


    }


}