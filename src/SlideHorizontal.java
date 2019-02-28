public class SlideHorizontal extends Photo {
	private Photo photo;

	@Override
	public Photo[] getPhotos() {
		return new Photo[]{ photo };
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
