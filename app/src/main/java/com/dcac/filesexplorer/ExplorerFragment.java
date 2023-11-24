package com.dcac.filesexplorer;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dcac.filesexplorer.databinding.FragmentExplorerBinding;
import com.dcac.filesexplorer.databinding.FragmentWelcomeBinding;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ExplorerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExplorerFragment extends Fragment {

    private FragmentExplorerBinding binding;
    private ExplorerAdapter adapter;


    public static ExplorerFragment newInstance() {
        ExplorerFragment fragment = new ExplorerFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentExplorerBinding.inflate(inflater, container, false);
        initRecyclerView();
        listFiles();

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(binding.fileExplorerRecyclerview.getContext(), DividerItemDecoration.VERTICAL);

        Drawable dividerDrawable = ContextCompat.getDrawable(getContext(), R.drawable.cell_border);
        dividerItemDecoration.setDrawable(dividerDrawable);

        binding.fileExplorerRecyclerview.addItemDecoration(dividerItemDecoration);
        return binding.getRoot();
    }

    private void listFiles() {
        File root = Environment.getExternalStorageDirectory();
        File[] files = root.listFiles();
        if (files != null) {
            adapter.updateFiles(Arrays.asList(files));
        }
    }

    private void initRecyclerView() {
        adapter = new ExplorerAdapter(new ArrayList<>());
        binding.fileExplorerRecyclerview.setAdapter(adapter);
        binding.fileExplorerRecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
    }
}