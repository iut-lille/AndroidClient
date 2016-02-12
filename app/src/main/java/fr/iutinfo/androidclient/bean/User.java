package fr.iutinfo.androidclient.bean;

/**
 * Created by julien on 10/02/16.
 */
public class User {

    private int id;
    private String name;
    private String alias;

    public User() {

    }

    public int getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getAlias() {
        return alias;
    }

    @Override
    public String toString() {
        return getAlias() == null || getAlias().isEmpty() ? getName() : getName() + " (" + getAlias() + ")";
    }

}
