package com.force.codes.fragmentnavigatordemo

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.force.codes.fragmentnavigatordemo.databinding.DogItemBinding
import com.stone.vega.library.VegaLayoutManager

data class Dog(
    val dogName: String? = null,
    var price: String? = null,
    @ColorRes val color: Int? = null,
    @DrawableRes val dogImage: Int
)

const val ARG_PARAM = "key"

class TestFragment : BaseFragment() {

    companion object {
        @JvmStatic
        fun newInstance(id: Int) =
            TestFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM, id)
                }
            }
    }
}

class TestFragmentTwo : BaseFragment() {

    companion object {

        @JvmStatic
        fun newInstance(id: Int) =
            TestFragmentTwo().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM, id)
                }
            }

    }
}

class TestFragmentThree : BaseFragment() {

    companion object {

        @JvmStatic
        fun newInstance(id: Int) =
            TestFragmentThree().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM, id)
                }
            }

    }
}

class TestFragmentFour : BaseFragment() {

    companion object {

        @JvmStatic
        fun newInstance(id: Int) =
            TestFragmentFour().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM, id)
                }
            }

    }
}

abstract class BaseFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_test, container, false)
    }

    @SuppressLint("ResourceAsColor")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view_1)

        val dog = when(arguments?.getInt(ARG_PARAM)) {
            0 -> {
                Dog(
                    dogName = "Golden Retriever",
                    price = "₱25,000",
                    color = R.color.golden,
                    dogImage = R.drawable.golden1
                )
            }

            1 -> {
                Dog(
                    dogName = "Pug",
                    price = "₱80,000",
                    color = R.color.shaded_pink,
                    dogImage = R.drawable.pug
                )
            }

            2 -> {
                Dog(
                    dogName = "Havanese",
                    price = "₱35,000",
                    color = R.color.purple,
                    dogImage = R.drawable.havenese
                )
            }
            else -> {
                Dog(
                    dogName = "Chow",
                    price = "₱15,000",
                    color = R.color.teal,
                    dogImage = R.drawable.chow1
                )
            }
        }

        recyclerView.layoutManager = VegaLayoutManager()
        recyclerView.adapter = DogAdapter(dog)
    }

}

class DogAdapter(private val dog: Dog) : RecyclerView.Adapter<DogAdapter.DogViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DogViewHolder {
        val binding = DogItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return DogViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DogViewHolder, position: Int) {
        holder.bind(dog)
    }

    override fun getItemCount(): Int {
        return 10
    }

    class DogViewHolder(private val binding: DogItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(dog: Dog) {
            binding.apply {
                dogTitle.text = dog.dogName
                dogPrice.text = dog.price
                cardRoot.setCardBackgroundColor(ContextCompat.getColor(root.context, dog.color!!))
                dogImage.background = ContextCompat.getDrawable(root.context, dog.dogImage)
            }
        }
    }

}