package io.github.safyre.harvester;

import org.bukkit.Location;

final class Utils {

	public static boolean sameLocation(Location l1, Location l2){
		if(l1.getBlockX() == l2.getBlockX()){
			if(l1.getBlockZ() == l2.getBlockZ()){
				return l1.getBlockY() == l2.getBlockY();
			}
			return false;
		}
		return false;
	}

	public static Location floorLocation(Location location){
		return new Location(location.getWorld(), location.getBlockX(), location.getBlockY(), location.getBlockZ());
	}

}
