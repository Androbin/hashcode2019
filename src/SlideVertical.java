public final class SlideVertical extends Slide {
	private final Photo left;
	private final Photo right;

	public SlideVertical(final Photo left, final Photo right) {
		this.left = left;
		this.right = right;
		cacheTags();
	}

	@Override
	public Photo[] getPhotos() {
		return new Photo[] { left, right };
	}

	@Override
	public boolean sanity() {
		return left.isVertical() && right.isVertical();
	}

	@Override
	public String toString() {
		return left.getIdentifier() + " " + right.getIdentifier();
	}
}
