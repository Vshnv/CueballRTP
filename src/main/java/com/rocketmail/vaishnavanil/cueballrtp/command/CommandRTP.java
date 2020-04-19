package com.rocketmail.vaishnavanil.cueballrtp.command;

import com.rocketmail.vaishnavanil.cueballrtp.CueballRTP;
import com.rocketmail.vaishnavanil.cueballrtp.utils.CooldownHandler;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.time.Instant;

public class CommandRTP implements CommandExecutor {
    private Instant lastUse = Instant.now();
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(Duration.between(lastUse,Instant.now()).toHours() > 2f){
            CueballRTP.getCooldownHandler().cleanUp();
        }
        if(!(sender instanceof Player)){
            sender.sendMessage("This command is limited to Players.");
            return true;
        }
        Player p = (Player) sender;
        if(!p.hasPermission("rtp.use")){// Safety check  - Already handled by plugin.yml perm
            return true;
        }
        String worldName = p.getLocation().getWorld().getName();
        if(!p.hasPermission("rtp.world."+worldName) && !p.hasPermission("rtp.world.*")){// World rtp permission check
            sender.sendMessage(CueballRTP.getConfigHandler().getMsgNoWorldPerm(p,p.getLocation()));
            return true;
        }
        if(!p.hasPermission("rtp.nocooldown")) {
            if (CueballRTP.getCooldownHandler().isInCooldown(p)) {
                sender.sendMessage(CueballRTP.getConfigHandler().getMsgInCooldown(p, p.getLocation()));
                return true;
            }
        }
        if(CueballRTP.getSafeTPUtil().safeTP(p,CueballRTP.getConfigHandler().getRadius())){
            CueballRTP.getCooldownHandler().registerUse(p);
            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING,1f,1f);
        }
        return true;
    }
}
