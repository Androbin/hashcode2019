import java.util.*;

public abstract class Slide {
	private List<String> cachedTags;

	/**
	 * Get contained photos
	 */
	public abstract Photo[] getPhotos();

	/**
	 * Check if sane
	 */
	public abstract boolean sanity();

	/**
	 * @return Returns merged tags of {@link getPhotos}.
	 */
	public List<String> getTags() {
		return cachedTags;
	}

	protected void cacheTags() {
		cachedTags = new LinkedList<>();

		final Photo[] photos = getPhotos();

		for (final Photo photo : photos) {
			for (final String tag : photo.getTags()) {
				if (!cachedTags.contains(tag)) {
					cachedTags.add(tag);
				}
			}
		}
	}
}