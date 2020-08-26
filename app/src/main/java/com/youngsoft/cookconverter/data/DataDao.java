package com.youngsoft.cookconverter.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

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
    long insertSingleRecipeListRecords(RecipeList recipeList);

    @Insert
    void insertRecipeConversionFactorCrossReference(RecipeConversionFactorCrossReference recipeConversionFactorCrossReference);

    @Query("SELECT * FROM ConversionFactorsRecord_Table ORDER BY conversionFactorID")
    LiveData<List<ConversionFactorsRecord>> getAllConversionFactorsRecordsSortById();

    @Query("SELECT * FROM IngredientsRecord_Table ORDER BY id")
    LiveData<List<IngredientsRecord>> getAllIngredientsRecordsSortById();

    @Query("SELECT * FROM RecipeList_Table ORDER BY recipeListID")
    LiveData<List<RecipeList>> getAllRecipeListSortById();

    @Query("SELECT * FROM ConversionFactorsRecord_Table WHERE type = :index")
    LiveData<List<ConversionFactorsRecord>> getSubsetConversionFactors(int index);

    @Query("SELECT * FROM ConversionFactorsRecord_Table WHERE type IN (1,2,4)")
    LiveData<List<ConversionFactorsRecord>> getAllMassVolumeConversionFactors();

    @Query("SELECT * FROM ConversionFactorsRecord_Table WHERE type = 1")
    LiveData<List<ConversionFactorsRecord>> getAllMassConversionFactors();

    @Query("SELECT * FROM ConversionFactorsRecord_Table WHERE type = 2")
    LiveData<List<ConversionFactorsRecord>> getAllVolumeConversionFactors();

    @Query("SELECT * FROM ConversionFactorsRecord_Table WHERE type = 3")
    LiveData<List<ConversionFactorsRecord>> getAllDistanceConversionFactors();

    @Query("DELETE FROM RecipeList_Table")
    void deleteAllRecipeListItems();

    @Query("DELETE FROM RecipeConversionFactorCrossReference")
    void RecipeConversionFactorCrossReference();

    @Query("DELETE FROM RecipeList_Table  WHERE recipeListID = :index")
    void deleteSingleRecipeListItem(long index);

    @Query("DELETE FROM RecipeConversionFactorCrossReference WHERE recipeListID = :index")
    void deleteSingleRecipeConversionFactorCrossReference(long index);

    @Transaction
    @Query("SELECT * FROM RecipeList_Table ORDER BY recipeListID")
    LiveData<List<RecipeWithConversionFactor>> getRecipeWithConversionFactor();
}
