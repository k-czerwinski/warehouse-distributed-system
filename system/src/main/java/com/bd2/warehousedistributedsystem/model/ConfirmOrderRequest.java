package com.bd2.warehousedistributedsystem.model;

public record ConfirmOrderRequest(String ordererName, String shippingAddress, Warehouse warehouse) {}
