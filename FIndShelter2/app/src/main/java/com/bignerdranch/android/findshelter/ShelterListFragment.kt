package com.bignerdranch.android.findshelter

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_item_shelter.view.*

private const val TAG = "ShelterListFragment"

// note: you can make the constant public as below and we can access it in details activity
// or make it private and duplicate here and in the details fragment

const val EXTRA_SHELTER = "extra_shelter"

class ShelterListFragment : Fragment() {
    private lateinit var shelterRecyclerView: RecyclerView
    private var adapter: ShelterAdapter? = null

    private val shelterListViewModel: ShelterListViewModel by lazy { ViewModelProviders.of(this).get(ShelterListViewModel::class.java)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Log.d(TAG, "Total shelters: ${shelterListViewModel.shelters.size}")  for testing
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_shelter_list, container, false)

        shelterRecyclerView =
            view.findViewById(R.id.shelter_recycler_view) as RecyclerView
        shelterRecyclerView.layoutManager = LinearLayoutManager(context)
        shelterListViewModel.initData(requireContext())
        shelterListViewModel.loadAllShelters().observe(requireActivity(), { shelters ->
            if (shelters.isEmpty()) {

                // ----- List of the fragment where I have passed latitude and longitude as a parameter -----

                val shelter1 = Shelter(latitude = -16.927436891923133,longitude = 145.77191497433202)
                shelter1.name = "Cairns Homelessness Services Hub"
                shelter1.address = "149-153 Bunda Street"
                shelter1.suburb = "Parramatta Park"
                shelter1.phone = "07 4046 8050"
                shelter1.email = "hubadmin@anglicare.net"
                shelters += shelter1 // Concatinating the shelter list

                val shelter2 = Shelter(latitude = -16.928893272553427,longitude = 145.77021565990813)
                shelter2.name = "Vinnies Homeless Hostel"
                shelter2.address = "197 Draper Street"
                shelter2.suburb = "Parramatta Park"
                shelter2.phone = "1800 846 643"
                shelter2.email = "reception@svdpqld.org.au"
                shelters += shelter2

                val shelter3 = Shelter(latitude = -16.916479904985984,longitude = 145.76987256094102)
                shelter3.name = "Douglas House"
                shelter3.address = "198 Grafton Street"
                shelter3.suburb = "Cairns"
                shelter3.phone = "07 4048 7500"
                shelter3.email = "musumecin@missionaustralia.com.au"
                shelters += shelter3

                val shelter4 = Shelter(latitude = -16.922791,longitude = 145.745504)
                shelter4.name = "St. John’s Young Men’s Shelter"
                shelter4.address = "Aumuller Street"
                shelter4.suburb = "Cairns"
                shelter4.phone = "07 4032 4971"
                shelter4.email = "stjohns.referrals@anglicare.net"
                shelters += shelter4

                val shelter5 = Shelter(latitude = -16.945571,longitude = 145.741207)
                shelter5.name = "St Margaret’s Young Women’s Shelter"
                shelter5.address = "6 McGuigan Street"
                shelter5.suburb = "Earlville"
                shelter5.phone = "07 4033 2678"
                shelter5.email = "admin@anglicare.net"
                shelters += shelter5

                val shelter6 = Shelter(latitude = 16.929026,longitude = 145.762279)
                shelter6.name = "Quigley Street Night Shelter"
                shelter6.address = "6 - 8 Quigley Street"
                shelter6.suburb = "Cairns"
                shelter6.phone = "07 4046 8092"
                shelter6.email = "quigleypm@anglicare.net"
                shelters += shelter6
                Thread{
                    for (shelter in shelters)
                        shelterListViewModel.addShelter(shelter)
                }.start()
                println("ADDED THE DATA")
            }
             // ---- adding the data using adapter in to list view -----

            adapter = ShelterAdapter(shelters)
            shelterRecyclerView.adapter = adapter
        })
        return view
    }

    private fun updateUI() {

    }

    private inner class ShelterHolder(view: View)
        : RecyclerView.ViewHolder(view), View.OnClickListener {

        private lateinit var shelter: Shelter

        // Its ok of the dispense with findViewById using plugin: 'kotlin-android-extensions'
        private val nameText: TextView = itemView.findViewById(R.id.text_name)
        private val suburbText: TextView = itemView.findViewById(R.id.text_suburb)
        private val timeText: TextView = itemView.findViewById(R.id.text_time)

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(shelter: Shelter) {
            this.shelter = shelter

            // if eliminating findViewById use view.component_name.text = this.shelter.name

            nameText.text = shelter.name
            suburbText.text = shelter.suburb
            timeText.text = shelter.phone
        }

        override fun onClick(v: View)
        {
            val intent = Intent(context, DetailsActivity::class.java)
            intent.putExtra(EXTRA_SHELTER, shelter)

            startActivity(intent)
        }
    }

    // Inner adapter class declaration -----

    private inner class ShelterAdapter(var shelters: List<Shelter>)
        : RecyclerView.Adapter<ShelterHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
                : ShelterHolder {
            val view = layoutInflater.inflate(R.layout.list_item_shelter, parent, false)
            return ShelterHolder(view)
        }

        override fun getItemCount() = shelters.size

        override fun onBindViewHolder(holder: ShelterHolder, position: Int) {
            val shelter = shelters[position]

            holder.bind(shelter)
        }
    }

    companion object
    {
        fun newInstance(): ShelterListFragment {
            return ShelterListFragment()
        }
    }
}