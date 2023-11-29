package com.dcac.filesexplorer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ParametersActivity extends AppCompatActivity implements ParametersActivityExpandableListAdapter.OnThemeChangeListener  {

    private ExpandableListView expandableListView;
    private ParametersActivityExpandableListAdapter parametersActivityExpandableListAdapter;
    private List<String> listGroupTitles;
    private HashMap<String, List<ParametersItem>> listChildData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Appliquer le thème sélectionné
        applySelectedTheme();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parameters);


        prepareListData(); // Préparez vos données ici

        expandableListView = findViewById(R.id.expandableListView);
        parametersActivityExpandableListAdapter = new ParametersActivityExpandableListAdapter(this, listGroupTitles, listChildData, this);
        expandableListView.setAdapter(parametersActivityExpandableListAdapter);

        // Gestion des clics sur les groupes et les enfants, si nécessaire
    }

    // Préparer les données pour l'ExpandableListView
    private void prepareListData() {
        SharedPreferences prefs = getSharedPreferences("AppSettingsPrefs", MODE_PRIVATE);
        int selectedThemeIndex = prefs.getInt("SelectedThemeIndex", 0); // 0 est l'index par défaut pour BaseTheme

        listGroupTitles = new ArrayList<>();
        listChildData = new HashMap<>();

        List<ParametersItem> themes = new ArrayList<>();
        List<ParametersItem> userCommands = new ArrayList<>();
        List<ParametersItem> thirdGroup = new ArrayList<>();

        for (int i = 0; i < themes.size(); i++) {
            themes.get(i).setChecked(i == selectedThemeIndex);
        }

        listGroupTitles.add("Themes");
        listGroupTitles.add("Global title example 2");
        listGroupTitles.add("Global title example 3");

        themes.add(new ParametersItem("Base theme", "this theme is the basic theme", selectedThemeIndex == 0));
        themes.add(new ParametersItem("Light theme", "this theme is for the day", selectedThemeIndex == 1));
        themes.add(new ParametersItem("Dark theme", "this theme is for the night", selectedThemeIndex == 2));

        userCommands.add(new ParametersItem("Title example 1", "Description example 1", false));
        userCommands.add(new ParametersItem("Title example 2", "Description example 2", false));
        userCommands.add(new ParametersItem("Title example 3", "Description example 3", false));

        thirdGroup.add(new ParametersItem("Title example 1", "Description example 1", false));
        thirdGroup.add(new ParametersItem("Title example 2", "Description example 2", false));
        thirdGroup.add(new ParametersItem("Title example 3", "Description example 3", false));


        listChildData.put(listGroupTitles.get(0), themes);
        listChildData.put(listGroupTitles.get(1), userCommands);
        listChildData.put(listGroupTitles.get(2), thirdGroup);

    }

    public void changeTheme(String themeName) {
        SharedPreferences prefs = getSharedPreferences("AppSettingsPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("SelectedTheme", themeName);
        editor.apply();

        recreate(); // Redémarrer l'activité pour appliquer le thème
    }

    private void applySelectedTheme() {
        SharedPreferences prefs = getSharedPreferences("AppSettingsPrefs", MODE_PRIVATE);
        String themeName = prefs.getString("SelectedTheme", "BaseTheme"); // "BaseTheme" est le thème par défaut

        Log.d("ThemeApply", "Applying theme: " + themeName);
        if (themeName.equals("LightTheme")) {
            setTheme(R.style.LightTheme);
        } else if (themeName.equals("DarkTheme")) {
            setTheme(R.style.DarkTheme);
        } else {
            setTheme(R.style.BaseTheme);
        }
    }

    @Override
    public void onThemeChanged(String themeName) {
        Log.d("ThemeChange", "Changing theme to: " + themeName);
        changeTheme(themeName);
    }

}