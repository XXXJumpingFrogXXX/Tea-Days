package top.nkchayuan.teadaysbackend.manager;

import top.nkchayuan.teadaysbackend.domain.Soil;
import top.nkchayuan.teadaysbackend.mapper.PlantMapper;
import top.nkchayuan.teadaysbackend.static_data.PlantStaticData;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Random;

public class PlantManager {
    private final PlantMapper plantMapper;
    public PlantManager(PlantMapper mapper) {
        this.plantMapper = mapper;
    }

    public void plant(int userId, int soilId, int speciesId) {
        ArrayList<Soil> plant = plantMapper.readOne(userId, soilId);
        if (plant.isEmpty()) {
            plantMapper.insert(userId, soilId, 0, 0);
        }
        plantMapper.update(speciesId, getTime(), userId, soilId, 0);
    }

    public void water(int userId, int soilId) {
        ArrayList<Soil> plant = plantMapper.readOne(userId, soilId);
        if (plant.isEmpty() || plant.get(0).getPlant_species() <= 0)
            return;
        int newProgress = plant.get(0).getProgress() + new Random().nextInt(3, 6);
        int maxProgress = PlantStaticData.lookup.get(plant.get(0).getPlant_species()).time;
        if (newProgress > maxProgress)
            newProgress = maxProgress;
        plantMapper.update(plant.get(0).getPlant_species(), getTime(), userId, soilId, newProgress);
    }

    public int getStage(int userId, int soilId) {
        ArrayList<Soil> plant = plantMapper.readOne(userId, soilId);
        if (plant.isEmpty() || plant.get(0).getPlant_species() <= 0)
            return 0;
        int maxProgress = PlantStaticData.lookup.get(plant.get(0).getPlant_species()).time;
        int progress = plant.get(0).getProgress();
        int ret = progress * 2 / maxProgress;
        if (progress >= maxProgress) ret = 2;
        return ret;
    }

    public boolean isOccupied(int userId, int soilId) {
        ArrayList<Soil> plant = plantMapper.readOne(userId, soilId);
        return !plant.isEmpty() && plant.get(0).getPlant_species() != 0;
    }

    public void updateSoil(int userId, int soilId) {
        ArrayList<Soil> plant = plantMapper.readOne(userId, soilId);
        if (plant.isEmpty()) {
            plantMapper.insert(userId, soilId, 0, 0);
            return;
        }
        int newProgress = plant.get(0).getProgress();
        int newTime = getTime();
        int oldTime = plant.get(0).getUpdate_time();
        int species = plant.get(0).getPlant_species();
        if (species == -1 || species == 0)
            return;
        if (newTime == oldTime)
            return;
        int maxProgress = PlantStaticData.lookup.get(species).time;
        for (int i = oldTime; i < newTime; ++i) {
            newProgress += 1;
            if (newProgress >= maxProgress) {
                newProgress = maxProgress;
                break;
            }
        }
        plantMapper.update(plant.get(0).getPlant_species(), newTime, userId, soilId, newProgress);
    }

    public int getSpeciesId(int userId, int soilId) {
        ArrayList<Soil> plant = plantMapper.readOne(userId, soilId);
        if (plant.isEmpty())
            return 0;
        return plant.get(0).getPlant_species();
    }

    public int getProgress(int userId, int soilId) {
        ArrayList<Soil> plant = plantMapper.readOne(userId, soilId);
        if (plant.isEmpty() || plant.get(0).getPlant_species() <= 0)
            return -1;
        return plant.get(0).getProgress();
    }

    public static int getTime() {
        LocalDateTime start = LocalDateTime.of(2022, 11, 1, 0, 0);
        LocalDateTime now = LocalDateTime.now();
        int sYear = start.getYear();
        int nYear = now.getYear();
        int sDay = start.getDayOfYear();
        int nDay = now.getDayOfYear();
        int sHour = start.getHour();
        int nHour = now.getHour();

        int dayDis = 365 * (nYear - sYear) + (nDay - sDay);
        return 24 * dayDis + (nHour - sHour);
    }
}
