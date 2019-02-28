import java.util.*;

public abstract class Slide {
	public Slide() {}

	/** Get contained photos
	 */
	public abstract Photo[] getPhotos();

	/** Check if sane
	 */
	public abstract boolean sanity();

	/**
	 * @return Returns merged tags of {@link getPhotos}.
	 */
	public List<String> getTags() {
		List<String> result = new LinkedList<>();
		Photo[] photos = getPhotos();
		for (Photo photo : photos) {
			for (String tag : photo.getTags()) {
				if (!result.contains(tag))
					result.add(tag);
			}
		}

		return result;
	}
}
