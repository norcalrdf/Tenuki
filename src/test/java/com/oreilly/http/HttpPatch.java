package com.oreilly.http;

import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;

public class HttpPatch extends HttpEntityEnclosingRequestBase {

    @Override
    public String getMethod() {
        return "PATCH";
    }

}

