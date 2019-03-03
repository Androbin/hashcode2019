import java.util.*;

public final class SolverSanity {
	private SolverSanity() {
	}

	public static boolean sanity(final List<Slide> slides) {
		final Set<Integer> photoIdentifiers = new HashSet<>();

		for (final Slide slide : slides) {
			for (final Photo photo : slide.getPhotos()) {
				if (photoIdentifiers.contains(photo.getIdentifier())) {
					return false;
				}

				photoIdentifiers.add(photo.getIdentifier());
			}
		}

		return true;
	}
}