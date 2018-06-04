package br.com.zedeliverychallenge.presentation.location;

import android.location.Address;
import android.support.annotation.NonNull;

public class AddressFormat {

    public static String full(@NonNull Address address) {
        StringBuilder builder = new StringBuilder();
        String thoroughfare = address.getThoroughfare();
        if (thoroughfare != null) {
            builder.append(thoroughfare);
            builder.append(" ");
        } else {
            thoroughfare = "";
        }
        String featureName = address.getFeatureName();
        if (featureName != null && !featureName.equals(thoroughfare)) {
            if (!thoroughfare.equals("")) {
                builder.append(", ");
            }
            builder.append(featureName);
            builder.append(" ");
        }
        if (address.getPostalCode() != null) {
            builder.append(" - ");
            builder.append(address.getPostalCode());
            builder.append(" ");
        }
        if (address.getSubLocality() != null) {
            builder.append(" - ");
            builder.append(address.getSubLocality());
        }
        if (address.getSubAdminArea() != null) {
            builder.append(" - ");
            builder.append(address.getSubAdminArea());
        }

        return builder.toString();
    }

    public static String simple(@NonNull Address address) {
        StringBuilder builder = new StringBuilder();
        String thoroughfare = address.getThoroughfare();
        if (thoroughfare != null) {
            builder.append(thoroughfare);
            builder.append(" ");
        } else {
            thoroughfare = "";
        }
        String featureName = address.getFeatureName();
        if (featureName != null && !featureName.equals(thoroughfare)) {
            if (!thoroughfare.equals("")) {
                builder.append(", ");
            }
            builder.append(featureName);
        }

        return builder.toString();
    }
}
