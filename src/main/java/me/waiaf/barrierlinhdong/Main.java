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

    public static HashMap<Player, Block> playerLastBlock = new HashMap<>();

    @Override
    public void onEnable() {

        this.getServer().getPluginManager().registerEvents(this, this);

    }

    @EventHandler
    public void onNiggaMove(PlayerMoveEvent event){

        if (!event.getPlayer().isFlying()){

            Player player = event.getPlayer();
            Block block = player.getLocation().getBlock();

            if (player.isSneaking() && !block.getType().equals(Material.WATER) && block.getType().equals(Material.AIR)){

                if (!playerLastBlock.containsKey(player)){

                    for (Player otherPlayers : Bukkit.getOnlinePlayers()){

                        if (otherPlayers != null){

                            if (!otherPlayers.getDisplayName().equals(player.getDisplayName())){

                                otherPlayers.sendBlockChange(player.getLocation(), Material.BARRIER.createBlockData());
                                playerLastBlock.put(player, block);

                            }

                        }

                    }

                } else if (playerLastBlock.containsKey(player)){

                    for (Player otherPlayers : Bukkit.getOnlinePlayers()){

                        if (otherPlayers != null){

                            if (!otherPlayers.getDisplayName().equals(player.getDisplayName())){

                                otherPlayers.sendBlockChange(playerLastBlock.get(player).getLocation(), Material.AIR.createBlockData());
                                otherPlayers.sendBlockChange(player.getLocation(), Material.BARRIER.createBlockData());
                                playerLastBlock.put(player, block);

                            }

                        }

                    }

                }

            } else if (!player.isSneaking() && !block.getType().equals(Material.WATER) && block.getType().equals(Material.AIR)){

                if (playerLastBlock.containsKey(player)){

                    for (Player otherPlayers : Bukkit.getOnlinePlayers()){

                        if (otherPlayers != null){

                            if (!otherPlayers.getDisplayName().equals(player.getDisplayName())){

                                otherPlayers.sendBlockChange(playerLastBlock.get(player).getLocation(), Material.AIR.createBlockData());

                            }

                            otherPlayers.sendBlockChange(player.getLocation(), Material.AIR.createBlockData());
                            playerLastBlock.remove(player);

                        }
                    }

                } else if (!playerLastBlock.containsKey(player)){

                    for (Player otherPlayers : Bukkit.getOnlinePlayers()){

                        if (otherPlayers != null){

                            if (!otherPlayers.getDisplayName().equals(player.getDisplayName())){

                                otherPlayers.sendBlockChange(player.getLocation(), Material.AIR.createBlockData());

                            }

                        }

                    }

                }

            }
        }

    }

}
