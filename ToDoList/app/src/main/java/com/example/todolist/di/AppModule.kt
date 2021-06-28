package com.example.todolist.di

import android.app.Application
import androidx.room.Room
import com.example.todolist.data.TaskDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Qualifier
import javax.inject.Singleton

//die db in der gesamten app benutzen
//object statt klasse weil mehr effizient für dagger code
//gibt dagger anweisungen wie man die database erstellt und die operationen
@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    //sagt dagger ist ne anweisung
    //nur eine instanz der db erstellen für die app
    @Provides
    @Singleton
    fun provideDatabase(
        //kontext
        app: Application,
        //aus der taskdatabase klasse
        callback: TaskDatabase.Callback
    )
        //baue datenbank in room - kontext = unsere app / unsere db die wir benutzen wollen mit java under the hood / name der db
        //callback = für dummy text für die app bei start
        = Room.databaseBuilder(app, TaskDatabase::class.java, "task_database").fallbackToDestructiveMigration()
        .addCallback(callback).build()

    //erstellt taksdao objekt - braucht dafür instanz von unserer db
    @Provides
    fun provideTaskDao(db: TaskDatabase) = db.taskDao()

    //eigenen coroutinen scope erstellen damit wir suspend fun operationen benutzen können
    //supervisorjob = damit wenn eine coroutine failt nicht alle anderen auch stoppen
    @ApplicationScope
    @Provides
    @Singleton
    fun provideApplicationScope() = CoroutineScope(SupervisorJob())
}

//damit auch mehrere coroutine scopes parallel funktionieren - für andere dann wsh noch so ne klasse mit anderem namen machen
@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class ApplicationScope






















