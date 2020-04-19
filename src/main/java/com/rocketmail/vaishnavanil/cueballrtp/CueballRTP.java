package com.rocketmail.vaishnavanil.cueballrtp;

import com.rocketmail.vaishnavanil.cueballrtp.command.CommandRTP;
import com.rocketmail.vaishnavanil.cueballrtp.utils.ConfigHandler;
import com.rocketmail.vaishnavanil.cueballrtp.utils.CooldownHandler;
import com.rocketmail.vaishnavanil.cueballrtp.utils.SafeTeleportUtil;
import org.bukkit.plugin.java.JavaPlugin;

public final class CueballRTP extends JavaPlugin {

    private static CueballRTP instance;

    public static CueballRTP getInstance() {
        return instance;
    }


    @Override
    public void onEnable() {
        instance = this;
        config = new ConfigHandler();
        cd = new CooldownHandler("CooldownData");
        stu = new SafeTeleportUtil();

        config.setupDefaults();
        config.load();

        cd.load();
        cd.cleanUp();
        getServer().getPluginCommand("rtp").setExecutor(new CommandRTP());
    }

    @Override
    public void onDisable() {

        cd.cleanUp();
        cd.save();
    }

    private ConfigHandler config;
    private CooldownHandler cd;
    private SafeTeleportUtil stu;

    public static ConfigHandler getConfigHandler(){
        return getInstance().config;
    }
    public static CooldownHandler getCooldownHandler(){
        return getInstance().cd;
    }
    public static SafeTeleportUtil getSafeTPUtil(){
        return getInstance().stu;
    }

}
