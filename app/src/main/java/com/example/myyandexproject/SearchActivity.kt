package com.example.myyandexproject

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import com.example.myyandexproject.ui.player.AudioPlayer
import com.example.myyandexproject.domain.models.Track
import com.example.myyandexproject.data.dto.TrackResponse
import com.example.myyandexproject.data.network.ItunesApi
import com.example.myyandexproject.data.network.RetrofitItunesClient
import com.example.myyandexproject.track_recycle.TrackAdapter
import com.example.myyandexproject.track_recycle.TrackClick
import com.google.android.material.button.MaterialButton
import com.google.android.material.progressindicator.CircularProgressIndicator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

const val SHARED_PREFERENCES_KEY = "music_app_shared_preferences_key"
const val MUSIC_HISTORY = "music_history"
const val CHOSEN_TRACK = "chosen_track"

class SearchActivity : AppCompatActivity() {

    companion object {
        private const val EDIT_TEXT_VAL = "EDIT_TEXT_VAL"
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        private const val INPUT_DEBOUNCE_DELAY = 1500L

        private const val TRACK_ID_KEY = "track_id_key"

        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
    }

    private var searchText : String = ""
    private lateinit var inputSearch : EditText
    private lateinit var btnBack : TextView
    private lateinit var trackRecycle : RecyclerView
    private lateinit var historyTrackRecycle : RecyclerView
    private lateinit var clearIcon : ImageView
    private lateinit var notFoundTracksView : LinearLayout
    private lateinit var errorRequestView : LinearLayout
    private lateinit var refreshBtn : MaterialButton
    private lateinit var tracksHistory : LinearLayout
    private lateinit var clearHistoryBtn : MaterialButton
    private lateinit var emptyHistoryTitle : LinearLayout
    private lateinit var progressBar : CircularProgressIndicator

    private val retrofitClient = RetrofitItunesClient.getClient()
    private val itunesService = retrofitClient.create(ItunesApi::class.java)

    private val tracks = ArrayList<Track>()
    private val trackAdapter = TrackAdapter(tracks)

    private var historyTracks = ArrayList<Track>()
    private val historyTrackAdapter = TrackAdapter(historyTracks)

    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())
    private val searchRunnable = Runnable { makeRequest() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        btnBack = findViewById(R.id.btnBack)
        clearIcon = findViewById(R.id.clearIcon)
        notFoundTracksView = findViewById(R.id.not_found_container)
        errorRequestView = findViewById(R.id.error_request_container)
        refreshBtn = findViewById(R.id.refresh_btn)
        tracksHistory = findViewById(R.id.songs_history)
        clearHistoryBtn = findViewById(R.id.clear_history_btn)
        emptyHistoryTitle = findViewById(R.id.empty_history_title)
        progressBar = findViewById(R.id.progressBar)

        trackRecycle = findViewById(R.id.track_list)
        historyTrackRecycle = findViewById(R.id.history_tracks)


        val sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_KEY, MODE_PRIVATE)

        val historyTracksFromPrefs = sharedPreferences.getString(MUSIC_HISTORY, null)

        if(historyTracksFromPrefs != null){
            val tracks = Track.createTracksListFromJson(historyTracksFromPrefs).toCollection(ArrayList())
            historyTracks.addAll(tracks)
        }

        historyTrackAdapter.setTrackClickListener( object : TrackClick {
            override fun onClick(track: Track) {
                startMediaActivity(track.trackId)
            }
        })

        trackAdapter.setTrackClickListener(object : TrackClick {
            override fun onClick(track: Track) {
                if(historyTracks.contains(track)){
                    historyTracks.remove(track)
                }
                historyTracks.add(0, track)
                if(historyTracks.size > 10){
                    historyTracks.removeLast()
                }
                sharedPreferences.edit()
                    .putString(MUSIC_HISTORY, Track.createJsonFromTracksList(historyTracks))
                    .apply()
                historyTrackAdapter.notifyDataSetChanged()
                startMediaActivity(track.trackId)
            }
        })

        historyTrackRecycle.layoutManager = LinearLayoutManager(this)
        historyTrackRecycle.adapter = historyTrackAdapter

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

        clearHistoryBtn.setOnClickListener {
            historyTracks.clear()
            historyTrackAdapter.notifyDataSetChanged()
            sharedPreferences.edit()
                .putString(MUSIC_HISTORY, "[]")
                .apply()
            tracksHistory.visibility = View.GONE
            emptyHistoryTitle.visibility = View.VISIBLE
        }

        clearIcon.setOnClickListener {
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            if (currentFocus != null) {
                inputMethodManager.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
            }
            inputSearch.setText("")
            tracks.clear()
        }

        inputSearch.setOnFocusChangeListener {view, isFocus ->
            if(historyTracks.isNotEmpty() && isFocus){
                tracksHistory.visibility = if (inputSearch.text.isEmpty()) View.VISIBLE else View.GONE
                emptyHistoryTitle.visibility = View.GONE
            }
            else{
                tracksHistory.visibility = View.GONE
                emptyHistoryTitle.visibility = if (inputSearch.text.isEmpty()) View.VISIBLE else View.GONE
            }
        }

        val searchTextInputWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                // Empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (inputSearch.hasFocus() && s?.isEmpty() == true){
                    if(historyTracks.isNotEmpty()){
                        tracksHistory.visibility = View.VISIBLE
                        emptyHistoryTitle.visibility = View.GONE
                    }
                    else{
                        tracksHistory.visibility = View.GONE
                        emptyHistoryTitle.visibility = View.VISIBLE
                    }
                }
                else{
                    tracksHistory.visibility = View.GONE
                    emptyHistoryTitle.visibility = View.GONE
                }
                if (s.isNullOrEmpty()) {
                    inputSearch.hint = getString(R.string.search)
                    searchText = ""
                } else {
                    inputSearch.hint = ""
                    searchText = s.toString()
                    searchDebounce()
                }
                clearIcon.visibility = clearIconIsVisible(s)
            }

            override fun afterTextChanged(p0: Editable?) {
                // Empty
            }
        }

        inputSearch.addTextChangedListener(searchTextInputWatcher)

        refreshBtn.setOnClickListener {
            makeRequest()
        }

    }



    private fun startMediaActivity(track_id : Int){
        if (clickDebounce()) {
            val audioPlayerIntent = Intent(this, AudioPlayer::class.java)
            audioPlayerIntent.putExtra(TRACK_ID_KEY, track_id)
            startActivity(audioPlayerIntent)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(EDIT_TEXT_VAL, searchText)
    }

    private fun makeRequest(){
        val s : String = inputSearch.text.toString()
        progressBar.visibility = View.VISIBLE
        trackRecycle.visibility = View.GONE
        itunesService.searchSong(s).enqueue(object  : Callback<TrackResponse>{
            override fun onResponse(
                call: Call<TrackResponse>,
                response: Response<TrackResponse>
            ) {
                if(response.code() == 200){
                    progressBar.visibility = View.GONE
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

    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, INPUT_DEBOUNCE_DELAY)
    }

    private fun clickDebounce() : Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    private fun clearIconIsVisible(s : CharSequence?) : Int {
        return if(s.isNullOrEmpty()){
            View.GONE
        }
        else{
            View.VISIBLE
        }
    }

}
