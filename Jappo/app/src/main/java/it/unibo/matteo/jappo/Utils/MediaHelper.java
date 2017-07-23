package it.unibo.matteo.jappo.Utils;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Class that writes in the phone the temp image of the completed {@link it.unibo.matteo.jappo.Model.Item}
 */
public class MediaHelper {

    public static final String TEMP_FOLDER = "tmp";


    public static void createFolder(Context context){
        File newDir= context.getDir(TEMP_FOLDER, Context.MODE_PRIVATE);
        if (!newDir.exists()){
            newDir.mkdirs();
        }
    }

    public static void deleteFolder(Context context){
        File dir = context.getDir(TEMP_FOLDER, Context.MODE_PRIVATE);
        if (dir.isDirectory()){
            String[] children = dir.list();
            for (String child: children) {
                new File(dir, child).delete();
            }
        }
    }

    public static void saveImage(Context context, Bitmap bmp, String name){
        ContextWrapper cw = new ContextWrapper(context);
        File directory = cw.getDir(TEMP_FOLDER, Context.MODE_PRIVATE);
        File myPath=new File(directory, name + ".jpg");
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(myPath);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static Bitmap loadImage(Context context, String name) {
        try {
            ContextWrapper cw = new ContextWrapper(context);
            File directory = cw.getDir(TEMP_FOLDER, Context.MODE_PRIVATE);
            File f = new File(directory, name + ".jpg");
            Bitmap bitmapImage = BitmapFactory.decodeStream(new FileInputStream(f));
            return bitmapImage;
        }
        catch (FileNotFoundException e){
            e.printStackTrace();
            return null;
        }
    }

    public static boolean hasImage(Context context, String name){
        ContextWrapper cw = new ContextWrapper(context);
        File directory = cw.getDir(TEMP_FOLDER, Context.MODE_PRIVATE);
        File file = new File(directory, name + ".jpg");
        return file.exists();
    }
}
