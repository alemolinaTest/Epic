package com.amolina.epic.data.db

import androidx.room.*
import androidx.room.ForeignKey.Companion.CASCADE
import com.amolina.epic.domain.model.DatesData
import com.amolina.epic.domain.model.ImagesData

@Entity(tableName = DateEntity.TABLE_NAME) data class DateEntity(
  @PrimaryKey(autoGenerate = true) val dateId: Int? = null,
  val date: String,
) {
  companion object {
    const val TABLE_NAME = "date_entity"
  }

  fun toModel(): DatesData {
    return DatesData(date = date,
                     downloaded = true)
  }
}

@Entity(tableName = DateImageEntity.TABLE_NAME) data class DateImageEntity(
  @PrimaryKey(autoGenerate = true) val dateImageId: Int? = null,
  var identifier: String,
  var caption: String?,
  var image: String?,
  var version: String?,
  val date: String?,
  var searchDate: String,
) {
  companion object {
    const val TABLE_NAME = "date_image_entity"
  }

  fun toModel(): ImagesData {
    return ImagesData(identifier = identifier,
                      caption = caption,
                      image = image,
                      version = version,
                      date = date,
                      url = "",
                      downloaded = true)
  }
} // trying relationship between tables One to One

@Entity data class CourseName(
  @PrimaryKey val loginNumber: Long,
  val name: String,
)

@Entity(foreignKeys = [ForeignKey(entity = CourseName::class,
                                  parentColumns = arrayOf("loginNumber"),
                                  childColumns = arrayOf("courseActiveID"),
                                  onDelete = CASCADE)]) data class Course(
  @PrimaryKey val courseId: Long,
  val title: String,
  val courseActiveID: Long,
)

data class CourseNameAndGfgCourse(
  @Embedded val gfgCourseName: CourseName,
  @Relation(parentColumn = "loginNumber",
            entityColumn = "courseActiveID") val course: Course,
) //one to many

@Entity data class OtmCourseName(
  @PrimaryKey val otmCourseNameId: Long,
  val name: String,
  val age: Int,
)

@Entity(foreignKeys = [ForeignKey(entity = OtmCourseName::class,
                                  parentColumns = arrayOf("otmCourseNameId"),
                                  childColumns = arrayOf("otmCourseNameCreatorId"),
                                  onDelete = CASCADE)]) data class OtmPlaylist(
  @PrimaryKey val courseId: Long,
  val otmCourseNameCreatorId: Long,
  val playlistName: String,
)

data class OtmCourseNameWithPlaylists(
  @Embedded val gfgCourseName: OtmCourseName,
  @Relation(parentColumn = "otmCourseNameId",
            entityColumn = "otmCourseNameCreatorId") val playlists: List<OtmPlaylist>,
)

//Many to Many

@Entity data class MtmVideoPlayList(
  @PrimaryKey val id: Long,
  val playListName: String,
)

@Entity data class MtmSong(
  @PrimaryKey val id: Long,
  val songName: String,
  val artist: String,
)

@Entity(primaryKeys = ["playListId", "songId"],
        foreignKeys = [
          ForeignKey(entity = MtmVideoPlayList::class,
                                  parentColumns = arrayOf("id"),
                                  childColumns = arrayOf("playListId")),
          ForeignKey(entity = MtmSong::class,
                     parentColumns = arrayOf("id"),
                     childColumns = arrayOf("songId"))
        ])
data class MtmPlaylistSongCrossRef(
  val playListId: Long,
  val songId: Long,
)

data class SongsList(
  @Embedded val videoPlaylist: MtmVideoPlayList,
  @Relation(
    parentColumn = "id",
    entityColumn = "songId",
    associateBy = Junction(MtmPlaylistSongCrossRef::class)
  )
  val songs: List<MtmSong>,
)

data class Playlist(
  @Embedded val song: MtmSong,
  @Relation(
    parentColumn = "id",
    entityColumn = "playListId",
    associateBy = Junction(MtmPlaylistSongCrossRef::class)
  )
  val videoPlaylists: List<MtmVideoPlayList>,
)



