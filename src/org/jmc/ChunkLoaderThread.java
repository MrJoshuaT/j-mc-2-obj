package org.jmc;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

import javax.swing.JOptionPane;

public class ChunkLoaderThread extends Thread {

	private PreviewPanel preview;
	private File savepath;
	
	public ChunkLoaderThread(PreviewPanel preview, File savepath) {
		this.preview=preview;
		this.savepath=savepath;
	}
	
	@Override
	public void run() {
	
		Vector<AnvilRegion> regions=null;
		try {
			regions=AnvilRegion.loadAllRegions(savepath);
		} catch (IOException e1) {
			JOptionPane.showMessageDialog(null, "Couldn't load regions: "+e1);
			return;
		}				

		System.out.println("Found "+regions.size()+" regions");


		for(AnvilRegion region:regions)
		{

			for(Chunk chunk:region)
			{											
				if(chunk==null)
				{
					MainWindow.log("Chunk couldn't be loaded.");
					return;
				}

				BufferedImage height_img=chunk.getHeightImage();
				BufferedImage img=chunk.getBlocks();						
				BufferedImage blend=preview.blend(img, height_img, 0.4);

				int ix=chunk.getPosX();
				int iy=chunk.getPosZ();
				
				preview.addImage(blend, ix*64, iy*64);
				preview.repaint();
			}

		}
		
	}

}
