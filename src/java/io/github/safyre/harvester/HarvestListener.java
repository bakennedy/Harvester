package io.github.safyre.harvester;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;

public class HarvestListener implements org.bukkit.event.Listener {

	private ArrayList<Block> harvestedBlocks = new ArrayList<>();

	private final Random RANDOM = new Random();

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event){
		if(event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if(event.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.AIR) && event.getPlayer().getInventory().getItemInOffHand().getType().equals(Material.AIR)) {
				try {
					Block block = event.getClickedBlock();
					BlockData blockData = block.getBlockData();
					//Harvester.log(block.getType().name());
					if (Harvester.CROPS.contains(block.getType())) {
						if (blockData instanceof Ageable) { //Wheat, Beetroot, Carrot, Potato, or Netherwart
							if (((Ageable) blockData).getAge() == ((Ageable) blockData).getMaximumAge()) {
								Harvester.fireEvent(new HarvestEvent(event.getClickedBlock(), event.getPlayer()));
							}
						} else { //Melon or Pumpkin
							Harvester.fireEvent(new HarvestEvent(event.getClickedBlock(), event.getPlayer()));
						}
					}
				} catch (Exception ignored) {
					Harvester.log(Level.SEVERE, "Error in Player Interaction, interacted block was null");
				}
			}
		}
	}

	@EventHandler
	public void onHarvest(HarvestEvent event){
		BlockBreakEvent breakEvent = new BlockBreakEvent(event.getBlock(), event.getPlayer());
		harvestedBlocks.add(event.getBlock());
		Material originalType = event.getBlock().getType();
		if(Harvester.config.getBoolean("permission-required")) {
			if(event.getPlayer().hasPermission("harvester.harvest")) { //If the player needs the permission and has it
				doHarvest(event, breakEvent, originalType);
			}
		} else {
			doHarvest(event, breakEvent, originalType);
		}
	}

	private void doHarvest(HarvestEvent event, BlockBreakEvent breakEvent, Material originalType) {
		Harvester.fireEvent(breakEvent);
		if(!breakEvent.isCancelled()) { //If the event isn't cancelled
			if(originalType != Material.MELON && originalType != Material.PUMPKIN) {
				event.getBlock().setType(originalType);
				if(event.getBlock() instanceof Ageable){
					((Ageable) event.getBlock()).setAge(0);
				}
			} else {
				event.getBlock().setType(Material.AIR);
			}
		}
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event){
		if(!event.isCancelled()){
			if(harvestedBlocks.contains(event.getBlock())){ //If the block is being harvested by the harvest event
				harvestedBlocks.remove(event.getBlock());
				event.setDropItems(false); //Cancel the drops, just in case
				givePlayerItems(event.getPlayer(), event.getBlock()); //Give item drops
			}
		}
	}

	private void givePlayerItems(Player harvester, Block crop){
		//harvester.playSound(crop.getLocation(), Sound.BLOCK_CROP_BREAK, 1f, 1f);
		ArrayList<ItemStack> itemsToInventory = new ArrayList<>();
		Material type = crop.getType();

		switch (type){
			case WHEAT:
				itemsToInventory.add(new ItemStack(Material.WHEAT, 1));
				itemsToInventory.add(new ItemStack(Material.WHEAT_SEEDS, RANDOM.nextInt(3)));
				break;
			case BEETROOTS:
				itemsToInventory.add(new ItemStack(Material.BEETROOT, 1));
				itemsToInventory.add(new ItemStack(Material.BEETROOT_SEEDS, RANDOM.nextInt(3)));
				break;
			case CARROTS:
				itemsToInventory.add(new ItemStack(Material.CARROT, RANDOM.nextInt(5)));
				break;
			case POTATOES:
				itemsToInventory.add(new ItemStack(Material.POTATO, RANDOM.nextInt(4)));
				if(RANDOM.nextInt(100) < 2){ //2% chance
					itemsToInventory.add(new ItemStack(Material.POISONOUS_POTATO, 1));
				}
				break;
			case NETHER_WART:
				itemsToInventory.add(new ItemStack(Material.NETHER_WART, RANDOM.nextInt(3) + 1));
				break;
			case MELON:
				itemsToInventory.add(new ItemStack(Material.MELON_SLICE, RANDOM.nextInt(5)+3));
				break;
			case PUMPKIN:
				itemsToInventory.add(new ItemStack(Material.PUMPKIN, 1));
		}

		//Adds items to player's inventory or drops them on the ground if inventory is full
		for(ItemStack item : itemsToInventory){
			if(!harvester.getInventory().addItem(item).isEmpty()){ //If the item doesn't fit in the inventory
				crop.getWorld().dropItemNaturally(crop.getLocation(), item);
			}
		}
		harvester.playSound(harvester.getLocation(), Sound.ENTITY_ITEM_PICKUP, 1f, 1f);
	}

}
