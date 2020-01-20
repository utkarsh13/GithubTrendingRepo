package com.example.gojekassignment.trendingRepositories

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gojekassignment.R
import com.example.gojekassignment.databinding.FragmentRepositoriesBinding
import com.example.gojekassignment.domain.Repository
import kotlinx.android.synthetic.main.fragment_repositories.*


class RepositoriesFragment : Fragment() {

    private var viewModelAdapter: RepositoriesAdapter? = null

    private val viewModel: RepositoriesViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }
        ViewModelProviders.of(this, RepositoriesViewModelFactory(activity.application))
            .get(RepositoriesViewModel::class.java)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setHasOptionsMenu(true)
        (activity as AppCompatActivity?)!!.setSupportActionBar(trToolbar)
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

        viewModel.repositoriesSorted.observe(viewLifecycleOwner, Observer<List<Repository>> { repositories ->
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        viewModel.updateSort(
            when (item.itemId) {
                R.id.sort_by_names -> RepositorySort.SORT_NAME
                R.id.sort_by_stars -> RepositorySort.SORT_STAR
                else -> RepositorySort.DEFAULT
            }
        )
        return true
    }

}
