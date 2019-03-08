import java.util.*;
import java.util.stream.*;

public final class Solver {
	private Solver() {
	}

	private static <T> int intersect(final List<T> l0, final List<T> l1) {
		return (int) l0.stream().filter(l1::contains).count();
	}

	private static int slideInterest(final Slide slide0, final Slide slide1) {
		final int intersection = intersect(slide0.getTags(), slide1.getTags());
		return Math.min(slide0.getTags().size() - intersection,
				Math.min(intersection, slide1.getTags().size() - intersection));
	}

	private static List<Slide> getHorizontalSlides(final List<Photo> photos) {
		return photos.stream().filter(Photo::isHorizontal).map(photo -> new SlideHorizontal(photo))
				.collect(Collectors.toList());
	}

	private static List<Slide> getVerticalSlides(final List<Photo> photos, final boolean even) {
		final List<Photo> verticalPhotos = photos.stream().filter(Photo::isVertical)
				.sorted((photo0, photo1) -> Integer.compare(photo0.getTags().size(), photo1.getTags().size()))
				.collect(Collectors.toList());
		final List<Slide> slides = new ArrayList<>(verticalPhotos.size() / 2);

		if (even) {
			for (int i = 0; i < verticalPhotos.size() / 2; i++) {
				final Photo first = verticalPhotos.get(i);
				final Photo last = verticalPhotos.get(verticalPhotos.size() - i - 1);
				slides.add(new SlideVertical(first, last));
			}
		} else {
			for (int i = 0; i < verticalPhotos.size() - 1; i += 2) {
				final Photo first = verticalPhotos.get(i);
				final Photo second = verticalPhotos.get(i + 1);
				slides.add(new SlideVertical(first, second));
			}
		}

		return slides;
	}

	public static List<Slide> solve(final List<Photo> photos) {
		final List<Slide> slides = new LinkedList<>();
		slides.addAll(getHorizontalSlides(photos));
		slides.addAll(getVerticalSlides(photos, true));
		return solveSlides(slides);
	}

	private static List<Slide> solveSlides(final List<Slide> slides) {
		final List<Slide> presentation = new ArrayList<>();
		final List<List<Slide>> order = splitByOrder(slides);

		for (final List<Slide> orderPhotos : order) {
			if (orderPhotos.isEmpty()) {
				continue;
			}

			presentation.addAll(solveGreedy(orderPhotos));
		}

		return presentation;
	}

	private static List<Slide> solveGreedy(final List<Slide> slides) {
		final Map<String, List<Slide>> tags1 = new HashMap<>();

		for (final Slide slide : slides) {
			for (final String tag : slide.getTags()) {
				tags1.computeIfAbsent(tag, foo -> new ArrayList<>()).add(slide);
			}
		}

		final List<Slide> presentation = new ArrayList<>(slides.size());
		final Slide first = slides.remove(0);
		presentation.add(first);
		Slide last = first;

		for (final String tag : last.getTags()) {
			tags1.get(tag).remove(last);
		}

		while (!slides.isEmpty()) {
			final List<Slide> reduced = last.getTags().stream().flatMap(tag -> tags1.get(tag).stream()).distinct()
					.collect(Collectors.toList());

			final Map<Slide, Integer> scores = new HashMap<>();

			for (final Slide slide : reduced) {
				scores.put(slide, slideInterest(last, slide));
			}

			final Slide next = reduced.stream().max((a, b) -> Integer.compare(scores.get(a), scores.get(b)))
					.orElseGet(() -> slides.get(0));

			slides.remove(next);
			presentation.add(next);
			last = next;

			for (final String tag : last.getTags()) {
				tags1.get(tag).remove(last);
			}
		}

		System.out.println(score(presentation));
		return presentation;
	}

	// split into buckets ordered by size
	private static List<List<Slide>> splitByOrder(final List<Slide> slides) {
		final List<List<Slide>> output = new ArrayList<>();
		final int spread = 1;

		for (int i = 0; i < 100 * spread; i++) {
			output.add(new ArrayList<>());
		}

		for (final Slide slide : slides) {
			final int mod = slide.getPhotos()[0].getIdentifier() % spread;
			output.get((slide.getTags().size() - 1) * spread + mod).add(slide);
			// output.get(0).add(slide);
		}

		return output;
	}

	public static int score(final List<Slide> slides) {
		int result = 0;
		Slide lastSlide = null;

		for (final Slide slide : slides) {
			if (lastSlide == null) {
				lastSlide = slide;
				continue;
			}

			// Compute interest rate
			result += slideInterest(lastSlide, slide);
			lastSlide = slide;
		}

		return result;
	}
}