package at.tu.graz.coffee.ui.comment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import at.tu.graz.coffee.R
import at.tu.graz.coffee.model.Review
import at.tu.graz.coffee.ui.coffee_detail.CoffeeDetailFragmentArgs
import com.google.android.material.slider.RangeSlider
import java.util.*

class CommentFragment : Fragment() {
    private val viewModel: CommentViewModel by viewModels()
    private val args: CoffeeDetailFragmentArgs by navArgs()
    private var reviewsList  = mutableListOf<Review>()
    private lateinit var listAdapter : CommentAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity).supportActionBar?.title = "Comments"

        val root = inflater.inflate(R.layout.fragment_comment, container, false)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val coffeeName = view.findViewById<TextView>(R.id.comment_coffee_name)

        val coffee = viewModel.getCoffee(args.coffeeId) ?: return
        coffeeName.text = coffee.name

        reviewsList = (viewModel.getCoffee(args.coffeeId)?.reviews) as MutableList<Review>

        val listView: ListView = view.findViewById(R.id.comment_listview)

        listView.adapter = CommentAdapter(requireContext(), reviewsList)
        listAdapter = listView.adapter as CommentAdapter

        val submitButton = view.findViewById<Button>(R.id.btn_comment_submit)
        submitButton.setOnClickListener {
            val text = view.findViewById(R.id.comment_text_field) as EditText

            val costSlider = view.findViewById(R.id.comment_cost) as RangeSlider
            val valueCost : MutableList<Float> = costSlider.values

            val tasteSlider = view.findViewById(R.id.comment_taste) as RangeSlider
            val valueTaste : MutableList<Float> = tasteSlider.values

            val availabilitySlider = view.findViewById(R.id.comment_availability) as RangeSlider
            val valueAvailability : MutableList<Float> = availabilitySlider.values


            val newReview = Review(valueTaste[0].toInt(), valueCost[0].toInt(), valueAvailability[0].toInt(), text.text.toString())
            coffee.reviews = (coffee.reviews.toMutableList() + newReview) as MutableList<Review>

            coffee.calculateNewEvaluation()

            listView.adapter = CommentAdapter(requireContext(), coffee.reviews)
            text.text.clear()
            costSlider.setValues(0.0F,0.0F)
            tasteSlider.setValues(0.0F,0.0F)
            availabilitySlider.setValues(0.0F,0.0F)

            Toast.makeText(activity, "Comment added", Toast.LENGTH_SHORT).show()
        }
    }
}