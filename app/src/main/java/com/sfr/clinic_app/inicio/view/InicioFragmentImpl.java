package com.sfr.clinic_app.inicio.view;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.sfr.clinic_app.api.Models.MedicalReport;
import com.sfr.clinic_app.databinding.FragmentInicioBinding;
import com.sfr.clinic_app.di.appComponent.AppComponent;
import com.sfr.clinic_app.di.appComponent.DaggerAppComponent;
import com.sfr.clinic_app.di.appModule.AppModule;
import com.sfr.clinic_app.di.appModule.ConnectionModule;
import com.sfr.clinic_app.di.appModule.SharedPreferencesModule;
import com.sfr.clinic_app.inicio.adapter.InicioAdapter;
import com.sfr.clinic_app.inicio.presenter.InicioPresenter;

import java.util.ArrayList;

import javax.inject.Inject;

public class InicioFragmentImpl extends Fragment implements InicioFragment, InicioAdapter.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {
    public InicioFragmentImpl() {
        // Required empty public constructor
    }

    private RecyclerView recyclerView;
    private InicioAdapter adapter;
    private FragmentInicioBinding binding;
    @Inject
    InicioPresenter inicioPresenter;


    public static InicioFragmentImpl newInstance() {
        InicioFragmentImpl fragment = new InicioFragmentImpl();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Utilizamos el objeto de ViewBinding para inflar el diseño
        binding = FragmentInicioBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        initInjection();

        recyclerView = binding.recycler;
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.swipeRefreshLayout.setOnRefreshListener(this);

        showLoading();
        inicioPresenter.onReportsFetched();
        return view;
    }

    private void initInjection() {
        AppComponent appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this, requireContext()))
                .connectionModule(new ConnectionModule())
                .sharedPreferencesModule(new SharedPreferencesModule(requireContext()))
                .build();
        appComponent.inject(this);
    }

    @Override
    public void showReports(ArrayList<MedicalReport> reports) {
        hideLoading();
        adapter = new InicioAdapter(reports, this);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    private void hideLoading() {
        // Ocultar el TextView y el ProgressBar
        binding.progressBar.setVisibility(View.GONE);
        binding.swipeRefreshLayout.setVisibility(View.VISIBLE);

    }

    private void showLoading() {
        // Mostrar el TextView y el ProgressBar
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.swipeRefreshLayout.setVisibility(View.GONE);

    }

    @Override
    public void onRefresh() {
        inicioPresenter.onReportsFetched();
        binding.swipeRefreshLayout.setRefreshing(false);

    }

    @Override
    public void onItemClick(MedicalReport report) {

    }
}