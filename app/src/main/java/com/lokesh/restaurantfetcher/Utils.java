package com.lokesh.restaurantfetcher;

import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;

/**
 * Created by lokeshponnada on 10/19/16.
 */

public class Utils {

    static Spannable budgetSpan = new SpannableString("$$$$");


    public static Spannable getBudgetString(String budget) {

        budgetSpan.setSpan(new ForegroundColorSpan(Color.BLACK), 0, budget.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        budgetSpan.setSpan(new ForegroundColorSpan(Color.GRAY), budget.length(), budgetSpan.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);

        return budgetSpan;
    }

    public static Spannable getETADelivery(int etaMin) {
        Spannable etaDeliveryMin = new SpannableString(String.valueOf(etaMin) + " mins");
        etaDeliveryMin.setSpan(new ForegroundColorSpan(Color.GREEN), 0, etaDeliveryMin.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return etaDeliveryMin;
    }

    public static Spannable getSpannableString(String text, String rgbColor) {
        Spannable str = new SpannableString(text);
        str.setSpan(new ForegroundColorSpan(Color.parseColor(rgbColor)), 0, text.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        return str;
    }
}
