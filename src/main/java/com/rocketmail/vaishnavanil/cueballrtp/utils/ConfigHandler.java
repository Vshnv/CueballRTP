package com.rocketmail.vaishnavanil.cueballrtp.utils;

import com.rocketmail.vaishnavanil.cueballrtp.CueballRTP;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class ConfigHandler {
    private int radius = 5000;
    private String msgSuccess = colorize("&cYou were teleported to %x%,%y%,%z% in world %world%!"),
                    msgNoWorldPerm= colorize("&cYou do not have permission to use rtp in this world: %world%!"),
                    msgInCooldown= colorize("&cYou may use this command again in %cooldown% hours!");
    private double cdHours = 24d;
    public void setupDefaults(){
        FileConfiguration config = CueballRTP.getInstance().getConfig();
        config.addDefault("Radius",radius);
        config.addDefault("Cooldown-In-Hours",cdHours);
        config.addDefault("Messages.Success",msgSuccess);
        config.addDefault("Messages.NoWorldPerm",msgNoWorldPerm);
        config.addDefault("Messages.InCooldown",msgInCooldown);
        config.options().copyDefaults(true);
        CueballRTP.getInstance().saveConfig();
    }

    public void load(){
        FileConfiguration config = CueballRTP.getInstance().getConfig();
        radius = config.getInt("Radius");
        cdHours = config.getDouble("Cooldown-In-Hours");
        msgSuccess = colorize(config.getString("Messages.Success"));
        msgNoWorldPerm = colorize(config.getString("Messages.NoWorldPerm"));
        msgInCooldown = colorize(config.getString("Messages.InCooldown"));

    }



    private static String colorize(String s){
        return ChatColor.translateAlternateColorCodes('&',s);
    }

    public int getRadius(){
        return radius;
    }
    public double getCooldown(){
        return cdHours;
    }
    public String getMsgSuccess(Player p,  Location l){
        return replacePlaceholders(msgSuccess,p,l);
    }
    public String getMsgNoWorldPerm(Player p,  Location l){
        return replacePlaceholders(msgNoWorldPerm,p,l);
    }
    public String getMsgInCooldown(Player p,  Location l){
        return replacePlaceholders(msgInCooldown,p,l).replaceAll("(?i)%cooldown%" , String.format("%.2f",CueballRTP.getCooldownHandler().getCDTimeLeft(p)));
    }

    public String replacePlaceholders(String msg,Player p ,Location l){
        return msg
                .replaceAll("(?i)%player%",p.getName())
                .replaceAll("(?i)%x%", String.valueOf(l.getBlockX()))
                .replaceAll("(?i)%y%",String.valueOf(l.getBlockY()))
                .replaceAll("(?i)%z%",String.valueOf(l.getBlockZ()))
                .replaceAll("(?i)%world%",l.getWorld().getName());
    }
}
