package com.vtmer.yisanbang.dto;

public class UserAddressDto {
    private Integer id;

    private Integer userId;

    private String addressName;

    private String phoneNumber;

    private Boolean isDefault;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getAddressName() {
        return addressName;
    }

    public void setAddressName(String addressName) {
        this.addressName = addressName == null ? null : addressName.trim();
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber == null ? null : phoneNumber.trim();
    }

    public Boolean getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }

    public UserAddressDto(Integer id, Integer userId, String addressName, String phoneNumber, Boolean isDefault) {
        this.id = id;
        this.userId = userId;
        this.addressName = addressName;
        this.phoneNumber = phoneNumber;
        this.isDefault = isDefault;
    }

    @Override
    public String toString() {
        return "UserAddressDto{" +
                "id=" + id +
                ", userId=" + userId +
                ", addressName='" + addressName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", isDefault=" + isDefault +
                '}';
    }
}
