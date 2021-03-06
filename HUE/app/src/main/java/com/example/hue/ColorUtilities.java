package com.example.hue;

import android.graphics.Color;

public class ColorUtilities {
    public static float[] convertRGBtoHSB(int[] rgb) {
        float[] hsb = new float[3];
        Color.RGBToHSV(rgb[0],rgb[1],rgb[2],hsb);
        return hsb;
    }

    public static int[] convertHSBtoSeperateValues(float[] hsb) {
        return new int[] {
         (int)((double)hsb[0]/360.0 * 65536.0),
         (int)(hsb[1]*255.0),
         (int)(hsb[2]*255.0),
        };
    }

    public static int[] HSBtoRGB(float[] hsb) {
        hsb[0] = (hsb[0]/65536) * 360;
        int color = Color.HSVToColor(hsb);
        return new int[] {Color.red(color), Color.green(color), Color.blue(color)};

    }


    public static String hsvToRgb(float hue, float saturation, float value) {

        int h = (int)(hue * 6);
        float f = hue * 6 - h;
        float p = value * (1 - saturation);
        float q = value * (1 - f * saturation);
        float t = value * (1 - (1 - f) * saturation);

        switch (h) {
            case 0: return rgbToString(value, t, p);
            case 1: return rgbToString(q, value, p);
            case 2: return rgbToString(p, value, t);
            case 3: return rgbToString(p, q, value);
            case 4: return rgbToString(t, p, value);
            case 5: return rgbToString(value, p, q);
            default: throw new RuntimeException("Something went wrong when converting from HSV to RGB. Input was " + hue + ", " + saturation + ", " + value);
        }
    }

    public static String rgbToString(float r, float g, float b) {
        String rs = Integer.toHexString((int)(r * 256));
        String gs = Integer.toHexString((int)(g * 256));
        String bs = Integer.toHexString((int)(b * 256));
        return rs + gs + bs;
    }
}
