package com.briar.server.model.domainmodelclasses;

public class UserContact {
    private long id;
    private String firstUserName;
    private long firstUserId;
    private boolean firstUserContactAcceptance;
    private String secondUserName;
    private long secondUserId;
    private boolean secondUserContactAcceptance;

    public UserContact() {
        // Default constructor to allow for injection of params by libraries
    }

    public UserContact(String firstUserName, long firstUserId, boolean firstUserContactAcceptance, String secondUserName, long secondUserId, boolean secondUserContactAcceptance) {
        this.firstUserName = firstUserName;
        this.firstUserId = firstUserId;
        this.firstUserContactAcceptance = firstUserContactAcceptance;
        this.secondUserName = secondUserName;
        this.secondUserId = secondUserId;
        this.secondUserContactAcceptance = secondUserContactAcceptance;
    }

    public UserContact(long id, String firstUserName, long firstUserId, boolean firstUserContactAcceptance, String secondUserName, long secondUserId, boolean secondUserContactAcceptance) {
        this.id = id;
        this.firstUserName = firstUserName;
        this.firstUserId = firstUserId;
        this.firstUserContactAcceptance = firstUserContactAcceptance;
        this.secondUserName = secondUserName;
        this.secondUserId = secondUserId;
        this.secondUserContactAcceptance = secondUserContactAcceptance;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstUserName() {
        return firstUserName;
    }

    public void setFirstUserName(String firstUserName) {
        this.firstUserName = firstUserName;
    }

    public boolean isFirstUserContactAcceptance() {
        return firstUserContactAcceptance;
    }

    public void setFirstUserContactAcceptance(boolean firstUserContactAcceptance) {
        this.firstUserContactAcceptance = firstUserContactAcceptance;
    }

    public String getSecondUserName() {
        return secondUserName;
    }

    public void setSecondUserName(String secondUserName) {
        this.secondUserName = secondUserName;
    }

    public boolean isSecondUserContactAcceptance() {
        return secondUserContactAcceptance;
    }

    public void setSecondUserContactAcceptance(boolean secondUserContactAcceptance) {
        this.secondUserContactAcceptance = secondUserContactAcceptance;
    }

    public long getFirstUserId() {
        return firstUserId;
    }

    public void setFirstUserId(long firstUserId) {
        this.firstUserId = firstUserId;
    }

    public long getSecondUserId() {
        return secondUserId;
    }

    public void setSecondUserId(long secondUserId) {
        this.secondUserId = secondUserId;
    }

    public String getOtherUser(String user) {
        if (user.equals(this.firstUserName)) {
            return this.secondUserName;
        } else {
            return this.firstUserName;
        }
    }

    public boolean isContactBilateral() {
        return firstUserContactAcceptance && secondUserContactAcceptance;
    }

    @Override
    public String toString() {
        return "UserContact{" +
                "id=" + id +
                ", firstUserName='" + firstUserName + '\'' +
                ", firstUserId=" + firstUserId +
                ", firstUserContactAcceptance=" + firstUserContactAcceptance +
                ", secondUserName='" + secondUserName + '\'' +
                ", secondUserId=" + secondUserId +
                ", secondUserContactAcceptance=" + secondUserContactAcceptance +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserContact)) return false;

        UserContact that = (UserContact) o;

        if (getId() != that.getId()) return false;
        if (getFirstUserId() != that.getFirstUserId()) return false;
        if (isFirstUserContactAcceptance() != that.isFirstUserContactAcceptance()) return false;
        if (getSecondUserId() != that.getSecondUserId()) return false;
        if (isSecondUserContactAcceptance() != that.isSecondUserContactAcceptance()) return false;
        if (getFirstUserName() != null ? !getFirstUserName().equals(that.getFirstUserName()) : that.getFirstUserName() != null)
            return false;
        return getSecondUserName() != null ? getSecondUserName().equals(that.getSecondUserName()) : that.getSecondUserName() == null;

    }

    @Override
    public int hashCode() {
        int result = (int) (getId() ^ (getId() >>> 32));
        result = 31 * result + (getFirstUserName() != null ? getFirstUserName().hashCode() : 0);
        result = 31 * result + (int) (getFirstUserId() ^ (getFirstUserId() >>> 32));
        result = 31 * result + (isFirstUserContactAcceptance() ? 1 : 0);
        result = 31 * result + (getSecondUserName() != null ? getSecondUserName().hashCode() : 0);
        result = 31 * result + (int) (getSecondUserId() ^ (getSecondUserId() >>> 32));
        result = 31 * result + (isSecondUserContactAcceptance() ? 1 : 0);
        return result;
    }

    @Override
    public UserContact clone() {
        return new UserContact(id, firstUserName, firstUserId, firstUserContactAcceptance, secondUserName, secondUserId, secondUserContactAcceptance);
    }

    public void copy(UserContact userContact) {
        this.id = userContact.getId();
        this.firstUserName = userContact.getFirstUserName();
        this.firstUserId = userContact.getFirstUserId();
        this.firstUserContactAcceptance = userContact.isFirstUserContactAcceptance();
        this.secondUserName = userContact.getSecondUserName();
        this.secondUserId = userContact.getSecondUserId();
        this.secondUserContactAcceptance = userContact.isSecondUserContactAcceptance();
    }
}
