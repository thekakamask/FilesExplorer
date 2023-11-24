package com.dcac.filesexplorer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.List;

public class ExplorerAdapter extends RecyclerView.Adapter<ExplorerAdapter.ViewHolder> {

    private List<File> mFiles;

    public ExplorerAdapter(List<File> files) {
        mFiles=files;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.explorer_fragment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExplorerAdapter.ViewHolder holder, int position) {
        File file = mFiles.get(position);
        holder.fileNameView.setText(file.getName());
    }

    @Override
    public int getItemCount() {
        return mFiles != null ? mFiles.size() : 0;
    }

    public void updateFiles(List<File> newFiles) {
        mFiles.clear();
        mFiles.addAll(newFiles);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView fileNameView;
        public ImageView fileIconView;

        public ViewHolder(View view) {
            super(view);
            fileNameView = view.findViewById(R.id.file_name_text);
            fileIconView = view.findViewById(R.id.file_icon);
        }
    }
}
