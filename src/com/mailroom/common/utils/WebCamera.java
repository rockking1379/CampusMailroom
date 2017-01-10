package com.mailroom.common.utils;

import com.github.sarxos.webcam.Webcam;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.RenderedImage;
import java.awt.image.WritableRaster;
import java.util.Hashtable;


/**
 * Class used to control access to the Web Cam.
 * This allows the application to take exclusive control
 * at startup and maintain control until shutdown.
 *
 * Created by James on 8/12/2016.
 */
public class WebCamera
{
    static WebCamera currentInstance;

    Webcam camera;

    /**
     * Singleton Constructor
     */
    private WebCamera()
    {
        try
        {
            Logger.logEvent("Attempting to get a Web Cam", "CAMERA");
            camera = Webcam.getDefault();
            Logger.logEvent("Attempting to Open Web Cam", "CAMERA");
            camera.open();
        }
        catch(Exception ex)
        {
            Logger.logException(ex);
        }
    }

    /**
     * Gets access to the current Web Cam
     * @return Web Cam that we have access to
     */
    public Webcam getCamera()
    {
        return camera;
    }

    /**
     * Gets an image from the Web Cam and returns the barcode result.
     * We assume that you are reading QR codes so the result will contain
     * the text value of the QR Code. or other barcode if applicable.
     * @return result of the QR Code. if no result was found then null is returned
     */
    public Result getResult()
    {
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(
                new BufferedImageLuminanceSource(convertRenderedImage(camera.getImage()))
        ));

        try
        {
            return new MultiFormatReader().decode(bitmap);
        }
        catch (NotFoundException nfe)
        {
            return null;
        }
    }

    /**
     * Used Internally to convert an image from Rendered to Buffered
     * @param img Image to convert
     * @return converted image
     */
    public BufferedImage convertRenderedImage(RenderedImage img) {
        if (img instanceof BufferedImage) {
            return (BufferedImage)img;
        }
        ColorModel cm = img.getColorModel();
        int width = img.getWidth();
        int height = img.getHeight();
        WritableRaster raster = cm.createCompatibleWritableRaster(width, height);
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        Hashtable properties = new Hashtable();
        String[] keys = img.getPropertyNames();
        if (keys!=null) {
            for (int i = 0; i < keys.length; i++) {
                properties.put(keys[i], img.getProperty(keys[i]));
            }
        }
        BufferedImage result = new BufferedImage(cm, raster, isAlphaPremultiplied, properties);
        img.copyData(raster);
        return result;
    }

    /**
     * Gets either the current instance or creates a new instance
     * @return an instance of this class
     */
    public static WebCamera getInstance()
    {
        Logger.logEvent("Camera Instance Requested", "CAMERA");
        if(currentInstance == null)
        {
            currentInstance = new WebCamera();
        }

        return currentInstance;
    }
}
