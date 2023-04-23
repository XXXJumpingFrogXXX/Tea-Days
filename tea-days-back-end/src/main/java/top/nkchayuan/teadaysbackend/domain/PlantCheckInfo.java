package top.nkchayuan.teadaysbackend.domain;

import lombok.Data;

@Data
public class PlantCheckInfo {
    public Integer species;
    public Integer progress;
    public Integer maxProgress;
    public Integer stage;
}
