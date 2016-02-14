package fr.iutinfo.androidclient.bean;

public class User {

    private int id;
    private String name;
    private String alias;

    public User() {

    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    @Override
    public String toString() {
        return getAlias() == null || getAlias().isEmpty() ? getName() : getName() + " (" + getAlias() + ")";
    }

}
