package com.submission.restaurantreview

import CustomerReviewsItem
import PostReviewResponse
import Restaurant
import RestaurantResponse
import android.content.Context
import android.hardware.input.InputManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.submission.restaurantreview.adapter.ReviewAdapter
import com.submission.restaurantreview.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val mainViewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

//        val mainViewModel = ViewModelProvider(
//            this,
//            ViewModelProvider.NewInstanceFactory()
//        ).get(MainViewModel::class.java)
        mainViewModel.restaurant.observe(this, { restaurant ->
            setRestaurantData(restaurant)
        })

        mainViewModel.listReview.observe(this, { consumerReview ->
            setReviewData(consumerReview)
        })

        mainViewModel.isLoading.observe(this, { showLoading(it) })

        mainViewModel.snackbarText.observe(this, {
            it.getContentIfNothandled()?.let { snackBartext ->
                Snackbar.make(
                    window.decorView.rootView,
                    snackBartext,
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        })

        val layoutManager = LinearLayoutManager(this)
        binding.rvReview.layoutManager = layoutManager

        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvReview.addItemDecoration(itemDecoration)

//        findRestaurant()

        binding.btnSend.setOnClickListener { view ->
            mainViewModel.postReview(binding.edReview.text.toString())
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }


    /*
    private fun postReview(review: String) {
        showLoading(true)
        val client = ApiConfig.getApiService().postReview(RESTAURANT_ID, "Dicoding", review)
        client.enqueue(object : Callback<PostReviewResponse> {
            override fun onResponse(
                call: Call<PostReviewResponse>,
                response: Response<PostReviewResponse>
            ) {
                showLoading(false)
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    setReviewData(responseBody.customerReviews)
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}", )
                }
            }

            override fun onFailure(call: Call<PostReviewResponse>, t: Throwable) {
                showLoading(false)
                Log.e(TAG, "onFailure: ${t.message}", )
            }
        })
    }*/

    /*
    private fun findRestaurant() {
        showLoading(true)
        /*
        disini kita memanggil class ApiConfig yang berisi kofigurasi retrofit
         */
        val client = ApiConfig.getApiService().getRestaurant(RESTAURANT_ID)
        /*
        disini kita menggunakan fungsi enqueue untuk menjalankan request secara asynchronus di background agar tidak terjadi freeze pada aplikasi
        hasilnya terdapat 2 callback, onResponse dan onFailure ketika gagal
        Ketika terdapat respone, bisa saja terjadi kegagalan seperti error 404 atau 500, maka kita mengeceknya melalui response.isSuccessful()
        untuk mengetahui apakah server mengembalikan nilai 200(OK) atau tidak. Untuk datanya bisa kita ambil melalui response.body()
         */
        client.enqueue(object : retrofit2.Callback<RestaurantResponse> {
            override fun onResponse(
                call: Call<RestaurantResponse>,
                response: Response<RestaurantResponse>
            ) {
                showLoading(false)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        setRestaurantData(responseBody.restaurant)
                        setReviewData(responseBody.restaurant.customerReviews)
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<RestaurantResponse>, t: Throwable) {
                showLoading(false)
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
        /*
        disini kita tidak perlu melakukan parsing lagi, karena data yang didapat sudah berupa POJO
        Proses parsing dilakukan secara otomatis oleh Retrofit dengan menggunakan kode .addConverterFactory(GsonConverterFactory.create())
        di class ApiaConfig dengan anotasi SerializedName pada masing-masing POJO
         */
    }*/

    private fun setRestaurantData(restaurant: Restaurant) {
        binding.tvTitle.text = restaurant.name
        binding.tvDescription.text = restaurant.description
        Glide.with(this@MainActivity)
            .load("https://restaurant-api.dicoding.dev/images/large/${restaurant.pictureId}")
            .into(binding.ivPicture)
    }

    private fun setReviewData(consumerReviews: List<CustomerReviewsItem>) {
        val listReview = ArrayList<String>()
        for (review in consumerReviews) {
            listReview.add(
                """
                    ${review.review}
                    - ${review.name}
                """.trimIndent()
            )
        }
        val adapter = ReviewAdapter(listReview)
        binding.rvReview.adapter = adapter
        binding.edReview.setText("")
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    companion object {
        private const val TAG = "MainActivity"
        private const val RESTAURANT_ID = "uewq1zg2zlskfw1e867"
    }
}