package app.melon.user.ui.widget

import android.content.Context
import android.text.InputType
import android.widget.EditText
import androidx.fragment.app.FragmentActivity
import app.melon.base.ui.BaseEpoxyHolder
import app.melon.data.entities.User
import app.melon.user.R
import app.melon.util.extensions.afterTextChanged
import app.melon.util.extensions.resolveTheme
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.google.android.material.datepicker.MaterialDatePicker

private typealias TextChangeListener = (String) -> Unit

@EpoxyModelClass
abstract class EditBasicInfo : EpoxyModelWithHolder<EditBasicInfo.Holder>() {

    override fun getDefaultLayout(): Int = R.layout.view_edit_basic_info

    @EpoxyAttribute lateinit var item: User

    @EpoxyAttribute var afterHometownChanged: TextChangeListener? = null
    @EpoxyAttribute var afterCollegeChanged: TextChangeListener? = null
    @EpoxyAttribute var afterMajorChanged: TextChangeListener? = null
    @EpoxyAttribute var afterDegreeChanged: TextChangeListener? = null

    override fun bind(holder: Holder) {
        with(holder) {
            birthDateInput.setText("TODO")
            birthDateInput.inputType = InputType.TYPE_NULL
            birthDateInput.setOnFocusChangeListener { v, hasFocus ->
                if (hasFocus) {
                    val builder = setupDateSelectorBuilder(v.context)
                    val picker = builder.build()
                    picker.addOnPositiveButtonClickListener {
                        birthDateInput.setText(picker.headerText)
                    }
                    picker.showNow((v.context as FragmentActivity).supportFragmentManager, picker.toString())
                }
            }
            hometownInput.setText(item.hometown)
            schoolInput.setText(item.school)
            collegeInput.setText(item.college)
            majorInput.setText(item.major)
            degreeInput.setText(item.degree)

            hometownInput.afterTextChanged { afterHometownChanged?.invoke(it) }
            collegeInput.afterTextChanged { afterCollegeChanged?.invoke(it) }
            majorInput.afterTextChanged { afterMajorChanged?.invoke(it) }
            degreeInput.afterTextChanged { afterDegreeChanged?.invoke(it) }
        }
    }

    private fun setupDateSelectorBuilder(
        context: Context
    ) = MaterialDatePicker.Builder.datePicker().apply {
        setSelection(MaterialDatePicker.todayInUtcMilliseconds())
        setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR)
        setTheme(context.resolveTheme(R.attr.materialCalendarTheme))
    }

    class Holder : BaseEpoxyHolder() {
        internal val birthDateInput: EditText by bind(R.id.birthdate_input)
        internal val hometownInput: EditText by bind(R.id.hometown_input)
        internal val schoolInput: EditText by bind(R.id.school_input)
        internal val collegeInput: EditText by bind(R.id.college_input)
        internal val majorInput: EditText by bind(R.id.major_input)
        internal val degreeInput: EditText by bind(R.id.degree_input)
    }
}