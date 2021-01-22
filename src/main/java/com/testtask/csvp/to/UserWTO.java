package com.testtask.csvp.to;

import com.opencsv.bean.CsvBindByName;

/**
    User Web Transport Object
    Initially, we accept all incoming data as strings and without required = true.
    This is necessary to validate each field separately and return errors for each field.
 */
public class UserWTO {

    @CsvBindByName(column = "MSISDN")
    private String msisdn;

    @CsvBindByName(column = "SIM_TYPE")
    private String symType;

    @CsvBindByName(column = "NAME")
    private String name;

    @CsvBindByName(column = "DATE_OF_BIRTH")
    private String dateOfBirth;

    @CsvBindByName(column = "GENDER")
    private String gender;

    @CsvBindByName(column = "ADDRESS")
    private String address;

    @CsvBindByName(column = "ID_NUMBER")
    private String id;

    private boolean valid = true;

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getSymType() {
        return symType;
    }

    public void setSymType(String symType) {
        this.symType = symType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }
}
