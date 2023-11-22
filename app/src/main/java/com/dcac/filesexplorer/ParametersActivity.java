package com.dcac.filesexplorer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ParametersActivity extends AppCompatActivity {

    private ExpandableListView expandableListView;
    private ParametersActivityExpandableListAdapter parametersActivityExpandableListAdapter;
    private List<String> listGroupTitles;
    private HashMap<String, List<ParametersItem>> listChildData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parameters);

        prepareListData(); // Préparez vos données ici

        expandableListView = findViewById(R.id.expandableListView);
        parametersActivityExpandableListAdapter = new ParametersActivityExpandableListAdapter(this, listGroupTitles, listChildData);
        expandableListView.setAdapter(parametersActivityExpandableListAdapter);

        // Gestion des clics sur les groupes et les enfants, si nécessaire
    }

    // Préparer les données pour l'ExpandableListView
    private void prepareListData() {
        listGroupTitles = new ArrayList<>();
        listChildData = new HashMap<>();

        List<ParametersItem> generalSettings = new ArrayList<>();
        List<ParametersItem> userCommands = new ArrayList<>();
        List<ParametersItem> thirdGroup = new ArrayList<>();

        listGroupTitles.add("Global title example 1");
        listGroupTitles.add("Global title example 2");
        listGroupTitles.add("Global title example 3");

        generalSettings.add(new ParametersItem("Title example 1", "Description example 1", false));
        generalSettings.add(new ParametersItem("Title example 2", "Description example 2", false));
        generalSettings.add(new ParametersItem("Title example 3", "Description example 3", false));

        userCommands.add(new ParametersItem("Title example 1", "Description example 1", false));
        userCommands.add(new ParametersItem("Title example 2", "Description example 2", false));
        userCommands.add(new ParametersItem("Title example 3", "Description example 3", false));

        thirdGroup.add(new ParametersItem("Title example 1", "Description example 1", false));
        thirdGroup.add(new ParametersItem("Title example 2", "Description example 2", false));
        thirdGroup.add(new ParametersItem("Title example 3", "Description example 3", false));


        listChildData.put(listGroupTitles.get(0), generalSettings);
        listChildData.put(listGroupTitles.get(1), userCommands);
        listChildData.put(listGroupTitles.get(2), thirdGroup);
    }
}