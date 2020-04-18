package com.rocketmail.vaishnavanil.cueballrtp.utils;

import com.rocketmail.vaishnavanil.cueballrtp.CueballRTP;
import org.bukkit.ChatColor;
import org.bukkit.HeightMap;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.concurrent.ThreadLocalRandom;



public class SafeTeleportUtil {


    private Material[] hazardousMaterials = {Material.LAVA,Material.CACTUS,Material.FIRE};

    /**
     *  Safely teleports player to random location within radius
     * @param p  Player to be teleported
     * @param radius Radius of teleport
     * @return
     */
    public boolean safeTP(Player p, int radius){
        return safeTP(p,radius,p.getLocation());
    }

    /**
     * In case center needs to be defined in any updates
     * @param p   Player to be teleported
     * @param radius  Radius of teleport
     * @param center  Center from where location will be chosen  / Currently Player Location
     * @return true: Teleport successfull   /    false: Teleport failed
     */
    public boolean safeTP(Player p, int radius, Location center){
        ThreadLocalRandom rnd = ThreadLocalRandom.current();
        int checkCount  = 0;
        boolean safe = false;
        safetyCheck:
        do {
            if(checkCount>5){
                p.sendMessage(ChatColor.RED + "Unable to find a suitable location to teleport you! Please try again.");
                return false;
            }
            checkCount++;
            int dx = rnd.nextInt(2 * radius) - radius;
            int dz = rnd.nextInt(2 * radius) - radius;

            Location tpLocation = center.clone().add(dx,0,dz);
            tpLocation.setY(tpLocation.getWorld().getHighestBlockYAt(tpLocation.getBlockX(),tpLocation.getBlockZ()));
            for(Material mat: hazardousMaterials){
                if(tpLocation.getBlock().getType() == mat)continue safetyCheck;
            }
            safe = true;
            tpLocation.setY(tpLocation.getY()+1);
            p.teleport(tpLocation);
            p.sendMessage(CueballRTP.getConfigHandler().getMsgSuccess(p,tpLocation));
        }while (!safe);
        return true;
    }
}
