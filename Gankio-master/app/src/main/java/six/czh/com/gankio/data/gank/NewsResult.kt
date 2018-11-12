package six.czh.com.gankio.data.gank

data class NewsResult(
        val _id: String,
        val createdAt: String,
        val desc: String,
        val images: List<String>,
        val publishedAt: String,
        val source: String,
        val type: String,
        val url: String,
        val used: Boolean,
        val who: String
)