import com.google.gson.annotations.SerializedName

/*
data class ini berfungsi untuk mengambil data respon dari server seperti error dan message, untuk menandakan sebuah variabel
terhubung dengan data JSON kita gunakan @SerializedName, pastikan juga tipe data sesuai dengan data yang diambil
 */
data class RestaurantResponse(

	@field:SerializedName("restaurant")
	val restaurant: Restaurant,

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String
)

/*
data class ini bertujuan untuk mengambil JSON Object customerReview
 */
data class CustomerReviewsItem(

	@field:SerializedName("date")
	val date: String,

	@field:SerializedName("review")
	val review: String,

	@field:SerializedName("name")
	val name: String
)

/*
data class ini bertujuan untuk mengambil JSON Object restaurant, karena data yang diambil menggunakan array
maka menggunakan "List<CustomerReviewsItem>"
 */
data class Restaurant(

	@field:SerializedName("customerReviews")
	val customerReviews: List<CustomerReviewsItem>,

	@field:SerializedName("pictureId")
	val pictureId: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("rating")
	val rating: Double,

	@field:SerializedName("description")
	val description: String,

	@field:SerializedName("id")
	val id: String
)

data class PostReviewResponse(
	@field:SerializedName("customerReviews")
	val customerReviews: List<CustomerReviewsItem>,

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String

)

