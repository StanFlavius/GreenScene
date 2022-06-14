package com.example.greenscene.Functionalities.Utils;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import java.util.Random;

public class Utils {
    public static ColorDrawable[] vibrantLightColorList =
            {
                    new ColorDrawable(Color.parseColor("#ffeead")),
                    new ColorDrawable(Color.parseColor("#93cfb3")),
                    new ColorDrawable(Color.parseColor("#fd7a7a")),
                    new ColorDrawable(Color.parseColor("#faca5f")),
                    new ColorDrawable(Color.parseColor("#1ba798")),
                    new ColorDrawable(Color.parseColor("#6aa9ae")),
                    new ColorDrawable(Color.parseColor("#ffbf27")),
                    new ColorDrawable(Color.parseColor("#d93947"))
            };

    public static ColorDrawable getRandomDrawbleColor() {
        int idx = new Random().nextInt(vibrantLightColorList.length);
        return vibrantLightColorList[idx];
    }

    public static String prettyFormatDate(String rawDate) {
        String formatedDate = rawDate.split("T")[0];
        String formatedHour = rawDate.split("T")[1].substring(0,6);

        return formatedHour + " / " + formatedDate;
    }
}
