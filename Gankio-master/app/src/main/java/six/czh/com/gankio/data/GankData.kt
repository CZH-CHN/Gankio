package six.czh.com.gankio.data

data class GankData(
        val error: Boolean,
        val results: List<GankResult>
)

data class GankResult(
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