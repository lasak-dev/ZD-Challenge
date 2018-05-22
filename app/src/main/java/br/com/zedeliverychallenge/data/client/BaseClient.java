package br.com.zedeliverychallenge.data.client;

import android.support.annotation.NonNull;

import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.response.CustomTypeAdapter;
import com.apollographql.apollo.response.CustomTypeValue;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import br.com.zedeliverychallenge.BuildConfig;
import br.com.zedeliverychallenge.type.CustomType;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

import static okhttp3.logging.HttpLoggingInterceptor.Level.BODY;

class BaseClient {

    final ApolloClient apolloClient;

    BaseClient() {
        HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor();
        logInterceptor.setLevel(BuildConfig.DEBUG ? BODY : HttpLoggingInterceptor.Level.NONE);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(logInterceptor)
                .build();

        final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.getDefault());
        CustomTypeAdapter<Date> dateTypeAdapter = new CustomTypeAdapter<Date>() {
            @Override
            public Date decode(CustomTypeValue value) {
                try {
                    return dateFormat.parse(value.value.toString());
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public CustomTypeValue encode(@NonNull Date value) {
                return new CustomTypeValue.GraphQLString(dateFormat.format(value));
            }
        };

        apolloClient = ApolloClient.builder()
                .serverUrl("https://803votn6w7.execute-api.us-west-2.amazonaws.com/dev/public/graphql")
                .okHttpClient(okHttpClient)
                .addCustomTypeAdapter(CustomType.DATETIME, dateTypeAdapter)
                .build();
    }
}
