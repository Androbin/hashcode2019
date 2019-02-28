import java.util.*;

public final class Solver {
	private Solver() {
	}

	public static List<Slide> solve(final List<Photo> photos) {
		final List<Slide> slides = new ArrayList<Slide>();
		final List<List<Photo>> order = splitByOrder(photos);

		for (final List<Photo> orderPhotos : order) {
			slides.addAll(solveSameOrder(orderPhotos));
		}

		return slides;
	}

	public static List<Slide> solveSameOrder(final List<Photo> photos) {
		final Map<String, List<Photo>> tags1 = new HashMap<>();
		final Map<String, List<Photo>> tags2 = new HashMap<>();

		for (final Photo photo : photos) {
			for (final String tag1 : photo.getTags()) {
				tags1.computeIfAbsent(tag1, foo -> new ArrayList<>()).add(photo);

				for (final String tag2 : photo.getTags()) {
					if (tag2 == tag1) {
						continue;
					}

					final String tag12 = tag1.compareTo(tag2) > 0 ? tag1 : tag2;
					tags2.computeIfAbsent(tag12, foo -> new ArrayList<>()).add(photo);
				}
			}
		}

		return Collections.emptyList();
	}

	public static List<List<Photo>> splitByOrder(List<Photo> photos) {

		List<List<Photo>> output = new ArrayList<>(100);
		for (int i = 1; i <= 100; i++) {
			output.add(new ArrayList<>());
		}
		for(Photo photo : photos){
			output.get(photo.getTags().size()).add(photo);
		}
		return output;
		
	}
}