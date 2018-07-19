package com.decimalcorp.aditya.creditmanagementapp.DatabaseHelpers;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;

import java.util.concurrent.Executors;

@Database(entities = {User.class, Transfer.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;
    public abstract DataExchange dataExchange();
    public abstract TransferTableInterface transferTableInterface();

    public static AppDatabase getAppDatabase(final Context context){
        if (INSTANCE == null){
            INSTANCE = Room.databaseBuilder(context,
                    AppDatabase.class,
                    "User_Database")
                    .addCallback(new Callback() {
                        @Override
                        public void onCreate(@NonNull final SupportSQLiteDatabase db) {
                            super.onCreate(db);
                            Executors.newSingleThreadScheduledExecutor().execute(new Runnable() {
                                @Override
                                public void run() {
                                    getAppDatabase(context).dataExchange().insertAll(User.populate());
                                }
                            });
                        }
                    })
                    .build();
        }
        return INSTANCE;
    }

    public static void destroyInstance(){
        INSTANCE = null;
    }
}
