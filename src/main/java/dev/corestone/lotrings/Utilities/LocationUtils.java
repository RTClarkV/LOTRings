package dev.corestone.lotrings.Utilities;

import org.bukkit.Location;
import org.bukkit.World;
import java.util.Random;

public class LocationUtils {

    private static final Random random = new Random();

    public static Location getRandomLocation(Location location, int range, boolean canBeInSky, boolean canBeUnderground) {
        World world = location.getWorld();
        if (world == null) {
            return null;
        }

        int randomX = location.getBlockX() + random.nextInt(range * 2) - range;
        int randomZ = location.getBlockZ() + random.nextInt(range * 2) - range;
        int randomY = location.getBlockY() + random.nextInt(range * 2) - range;

        if (randomY > world.getMaxHeight()) {
            randomY = world.getMaxHeight();
        } else if (randomY < world.getMinHeight()) {
            randomY = world.getMinHeight();
        }

        if (canBeInSky) {
            if (randomY < location.getBlockY()) {
                randomY = location.getBlockY() + random.nextInt(world.getMaxHeight() - location.getBlockY());
            }
        } else if (canBeUnderground) {
            if (randomY > location.getBlockY()) {
                randomY = location.getBlockY() - random.nextInt(location.getBlockY() - world.getMinHeight());
            }
        } else {
            randomY = world.getHighestBlockYAt(randomX, randomZ);
        }


        return new Location(world, randomX + 0.5, randomY + 0.5, randomZ + 0.5);
    }

    private static boolean isValidSurface(World world, int x, int y, int z) {
        return !world.getBlockAt(x, y, z).getType().isSolid() && world.getBlockAt(x, y - 1, z).getType().isSolid();
    }

    public static Location getRandomLocationInWorld(World world, boolean canBeInSky, boolean canBeUnderground) {
        int range = (int) (world.getWorldBorder().getSize() / 2);
        Location center = world.getSpawnLocation();
        return getRandomLocation(center, range, canBeInSky, canBeUnderground);
    }

    public static Location getRandomLocationInRadius(Location center, int radius, boolean canBeInSky, boolean canBeUnderground) {
        double angle = random.nextDouble() * 2 * Math.PI;
        double distance = radius * Math.sqrt(random.nextDouble());
        int randomX = center.getBlockX() + (int) (distance * Math.cos(angle));
        int randomZ = center.getBlockZ() + (int) (distance * Math.sin(angle));
        return getRandomLocation(new Location(center.getWorld(), randomX, center.getY(), randomZ), radius, canBeInSky, canBeUnderground);
    }

    public static Location getRandomLocationInCube(Location center, int edgeLength) {
        int halfEdge = edgeLength / 2;

        int randomX = center.getBlockX() + random.nextInt(edgeLength) - halfEdge;
        int randomY = center.getBlockY() + random.nextInt(edgeLength) - halfEdge;
        int randomZ = center.getBlockZ() + random.nextInt(edgeLength) - halfEdge;

        if (randomY > center.getWorld().getMaxHeight()) {
            randomY = center.getWorld().getMaxHeight();
        } else if (randomY < center.getWorld().getMinHeight()) {
            randomY = center.getWorld().getMinHeight();
        }

        return new Location(center.getWorld(), randomX + 0.5, randomY + 0.5, randomZ + 0.5);
    }
}


