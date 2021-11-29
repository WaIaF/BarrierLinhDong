package me.waiaf.barrierlinhdong;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public final class Main extends JavaPlugin implements Listener {

    public HashMap<Player, Block> playerLastBlock = new HashMap<>();

    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onNiggaMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (player.isFlying()) return;
        Block block = player.getLocation().getBlock();
        if (player.isSneaking() && !block.getType().equals(Material.WATER)) {
            if (playerLastBlock.get(player) == null || !playerLastBlock.containsKey(player)) {
                for (Player otherPlayers : Bukkit.getOnlinePlayers()) {
                    if (otherPlayers.getName().equals(player.getName())) return;
                    otherPlayers.sendBlockChange(player.getLocation(), Material.BARRIER.createBlockData());
                    if (otherPlayers.getNearbyEntities(0.1,1.25,0.1).contains(player)) {
                        otherPlayers.sendBlockChange(player.getLocation(), Material.AIR.createBlockData());
                    }
                    playerLastBlock.put(player, block);
                }

            } else if (playerLastBlock.get(player) != null && playerLastBlock.containsKey(player)) {
                for (Player otherPlayers : Bukkit.getOnlinePlayers()) {
                    if (!otherPlayers.getName().equals(player.getName())) return;
                    Block last = playerLastBlock.get(player);
                    Bukkit.getScheduler().runTask(this, () -> {
                        if (player.isFlying()) return;
                        otherPlayers.sendBlockChange(last.getLocation(), last.getType().createBlockData());
                        otherPlayers.sendBlockChange(player.getLocation(), Material.BARRIER.createBlockData());
                        if (otherPlayers.getNearbyEntities(0.1,1.25,0.1).contains(player)) {
                            otherPlayers.sendBlockChange(player.getLocation(), Material.AIR.createBlockData());
                        }
                        playerLastBlock.put(player, block);
                    });
                }
            }

        } else if (!player.isSneaking() && !block.getType().equals(Material.WATER)) {
            if (playerLastBlock.get(player) != null && playerLastBlock.containsKey(player)) {
                for (Player otherPlayers : Bukkit.getOnlinePlayers()) {
                    if (playerLastBlock.get(player) == null || otherPlayers.getName().equals(player.getName()))
                        continue;
                    Block last = playerLastBlock.get(player);
                    Bukkit.getScheduler().runTask(this, () -> {
                        otherPlayers.sendBlockChange(last.getLocation(), block.getType().createBlockData());
                        otherPlayers.sendBlockChange(player.getLocation(), last.getType().createBlockData());
                        playerLastBlock.remove(player);
                    });
                }

            } else if (playerLastBlock.get(player) == null || !playerLastBlock.containsKey(player)) {
                for (Player otherPlayers : Bukkit.getOnlinePlayers()) {
                    if (otherPlayers.getName().equals(player.getName())) continue;
                    otherPlayers.sendBlockChange(player.getLocation(), block.getType().createBlockData());
                }
            }
        }
    }
}