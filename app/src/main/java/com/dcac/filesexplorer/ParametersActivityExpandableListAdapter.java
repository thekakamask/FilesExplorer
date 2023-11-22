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

    public ParametersActivityExpandableListAdapter(Context context, List<String> listGroupTitles,
                                                   HashMap<String, List<ParametersItem>> listChildData) {
        this.context = context;
        this.listGroupTitles = listGroupTitles;
        this.listChildData = listChildData;
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

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final ParametersItem childItem = (ParametersItem) getChild(groupPosition, childPosition);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.parameters_onglets_elements, parent, false);
        }
        TextView tvChild = convertView.findViewById(R.id.tvChild);
        TextView tvChildDesc = convertView.findViewById(R.id.tvChildDesc);
        CheckBox checkBox = convertView.findViewById(R.id.checkBox);

        tvChild.setText(childItem.getTitle());
        tvChildDesc.setText(childItem.getDescription());
        checkBox.setChecked(childItem.isChecked());
        // Ajouter des gestionnaires pour le CheckBox si nécessaire

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
