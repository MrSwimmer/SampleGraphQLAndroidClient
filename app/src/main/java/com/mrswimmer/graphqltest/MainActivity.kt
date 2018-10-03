package com.mrswimmer.graphqltest

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.ApolloCallback
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import okhttp3.OkHttpClient

class MainActivity : AppCompatActivity() {

    val TAG = "code"

    private fun setupApollo(): ApolloClient {
        val okHttp = OkHttpClient
                .Builder()
                .addInterceptor { chain ->
                    val original = chain.request()
                    val builder = original.newBuilder().method(original.method(),
                            original.body())
                    builder.addHeader("Content-Type"
                            , "application/graphql")
                    chain.proceed(builder.build())
                }
                .build()
        return ApolloClient.builder()
                .serverUrl("http://192.168.0.95:3000/")
                .okHttpClient(okHttp)
                .build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val client = setupApollo()
        client.query(Sample
                .builder()
                .build())
                .enqueue(object : ApolloCall.Callback<Sample.Data>() {
                    override fun onFailure(e: ApolloException) {
                        Log.i(TAG, "apollo error ${e.message}")
                    }

                    override fun onResponse(response: Response<Sample.Data>) {
                        Log.i(TAG, "apollo success ${response.data()}")
                    }

                })
    }
}
