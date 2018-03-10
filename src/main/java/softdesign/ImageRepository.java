package main.java.softdesign.image;

import java.awt.image.BufferedImage;
import java.util.List;

/**
 * Repository for storing images.
 *
 * This interfaces serves as an abstraction for image storage. Its purpose is to abstract the underlying storage
 * mechanism from the rest of the application.
 */
public interface ImageRepository {

    /**
     * Returns a list of all images.
     *
     * @return a list of all images
     */
    List<BufferedImage> findAll();

    /**
     * Saves an image.
     *
     * @param image image to be saved
     */
    void save(BufferedImage image);
}
