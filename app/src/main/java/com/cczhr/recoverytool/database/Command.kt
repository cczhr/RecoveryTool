package com.cczhr.recoverytool.database

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
/**
 * @author cczhr
 * @since  2020/9/6
 * @description https://github.com/cczhr
 */
@Entity
data class Command(
    @PrimaryKey(autoGenerate = true)
    var id: Long,
    var title: String?,
    var commands: String?,
    @Ignore
    var isSelect: Boolean =false
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString(),
        parcel.readString(),
        parcel.readByte() != 0.toByte()
    ) {
    }

    constructor(title: String, commands: String) : this(0L, title, commands, false)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(title)
        parcel.writeString(commands)
        parcel.writeByte(if (isSelect) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Command> {
        override fun createFromParcel(parcel: Parcel): Command {
            return Command(parcel)
        }

        override fun newArray(size: Int): Array<Command?> {
            return arrayOfNulls(size)
        }
    }
}