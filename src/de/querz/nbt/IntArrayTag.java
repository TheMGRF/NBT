package de.querz.nbt;

import java.io.IOException;

public class IntArrayTag extends Tag {
	private int[] value;
	
	protected IntArrayTag() {
		this(new int[0]);
	}
	
	public IntArrayTag(int[] value) {
		this("", value);
	}
	
	public IntArrayTag(String name, int[] value) {
		super(TagType.INT_ARRAY, name);
		setValue(value);
	}
	
	public void setValue(int[] value) {
		this.value = value;
	}
	
	public int length() {
		return value.length;
	}
	
	@Override
	public int[] getValue() {
		return value;
	}

	@Override
	protected void serialize(NBTOutputStream nbtOut) throws IOException {
		nbtOut.dos.writeInt(value.length);
		for (int i : value)
			nbtOut.dos.writeInt(i);
	}
	
	@Override
	protected IntArrayTag deserialize(NBTInputStream nbtIn) throws IOException {
		int length = nbtIn.dis.readInt();
		value = new int[length];
		for (int i = 0; i < length; i++)
			value[i] = nbtIn.dis.readInt();
		return this;
	}

	@Override
	public String toTagString() {
		return NBTUtil.checkColon(this) + "[" + NBTUtil.joinInts(",", value) + "]";
	}
	
	@Override
	public String toString() {
		return "<int[]:" + getName() + ":[" + NBTUtil.joinInts(",", value) + "]>";
	}
	
	@Override
	public IntArrayTag clone() {
		return new IntArrayTag(getName(), value);
	}
}