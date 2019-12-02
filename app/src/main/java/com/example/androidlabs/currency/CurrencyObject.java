package com.example.androidlabs.currency;

public class CurrencyObject {

    /**
     * name of currency.
     */
    private String name;
    /**
     * database id.
     */
    private long id;
    /**
     * What currency to convert to in favourite, if any.
     */
    private String favourite;

    /**
     * Constructor to init name in new object.
     *
     * @param name initialize name.
     */
    public CurrencyObject(String name) {
        this.name = name;
    }

    /**
     * Parameterized constructor to init new object with name, favourite and id. TO insert into array list and database.
     *
     * @param id        database id assigned.
     * @param name      name of currency.
     * @param favourite Name of favourite if it has one.
     */
    public CurrencyObject(long id, String name, String favourite) {
        this.id = id;
        this.name = name;
        this.favourite = favourite;
    }


    /**
     * @return this object's name.
     */
    public String getName() {
        return name;
    }

    /**
     * @return this objects database id.
     */
    public long getId() {
        return id;
    }

    /**
     * @return currency favourite.
     */
    public String getFavourite() {
        return favourite;
    }

    /**
     * @return name of this currency.
     */
    @Override
    public String toString() {
        return this.name;
    }
}
