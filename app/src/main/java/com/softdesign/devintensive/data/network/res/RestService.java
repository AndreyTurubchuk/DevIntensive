package com.softdesign.devintensive.data.network.res;

import com.softdesign.devintensive.data.network.req.UserLoginReq;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RestService {
    @POST("login")
    Call <UserModelRes> loginUser (@Body UserLoginReq req);
}
