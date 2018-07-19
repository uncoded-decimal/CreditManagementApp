package com.decimalcorp.aditya.creditmanagementapp.DatabaseHelpers;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface TransferTableInterface {

    @Query("SELECT * FROM transfers_table")
    List<Transfer> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Transfer...transfers);
}
