package vara17.chatapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.*
import com.google.firebase.ktx.Firebase
import vara17.chatapp.R
import vara17.chatapp.adapters.ChatAdapter
import vara17.chatapp.databinding.ActivityLoginBinding
import vara17.chatapp.databinding.FragmentChatBinding
import vara17.chatapp.models.Message
import vara17.chatapp.toast
import java.util.*
import java.util.EventListener
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class ChatFragment : Fragment() {

    private lateinit var binding: FragmentChatBinding

    private lateinit var view: View
    private lateinit var adapter: ChatAdapter
    private val messageList: ArrayList<Message> = ArrayList()

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var currentUser: FirebaseUser

    private val store: FirebaseFirestore = FirebaseFirestore.getInstance()
    private lateinit var chatDBRef: CollectionReference

    private var chatSubscription: ListenerRegistration? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                                savedInstanceState: Bundle?): View? {
        binding = FragmentChatBinding.inflate(inflater, container, false)

        setUpChatDatabase()
        setUpCurrentUser()
        setUpRecyclerView()
        setUpChatBtn()
        suscribeToChatMessages()

        return binding.root
    }

    private fun setUpChatDatabase() {
        chatDBRef = store.collection("chat")
    }

    private fun setUpCurrentUser() {
        currentUser = auth.currentUser!!
    }

    private fun setUpRecyclerView() {
        val layoutManager = LinearLayoutManager(context)
        adapter = ChatAdapter(messageList, currentUser.uid)

        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.itemAnimator = DefaultItemAnimator()
        binding.recyclerView.adapter = adapter

    }

    private fun setUpChatBtn() {
        binding.buttonSend.setOnClickListener {
            val messageText = binding.editTextMessage.text.toString()
            if(messageText.isNotEmpty()){
                val photo = currentUser.photoUrl?.let{
                    currentUser.photoUrl.toString()
                }?: run {
                    ""
                }
                val message = Message(currentUser.uid, messageText, photo, Date())
                saveMessage(message)
                binding.editTextMessage.setText("")
            }
        }
    }

    private fun saveMessage(message: Message){
        val newMessage = HashMap<String, Any>()
        newMessage["authorId"] = message.authorId
        newMessage["message"] = message.message
        newMessage["profileImageUrl"] = message.profileImageUrl
        newMessage["sentAt"] = message.sentAt

        chatDBRef.add(newMessage)
            .addOnCompleteListener {
                requireActivity().toast("Message added!")
            }
            .addOnFailureListener {
                requireActivity().toast("Message NOT added!")
            }
    }

    private fun suscribeToChatMessages(){
        chatSubscription = chatDBRef
            .orderBy("sentAt", Query.Direction.ASCENDING)
            .limit(100)
            .addSnapshotListener(object: EventListener, com.google.firebase.firestore.EventListener<QuerySnapshot>{
            override fun onEvent(snapshot: QuerySnapshot?, e: FirebaseFirestoreException?) {
                e?.let {
                    requireActivity()!!.toast("Exception")
                    return
                }
                snapshot?.let {
                    messageList.clear()
                    val messages = it.toObjects(Message::class.java)
                    messageList.addAll(messages)
                    adapter.notifyDataSetChanged()
                    binding.recyclerView.smoothScrollToPosition(messageList.size)
                }
            }
        })
    }

    override fun onDestroy() {
        chatSubscription?.remove()
        super.onDestroy()
    }

}