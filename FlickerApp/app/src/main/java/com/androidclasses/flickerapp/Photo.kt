package com.androidclasses.flickerapp

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

//import kotlinx.android.parcel.Parcelize

@Parcelize
class Photo(
    var title:String, var author:String, var authorId:String,
    var link:String, var tags:String, var image:String) : Parcelable
{
//    companion object {
//        private const val serialVersionUID = 1L
//    }

    override fun toString(): String {
        return "Photo(title='$title', author='$author', authorId='$authorId" +
                "', link='$link', tags='$tags', image='$image')"
    }

//    @Throws(IOException::class)
//    private fun writeObject(outStream: java.io.ObjectOutputStream) {
//        Log.d("Photo", "writeObject called")
//        outStream.writeUTF(title)
//        outStream.writeUTF(author)
//        outStream.writeUTF(authorId)
//        outStream.writeUTF(link)
//        outStream.writeUTF(tags)
//        outStream.writeUTF(image)
//    }
//
//    @Throws(IOException::class, ClassNotFoundException::class)
//    private fun readObject(inStream : ObjectInputStream) {
//        Log.d("Photo", "readObject called")
//        title = inStream.readUTF()
//        author = inStream.readUTF()
//        authorId = inStream.readUTF()
//        link = inStream.readUTF()
//        tags = inStream.readUTF()
//        image = inStream.readUTF()
//    }
//
//    @Throws(ObjectStreamException::class)
//    private fun readObjectNoData() {
//        Log.d("Photo", "readObjectNoData called")
//    }
}