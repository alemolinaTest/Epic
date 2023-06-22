package com.amolina.epic.data.db

import android.provider.SyncStateContract.Helpers.insert
import androidx.room.*
import com.amolina.epic.data.model.EpicDatesDto
import com.amolina.epic.data.model.EpicImagesDataDto

@Dao interface EpicDao {
  @Query("Select * FROM ${DateEntity.TABLE_NAME} WHERE dateId = :dateId")
  suspend fun getDate(dateId: String): DateEntity

  @Query("SELECT * FROM ${DateEntity.TABLE_NAME}")
  suspend fun getAllDates(): List<DateEntity>

  @Query("Select * FROM ${DateImageEntity.TABLE_NAME} WHERE dateImageId = :dateImageId")
  suspend fun getImage(dateImageId: String): DateImageEntity

  @Query("SELECT * FROM ${DateImageEntity.TABLE_NAME}")
  suspend fun getAllImages(): List<DateImageEntity>

  @Query("Select * FROM ${DateImageEntity.TABLE_NAME} WHERE searchDate = :searchDate")
  suspend fun getImagesForDate(searchDate: String): List<DateImageEntity>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun saveAllDates(datesList: List<DateEntity>)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun saveDate(date: DateEntity)

  @Query("DELETE FROM ${DateEntity.TABLE_NAME}")
  suspend fun deleteAllDates()

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun saveAllImages(imagesList: List<DateImageEntity>)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun saveImage(image: DateImageEntity)

  @Query("DELETE FROM ${DateImageEntity.TABLE_NAME}")
  suspend fun deleteAllImages()

  @Transaction
  @Query("SELECT * FROM Playlist")
  fun getPlaylistWithSongs(): List<Playlist>

  @Transaction
  @Query("SELECT * FROM CourseName")
  fun getSongsWithPlaylists(): List<SongsList>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun saveSong(date: MtmSong)

  @Delete
  suspend fun deleteSong(song: MtmSong)

  @Transaction
  suspend fun insertAndDeleteInTransaction(newSong: MtmSong, oldSong: MtmSong) {
    // Anything inside this method runs in a single transaction.
    deleteSong(oldSong)
    saveSong(newSong)
  }
}