package com.example.ayushmittal.records.objectclass;

import java.util.ArrayList;
import java.util.Date;

public class equipment {
    String name;
    String lab;
    String dop;
    String serialStart;
    String serialEnd;
    String consignor;
    float cost;
    int qtyreceipt;
    int qtyissued;
    int qtyStock;
    String nonworking;
    String description;
    String billnumber;
    String checkedby;
    String updatedby;

    public equipment(){

        description="";
    }


    public int getQtyissued() {
        return qtyissued;
    }


    public int getQtyStock() {
        return qtyStock;
    }

    public String getConsignor() {
        return consignor;
    }



    public String getSerialEnd() {
        return serialEnd;
    }

    public float getCost() {
        return cost;
    }

    public int getQtyreceipt() {
        return qtyreceipt;
    }

    public String getNonworking() {
        return nonworking;
    }

    public String getCheckedby() {
        return checkedby;
    }


    public String getUpdatedby() {
        return updatedby;
    }


    public String getBillnumber() {
        return billnumber;
    }

    public String getDescription() {
        return description;
    }

    public String getSerialStart() {
        return serialStart;
    }


    public String getName() {
        return name;
    }

    public String getLab() {
        return lab;
    }

    public String getDop() {
        return dop;
    }

    public void setConsignor(String consignor) {
        this.consignor = consignor;
    }
    public void setNonworking(String nonworking) {
        this.nonworking = nonworking;
    }
    public void setQtyissued(int qtyissued) {
        this.qtyissued = qtyissued;
    }

    public void setQtyStock(int qtyStock) {
        this.qtyStock = qtyStock;
    }
    public void setSerialStart(String serialStart) {
        this.serialStart = serialStart;
    }


    public void setCost(float cost) {
        this.cost = cost;
    }

    public void setDop(String dop) {
        this.dop = dop;
    }

    public void setLab(String lab) {
        this.lab = lab;
    }
    public void setCheckedby(String checkedby) {
        this.checkedby = checkedby;
    }
    public void setUpdatedby(String updatedby) {
        this.updatedby = updatedby;
    }

    public void setName(String name) {
        this.name = name;
    }



    public void setBillnumber(String billnumber) {
        this.billnumber = billnumber;
    }

    public void setDescription(String description) {
        this.description= description;
    }
    public void setQtyreceipt(int qtyeceipt) {
        this.qtyreceipt = qtyeceipt;
    }
    public void setSerialEnd(String serialEnd) {
        this.serialEnd = serialEnd;
    }


}
