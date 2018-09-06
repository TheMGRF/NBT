package net.querz.nbt.mca;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public final class MCAUtil {

	private MCAUtil() {}

	//convenience methods to be consistent with NBTUtil
	public static MCAFile readMCAFile(File file) throws IOException {
		try (RandomAccessFile raf = new RandomAccessFile(file, "r")) {
			MCAFile mcaFile = new MCAFile();
			mcaFile.deserialize(raf);
			return mcaFile;
		}
	}

	public static MCAFile readMCAFile(String file) throws IOException {
		return readMCAFile(new File(file));
	}

	public static int writeMCAFile(File file, MCAFile mcaFile, boolean changeLastUpdate) throws IOException {
		try (RandomAccessFile raf = new RandomAccessFile(file, "rw")) {
			return mcaFile.serialize(raf, changeLastUpdate);
		}
	}

	public static int writeMCAFile(File file, MCAFile mcaFile) throws IOException {
		return writeMCAFile(file, mcaFile, false);
	}

	public static int writeMCAFile(String file, MCAFile mcaFile, boolean changeLastUpdate) throws IOException {
		return writeMCAFile(new File(file), mcaFile, changeLastUpdate);
	}

	public static int writeMCAFile(String file, MCAFile mcaFile) throws IOException {
		return writeMCAFile(new File(file), mcaFile, false);
	}

	public static String createNameFromChunkLocation(int chunkX, int chunkZ) {
		return "r." + chunkToRegion(chunkX) + "." + chunkToRegion(chunkZ) + ".mca";
	}

	public static String createNameFromBlockLocation(int blockX, int blockZ) {
		return "r." + blockToRegion(blockX) + "." + blockToRegion(blockZ) + ".mca";
	}

	public static String createNameFromRegionLocation(int regionX, int regionZ) {
		return "r." + regionX + "." + regionZ + ".mca";
	}

	public static int blockToChunk(int block) {
		return block >> 4;
	}

	public static int blockToRegion(int block) {
		return block >> 9;
	}

	public static int chunkToRegion(int chunk) {
		return chunk >> 5;
	}

	public static int regionToChunk(int region) {
		return region << 5;
	}

	public static int regionToBlock(int region) {
		return region << 9;
	}

	public static int chunkToBlock(int chunk) {
		return chunk << 4;
	}
}