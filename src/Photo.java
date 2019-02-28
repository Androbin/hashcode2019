import java.util.*;

public class Photo {
	private boolean vertical;
	private List<String> tags;

	public Photo(boolean vertical, List<String> tags) {
		this.vertical = vertical;
		this.tags = tags;
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

	public void set(boolean vertical, List<String> tags) {
		this.vertical = vertical;
		this.tags = tags;
	}
}
