package com.example.gojekassignment.trendingRepositories

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.gojekassignment.R
import com.example.gojekassignment.databinding.FragmentRepositoriesBinding
import com.example.gojekassignment.domain.Repository


class RepositoriesFragment : Fragment() {

    private var viewModelAdapter: RepositoriesAdapter? = null

    private val viewModel: RepositoriesViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }
        ViewModelProviders.of(this, RepositoriesViewModelFactory(activity.application))
            .get(RepositoriesViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentRepositoriesBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_repositories,
            container,
            false)
        binding.setLifecycleOwner(viewLifecycleOwner)
        binding.viewModel = viewModel

        viewModel.repositories.observe(viewLifecycleOwner, Observer<List<Repository>> { repositories ->
            repositories?.apply {
                viewModelAdapter?.repositories = repositories
            }
        })

        viewModelAdapter = RepositoriesAdapter(viewModel.expandedPosition)

        binding.root.findViewById<RecyclerView>(R.id.repositories_recycler_view).apply {
            layoutManager = LinearLayoutManager(context)
            adapter = viewModelAdapter
        }

        return binding.root
    }

}
