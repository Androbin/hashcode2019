import java.util.*;

public class Photo {
	private boolean vertical;
	private List<String> tags;
	private int id;

	public Photo(boolean vertical, List<String> tags, int id) {
		this.vertical = vertical;
		this.tags = tags;
		this.id = id;
	}

	public boolean isVertical() {
		return vertical;
	}

	public boolean isHorizontal() {
		return !vertical;
	}

	public List<String> getTags() {
		return tags;
	}

	public int getIdentifier() {
		return id;
	}

	public void set(boolean vertical, List<String> tags) {
		this.vertical = vertical;
		this.tags = tags;
	}
}
