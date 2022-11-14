package com.example.criminalintent.controller

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.criminalintent.R
import com.example.criminalintent.model.Crime
import com.example.criminalintent.model.CrimeDetailsViewModel
import java.text.SimpleDateFormat
import java.util.*

private const val ARG_CRIME_ID = "crime_id"
private const val TAG = "CrimeFragment"
private const val DIALOG_DATE = "DialogDate"
private const val REQUEST_DATE = 0

class CrimeFragment : Fragment(),DatePickerFragment.Callbacks {
    private lateinit var crime: Crime
    private lateinit var titleEdit: EditText
    private lateinit var buttonDate: Button
    private lateinit var solvedCheckBox: CheckBox

    private val crimeDetailViewModel:
            CrimeDetailsViewModel by lazy {
        ViewModelProviders.of(this)[CrimeDetailsViewModel::class.java]
    }

    @SuppressLint("SimpleDateFormat")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        crime = Crime()
        val uuidCrime: UUID = arguments?.getSerializable(ARG_CRIME_ID) as UUID
        crimeDetailViewModel.loadCrime(uuidCrime)

        titleEdit = view.findViewById(R.id.crime_title) as EditText
        solvedCheckBox = view.findViewById(R.id.crime_solved) as CheckBox
        buttonDate = view.findViewById(R.id.crime_date) as Button
       // val formatter = SimpleDateFormat("EEE, MMM d, ''yy")
        //val date: String = formatter.format(Date())

        crimeDetailViewModel.crimeLiveData.observe(
            viewLifecycleOwner, androidx.lifecycle.Observer { crime ->
                crime?.let {
                    this.crime = crime
                    updateUI()
                }


            }
        )

    }



    private fun updateUI() {
        titleEdit.setText(crime.title)
        buttonDate.text = crime.date.toString()
        solvedCheckBox.apply {
            isChecked = crime.isSolved
            jumpDrawablesToCurrentState()
        }

    }
    override fun onStop() {
        super.onStop()
        crimeDetailViewModel.saveCrime(crime)
    }

    override fun onStart() {
        super.onStart()
        val titleWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                crime.title = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {

            }

        }
        titleEdit.addTextChangedListener(titleWatcher)
        solvedCheckBox.setOnCheckedChangeListener { _, isChecked ->
            crime.isSolved = isChecked
        }
        buttonDate.setOnClickListener {
            DatePickerFragment.newInstance(crime.date).apply {
                setTargetFragment(this@CrimeFragment, REQUEST_DATE)
                show(this@CrimeFragment.requireFragmentManager(), DIALOG_DATE)
            }
        }


    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_crime, container, false)
    }

    companion object {

        @JvmStatic
        fun newInstance(crimeId: UUID): CrimeFragment {
            val args = Bundle().apply {
                putSerializable(ARG_CRIME_ID, crimeId)
            }
            return CrimeFragment().apply {
                arguments = args
            }

        }
    }

    override fun onDateSelected(date: Date) {
       crime.date = date
        updateUI()
    }
}