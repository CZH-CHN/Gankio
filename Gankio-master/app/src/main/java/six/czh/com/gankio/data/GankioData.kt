package six.czh.com.gankio.data

import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Relation
import android.media.Image

data class GankioData( @Embedded   //请看之前的文章,这是指把User中所有元素带入到这里作为元素存在
                       val gankResult: GankResult,
                       //@Relation
                       @Relation(parentColumn = "_id",entityColumn = "gankId",entity = Image::class)
                       var images: List<GankImage>)

