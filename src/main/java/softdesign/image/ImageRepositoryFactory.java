package main.java.softdesign.image;

/**
 * Factory for creating instances of {@link ImageRepository}.
 */
public final class ImageRepositoryFactory {

	private ImageRepositoryFactory() {
	}

	/**
	 * Returns the default in-memory implementation.
	 *
	 * @return default implementation
	 */
	public static ImageRepository get() {
		return new ImageArrayListStore();
	}
}
