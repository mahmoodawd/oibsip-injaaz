package dev.awd.injaaz.di

import android.content.Context
import androidx.room.Room
import com.google.firebase.auth.FirebaseAuth
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
object TestAppModule {

    @Singleton
    @Provides
    fun providesFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Singleton
    @Provides
    fun providesAuthClient(@ApplicationContext context: Context): AuthUiClient =
        GoogleAuthUiClient(context)

    @Singleton
    @Provides
    fun providesTasksRepo(db: InjaazDb, firebaseAuth: FirebaseAuth): TasksRepository =
        TasksRepoImpl(db.tasksDao, firebaseAuth)

    @Singleton
    @Provides
    fun providesNotesRepo(db: InjaazDb, firebaseAuth: FirebaseAuth): NotesRepository =
        NotesRepoImpl(db.notesDao, firebaseAuth)


    @Provides
    @Singleton
    fun provideTasksDb(@ApplicationContext context: Context): InjaazDb =
        Room.inMemoryDatabaseBuilder(
            context,
            InjaazDb::class.java,
        ).build()

    @Singleton
    @Provides
    fun provideTasksDao(database: InjaazDb): TasksDao = database.tasksDao

    @Singleton
    @Provides
    fun provideNotesDao(database: InjaazDb): NotesDao = database.notesDao


}


