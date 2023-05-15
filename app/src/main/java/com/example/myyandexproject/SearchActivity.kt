package com.example.myyandexproject

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView

class SearchActivity : AppCompatActivity() {

    private var searchText : String = ""
    private lateinit var inputSearch : EditText

    companion object {
        const val EDIT_TEXT_VAL = "EDIT_TEXT_VAL"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val btnBack = findViewById<TextView>(R.id.btnBack)
        inputSearch = findViewById(R.id.searchInput)

        if (savedInstanceState != null) {
            val savedText = savedInstanceState.getString(EDIT_TEXT_VAL,"")
            inputSearch.setText(savedText)
        }

        val clearIcon = findViewById<ImageView>(R.id.clearIcon)

        btnBack.setOnClickListener {
            super.onBackPressed();
        }

        clearIcon.setOnClickListener {
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            if (currentFocus != null) {
                inputMethodManager.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
            }
            inputSearch.setText("")
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
                }
                clearIcon.visibility = clearIconIsVisible(s)
            }

            override fun afterTextChanged(p0: Editable?) {
                // Empty
            }
        }

        inputSearch.addTextChangedListener(searchTextInputWatcher)

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(EDIT_TEXT_VAL, searchText)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val savedText = savedInstanceState.getString(EDIT_TEXT_VAL,"")
        inputSearch.setText(savedText)
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