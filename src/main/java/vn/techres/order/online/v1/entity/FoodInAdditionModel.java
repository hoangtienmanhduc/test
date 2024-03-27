package vn.techres.order.online.v1.entity;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class FoodInAdditionModel {


    @JsonProperty("id")
    private int id;

    @JsonProperty("name")
    private String name;
}
