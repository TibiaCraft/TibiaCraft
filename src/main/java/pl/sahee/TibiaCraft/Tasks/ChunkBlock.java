package pl.sahee.TibiaCraft.Tasks;


import pl.sahee.TibiaCraft.Builder.Block;

import java.util.Deque;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;

public class ChunkBlock {
  public Deque<Block> queue = new ConcurrentLinkedDeque<>();

  public void add(List<Block> block) {
    queue.addAll(block);
  }

  public Block getFirstBlock() {
    return queue.getFirst();
  }
}