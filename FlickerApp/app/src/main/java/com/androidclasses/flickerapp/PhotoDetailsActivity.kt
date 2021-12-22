package com.androidclasses.flickerapp

import android.os.Bundle
import com.androidclasses.flickerapp.databinding.ContentPhotoDetailsBinding
import com.squareup.picasso.Picasso

class PhotoDetailsActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_details)

        activateToolbar(true)

//        val photo = intent.getSerializableExtra(PHOTO_TRANSFER) as Photo
        val photo = intent.extras?.getParcelable<Photo>(PHOTO_TRANSFER)

        val photoDetails = ContentPhotoDetailsBinding.inflate(layoutInflater)
//        pdits.photoTitle.text = "Title: " + photo?.title
        photoDetails.photoTitle.text = resources.getString(R.string.photo_title_text, photo?.title)
//        pdits.photoTags.text = "Tags: " + photo?.tags
        photoDetails.photoTags.text = resources.getString(R.string.photo_tags_text, photo?.tags)
        photoDetails.photoAuthor.text = photo?.author
//        pdits.photoAuthor.text = resources.getString(R.string.photo_author_text, "my", "red", "car")

        Picasso.Builder(this)
            .build()
            .load(photo?.link)
            .error(R.drawable.placeholder_image)
            .into(photoDetails.photoImage)
    }
}