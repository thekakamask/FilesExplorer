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
        return new ExplorerFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            if (isGranted) {
                listFiles();
                listImages();
            } else {
                // Gérer le cas où la permission est refusée
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentExplorerBinding.inflate(inflater, container, false);
        adapter = new ExplorerAdapter(new ArrayList<>(), this); // Initialisation de l'adapter
        binding.explorerRecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.explorerRecyclerview.setAdapter(adapter); // Attachement de l'adapter
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        int backgroundColor = getThemeColor(getContext(), androidx.appcompat.R.attr.colorPrimary);
        binding.backButton.setBackgroundColor(backgroundColor);

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            listFiles();       // listFiles() appelé après l'initialisation de l'adapter
            listImages();
        } else {
            requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
        }

        // Configuration de la décoration du RecyclerView
        configureRecyclerViewDecoration();

        binding.backButton.setOnClickListener(v -> goBackToParentFolder());
    }

    private void configureRecyclerViewDecoration() {
        int dividerHeightInDp = 1;
        binding.explorerRecyclerview.addItemDecoration(
                new ThemeDividerItemDecoration(getContext(), androidx.appcompat.R.attr.colorButtonNormal, dividerHeightInDp)
        );

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(binding.explorerRecyclerview.getContext(), DividerItemDecoration.VERTICAL);
        Drawable dividerDrawable = ContextCompat.getDrawable(getContext(), R.drawable.cell_border);
        dividerItemDecoration.setDrawable(dividerDrawable);
        binding.explorerRecyclerview.addItemDecoration(dividerItemDecoration);
    }

    private void goBackToParentFolder() {
        if (currentDirectory != null && !currentDirectory.equals(Environment.getExternalStorageDirectory())) {
            updateFileList(currentDirectory.getParentFile());
        }
    }

    @Override
    public void onFileSelected(File file) {
        if (file.isDirectory()) {
            updateFileList(file);
        } else {
            // Traiter le cas des fichiers non répertoire
        }
    }

    private void updateFileList(File directory) {
        currentDirectory = directory;
        File[] files = directory.listFiles();
        Log.d("ExplorerFragment", "Nombre de fichiers: " + files.length);
        for (File file : files) {
            Log.d("ExplorerFragment", "Fichier trouvé: " + file.getName());
        }
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

    private void listImages() {
        String[] projection = new String[]{
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.SIZE
        };

        try (Cursor cursor = getContext().getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection,
                null,
                null,
                null)) {

            if (cursor != null) {
                int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
                int nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME);
                int sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE);

                while (cursor.moveToNext()) {
                    long id = cursor.getLong(idColumn);
                    String name = cursor.getString(nameColumn);
                    int size = cursor.getInt(sizeColumn);

                    Log.d("ExplorerFragment", "Image trouvée: " + name + ", Taille: " + size);
                }
            }
        }
    }
}