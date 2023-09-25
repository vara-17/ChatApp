package vara17.chatapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.*
import com.squareup.picasso.Picasso
import io.reactivex.disposables.Disposable
import vara17.chatapp.R
import vara17.chatapp.databinding.FragmentInfoBinding
import vara17.chatapp.models.TotalMessagesEvent
import vara17.chatapp.toast
import vara17.chatapp.utils.CircleTransform
import vara17.chatapp.utils.RxBus

class InfoFragment : Fragment() {

    private lateinit var binding: FragmentInfoBinding

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var currentUser: FirebaseUser

    private val store: FirebaseFirestore = FirebaseFirestore.getInstance()
    private lateinit var chatDBRef: CollectionReference

    private var chatSubscription: ListenerRegistration? = null
    private lateinit var infoBusListener: Disposable

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentInfoBinding.inflate(inflater, container, false)

        setUpChatDatabase()
        setUpCurrentUser()
        setUpCurrentUserInfoUI()

        //FirebaseStyle
        //subscribeToTotalMessagesFirebaseStyle()
        subscribeToTotalMessagesEventBusReactiveStyle()

        return binding.root
    }

    private fun setUpChatDatabase() {
        chatDBRef = store.collection("chat")
    }

    private fun setUpCurrentUser() {
        currentUser = auth.currentUser!!
    }

    private fun setUpCurrentUserInfoUI() {
        binding.textViewInfoEmail.text = currentUser.email
        binding.textViewInfoName.text = currentUser.displayName?.let { currentUser.displayName }
            ?: run { getString(R.string.info_no_name) }

        currentUser.photoUrl?.let {
            Picasso.get().load(currentUser.photoUrl).resize(300, 300)
                .centerCrop().transform(CircleTransform()).into(binding.imageViewInfoAvatar)
        } ?: run {
            //TODO no carga la imagen del drawable
            Picasso.get().load(R.drawable.ic_person).resize(300, 300)
                .centerCrop().transform(CircleTransform()).into(binding.imageViewInfoAvatar)
        }
    }

    private fun subscribeToTotalMessagesFirebaseStyle() {
        chatSubscription = chatDBRef.addSnapshotListener(object : java.util.EventListener,
            com.google.firebase.firestore.EventListener<QuerySnapshot> {
            override fun onEvent(snapshot: QuerySnapshot?, e: FirebaseFirestoreException?) {
                e?.let {
                    requireActivity()!!.toast("Exception")
                    return
                }

                snapshot?.let { binding.textViewInfoLblTotalMessages.text = "${it.size()}" }
            }
        })
    }

    private fun subscribeToTotalMessagesEventBusReactiveStyle() {
        infoBusListener = RxBus.listen(TotalMessagesEvent::class.java).subscribe({
            binding.textViewInfoLblTotalMessages.text = "${it.total}"
        })
    }

    override fun onDestroy() {
        infoBusListener.dispose()
        chatSubscription?.remove()
        super.onDestroy()
    }

}