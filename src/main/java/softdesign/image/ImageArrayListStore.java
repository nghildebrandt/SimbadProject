package main.java.softdesign.image;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * In-memory store for image.
 *
 * <p>A very primitive implementation of {@link ImageRepository}. It uses an {@link ArrayList} as its underlying storage
 * mechanism.
 *
 * <p>While in reality you'd want a persistent store, this implementation is sufficient for the purpose of the project
 * demo.
 */
public class ImageArrayListStore implements ImageRepository {

	private List<BufferedImage> imageList;

	public ImageArrayListStore() {
		this.imageList = new ArrayList<>();
	}

	@Override
	public List<BufferedImage> findAll() {
		return imageList;
	}

	@Override
	public void save(BufferedImage image) {
		imageList.add(image);
	}
}
