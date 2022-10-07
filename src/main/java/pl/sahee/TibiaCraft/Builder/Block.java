package pl.sahee.TibiaCraft.Builder;

import org.bukkit.Material;
import org.bukkit.block.data.BlockData;

import javax.annotation.Nullable;


public class Block {
  final public int x, y, z;
  final public Material material;
  @Nullable
  final public BlockData data;

  public Block(int x, int y, int z, Material material, BlockData data) {
    this.x = x;
    this.y = y;
    this.z = z;
    this.material = material;
    this.data = data;
  }

  @Override
  public String toString() {
    return "Block x:" + x + " y:" + y + " z:" + z + " material: " + material;
  }
}
