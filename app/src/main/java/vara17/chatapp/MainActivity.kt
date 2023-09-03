package vara17.chatapp

import android.os.Bundle
import android.view.MenuItem
import androidx.viewpager.widget.ViewPager
import com.google.firebase.auth.FirebaseAuth
import vara17.chatapp.activities.base.ToolbarActivity
import vara17.chatapp.adapters.PagerAdapter
import vara17.chatapp.databinding.ActivityMainBinding
import vara17.chatapp.fragments.ChatFragment
import vara17.chatapp.fragments.InfoFragment
import vara17.chatapp.fragments.RatesFragment

class MainActivity : ToolbarActivity() {

    private lateinit var binding: ActivityMainBinding

    private var prevBottomSelected: MenuItem? = null

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        toolbarToLoad(binding.toolbarView.toolbar)

        setUpViewPager(getPagerAdapter())
        setUpBottomNavigationBaer()
    }

    private fun getPagerAdapter(): PagerAdapter {
        val adapter: PagerAdapter
        adapter = PagerAdapter(supportFragmentManager)
        adapter.addFragment(InfoFragment())
        adapter.addFragment(RatesFragment())
        adapter.addFragment(ChatFragment())
        return adapter
    }

    private fun setUpViewPager(adapter: PagerAdapter){
        binding.viewPager.adapter = adapter
        binding.viewPager.addOnPageChangeListener(object: ViewPager.OnPageChangeListener{

            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
            override fun onPageSelected(position: Int) {
                if(prevBottomSelected == null){
                    binding.bottomNavigation.menu.getItem(0).isChecked = false
                }else{
                    prevBottomSelected!!.isChecked = false
                }
                binding.bottomNavigation.menu.getItem(position).isChecked = true
                prevBottomSelected = binding.bottomNavigation.menu.getItem(position)
            }
        })
    }

    private fun setUpBottomNavigationBaer(){
        binding.bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId){
                R.id.bottom_nav_info -> {
                    binding.viewPager.currentItem = 0; true
                }
                R.id.bottom_nav_rates -> {
                    binding.viewPager.currentItem = 1; true
                }
                R.id.bottom_nav_chat -> {
                    binding.viewPager.currentItem = 2; true
                }
                else -> false
            }
        }
    }
}