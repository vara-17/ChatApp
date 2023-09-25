package vara17.chatapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.*
import io.reactivex.disposables.Disposable
import vara17.chatapp.adapters.RatesAdapter
import vara17.chatapp.databinding.FragmentRatesBinding
import vara17.chatapp.dialog.RateDialog
import vara17.chatapp.models.Rate
import vara17.chatapp.models.RateEvent
import vara17.chatapp.toast
import vara17.chatapp.utils.RxBus
import java.util.EventListener

class RatesFragment : Fragment() {

    private lateinit var binding: FragmentRatesBinding

    private lateinit var adapter: RatesAdapter
    private val ratesList: ArrayList<Rate> = ArrayList()

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var currentUser: FirebaseUser

    private val store: FirebaseFirestore = FirebaseFirestore.getInstance()
    private lateinit var ratesDBRef: CollectionReference

    private var ratesSubscription: ListenerRegistration? = null
    private lateinit var rateBusListener: Disposable

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRatesBinding.inflate(inflater, container, false)

        setUpRatesDB()
        setUpCurrentUser()

        setUpRecyclerView()
        setUpFab()

        subscribeToRatings()
        subscribeToNewRatings()

        return binding.root
    }

    private fun setUpRatesDB() {
        ratesDBRef = store.collection("rates")
    }

    private fun setUpCurrentUser() {
        currentUser = auth.currentUser!!
    }

    private fun setUpRecyclerView() {
        val layoutManager = LinearLayoutManager(context)
        adapter = RatesAdapter(ratesList)

        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.itemAnimator = DefaultItemAnimator()
        binding.recyclerView.adapter = adapter
    }

    private fun setUpFab() {
        binding.fabRating.setOnClickListener { RateDialog().show(parentFragmentManager, "") }
    }

    private fun saveRate(rate: Rate){
        val newRating = HashMap<String, Any>()
        newRating["text"] = rate.text
        newRating["rate"] = rate.rate
        newRating["createAt"] = rate.createAt
        newRating["profileImgURL"] = rate.profileImgURL

        ratesDBRef.add(newRating)
            .addOnCompleteListener {
                requireActivity().toast("Rating added")
            }
            .addOnFailureListener{
                requireActivity().toast("Rating error, try again!")
            }
    }

    private fun subscribeToRatings() {
        ratesSubscription = ratesDBRef.orderBy("createAt", Query.Direction.DESCENDING)
            .addSnapshotListener(object: EventListener, com.google.firebase.firestore.EventListener<QuerySnapshot>{
                override fun onEvent(snapshot: QuerySnapshot?, e: FirebaseFirestoreException?) {
                    e?.let{
                        activity!!.toast("Exception!")
                        return
                    }

                    snapshot?.let {
                        ratesList.clear()
                        val rates = it.toObjects(Rate::class.java)
                        ratesList.addAll(rates)
                        adapter.notifyDataSetChanged()
                        binding.recyclerView.smoothScrollToPosition(0)
                    }
                }
            })
    }

    private fun subscribeToNewRatings() {
        rateBusListener = RxBus.listen(RateEvent::class.java).subscribe({
            saveRate(it.rate)
        })
    }


    override fun onDestroyView() {
        rateBusListener.dispose()
        ratesSubscription?.remove()
        super.onDestroyView()
    }
}