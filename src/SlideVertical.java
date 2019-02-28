public class SlideVertical extends Slide {
	private Photo left, right;

	public SlideVertical(Photo left, Photo right) {
		this.left = left;
		this.right = right;
	}

	public Photo getLeft() {
		return left;
	}

	public Photo getRight() {
		return right;
	}

	@Override
	public Photo[] getPhotos() {
		return new Photo[] { left, right };
	}

	@Override
	public boolean sanity() {
		return getLeft().isVertical() && getRight().isVertical();
	}

	@Override
	public String toString() {
		return left.getIdentifier() + " " + right.getIdentifier();
	}
}