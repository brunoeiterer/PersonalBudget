package com.example.personalbudget;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class BudgetDataFileHandler {
    private static HashMap<String, File> budgetDataFileList;
    private static Context activityContext;

    public static void InitializeBudgetDataFileHandler(ArrayList<String> budgetDataNameList, Context newActivityContext) {
        BudgetDataFileHandler.budgetDataFileList = new HashMap<>();
        for (String key: budgetDataNameList) {
            activityContext = newActivityContext;
            File directory = activityContext.getFilesDir();
            BudgetDataFileHandler.budgetDataFileList.put(key, new File(directory, key + "budgetData.bin"));

            if(!BudgetDataFileHandler.budgetDataFileList.get(key).exists()) {
                try {
                    BudgetDataFileHandler.budgetDataFileList.get(key).createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void WriteBudgetDataToFile(String key, BudgetData budgetData) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(budgetDataFileList.get(key));
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(budgetData);
        objectOutputStream.close();
    }

    public static BudgetData ReadBudgetDataFromFile(String key) throws IOException, ClassNotFoundException {
        FileInputStream fileInputStream = new FileInputStream(budgetDataFileList.get(key));
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        BudgetData budgetData = (BudgetData)objectInputStream.readObject();
        objectInputStream.close();

        return budgetData;
    }
}
