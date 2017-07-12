package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

/**
 * Created by touseef.elahi on 11-Jul-17.
 */
public class Utility{
    public static int toggleBit(int value,int bitNumberToToggle){
        return value^ (1 << bitNumberToToggle );
    }
    public static boolean isBitSet(int value,int bitNumberToCheck){
        return ((value & (1 << bitNumberToCheck)) != 0);
    }
    public static boolean setAffinityForWindows(int processID, int affinity){
        boolean status=false;
        try {
            String currentDir=System.getProperty("user.dir");
            Process process = Runtime.getRuntime().exec(currentDir + "/src/Resources/ProcessRestrictorConsole.exe " + processID + " " + affinity);
            int valueExit = process.waitFor();
            if(valueExit==0)status = true;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return status;
    }
    public static boolean setAffinityForWindows(String processName, int affinity){
        boolean status=false;
        String[]processString = processName.split(".exe");
        //if(processString.length>1)
        processName=processString[0];
        try {
            String currentDir=System.getProperty("user.dir");
            Process process = Runtime.getRuntime().exec(currentDir + "/src/Resources/ProcessRestrictorConsole.exe " + processName + " " + affinity);
            int valueExit = process.waitFor();
            if(valueExit==0)status = true;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return status;
    }
    public static List<String> listRunningProcesses() {
        List<String> processes = new ArrayList<String>();
        try {
            String line;
            Process p = Runtime.getRuntime().exec("tasklist.exe /fo csv /nh");
            BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
            while ((line = input.readLine()) != null) {
                if (!line.trim().equals("")) {
                    // keep only the process name
                    line = line.substring(1);
                    processes.add(line.substring(0, line.indexOf("\"")));
                }
            }
            input.close();
        } catch (Exception err) {
            err.printStackTrace();
        }
        return processes;
    }
    public static ObservableList<String[]> listRunningProcessesWithDetails() {
        ObservableList<String[]> processes = FXCollections.observableArrayList();

        try {
            String line;
            Process p = Runtime.getRuntime().exec("tasklist.exe /fo csv /nh");
            BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
            while ((line = input.readLine()) != null) {
                if (!line.trim().equals("")) {
                    String[] singleProcess = new String[2];
                    String[] listOfString = line.split(",");
                    singleProcess[0] = (listOfString[0].substring(1, listOfString[0].length()-1));
                    singleProcess[1] = (listOfString[1].substring(1, listOfString[1].length()-1));
                    processes.add(singleProcess);
                }
            }
            input.close();
        } catch (Exception err) {
            err.printStackTrace();
        }
        return processes;
    }
}
