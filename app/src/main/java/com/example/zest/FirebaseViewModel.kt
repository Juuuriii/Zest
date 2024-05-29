package com.example.zest

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.zest.data.Repository
import com.example.zest.data.model.CalendarDay
import com.example.zest.data.model.Entry
import com.example.zest.data.model.ZestUser
import com.example.zest.data.remote.QuoteApi
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth
import java.time.ZoneId
import java.util.Date

class FirebaseViewModel : ViewModel() {

    private val tagList = mutableListOf(
        "Work",
        "Food",
        "Tennis",
        "Games"
    ) //TODO(Safe used tags for AutocompleteTextView)

    private val firebaseAuth = Firebase.auth

    private val firestoreDatabase = Firebase.firestore

    private val firebaseStorage = Firebase.storage

    private val repository = Repository(QuoteApi)

    val quotes = repository.quoteList


    private var _curUser = MutableLiveData<FirebaseUser?>(firebaseAuth.currentUser)
    val curUser: LiveData<FirebaseUser?>
        get() = _curUser

    private var _curUserProfile = MutableLiveData<ZestUser>()
    val curUserProfile: LiveData<ZestUser>
        get() = _curUserProfile

    private var _curDate = MutableLiveData<LocalDate>(LocalDate.now())

    val curDate: LiveData<LocalDate>
        get() = _curDate

    private var _curCalendarMonth = MutableLiveData<YearMonth>(YearMonth.from(LocalDate.now()))

    val curCalendarMonth: LiveData<YearMonth>
        get() = _curCalendarMonth

    private var _curCalenderMonthDays = MutableLiveData<List<CalendarDay>>()

    val curCalenderMonthDays: LiveData<List<CalendarDay>>
        get() = _curCalenderMonthDays


    private var _entriesOfSelectedDay = MutableLiveData<List<Entry>>()

    val entriesOfSelectedDay: LiveData<List<Entry>>
        get() = _entriesOfSelectedDay


    private var _entriesOfSelectedMonth = MutableLiveData<List<Entry>>()

    private val entriesOfSelectedMonth: LiveData<List<Entry>>
        get() = _entriesOfSelectedMonth

    private var _curEntry = MutableLiveData<Entry>()

    val curEntry: LiveData<Entry>
        get() = _curEntry

    private var _curEntryTags = MutableLiveData<MutableList<String>>()

    val curEntryTags: LiveData<MutableList<String>>
        get() = _curEntryTags

    private var _profilePic = MutableLiveData<Bitmap>()

    val profilePic: LiveData<Bitmap>
        get() = _profilePic

    init {
        loadQuotes()
    }
    fun loadQuotes() {

        viewModelScope.launch(Dispatchers.IO) {

            repository.getQuotes()

        }
    }

    fun register(email: String, pwd: String, username: String, completion: () -> Unit) {
        if (email.isNotBlank() && pwd.isNotBlank()) {
            firebaseAuth.createUserWithEmailAndPassword(email, pwd)
                .addOnCompleteListener { authResult ->
                    if (authResult.isSuccessful) {


                        createUser(username)

                        completion()

                    } else {
                        Log.e("FIREBASE_AUTH", authResult.exception.toString())
                    }
                }
        }
    }

    fun login(email: String, pwd: String) {
        if (email.isNotBlank() && pwd.isNotBlank()) {
            firebaseAuth.signInWithEmailAndPassword(email, pwd)
                .addOnCompleteListener { authResult ->
                    if (authResult.isSuccessful) {
                        _curUser.value = firebaseAuth.currentUser
                    } else {
                        Log.e("FIREBASE_AUTH", authResult.exception.toString())
                    }
                }
        }
    }

    private fun createUser(username: String) {

        firestoreDatabase.collection("users")
            .document(firebaseAuth.currentUser!!.uid)
            .set(
                ZestUser(
                    username = username,
                    userId = firebaseAuth.currentUser!!.uid,
                    userEmail = firebaseAuth.currentUser?.email ?: ""
                )
            )
            .addOnFailureListener { exception ->
                Log.w("createUser", "$exception")
            }
    }



    fun createEntry(
        title: String,
        text: String,
        tags: MutableList<String>,
        date: LocalDate = curDate.value!!
    ) {
        if (title.isNotEmpty() && text.isNotEmpty()) {

            firestoreDatabase.collection("users")
                .document(firebaseAuth.currentUser!!.uid)
                .collection("journal")
                .document(date.toString())
                .set(
                    mapOf(
                        "date" to date
                    )
                )

            firestoreDatabase.collection("users")
                .document(firebaseAuth.currentUser!!.uid)
                .collection("journal")
                .document(date.toString())
                .collection("entries")
                .document(Entry().time)
                .set(
                    Entry(
                        title = title,
                        text = text,
                        tags = tags,
                        date = date.toString(),
                        day = date.dayOfMonth.toString(),
                        month = date.month.value.toString(),
                        year = date.year.toString(),
                        userId = firebaseAuth.currentUser!!.uid
                    )
                )
        }
    }

    fun getUser() {

        firestoreDatabase.collection("users").document(firebaseAuth.currentUser!!.uid).addSnapshotListener { value, error ->
            _curUserProfile.value = value?.toObject(ZestUser::class.java)
        }
        loadProfilePicture()

    }

    fun changeUserName(newName: String) {

        firestoreDatabase.collection("users").document(firebaseAuth.currentUser!!.uid)
            .update("username", newName)

    }

    fun logout() {
        firebaseAuth.signOut()
        _curUser.value = firebaseAuth.currentUser
    }


    fun curDatePlusOne() {

        if (_curDate.value != LocalDate.now()) {

            _curDate.value = _curDate.value!!.plusDays(1)

        }
        getEntriesOfDay(curDate.value!!)
    }

    fun curDateMinusOne() {

        _curDate.value = _curDate.value!!.minusDays(1)
        getEntriesOfDay(curDate.value!!)
    }

    private fun getEntryRef(date: String): CollectionReference {
        return firestoreDatabase.collection("users")
            .document(firebaseAuth.currentUser!!.uid)
            .collection("journal")
            .document(date)
            .collection("entries")
    }

    val deleteEntry: (time: String) -> Unit = { time ->


                getEntryRef(curDate.value.toString()).document(time).delete()
                getEntriesOfDay(curDate.value!!)
                _curDate.value = _curDate.value
    }

    val setCurEntry: (entry: Entry) -> Unit = { entry ->

        _curEntry.value = entry
        _curEntryTags.value = entry.tags

    }
    fun updateEntry(title: String, text: String, tags: MutableList<String>) {

        getEntryRef(curDate.value.toString()).document(curEntry.value!!.time)
            .update(
                mapOf(
                    "title" to title,
                    "text" to text,
                    "tags" to tags
                )
            )
    }
    fun setEmptyEntry() {
        _curEntry.value = Entry(userId = firebaseAuth.currentUser!!.uid)
        _curEntryTags.value = mutableListOf<String>()
    }

    val deleteTag: (position: Int) -> Unit = { position ->
        _curEntryTags.value!!.removeAt(position)
        _curEntryTags.value = _curEntryTags.value

    }

    val addTag: (tag: String) -> Unit = { tag ->

        if (tag != "" && !_curEntry.value!!.tags.contains(tag)) {
            _curEntryTags.value?.add(0, tag) ?: mutableListOf<String>()
            _curEntryTags.value = _curEntryTags.value
            Log.i("ΩTags", "${_curEntry.value!!.tags}")

        }
    }

    fun datePicker(activity: MainActivity) {

        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTheme(R.style.ThemeOverlay_App_MaterialCalendar)
            .build()

        datePicker.addOnPositiveButtonClickListener {

            val date = Date(it).toInstant().atZone(ZoneId.systemDefault()).toLocalDate()

            if (date > LocalDate.now()) {

                _curDate.value = LocalDate.now()

            } else {

                _curDate.value = date

            }
        }

        datePicker.show(activity.supportFragmentManager, "materialDatePicker")

    }

    val setCurDate: (localDate: LocalDate) -> Unit = {

        _curDate.value = it

    }

    fun nextCurrentMonth() {

        _curCalendarMonth.value = _curCalendarMonth.value?.plusMonths(1)
        getEntriesOfMonth(_curCalendarMonth.value!!)
    }

    fun previousCurrentMonth() {

        _curCalendarMonth.value = _curCalendarMonth.value?.minusMonths(1)
        getEntriesOfMonth(_curCalendarMonth.value!!)
    }

    fun setCurrentCalendarMonth() {

        _curCalendarMonth.value = YearMonth.from(LocalDate.now())
        getEntriesOfMonth(_curCalendarMonth.value!!)

    }

    private fun getDaysOfMonth(yearMonth: YearMonth): MutableList<CalendarDay> {


        Log.i("ΩCalendar", "$yearMonth")
        val daysInMonthList = mutableListOf<CalendarDay>()

        val daysInMonth = yearMonth.lengthOfMonth()

        val firstOfMonth = yearMonth.atDay(1)

        val dayOfWeek = firstOfMonth.dayOfWeek.value - 1

        (1..42).forEach {
            if (it <= dayOfWeek || it > daysInMonth + dayOfWeek) {
                daysInMonthList.add(CalendarDay("", "", "", false, false))
            } else {

                var isToday = false

                if (it <= 31) {

                    isToday = yearMonth.atDay(it - dayOfWeek) == LocalDate.now()
                }

                daysInMonthList.add(
                    CalendarDay(
                        (it - dayOfWeek).toString(),
                        yearMonth.month.value.toString(),
                        yearMonth.year.toString(),
                        false,
                        isToday
                    )
                )
            }
        }

        Log.i("ΩCalendar", "$daysInMonthList")

        return daysInMonthList

    }

     fun getEntriesOfMonth(yearMonth: YearMonth) {

        val query = firestoreDatabase
            .collectionGroup("entries")
            .whereEqualTo("userId", firebaseAuth.currentUser!!.uid)
            .whereEqualTo("month", yearMonth.month.value.toString())
            .whereEqualTo("year", yearMonth.year.toString())


        query
            .get()
            .addOnSuccessListener { querySnapshot ->

                _entriesOfSelectedMonth.value =
                    querySnapshot.map { it.toObject(Entry::class.java) }


                val calendarDays = getDaysOfMonth(yearMonth)

                calendarDays.forEach { calendarDay ->
                    calendarDay.hasEntry =
                        entriesOfSelectedMonth.value!!.any { it.day == calendarDay.day }
                }
                _curCalenderMonthDays.value = calendarDays

            }
            .addOnFailureListener { exception ->
                Log.e("getEntriesOfMonth", "$exception")
            }
    }

    fun getEntriesOfDay(date: LocalDate) {

        val query = firestoreDatabase
            .collectionGroup("entries")
            .whereEqualTo("userId", firebaseAuth.currentUser!!.uid)
            .whereEqualTo("date", date.toString())
        query
            .get()
            .addOnSuccessListener { querySnapshot ->

                _entriesOfSelectedDay.value =
                    querySnapshot.map { it.toObject(Entry::class.java) }

            }
            .addOnFailureListener { exception ->
                Log.e("getEntriesOfDay", "$exception")
            }
    }

    fun uploadProfilePicture(uri: Uri) {

        val path = "Users/${firebaseAuth.currentUser!!.uid}/profilePic"

        firestoreDatabase.collection("users").document(firebaseAuth.currentUser!!.uid).update("profilePicPath", path)

        firebaseStorage.getReference(path)
            .putFile(uri).addOnCompleteListener {

                loadProfilePicture()

            }

    }

    private fun loadProfilePicture() {

        val storageRef = firebaseStorage
            .reference
            .child("Users/${firebaseAuth.currentUser!!.uid}/profilePic")


        val maxDownloadSizeBytes: Long = 1024 * 1024 * 10

        storageRef.getBytes(maxDownloadSizeBytes).addOnCompleteListener {

            val image = BitmapFactory.decodeByteArray(it.result, 0, it.result.size)
            _profilePic.value = image

        }


    }

    fun getAllEntries() {

        firestoreDatabase
            .collectionGroup("entries")
            .whereEqualTo("userId", firebaseAuth.currentUser!!.uid)
            .get()
            .addOnSuccessListener { querySnapshot ->

                val allEntries = querySnapshot.map { it.toObject(Entry::class.java) }

                allEntries.forEach {

                    Log.i(
                        "AllEntriesΩ",
                        "${it.date} | ${it.time} | ${it.title} | ${it.text} | ${it.tags}"
                    )

                }

            }.addOnFailureListener {

                Log.i("AllEntriesΩ", "${it}")

            }


    }

}










