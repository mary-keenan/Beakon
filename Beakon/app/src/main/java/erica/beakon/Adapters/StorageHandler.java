package erica.beakon.Adapters;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Callable;

import erica.beakon.Objects.Movement;

public class StorageHandler {

    private static final String TAG = "STORAGE_HANDLER";
    public static final String POPULAR_MOVEMENTS_FILENAME = "movements";
    public static final String POPULAR_MOVEMENTS_RANKS_FILENAME = "movementsRanks";

    private Context context;

    public StorageHandler(Context context) {
        this.context = context;
    }

    public void savePopularMovements(ArrayList<Movement> popularMovements) {
        saveToInternalStorage(POPULAR_MOVEMENTS_FILENAME, popularMovements);
    }

    public void savePopularMovementsRanks(HashMap<String, Integer> movementPopularRanks) {
        saveToInternalStorage(POPULAR_MOVEMENTS_RANKS_FILENAME, movementPopularRanks);
    }

    public void saveToInternalStorage(String fileName, Object o) {
        try {
            File file = new File(getContext().getFilesDir(), fileName + ".ser");
            FileOutputStream fileOut = new FileOutputStream(file);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOut);
            objectOutputStream.writeObject(o);
            objectOutputStream.close();
            fileOut.close();
        } catch(Exception ex) {
            Log.d(TAG, ex.getMessage());
        }
    }

    public Object loadSavedObject(String fileName, Callable<Object> onError) {
        String filePath = getContext().getFilesDir() + "/" + fileName + ".ser";
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath));
            return ois.readObject(); //need to check for case where popularmovements was empty
        } catch (Exception ex) {
            Log.d(TAG, ex.getMessage());
            try {
                return onError.call();
            } catch (Exception e){
                Log.d(TAG, e.getMessage());
                return null;
            }

        }
    }

    private Context getContext() {
        return context;
    }
}
