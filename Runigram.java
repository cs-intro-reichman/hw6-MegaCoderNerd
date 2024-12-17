import java.awt.Color;

/** A library of image processing functions. */
public class Runigram {

	public static void main(String[] args) {
	    
		//// Hide / change / add to the testing code below, as needed.
		
		// Tests the reading and printing of an image:	
		Color[][] tinypic = read("tinypic.ppm");
		print(tinypic);

		// Creates an image which will be the result of various 
		// image processing operations:
		Color[][] image;

		// Tests the horizontal flipping of an image:
		image = flippedHorizontally(tinypic);
		System.out.println();
		print(image);
		
		//// Write here whatever code you need in order to test your work.
		//// You can continue using the image array.
		image = flippedVertically(tinypic);
		System.out.println();
		print(image);

		image = grayScaled(tinypic);
		System.out.println();
		print(image);

		image = scaled(tinypic, 3, 5);
		System.out.println();
		print(image);
		image = blend(grayScaled(tinypic), tinypic, 0.5);
		System.out.println();
		print(image);
		setCanvas(image);
		
		morph(image, tinypic,2);
	}

	/** Returns a 2D array of Color values, representing the image data
	 * stored in the given PPM file. */
	public static Color[][] read(String fileName) {
		In in = new In(fileName);
		// Reads the file header, ignoring the first and the third lines.
		in.readString();
		int numCols = in.readInt();
		int numRows = in.readInt();
		in.readInt();
		// Creates the image array
		Color[][] image = new Color[numRows][numCols];
		// Reads the RGB values from the file into the image array. 
		// For each pixel (i,j), reads 3 values from the file,
		// creates from the 3 colors a new Color object, and 
		// makes pixel (i,j) refer to that object.
		//// Replace the following statement with your code.
		int rIndex = 0; 
		int colIndex = 0;
		while (!in.isEmpty() && rIndex < numRows){
			int red = in.readInt(); 
			int green = in.readInt(); 
			int blue = in.readInt();
			Color nColor = new Color(red,green,blue);
			if (colIndex == numCols){
				colIndex = 0; 
				rIndex++;
			}
			image[rIndex][colIndex] = nColor;
			colIndex++;
		}
		return image;
	}

    // Prints the RGB values of a given color.
	private static void print(Color c) {
	    System.out.print("(");
		System.out.printf("%3s,", c.getRed());   // Prints the red component
		System.out.printf("%3s,", c.getGreen()); // Prints the green component
        System.out.printf("%3s",  c.getBlue());  // Prints the blue component
        System.out.print(")  ");
	}

	// Prints the pixels of the given image.
	// Each pixel is printed as a triplet of (r,g,b) values.
	// This function is used for debugging purposes.
	// For example, to check that some image processing function works correctly,
	// we can apply the function and then use this function to print the resulting image.
	private static void print(Color[][] image) {
		//// Notice that all you have to so is print every element (i,j) of the array using the print(Color) function.
		for (int i = 0; i < image.length; i++){
			for (int j = 0; j < image[0].length-1; j++){
				print(image[i][j]);
				System.out.print(" ");
			}
			print(image[i][image[0].length-1]);
			System.out.println();
		}
	}
	
	/**
	 * Returns an image which is the horizontally flipped version of the given image. 
	 */
	public static Color[][] flippedHorizontally(Color[][] image) {
		Color[][] flippedImage = new Color[image.length][image[0].length];
		// we have an index for the flippedImage as we go in reverse order for the cols
		int colIndex = 0;
		for (int i = 0; i < image.length; i++){
			// we go in reverse to flip the array horizontally
			for (int j = image[0].length-1; j >= 0; j--){
				flippedImage[i][colIndex] = image[i][j];
				colIndex++;
			}
			colIndex = 0;
		}
		return flippedImage;
	}
	
	/**
	 * Returns an image which is the vertically flipped version of the given image. 
	 */
	public static Color[][] flippedVertically(Color[][] image){
		Color[][] flippedImage = new Color[image.length][image[0].length];
		// we have an index for the flippedImage as we go in reverse order for the rows
		int rowIndex = 0;
		// we iterate over the cols and then the rows
		// in order to set values vertically
		for (int c = 0; c < image[0].length; c++){
			// we go in reverse order for the rows
			for (int r = image.length-1; r >= 0; r--){
				flippedImage[rowIndex][c] = image[r][c];
				rowIndex++;
			}
			rowIndex = 0;
		}
		return flippedImage;
	}
	
	// Computes the luminance of the RGB values of the given pixel, using the formula 
	// lum = 0.299 * r + 0.587 * g + 0.114 * b, and returns a Color object consisting
	// the three values r = lum, g = lum, b = lum.
	private static Color luminance(Color pixel) {
		double rVal = (0.299 * pixel.getRed());
		double gVal = (0.587 * pixel.getGreen());
		double bVal = (0.114 * pixel.getBlue());
		// the value I give every pixel when gray scaled by formula
		int lum = (int) (rVal + gVal + bVal);
		Color lumColor = new Color(lum, lum, lum);
		return lumColor;
	}
	
	/**
	 * Returns an image which is the grayscaled version of the given image.
	 */
	public static Color[][] grayScaled(Color[][] image) {
		Color[][] grayImage = new Color[image.length][image[0].length];
		for (int i = 0; i < image.length; i++){
			for (int j = 0; j < image[0].length; j++){
				// I gray scale each pixel 
				grayImage[i][j] = luminance(image[i][j]);
			}

		}
		return grayImage;
	}	
	
	/**
	 * Returns an image which is the scaled version of the given image. 
	 * The image is scaled (resized) to have the given width and height.
	 */
	public static Color[][] scaled(Color[][] image, int width, int height) {
		double pixelHeightMul = (double) image.length / height; 
		double pixelWidthMul = (double) image[0].length / width;
		Color[][] scaledImage = new Color[height][width];
		for (int i = 0; i < height; i++){
			// the row index for our original image
			// we also don't want it to go out of bounds thus modulo is used
			int rowIndex = (int) (i*pixelHeightMul) % (image.length);
			for (int j = 0; j < width; j++){
				// the col index for our original image 
				// we also don't want it to go out of bounds thus modulo is used
				int colIndex = (int) (j*pixelWidthMul) % (image[0].length);
				scaledImage[i][j] = image[rowIndex][colIndex];
			}

		}
		return scaledImage;

	}
	
	/**
	 * Computes and returns a blended color which is a linear combination of the two given
	 * colors. Each r, g, b, value v in the returned color is calculated using the formula 
	 * v = alpha * v1 + (1 - alpha) * v2, where v1 and v2 are the corresponding r, g, b
	 * values in the two input color.
	 */
	public static Color blend(Color c1, Color c2, double alpha) {
		// just like the formula for each color individually
		int rVal = (int) (alpha * c1.getRed() + (1 - alpha) * c2.getRed());
		int gVal = (int) (alpha * c1.getGreen() + (1 - alpha) * c2.getGreen());
		int bVal = (int) (alpha * c1.getBlue() + (1 - alpha) * c2.getBlue());
		Color blended = new Color(rVal,gVal,bVal);
		return blended;
	}
	
	/**
	 * Cosntructs and returns an image which is the blending of the two given images.
	 * The blended image is the linear combination of (alpha) part of the first image
	 * and (1 - alpha) part the second image.
	 * The two images must have the same dimensions.
	 */
	public static Color[][] blend(Color[][] image1, Color[][] image2, double alpha) {
		Color[][] blendedImage = new Color[image1.length][image1[0].length];
		for (int i = 0; i < image1.length; i++){
			for (int j = 0; j < image1[0].length; j++){
				// I blend each pixel individually 
				blendedImage[i][j] = blend(image1[i][j], image2[i][j], alpha);
			}

		}
		return blendedImage;
	}

	/**
	 * Morphs the source image into the target image, gradually, in n steps.
	 * Animates the morphing process by displaying the morphed image in each step.
	 * Before starting the process, scales the target image to the dimensions
	 * of the source image.
	 */
	public static void morph(Color[][] source, Color[][] target, int n) {
		double currAlpha = 1;
		int height = source.length;
		int width = source[0].length;
		// scaling the target img if not the same size as the source
		if (target.length != height || target[0].length != width){
			target = scaled(target,width, height);
		} 
		// we execute the steps and change the alpha accordingly
		for (int i = 0; i < n; i++){
			currAlpha = (double) (n-i) / n;
			source = blend(source, target, currAlpha);
			// displaying intermidiate results
			display(source);
			// 500 ms is quite a long time between steps...
			StdDraw.pause(100);
		}
	}
	
	/** Creates a canvas for the given image. */
	public static void setCanvas(Color[][] image) {
		StdDraw.setTitle("Runigram 2023");
		int height = image.length;
		int width = image[0].length;
		StdDraw.setCanvasSize(height, width);
		StdDraw.setXscale(0, width);
		StdDraw.setYscale(0, height);
        // Enables drawing graphics in memory and showing it on the screen only when
		// the StdDraw.show function is called.
		StdDraw.enableDoubleBuffering();
	}

	/** Displays the given image on the current canvas. */
	public static void display(Color[][] image) {
		int height = image.length;
		int width = image[0].length;
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				// Sets the pen color to the pixel color
				StdDraw.setPenColor( image[i][j].getRed(),
					                 image[i][j].getGreen(),
					                 image[i][j].getBlue() );
				// Draws the pixel as a filled square of size 1
				StdDraw.filledSquare(j + 0.5, height - i - 0.5, 0.5);
			}
		}
		StdDraw.show();
	}
}

