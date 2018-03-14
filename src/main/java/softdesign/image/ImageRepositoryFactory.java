package main.java.softdesign.image;

public final class ImageRepositoryFactory {

	private ImageRepositoryFactory() {
	}

	public static ImageRepository get() {
		return new ImageArrayListStore();
	}
}
