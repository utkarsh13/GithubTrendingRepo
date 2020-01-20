package com.example.gojekassignment.trendingRepositories

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.gojekassignment.R
import com.example.gojekassignment.databinding.FragmentRepositoriesBinding
import com.example.gojekassignment.domain.Repository
import com.facebook.shimmer.ShimmerFrameLayout
import kotlinx.android.synthetic.main.appbar_layout.*


class RepositoriesFragment : Fragment() {

    private var viewModelAdapter: RepositoriesAdapter? = null

    private val viewModel: RepositoriesViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }
        ViewModelProviders.of(this, RepositoriesViewModelFactory(activity.application))
            .get(RepositoriesViewModel::class.java)
    }

    private var swipeRefreshLayout: SwipeRefreshLayout? = null
    private var shimmerLayout: ShimmerFrameLayout? = null
    private var retryView: ConstraintLayout? = null
    private var recyclerView: RecyclerView? = null

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

        bindLayouts(binding)
        bindObservers()
        setupRecyclerView(binding)

        return binding.root
    }

    private fun setupRecyclerView(binding: FragmentRepositoriesBinding) {
        viewModelAdapter = RepositoriesAdapter(viewModel.expandedPosition)

        binding.root.findViewById<RecyclerView>(R.id.repositories_recycler_view).apply {
            layoutManager = LinearLayoutManager(context)
            adapter = viewModelAdapter
        }
    }

    private fun bindObservers() {
        viewModel.repositories.observe(
            viewLifecycleOwner,
            Observer<List<Repository>> { repositories ->
                repositories?.apply {
                    viewModelAdapter?.repositories = repositories
                }
            })

        viewModel.repositoriesSorted.observe(
            viewLifecycleOwner,
            Observer<List<Repository>> { repositories ->
                repositories?.apply {
                    viewModelAdapter?.repositories = repositories
                }
            })

        viewModel.repositoriesApiStatus.observe(
            viewLifecycleOwner,
            Observer<RepositoriesApiStatus> {
                when (it) {
                    RepositoriesApiStatus.LOADING -> {
                        shimmerLayout?.startShimmer()
                        shimmerLayout?.visibility = View.VISIBLE
                        //Added this to hide flicker
                        viewModelAdapter?.repositories = listOf()
                        recyclerView?.visibility = View.GONE
                        retryView?.visibility = View.GONE
                    }
                    RepositoriesApiStatus.SUCCESS -> {
                        shimmerLayout?.stopShimmer()
                        shimmerLayout?.visibility = View.GONE
                        recyclerView?.visibility = View.VISIBLE
                        retryView?.visibility = View.GONE
                        stopSwipeRefreshAnimation()
                    }
                    RepositoriesApiStatus.ERROR -> {
                        shimmerLayout?.stopShimmer()
                        shimmerLayout?.visibility = View.GONE
                        recyclerView?.visibility = View.GONE
                        retryView?.visibility = View.VISIBLE
                        stopSwipeRefreshAnimation()
                    }
                }
            })
    }

    private fun bindLayouts(binding: FragmentRepositoriesBinding) {
        setSwipeRefreshLayout(binding)
        setShimmerLayout(binding)
        setRetryView(binding)
        setRecyclerView(binding)
    }

    private fun setRetryView(binding: FragmentRepositoriesBinding) {
        retryView = binding.root.findViewById(R.id.retry_view)
    }

    private fun setRecyclerView(binding: FragmentRepositoriesBinding) {
        recyclerView = binding.root.findViewById(R.id.repositories_recycler_view)
    }

    private fun stopSwipeRefreshAnimation() {
        if (swipeRefreshLayout?.isRefreshing == true) {
            swipeRefreshLayout?.isRefreshing = false
        }
    }

    private fun setSwipeRefreshLayout(binding: FragmentRepositoriesBinding) {
        swipeRefreshLayout = binding.root.findViewById(R.id.swipe_refresh)
        swipeRefreshLayout?.let {
            it.setOnRefreshListener {
                viewModel.refreshRepositories()
            }
        }
    }

    private fun setShimmerLayout(binding: FragmentRepositoriesBinding) {
        shimmerLayout = binding.root.findViewById(R.id.fb_shimmer)
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
