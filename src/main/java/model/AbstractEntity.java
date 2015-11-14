package model;

/**
 * Created by nimrod on 5/24/15.
 */
public abstract class AbstractEntity {
    private int id;
    private String name;
    private String googleId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AbstractEntity that = (AbstractEntity) o;

        if (id != that.id) return false;
        return googleId.equals(that.googleId);

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + googleId.hashCode();
        return result;
    }

    public enum typeOf{
        Category,
        City,
        Country,
        Place,
        Vote
    };
    protected typeOf myType;

    public typeOf getType(){
        return this.myType;

    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGoogleId() {
        return googleId;
    }

    public void setGoogleId(String googleId) {
        this.googleId = googleId;
    }
}
