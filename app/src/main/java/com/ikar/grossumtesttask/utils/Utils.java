package com.ikar.grossumtesttask.utils;

import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

/**
 * Created by igorkarun on 2/27/17.
 */

public class Utils {

    private final static String TAG = Utils.class.getName();

    public static byte[] serializeObject(Object o) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        try {
            ObjectOutput out = new ObjectOutputStream(bos);
            out.writeObject(o);
            out.close();

            // Get the bytes of the serialized object
            byte[] buf = bos.toByteArray();

            return buf;
        } catch(IOException ioe) {
            Log.e(TAG, "error", ioe);

            return null;
        }
    }

    public static Object deserializeObject(byte[] b) {
        try {
            ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(b));
            Object object = in.readObject();
            in.close();

            return object;
        } catch(ClassNotFoundException cnfe) {
            Log.e(TAG, "class not found error", cnfe);

            return null;
        } catch(IOException ioe) {
            Log.e(TAG, "io error", ioe);

            return null;
        }
    }
}
