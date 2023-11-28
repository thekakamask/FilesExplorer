package com.dcac.filesexplorer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

public class ParametersActivityExpandableListAdapter extends BaseExpandableListAdapter {


    private Context context;
    private List<String> listGroupTitles; // Les titres des groupes
    private HashMap<String, List<ParametersItem>> listChildData; // Les données pour les enfants, mappées par groupe

    public interface OnThemeChangeListener {
        void onThemeChanged(String themeName);
    }

    private OnThemeChangeListener onThemeChangeListener;

    public ParametersActivityExpandableListAdapter(Context context, List<String> listGroupTitles,
                                                   HashMap<String, List<ParametersItem>> listChildData, OnThemeChangeListener onThemeChangeListener) {
        this.context = context;
        this.listGroupTitles = listGroupTitles;
        this.listChildData = listChildData;
        this.onThemeChangeListener = onThemeChangeListener;
    }


    @Override
    public int getGroupCount() {
        return listGroupTitles.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.listChildData.get(this.listGroupTitles.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.listGroupTitles.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.listChildData.get(this.listGroupTitles.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String groupTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.parameters_onglets, null);
        }
        TextView tvGroup = convertView.findViewById(R.id.parameters_onglets);
        tvGroup.setText(groupTitle);
        return convertView;
    }


    static class ViewHolder {
        TextView tvChild;
        TextView tvChildDesc;
        CheckBox checkBox;
    }
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ViewHolder holder;

        final ParametersItem childItem = (ParametersItem) getChild(groupPosition, childPosition);

        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.parameters_onglets_elements, parent, false);
            holder.tvChild = convertView.findViewById(R.id.tvChild);
            holder.tvChildDesc = convertView.findViewById(R.id.tvChildDesc);
            holder.checkBox = convertView.findViewById(R.id.checkBox);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvChild.setText(childItem.getTitle());
        holder.tvChildDesc.setText(childItem.getDescription());
        holder.checkBox.setChecked(childItem.isChecked());

        //Ajout de l'ecouteur de clics sur la Checkbox
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Décocher toutes les autres CheckBox du groupe
                for (int i = 0; i < getChildrenCount(groupPosition); i++) {
                    ParametersItem item = listChildData.get(listGroupTitles.get(groupPosition)).get(i);
                    item.setChecked(false);
                }

                // Cocher la CheckBox actuelle
                childItem.setChecked(true);
                notifyDataSetChanged(); // Rafraîchir la vue

                if (groupPosition == 0) { // Si c'est le groupe des thèmes
                    String themeName = null;
                    switch (childItem.getTitle()) {
                        case "Light theme":
                            themeName = "LightTheme";
                            break;
                        case "Dark theme":
                            themeName = "DarkTheme";
                            break;
                        default:
                            themeName = "BaseTheme";
                            break;
                    }
                    onThemeChangeListener.onThemeChanged(themeName);
                }
            }
        });

        return convertView;

    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
