public class SolverSanity {

	private SolverSanity() {}

	public static boolean sanity(List<Slide> slides) {
		List<Integer> photoIdentifiers = new LinkedList<>();
		for (Slide slide : slides) {
			for (Photo photo : slide.getPhotos()) {
				if (photoIdentifiers.contains(photo.getIdentifier()))
					return false;

				photoIndentifiers.add(photo.getIdentifier());
		}

		return true;
	}
}
