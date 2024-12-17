import java.awt.Color;

/*
 * This program combines two images one which is the source and one which
 *  is the grayScaled version of the source, we are given two arguments one which is 
 * the image itself and the other the number of sets for the morphing process
 */
public class Editor4 {

	public static void main (String[] args) {
		// exactly like Editor3 but here I make my target the grayScaled version of the source
		String source = args[0];
		int n = Integer.parseInt(args[1]);
		Color[][] sourceImage = Runigram.read(source);
		Color[][] target = Runigram.grayScaled(sourceImage);
		Runigram.setCanvas(sourceImage);
		Runigram.morph(sourceImage, target, n);
	}
}
