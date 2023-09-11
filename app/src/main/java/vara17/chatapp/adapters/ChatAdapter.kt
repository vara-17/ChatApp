package vara17.chatapp.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.squareup.picasso.Picasso
import vara17.chatapp.R
import vara17.chatapp.databinding.FragmentChatItemLeftBinding
import vara17.chatapp.databinding.FragmentChatItemRightBinding
import vara17.chatapp.inflate
import vara17.chatapp.models.Message
import vara17.chatapp.utils.CircleTransform
import java.text.SimpleDateFormat
import javax.microedition.khronos.opengles.GL

class ChatAdapter(val items: List<Message>, val userId: String) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val GLOBAL_MESSAGE = 1
    private val MY_MESSAGE = 2

    private val layoutRight = R.layout.fragment_chat_item_right
    private val layoutLeft = R.layout.fragment_chat_item_left

    override fun getItemViewType(position: Int) = if(items[position].authorId == userId) MY_MESSAGE else GLOBAL_MESSAGE
    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            MY_MESSAGE -> ViewHolderR(parent.inflate(layoutRight))
            else -> ViewHolderL(parent.inflate(layoutLeft))
        }
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder.itemViewType){
            MY_MESSAGE -> (holder as ViewHolderR).bind(items[position])
            GLOBAL_MESSAGE -> (holder as ViewHolderL).bind(items[position])
        }
    }

    class ViewHolderR(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bind(message: Message) = with(itemView){
            val textViewMessageRight = findViewById<TextView>(R.id.textViewMessageRight)
            textViewMessageRight.text = message.message
            val textViewTimeRight = findViewById<TextView>(R.id.textViewTimeRight)
            textViewTimeRight.text = SimpleDateFormat("hh:mm").format(message.sentAt)
            val imageViewProfileRight = findViewById<ImageView>(R.id.imageViewProfileRight)
            if(message.profileImageUrl.isEmpty()){
                //TODO no carga la imagen del drawable
                Picasso.get().load(R.drawable.ic_person).resize(100, 100)
                    .centerCrop().transform(CircleTransform()).into(imageViewProfileRight)
            }else{
                Picasso.get().load(message.profileImageUrl).resize(100, 100)
                    .centerCrop().transform(CircleTransform()).into(imageViewProfileRight)
            }
        }
    }

    class ViewHolderL(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bind(message: Message) = with(itemView){
            val textViewMessageLeft = findViewById<TextView>(R.id.textViewMessageLeft)
            textViewMessageLeft.text = message.message
            val textViewTimeLeft = findViewById<TextView>(R.id.textViewTimeLeft)
            textViewTimeLeft.text = SimpleDateFormat("hh:mm").format(message.sentAt)
            val imageViewProfileLeft = findViewById<ImageView>(R.id.imageViewProfileLeft)
            if(message.profileImageUrl.isEmpty()){
                Picasso.get().load(R.drawable.ic_person).resize(100, 100)
                    .centerCrop().transform(CircleTransform()).into(imageViewProfileLeft)
            }else{
                Picasso.get().load(message.profileImageUrl).resize(100, 100)
                    .centerCrop().transform(CircleTransform()).into(imageViewProfileLeft)
            }
        }
    }
}