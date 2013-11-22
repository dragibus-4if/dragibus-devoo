package model;

public class Client {

    private Long id;
    private String phoneNum;
    private String address;
    private String name;

    public Client(Long id, String phoneNum, String address, String name) {
        this.id = id;
        this.phoneNum = phoneNum;
        this.address = address;
        this.name = name;
    }

    public Client(long id) {
        this(new Long(id));
    }

    public Client(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
