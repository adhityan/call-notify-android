package com.gamechange.adhityan.callspyplusplus.libs.api;

public interface apiInterface {
    void apiResponse(String response, String code);

    void apiError(String message, String code);

    void connectionError(int Status, String code);
}