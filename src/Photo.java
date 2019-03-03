import java.util.*;

public final class Photo {
	private final boolean vertical;
	private final List<String> tags;
	private final int id;

	public Photo(final boolean vertical, final List<String> tags, final int id) {
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
}
