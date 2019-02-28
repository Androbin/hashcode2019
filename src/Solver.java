import java.util.*;
import java.util.stream.*;

public final class Solver {
	private Solver() {
	}

	private static List<Slide> getHorizontalSlides(final List<Photo> photos) {
		return photos.stream()
			.filter(Photo::isHorizontal)
			.map(photo -> new SlideHorizontal(photo))
			.collect(Collectors.toList());
	}

	private static List<Slide> getVerticalSlides(final List<Photo> photos) {
		List<Photo> verticalPhotos = photos.stream()
			.filter(Photo::isVertical)
			.sorted((photo0, photo1)
					-> Integer.compare(photo0.getTags().size(),
						               photo1.getTags().size()))
			.collect(Collectors.toList());
		// Last with first ...
		List<Slide> slides = new LinkedList<>();
		int firstIndex = 0, lastIndex = verticalPhotos.size() - 1;
		while (firstIndex < lastIndex) {
			Photo first = verticalPhotos.get(firstIndex);
			Photo last = verticalPhotos.get(lastIndex);
			slides.add(new VerticalSlide(first, last));
			firstIndex++; lastIndex--;
		}

		return slides;
	}

	public static List<slide> solve(final List<Photo> photos) {
		List<Slide> slides = new LinkedList<>();
		slides.addAll(getHorizontalSlides(photos));
		slides.addAll(getVerticalSlides(photos));
		return solveSlides(slides);
	}

	public static List<Slide> solveSlides(final List<Slide> slides) {
		final List<List<Photo>> order = splitByOrder(slides);

		for (final List<Photo> orderPhotos : order) {
			slides.addAll(solveSameOrder(orderPhotos));
		}

		return slides;
	}

	public static List<Slide> solveSameOrder(final List<Slide> slides) {
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
