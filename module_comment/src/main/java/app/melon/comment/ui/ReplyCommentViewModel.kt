package app.melon.comment.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import javax.inject.Inject

class ReplyCommentViewModel @Inject constructor() : ViewModel() {

    private val _totalCount = MutableLiveData<Int>()
    val totalCount: LiveData<Int> = _totalCount

    init {
        _totalCount.value = 1000
    }
}