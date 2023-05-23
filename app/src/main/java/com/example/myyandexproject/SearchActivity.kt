package com.example.myyandexproject

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myyandexproject.repository.Track
import com.example.myyandexproject.repository.TrackResponse
import com.example.myyandexproject.retrofit_services.ItunesApi
import com.example.myyandexproject.retrofit_services.RetrofitItunesClient
import com.example.myyandexproject.track_recycle.TrackAdapter
import com.google.android.material.button.MaterialButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchActivity : AppCompatActivity() {

    private var searchText : String = ""
    private lateinit var inputSearch : EditText
    private lateinit var btnBack : TextView
    private lateinit var trackRecycle : RecyclerView
    private lateinit var clearIcon : ImageView
    private lateinit var notFoundTracksView : LinearLayout
    private lateinit var errorRequestView : LinearLayout
    private lateinit var refreshBtn : MaterialButton

    private val retrofitClient = RetrofitItunesClient.getClient()
    private val itunesService = retrofitClient.create(ItunesApi::class.java)

    private val tracks = ArrayList<Track>()
    private val trackAdapter = TrackAdapter(tracks)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        btnBack = findViewById(R.id.btnBack)
        trackRecycle = findViewById(R.id.track_list)
        clearIcon = findViewById(R.id.clearIcon)
        notFoundTracksView = findViewById(R.id.not_found_container)
        errorRequestView = findViewById(R.id.error_request_container)
        refreshBtn = findViewById(R.id.refresh_btn)

        trackRecycle.layoutManager = LinearLayoutManager(this)
        trackRecycle.adapter = trackAdapter


        inputSearch = findViewById(R.id.searchInput)

        if (savedInstanceState != null) {
            val savedText = savedInstanceState.getString(EDIT_TEXT_VAL,"")
            inputSearch.setText(savedText)
        }


        btnBack.setOnClickListener {
            super.onBackPressed()
        }

        clearIcon.setOnClickListener {
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            if (currentFocus != null) {
                inputMethodManager.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
            }
            inputSearch.setText("")
            tracks.clear()
        }

        val searchTextInputWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                // Empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()) {
                    inputSearch.hint = getString(R.string.search)
                    searchText = ""
                } else {
                    inputSearch.hint = ""
                    searchText = s.toString()
                    makeRequest(searchText)
                }
                clearIcon.visibility = clearIconIsVisible(s)
            }

            override fun afterTextChanged(p0: Editable?) {
                // Empty
            }
        }

        inputSearch.addTextChangedListener(searchTextInputWatcher)

        refreshBtn.setOnClickListener {
            makeRequest(searchText)
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(EDIT_TEXT_VAL, searchText)
    }

    private fun makeRequest(s : String){
        itunesService.searchSong(s).enqueue(object  : Callback<TrackResponse>{
            override fun onResponse(
                call: Call<TrackResponse>,
                response: Response<TrackResponse>
            ) {
                if(response.code() == 200){
                    tracks.clear()
                    if (response.body()?.results?.isNotEmpty() == true) {
                        tracks.addAll(response.body()?.results!!)
                        trackAdapter.notifyDataSetChanged()
                        notFoundTracksView.visibility = View.GONE
                        errorRequestView.visibility = View.GONE
                        trackRecycle.visibility = View.VISIBLE
                    }
                    else if (tracks.isEmpty()){
                        trackRecycle.visibility = View.GONE
                        errorRequestView.visibility = View.GONE
                        notFoundTracksView.visibility = View.VISIBLE
                    }
                }
            }

            override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                trackRecycle.visibility = View.GONE
                notFoundTracksView.visibility = View.GONE
                errorRequestView.visibility = View.VISIBLE
            }

        })
    }

    private fun clearIconIsVisible(s : CharSequence?) : Int {
        return if(s.isNullOrEmpty()){
            View.GONE
        }
        else{
            View.VISIBLE
        }
    }

    companion object {
        const val EDIT_TEXT_VAL = "EDIT_TEXT_VAL"
    }


}