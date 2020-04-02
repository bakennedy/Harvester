package io.github.safyre.harvester;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class HarvestEvent extends Event implements Cancellable {

	private boolean cancelled = false;
	private Player player;
	private Block block;
	private static final HandlerList HANDLERS = new HandlerList();

	public HarvestEvent(Block block, Player player){
		this.block = block;
		this.player = player;
	}


	public HandlerList getHandlers() {
		return HANDLERS;
	}

	public static HandlerList getHandlerList(){
		return HANDLERS;
	}

	/**
	 * Gets the player that harvested the block
	 * @return player that harvested the block
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * Gets the block that was harvested
	 * @return block that was harvested
	 */
	public Block getBlock(){
		return block;
	}

	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled(boolean cancel) {
		cancelled = cancel;
	}
}
