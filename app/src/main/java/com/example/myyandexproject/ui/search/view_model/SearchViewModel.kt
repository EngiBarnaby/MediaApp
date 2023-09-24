package com.example.myyandexproject.ui.search.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myyandexproject.data.dto.Resource
import com.example.myyandexproject.domain.search.models.Track
import com.example.myyandexproject.domain.search.api.SearchInteractor
import com.example.myyandexproject.domain.search.api.TrackState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SearchViewModel(
    private val searchInteractor: SearchInteractor,
) : ViewModel() {


    private var historyTracks = MutableLiveData<ArrayList<Track>>()

    init {
        historyTracks.value = fetchHistoryTracks()
    }


    private var textInput = MutableLiveData<String>("")

    private val stateLiveData = MutableLiveData<TrackState>()
    fun getTrackState(): LiveData<TrackState> = stateLiveData

    private fun renderState(state: TrackState) {
        stateLiveData.value = state
    }

    fun getHistoryTracks(): LiveData<ArrayList<Track>> = historyTracks
    fun getTextInput() : LiveData<String> = textInput

    fun makeRequest(){

        renderState(TrackState.Loading)

        viewModelScope.launch(Dispatchers.IO) {
            val response = searchInteractor.getSongs(getInputText())
            viewModelScope.launch(Dispatchers.Main) {
                when(response){
                    is Resource.Success -> {
                        if(response.data != null){
                            if(response.data.isEmpty()){
                                renderState(TrackState.Empty("Список пуст"))
                            }
                            else{
                                renderState(TrackState.Content(response.data))
                            }
                        }
                    }
                    is Resource.Error -> {
                        renderState(TrackState.Error("Ошибка сервера"))
                    }
                }
            }
        }
    }

    fun setInputText(text : String){
        searchInteractor.setSearchText(EDIT_TEXT_VAL, text)
    }

    private fun getInputText() : String{
        return searchInteractor.getSearchText(EDIT_TEXT_VAL)
    }

    fun clearHistory(){
        searchInteractor.setHistoryTracksToShared(MUSIC_HISTORY, "[]")
        historyTracks.value?.clear()
    }

    fun setHistoryTracks(track : Track){
        historyTracks.postValue(historyTracks.value?.apply {
            if (contains(track)) {
                remove(track)
            }
            add(0, track)
            if (size > 10) {
                removeLast()
            }
            val json = Track.createJsonFromTracksList(historyTracks.value!!)
            searchInteractor.setHistoryTracksToShared(MUSIC_HISTORY, json)
        })
    }

    private fun fetchHistoryTracks(): ArrayList<Track> {
        val json = searchInteractor.getHistoryTracksFromShared(MUSIC_HISTORY)
        return if (!json.isNullOrEmpty()) {
            Track.createTracksListFromJson(json)
        } else {
            ArrayList<Track>()
        }
    }

    companion object {
        private const val EDIT_TEXT_VAL = "EDIT_TEXT_VAL"
        private const val MUSIC_HISTORY = "MUSIC_HISTORY"
    }

}