package com.dcac.filesexplorer;

import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dcac.filesexplorer.databinding.FragmentExplorerBinding;
import com.dcac.filesexplorer.databinding.FragmentWelcomeBinding;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import android.Manifest;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ExplorerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExplorerFragment extends Fragment implements ExplorerAdapter.OnFileSelectedListener {

    private FragmentExplorerBinding binding;
    private ExplorerAdapter adapter;

    private File currentDirectory;

    private ActivityResultLauncher<String> requestPermissionLauncher;



    public static ExplorerFragment newInstance() {
        ExplorerFragment fragment = new ExplorerFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            if (isGranted) {
                listFiles();
                // Vous pouvez lister les images, vidéos, audio ici si nécessaire
            } else {
                // Gérer le cas où la permission est refusée
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentExplorerBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        int backgroundColor = getThemeColor(getContext(), androidx.appcompat.R.attr.colorPrimary);
        binding.backButton.setBackgroundColor(backgroundColor);

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
            requestPermissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES);
        }
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_MEDIA_VIDEO) != PackageManager.PERMISSION_GRANTED) {
            requestPermissionLauncher.launch(Manifest.permission.READ_MEDIA_VIDEO);
        }
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_MEDIA_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            requestPermissionLauncher.launch(Manifest.permission.READ_MEDIA_AUDIO);
        }

        initRecyclerView();
        listFiles();

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(binding.explorerRecyclerview.getContext(), DividerItemDecoration.VERTICAL);

        Drawable dividerDrawable = ContextCompat.getDrawable(getContext(), R.drawable.cell_border);
        dividerItemDecoration.setDrawable(dividerDrawable);

        binding.explorerRecyclerview.addItemDecoration(dividerItemDecoration);

        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBackToParentFolder();
            }
        });

    }

    private void goBackToParentFolder() {
        if (currentDirectory != null && !currentDirectory.equals(Environment.getExternalStorageDirectory())) {
            updateFileList(currentDirectory.getParentFile());
        }
    }




    private void initRecyclerView() {
        adapter = new ExplorerAdapter(new ArrayList<>(), (ExplorerAdapter.OnFileSelectedListener) this);
        binding.explorerRecyclerview.setAdapter(adapter);
        binding.explorerRecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
    }


    @Override
    public void onFileSelected (File file) {
        if (file.isDirectory()) {
            updateFileList(file);
        } else {
        }
    }

    private void updateFileList(File directory) {
        currentDirectory = directory;
        File[] files = directory.listFiles();
        if (files != null) {
            adapter.updateFiles(Arrays.asList(files));
        }

        binding.backButton.setVisibility(currentDirectory.equals(Environment.getExternalStorageDirectory()) ? View.GONE : View.VISIBLE);

    }

    private void listFiles() {
        if (currentDirectory == null) {
            currentDirectory = Environment.getExternalStorageDirectory();
        }
        updateFileList(currentDirectory);
    }


    public static int getThemeColor(Context context, int attributeColor) {
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(attributeColor, typedValue, true);
        return typedValue.data;
    }
}