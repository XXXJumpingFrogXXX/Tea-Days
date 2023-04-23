package top.nkchayuan.teadaysbackend.domain;

import lombok.Data;

@Data
public class Soil {
    private Integer soil_id;
    private Integer user_id;
    private Integer update_time;
    private Integer plant_species;
    private Integer progress;
}