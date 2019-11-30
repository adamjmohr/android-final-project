package com.example.androidlabs.currency;

public class CurrencyObject {

    /**
     *
     */
    private String name;
    /**
     *
     */
    private long id;
    /**
     *
     */
    private String favourite;

    /**
     * @param name
     */
    public CurrencyObject(String name) {
        this.name = name;
    }

    /**
     * @param id
     * @param name
     * @param favourite
     */
    public CurrencyObject(long id, String name, String favourite) {
        this.id = id;
        this.name = name;
        this.favourite = favourite;
    }


    /**
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * @return
     */
    public long getId() {
        return id;
    }

    /**
     * @return
     */
    public String getFavourite() {
        return favourite;
    }

    /**
     * @return
     */
    @Override
    public String toString() {
        return this.name;
    }
}
