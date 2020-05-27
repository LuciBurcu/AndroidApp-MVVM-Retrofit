package com.luciburcugithub.mobilebackend

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.luciburcugithub.mobilebackend.adapter.AdapterRecycler
import com.luciburcugithub.mobilebackend.databinding.ActivityMainBinding
import com.luciburcugithub.mobilebackend.databinding.BottomSheetDialogBinding
import com.luciburcugithub.mobilebackend.model.CityEntity
import com.luciburcugithub.mobilebackend.viewmodel.MainActivityViewModel


class MainActivity : AppCompatActivity() {

    private lateinit var mainActivityViewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)

        val adapterRecycler = AdapterRecycler(baseContext) {
            openEditSheetDialog(it)
            false
        }

        mainActivityViewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)
        mainActivityViewModel.cityLiveData.observe(this, Observer {
            adapterRecycler.setItems(it)
        })

        binding.layoutContent.recyclerviewCities.apply {
            layoutManager = LinearLayoutManager(baseContext, LinearLayoutManager.VERTICAL, false)
            adapter = adapterRecycler

        }

        itemTouchHelperProvider(
            baseContext,
            adapterRecycler
        ).attachToRecyclerView(binding.layoutContent.recyclerviewCities)

        setSupportActionBar(binding.topAppBar)
        setContentView(binding.root)

    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.open_dialog -> {
                openCreateSheetDialog()
                true
            }
            R.id.refresh_items -> {
                mainActivityViewModel.getCities()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }


    private fun openCreateSheetDialog() {
        val bottomSheetDialogBinding = BottomSheetDialogBinding.inflate(layoutInflater)
        val bottomSheetDialog =
            BottomSheetDialog(bottomSheetDialogBinding.root.context, R.style.BottomSheetDialogStyle)

        bottomSheetDialog.setContentView(bottomSheetDialogBinding.root)
        bottomSheetDialogBinding.btnCreateEditCity.setOnClickListener {
            if (bottomSheetDialogBinding.edtCityPop.text.isNullOrEmpty() || bottomSheetDialogBinding.edtCityName.text.isNullOrEmpty()) {
                bottomSheetDialog.dismiss()

            } else {
                mainActivityViewModel.createCity(
                    CityEntity(
                        mainActivityViewModel.getId(),
                        population = bottomSheetDialogBinding.edtCityPop.text.toString().toInt(),
                        name = bottomSheetDialogBinding.edtCityName.text.toString()
                    )
                )
                bottomSheetDialog.dismiss()
            }

        }

        bottomSheetDialog.setCancelable(true)
        bottomSheetDialog.show()
    }


    private fun openEditSheetDialog(cityEntity: CityEntity) {
        val bottomSheetDialogBinding = BottomSheetDialogBinding.inflate(layoutInflater)
        val bottomSheetDialog =
            BottomSheetDialog(bottomSheetDialogBinding.root.context, R.style.BottomSheetDialogStyle)

        bottomSheetDialog.setContentView(bottomSheetDialogBinding.root)

        bottomSheetDialogBinding.edtCityName.setText(cityEntity.name)
        bottomSheetDialogBinding.edtCityPop.setText(cityEntity.population.toString())
        bottomSheetDialogBinding.btnCreateEditCity.text = "EDIT CITY"

        bottomSheetDialogBinding.btnCreateEditCity.setOnClickListener {
            mainActivityViewModel.updateCity(
                cityEntity.id,
                CityEntity(
                    id = cityEntity.id,
                    population = bottomSheetDialogBinding.edtCityPop.text.toString().toInt(),
                    name = bottomSheetDialogBinding.edtCityName.text.toString()
                )
            )
            bottomSheetDialog.dismiss()
        }

        bottomSheetDialog.setCancelable(true)
        bottomSheetDialog.show()
    }


    private fun itemTouchHelperProvider(
        context: Context,
        adapter: AdapterRecycler
    ): ItemTouchHelper {
        return ItemTouchHelper(
            object : ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.UP or ItemTouchHelper.DOWN,
                ItemTouchHelper.RIGHT
            ) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: ViewHolder, target: ViewHolder
                ): Boolean {

                    return true
                }

                override fun onSwiped(viewHolder: ViewHolder, direction: Int) {
                    mainActivityViewModel.deleteCity(
                        mainActivityViewModel.cityLiveData.value?.get(
                            viewHolder.adapterPosition
                        )?.id!!
                    )
                    adapter.notifyDataSetChanged()

                }

                override fun onChildDraw(
                    c: Canvas,
                    recyclerView: RecyclerView,
                    viewHolder: ViewHolder,
                    dX: Float,
                    dY: Float,
                    actionState: Int,
                    isCurrentlyActive: Boolean
                ) {
                    super.onChildDraw(
                        c,
                        recyclerView,
                        viewHolder,
                        dX,
                        dY,
                        actionState,
                        isCurrentlyActive
                    )
                    val background =
                        ColorDrawable(ContextCompat.getColor(context, R.color.colorDelete))
                    background.setBounds(
                        0, viewHolder.itemView.top,
                        (viewHolder.itemView.left + dX).toInt(), viewHolder.itemView.bottom
                    )
                    background.draw(c)
                }
            }
        )
    }


}
