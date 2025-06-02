package com.example.pix.data.flickr.mapper

import com.example.pix.data.flickr.dto.PhotoDto
import com.example.pix.data.room.PictureDbo
import com.example.pix.domain.entity.Picture

// https://www.flickr.com/services/api/misc.urls.html
fun PhotoDto.toEntity(quality: String): Picture = Picture(
    id = id,
    title = title,
    lowQualityUrl = "https://live.staticflickr.com/${server}/${id}_${secret}_${quality}.jpg",
    highQualityUrl = "https://live.staticflickr.com/${server}/${id}_${secret}_b.jpg"
)

fun Picture.toDboEntity(): PictureDbo = PictureDbo(
    id = id,
    title = title,
    lowQualityUrl = lowQualityUrl,
    highQualityUrl = highQualityUrl
)

fun PictureDbo.toEntity(): Picture = Picture(
    id = id,
    title = title,
    lowQualityUrl = lowQualityUrl,
    highQualityUrl = highQualityUrl
)
