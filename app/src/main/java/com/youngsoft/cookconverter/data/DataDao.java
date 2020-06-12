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

    @Insert
    void insertMultipleRecipeListRecords(RecipeList... recipeLists);

    @Query("SELECT * FROM ConversionFactorsRecord_Table ORDER BY id")
    LiveData<List<ConversionFactorsRecord>> getAllConversionFactorsRecordsSortById();

    @Query("SELECT * FROM IngredientsRecord_Table ORDER BY id")
    LiveData<List<IngredientsRecord>> getAllIngredientsRecordsSortById();

    @Query("SELECT * FROM PanTypeRecord_Table ORDER BY id")
    LiveData<List<PanTypeRecord>> getAllPanTypeRecordsSortById();

    @Query("SELECT * FROM RecipeList_Table ORDER BY id")
    LiveData<List<RecipeList>> getAllRecipeListSortById();

    @Query("SELECT * FROM ConversionFactorsRecord_Table WHERE type = :index")
    LiveData<List<ConversionFactorsRecord>> getSubsetConversionFactors(int index);

    @Query("SELECT * FROM ConversionFactorsRecord_Table WHERE type IN (1,2)")
    LiveData<List<ConversionFactorsRecord>> getAllMassVolumeConversionFactors();

    @Query("SELECT * FROM ConversionFactorsRecord_Table WHERE type = 3")
    LiveData<List<ConversionFactorsRecord>> getAllDistanceConversionFactors();
}
