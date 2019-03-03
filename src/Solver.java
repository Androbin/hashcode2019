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

	private static List<Slide> getVerticalSlides(final List<Photo> photos) {
		List<Photo> verticalPhotos = photos.stream().filter(Photo::isVertical)
				.sorted((photo0, photo1) -> Integer.compare(photo0.getTags().size(), photo1.getTags().size()))
				.collect(Collectors.toList());
		// First with second ...
		final List<Slide> slides = new ArrayList<>(verticalPhotos.size() / 2);

		for (int i = 0; i < verticalPhotos.size() - 1; i += 2) {
			final Photo first = verticalPhotos.get(i);
			final Photo second = verticalPhotos.get(i + 1);
			slides.add(new SlideVertical(first, second));
		}

		return slides;
	}

	public static List<Slide> solve(final List<Photo> photos) {
		final List<Slide> slides = new LinkedList<>();
		slides.addAll(getHorizontalSlides(photos));
		slides.addAll(getVerticalSlides(photos));
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

	// use for dataset B
	private static List<Slide> solveUniSlides(final List<Slide> slides) {
		final List<UniSlide> uniSlides = new ArrayList<>();
		final Map<Slide, List<UniSlide>> map = new HashMap<>();

		for (final Slide slide : slides) {
			map.put(slide, new ArrayList<>());

			for (final String tag : slide.getTags()) {
				final UniSlide us = new UniSlide(slide, tag);
				uniSlides.add(us);
				map.get(slide).add(us);
			}
		}

		uniSlides.sort((s1, s2) -> s1.tag.compareTo(s2.tag));

		while (uniSlides.size() > slides.size()) {
			System.out.println(uniSlides.size() + "\t" + slides.size());

			for (int i = 0; i < uniSlides.size(); i++) {
				uniSlides.get(i).score = 0;
			}

			for (int i = 0; i < uniSlides.size() - 1; i++) {
				final UniSlide us1 = uniSlides.get(i);
				final UniSlide us2 = uniSlides.get(i + 1);
				final int score = slideInterest(us1.slide, us2.slide);
				us1.score += score;
				us2.score += score;
			}

			map.forEach((slide, uniSlides2) -> {
				uniSlides2.sort((us1, us2) -> -Integer.compare(us1.score, us2.score));
				final int keep = (uniSlides2.size() + 1) / 2;

				while (uniSlides2.size() > keep) {
					uniSlides.remove(uniSlides2.remove(uniSlides2.size() - 1));
				}
			});
		}

		final List<Slide> presentation = new ArrayList<>();

		for (final UniSlide uniSlide : uniSlides) {
			presentation.add(uniSlide.slide);
		}

		return presentation;
	}

	private static List<Slide> solveGreedy(final List<Slide> slides) {
		final List<Slide> presentation = new ArrayList<>(slides.size());
		final Slide first = slides.remove(0);
		presentation.add(first);
		Slide last = first;

		while (!slides.isEmpty()) {
			final Map<Slide, Integer> scores = new HashMap<>();

			for (final Slide slide : slides) {
				scores.put(slide, slideInterest(last, slide));
			}

			final Slide next = slides.stream().max((a, b) -> Integer.compare(scores.get(a), scores.get(b))).get();

			slides.remove(next);
			presentation.add(next);
			last = next;
		}

		System.out.println(score(presentation));
		return presentation;
	}

	// split into buckets ordered by size
	private static List<List<Slide>> splitByOrder(final List<Slide> slides) {
		final List<List<Slide>> output = new ArrayList<>();
		final int spread = 5;

		for (int i = 0; i < 100 * spread; i++) {
			output.add(new ArrayList<>());
		}

		for (final Slide slide : slides) {
			final int mod = slide.getPhotos()[0].getIdentifier() % spread;
			output.get((slide.getTags().size() - 1) * spread + mod).add(slide);
		}

		return output;
	}

	private static class UniSlide {
		public final Slide slide;
		public final String tag;
		public int score;

		public UniSlide(final Slide slide, final String tag) {
			this.slide = slide;
			this.tag = tag;
		}
	}

	// TODO: Google calculates a different score?
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