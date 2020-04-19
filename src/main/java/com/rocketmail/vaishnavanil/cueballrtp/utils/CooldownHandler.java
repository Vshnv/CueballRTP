package com.rocketmail.vaishnavanil.cueballrtp.utils;

import com.rocketmail.vaishnavanil.cueballrtp.CueballRTP;
import org.bukkit.entity.Player;

import java.io.*;
import java.time.Duration;
import java.time.Instant;
import java.util.*;

public class CooldownHandler {
    private Map<UUID, Instant> epochMap;
    private File dataLoc;
    public CooldownHandler(String fileName){
        File dataFolder = CueballRTP.getInstance().getDataFolder();
        dataLoc = new File(dataFolder,fileName + ".dat");
        if(!dataFolder.exists()){
            dataFolder.mkdirs();
            return;
        }
    }

    public void load(){
        if(dataLoc.exists()){
            ObjectInputStream ois = null;
            try {
                FileInputStream fis = new FileInputStream(dataLoc);

                ois = new ObjectInputStream(fis);

                epochMap = (Map<UUID, Instant>) ois.readObject();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }finally {
                if(epochMap == null)epochMap = new HashMap<>();
                if(ois!=null) {
                    try {
                        ois.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }else{
            try {
                dataLoc.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            epochMap = new HashMap<>();
        }
    }
    public void save(){
        try {
            FileOutputStream fos = new FileOutputStream(dataLoc);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(epochMap);
            oos.flush();
            oos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public boolean isInCooldown(Player player){
        if(epochMap.containsKey(player.getUniqueId())){
            Instant then = epochMap.get(player.getUniqueId());
            return Duration.between(then,Instant.now()).toHours() < CueballRTP.getConfigHandler().getCooldown();
        }else {
            return false;
        }
    }

    public void registerUse(Player player){
        epochMap.put(player.getUniqueId(),Instant.now());
    }

    public float getCDTimeLeft(Player player){
        if(epochMap.containsKey(player.getUniqueId())){
            Instant then = epochMap.get(player.getUniqueId());
            float fin = (float) (CueballRTP.getConfigHandler().getCooldown() - Duration.between(then,Instant.now()).toHours());
            if(fin<0)return 0f;
            return fin;
        }else {
            return 0f;
        }
    }

    public void cleanUp(){
        if(epochMap.isEmpty())return;
        List<UUID> toClean = new ArrayList<>();
        for(UUID id: epochMap.keySet()){
            Instant then = epochMap.get(id);
            if(Duration.between(then,Instant.now()).toHours() >= CueballRTP.getConfigHandler().getCooldown()){
                toClean.add(id);
            }
        }
        if(toClean.isEmpty())return;
        toClean.forEach(c->epochMap.remove(c));
    }
}
