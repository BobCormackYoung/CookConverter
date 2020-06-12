package com.youngsoft.cookconverter.data;

import android.content.Context;
import android.provider.ContactsContract;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.Executors;

@Database(entities = {ConversionFactorsRecord.class, IngredientsRecord.class, PanTypeRecord.class, RecipeList.class}, version = 1, exportSchema = false)
public abstract class DataDatabase extends RoomDatabase {

    private static DataDatabase instance;
    public abstract DataDao dataDao();

    public static synchronized DataDatabase getInstance(Context context) {
        if (instance == null) {
            //Populate database on first creation
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    DataDatabase.class, "dataDatabase")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    //populate the database with required data
    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            Executors.newSingleThreadExecutor().execute(new Runnable() {
                @Override
                public void run() {
                    //populate the database with the initial data stored in the database objects
                    instance.dataDao().insertMultipleConversionFactorRecords(ConversionFactorsRecord.populateConversionFactorRecordData());
                    instance.dataDao().insertMultipleIngredientRecords(IngredientsRecord.populateIngredientsRecordData());
                    instance.dataDao().insertMultiplePanTypeRecords(PanTypeRecord.populatePanTypeRecordData());
                }
            });
        }
    };

}
