public final class SlideHorizontal extends Slide {
	private final Photo photo;

	public SlideHorizontal(final Photo photo) {
		this.photo = photo;
		cacheTags();
	}

	@Override
	public Photo[] getPhotos() {
		return new Photo[] { photo };
	}

	public Photo getPhoto() {
		return photo;
	}

	@Override
	public boolean sanity() {
		return photo.isHorizontal();
	}

	@Override
	public String toString() {
		return "" + photo.getIdentifier();
	}
}
