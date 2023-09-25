package vara17.chatapp.adapters

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import vara17.chatapp.R
import vara17.chatapp.inflate
import vara17.chatapp.models.Rate
import vara17.chatapp.utils.CircleTransform
import java.text.SimpleDateFormat

class RatesAdapter(private val items: List<Rate>): RecyclerView.Adapter<RatesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(parent.inflate(R.layout.fragment_rates_item))
    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(items[position])
    override fun getItemCount() = items.size

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        fun bind(rate: Rate) = with(itemView){
            val textViewRate = findViewById<TextView>(R.id.textViewRate)
            textViewRate.text = rate.text
            val textViewStar = findViewById<TextView>(R.id.textViewStar)
            textViewStar.text = rate.rate.toString()
            val textViewCalendar = findViewById<TextView>(R.id.textViewCalendar)
            textViewCalendar.text = SimpleDateFormat("dd MMM, yyyy").format(rate.createAt)
            val imageViewProfile = findViewById<ImageView>(R.id.imageViewProfile)
            if(rate.profileImgURL.isEmpty()){
                Picasso.get().load(R.drawable.ic_person).resize(100,100)
                    .centerCrop().transform(CircleTransform()).into(imageViewProfile)
            }else{
                Picasso.get().load(rate.profileImgURL).resize(100,100)
                    .centerCrop().transform(CircleTransform()).into(imageViewProfile)
            }
        }
    }



}