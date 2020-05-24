package com.youngsoft.cookconverter.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DataDao {

    @Insert
    void insertMultipleConversionFactorRecords(ConversionFactorsRecord... conversionFactorsRecords);

    @Insert
    void insertMultipleIngredientRecords(IngredientsRecord... ingredientsRecords);

    @Insert
    void insertMultiplePanTypeRecords(PanTypeRecord... panTypeRecords);

    @Query("SELECT * FROM ConversionFactorsRecord_Table ORDER BY id")
    LiveData<List<ConversionFactorsRecord>> getAllConversionFactorsRecordsSortById();

    @Query("SELECT * FROM IngredientsRecord_Table ORDER BY id")
    LiveData<List<IngredientsRecord>> getAllIngredientsRecordsSortById();

    @Query("SELECT * FROM PanTypeRecord_Table ORDER BY id")
    LiveData<List<PanTypeRecord>> getAllPanTypeRecordsSortById();
}
