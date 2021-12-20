package com.bitrefactor.igniterbiu

import android.app.Application
import android.content.res.Configuration
import android.content.res.Resources
import androidx.room.Room
import com.bitrefactor.igniterbiu.data.localsource.database.IgniterDatabase

class IgniterApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        if (app == null) {
            app = this
        }
        db = Room.databaseBuilder(
            app!!,
            IgniterDatabase::class.java, "igniter_database"
        ).allowMainThreadQueries().build()
    }

    companion object {
         var db: IgniterDatabase? = null
            private set
        var app: IgniterApplication? = null
            private set
    }
}