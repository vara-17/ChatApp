package vara17.chatapp.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.EditText
import android.widget.RatingBar
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.google.firebase.auth.FirebaseAuth
import vara17.chatapp.R
import vara17.chatapp.models.Rate
import vara17.chatapp.models.RateEvent
import vara17.chatapp.toast
import vara17.chatapp.utils.RxBus
import java.util.*

class RateDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val view = requireActivity().layoutInflater.inflate(R.layout.dialog_rate, null)

        return AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.dialog_title))
            .setView(view)
            .setPositiveButton(getString(R.string.dialog_ok)) { _, _ ->
                val editTextRateFeedback = view.findViewById<EditText>(R.id.editTextRateFeedback)
                val ratingBarFeedback = view.findViewById<RatingBar>(R.id.ratingBarFeedback)
                val textRate = editTextRateFeedback.text.toString()
                if(textRate.isNotEmpty()){
                    val imgURL = FirebaseAuth.getInstance().currentUser!!.photoUrl?.toString() ?: run { "" }
                    val rate = Rate(textRate, ratingBarFeedback.rating, Date(), imgURL)
                    RxBus.publish(RateEvent(rate))
                }
            }
            .setNegativeButton(getString(R.string.dialog_cancel)) { _, _ ->
                requireActivity().toast("Pressed Cancel")
            }
            .create()
    }
}