package com.vtmer.yisanbang.dto;

public class UserDTO {
    private Integer id;

    private String openId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId == null ? null : openId.trim();
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "id=" + id +
                ", openId='" + openId + '\'' +
                '}';
    }

    public UserDTO(Integer id, String openId) {
        this.id = id;
        this.openId = openId;
    }
}
