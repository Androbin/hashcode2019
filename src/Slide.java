public abstract class Slide {
	public Slide() {}

	/** Get contained photos
	 */
	public abstract Photo[] getPhotos();

	/** Check if sane
	 */
	public abstract boolean sanity();
}
