package net.querz.nbt;

import junit.framework.TestCase;

import java.util.Arrays;
import java.util.Comparator;

public class ListTagTest extends NBTTestCase {

	private ListTag<ByteTag> createListTag() {
		ListTag<ByteTag> bl = new ListTag<>();
		bl.add(new ByteTag(Byte.MIN_VALUE));
		bl.add(new ByteTag((byte) 0));
		bl.add(new ByteTag(Byte.MAX_VALUE));
		return bl;
	}

	public void testStringConversion() {
		ListTag<ByteTag> bl = createListTag();
		assertTrue(3 == bl.size());
		assertEquals(Byte.MIN_VALUE, bl.get(0).asByte());
		assertEquals(0, bl.get(1).asByte());
		assertEquals(Byte.MAX_VALUE, bl.get(2).asByte());
		assertEquals("[-128b,0b,127b]", bl.toTagString());
		assertEquals("{\"type\":\"ListTag\"," +
				"\"value\":{" +
				"\"type\":\"ByteTag\"," +
				"\"list\":[" +
				"{\"type\":\"ByteTag\",\"value\":-128}," +
				"{\"type\":\"ByteTag\",\"value\":0}," +
				"{\"type\":\"ByteTag\",\"value\":127}]}}", bl.toString());
	}

	public void testEquals() {
		ListTag<ByteTag> bl = createListTag();

		ListTag<ByteTag> bl2 = new ListTag<>();
		bl2.addByte(Byte.MIN_VALUE);
		bl2.addByte((byte) 0);
		bl2.addByte(Byte.MAX_VALUE);
		assertTrue(bl.equals(bl2));

		ListTag<ByteTag> bl3 = new ListTag<>();
		bl2.addByte(Byte.MAX_VALUE);
		bl2.addByte((byte) 0);
		bl2.addByte(Byte.MIN_VALUE);
		assertFalse(bl.equals(bl3));

		ListTag<ByteTag> bl4 = new ListTag<>();
		bl2.addByte(Byte.MIN_VALUE);
		bl2.addByte((byte) 0);
		assertFalse(bl.equals(bl4));
	}

	public void testClone() {
		ListTag<ByteTag> bl = createListTag();

		ListTag<ByteTag> tc = bl.clone();
		assertTrue(bl.equals(tc));
		assertFalse(bl == tc);
		assertFalse(invokeGetValue(bl) == invokeGetValue(tc));
	}

	public void testSerializeDeserialize() {
		ListTag<ByteTag> bl = createListTag();
		byte[] data = serialize(bl);
		assertTrue(Arrays.equals(new byte[]{9, 0, 0, 1, 0, 0, 0, 3, -128, 0, 127}, data));
		ListTag<?> tt = (ListTag<?>) deserialize(data);
		assertNotNull(tt);
		ListTag<ByteTag> ttt = tt.asByteTagList();
		assertTrue(bl.equals(ttt));
	}

	public void testSerializeDeserializeEmptyList() {
		ListTag<IntTag> empty = new ListTag<>();
		byte[] data = serialize(empty);
		assertTrue(Arrays.equals(new byte[]{9, 0, 0, 0, 0, 0, 0, 0}, data));
		ListTag<?> et = (ListTag<?>) deserialize(data);
		assertNotNull(et);
		ListTag<ByteTag> ett = et.asByteTagList();
		assertTrue(empty.equals(ett));
	}

	public void testCasting() {
		ListTag<ByteTag> b = new ListTag<>();
		assertThrowsNoRuntimeException(() -> b.addShort((short) 123));
		assertThrowsRuntimeException(() -> b.addByte((byte) 123), IllegalArgumentException.class);
		assertThrowsNoRuntimeException(b::asShortTagList);
		assertThrowsRuntimeException(b::asByteTagList, ClassCastException.class);
		assertThrowsNoRuntimeException(() -> b.asTypedList(ShortTag.class));
		assertThrowsRuntimeException(() -> b.asTypedList(ByteTag.class), ClassCastException.class);
		b.remove(0);
		assertEquals(0, b.getTypeID());
		assertEquals(EndTag.class, b.getTypeClass());
		b.addByte((byte) 1);
		assertEquals(1, b.getTypeID());
		assertEquals(ByteTag.class, b.getTypeClass());
		b.clear();
		assertEquals(0, b.getTypeID());
		assertEquals(EndTag.class, b.getTypeClass());

		ListTag<?> l = new ListTag<>();
		assertThrowsNoRuntimeException(l::asByteTagList);
		l.addByte(Byte.MAX_VALUE);
		assertThrowsNoRuntimeException(l::asByteTagList);
		assertThrowsRuntimeException(l::asShortTagList, ClassCastException.class);

		l = new ListTag<>();
		l.addShort(Short.MAX_VALUE);
		assertThrowsNoRuntimeException(l::asShortTagList);
		assertThrowsRuntimeException(l::asIntTagList, ClassCastException.class);

		l = new ListTag<>();
		l.addInt(Integer.MAX_VALUE);
		assertThrowsNoRuntimeException(l::asIntTagList);
		assertThrowsRuntimeException(l::asLongTagList, ClassCastException.class);

		l = new ListTag<>();
		l.addLong(Long.MAX_VALUE);
		assertThrowsNoRuntimeException(l::asLongTagList);
		assertThrowsRuntimeException(l::asFloatTagList, ClassCastException.class);

		l = new ListTag<>();
		l.addFloat(Float.MAX_VALUE);
		assertThrowsNoRuntimeException(l::asFloatTagList);
		assertThrowsRuntimeException(l::asDoubleTagList, ClassCastException.class);

		l = new ListTag<>();
		l.addDouble(Double.MAX_VALUE);
		assertThrowsNoRuntimeException(l::asDoubleTagList);
		assertThrowsRuntimeException(l::asStringTagList, ClassCastException.class);

		l = new ListTag<>();
		l.addString("foo");
		assertThrowsNoRuntimeException(l::asStringTagList);
		assertThrowsRuntimeException(l::asByteArrayTagList, ClassCastException.class);

		l = new ListTag<>();
		l.addByteArray(new byte[]{Byte.MIN_VALUE, 0, Byte.MAX_VALUE});
		assertThrowsNoRuntimeException(l::asByteArrayTagList);
		assertThrowsRuntimeException(l::asIntArrayTagList, ClassCastException.class);

		l = new ListTag<>();
		l.addIntArray(new int[]{Integer.MIN_VALUE, 0, Integer.MAX_VALUE});
		assertThrowsNoRuntimeException(l::asIntArrayTagList);
		assertThrowsRuntimeException(l::asLongArrayTagList, ClassCastException.class);

		l = new ListTag<>();
		l.addLongArray(new long[]{Long.MIN_VALUE, 0, Long.MAX_VALUE});
		assertThrowsNoRuntimeException(l::asLongArrayTagList);
		assertThrowsRuntimeException(l::asListTagList, ClassCastException.class);

		ListTag<ListTag<?>> lis = new ListTag<>();
		lis.add(new ListTag<>());
		assertThrowsNoRuntimeException(lis::asListTagList);
		assertThrowsRuntimeException(lis::asCompoundTagList, ClassCastException.class);

		ListTag<CompoundTag> lco = new ListTag<>();
		lco.add(new CompoundTag());
		assertThrowsNoRuntimeException(lco::asCompoundTagList);
		assertThrowsRuntimeException(lco::asByteTagList, ClassCastException.class);
	}

	public void testCompareTo() {
		ListTag<IntTag> li = new ListTag<>();
		li.addInt(1);
		li.addInt(2);
		ListTag<IntTag> lo = new ListTag<>();
		lo.addInt(3);
		lo.addInt(4);
		assertEquals(0, li.compareTo(lo));
		lo.addInt(5);
		assertEquals(-1, li.compareTo(lo));
		lo.remove(2);
		lo.remove(1);
		assertEquals(1, li.compareTo(lo));
		assertEquals(0, li.compareTo(null));
	}

	public void testMaxDepth() {
		ListTag<ListTag<?>> root = new ListTag<>();
		ListTag<ListTag<?>> rec = root;
		for (int i = 0; i < Tag.MAX_DEPTH + 1; i++) {
			ListTag<ListTag<?>> l = new ListTag<>();
			rec.add(l);
			rec = l;
		}
		assertThrowsRuntimeException(() -> serialize(root), MaxDepthReachedException.class);
		assertThrowsRuntimeException(() -> deserializeFromFile("max_depth_reached.dat"), MaxDepthReachedException.class);
		assertThrowsRuntimeException(root::toString, MaxDepthReachedException.class);
		assertThrowsRuntimeException(root::toTagString, MaxDepthReachedException.class);
		assertThrowsRuntimeException(() -> root.valueToString(-1), IllegalArgumentException.class);
		assertThrowsRuntimeException(() -> root.valueToTagString(-1), IllegalArgumentException.class);
	}

	public void testRecursion() {
		ListTag<ListTag<?>> recursive = new ListTag<>();
		recursive.add(recursive);
		assertThrowsRuntimeException(() -> serialize(recursive), MaxDepthReachedException.class);
		assertThrowsRuntimeException(recursive::toString, MaxDepthReachedException.class);
		assertThrowsRuntimeException(recursive::toTagString, MaxDepthReachedException.class);
	}

	public void testContains() {
		ListTag<IntTag> l = new ListTag<>();
		l.addInt(1);
		l.addInt(2);
		assertTrue(l.contains(new IntTag(1)));
		assertFalse(l.contains(new IntTag(3)));
		assertTrue(l.containsAll(Arrays.asList(new IntTag(1), new IntTag(2))));
		assertFalse(l.containsAll(Arrays.asList(new IntTag(1), new IntTag(3))));
	}

	public void testSort() {
		ListTag<IntTag> l = new ListTag<>();
		l.addInt(2);
		l.addInt(1);
		l.sort(Comparator.comparingInt(NumberTag::asInt));
		assertEquals(1, l.get(0).asInt());
		assertEquals(2, l.get(1).asInt());
	}

	public void testIterator() {
		ListTag<IntTag> l = new ListTag<>();
		l.addInt(1);
		l.addInt(2);
		for (IntTag i : l) {
			assertNotNull(i);
		}
		l.forEach(TestCase::assertNotNull);
	}

	public void testSet() {
		ListTag<ByteTag> l = createListTag();
		l.set(1, new ByteTag((byte) 5));
		assertEquals(3, l.size());
		assertEquals(5, l.get(1).asByte());
		assertThrowsRuntimeException(() -> l.set(0, null), NullPointerException.class);
	}

	public void testAddAll() {
		ListTag<ByteTag> l = createListTag();
		l.addAll(Arrays.asList(new ByteTag((byte) 5), new ByteTag((byte) 7)));
		assertEquals(5, l.size());
		assertEquals(5, l.get(3).asByte());
		assertEquals(7, l.get(4).asByte());
		l.addAll(1, Arrays.asList(new ByteTag((byte) 9), new ByteTag((byte) 11)));
		assertEquals(7, l.size());
		assertEquals(9, l.get(1).asByte());
		assertEquals(11, l.get(2).asByte());
	}

	public void testAdd() {
		ListTag<ByteTag> l = new ListTag<>();
		l.addBoolean(true);
		assertThrowsRuntimeException(() -> l.addShort((short) 5), IllegalArgumentException.class);
		assertEquals(1, l.size());
		assertEquals(1, l.get(0).asByte());
		l.addByte(Byte.MAX_VALUE);
		assertEquals(2, l.size());
		assertEquals(Byte.MAX_VALUE, l.get(1).asByte());
		ListTag<ShortTag> s = new ListTag<>();
		s.addShort(Short.MAX_VALUE);
		assertEquals(1, s.size());
		assertEquals(Short.MAX_VALUE, s.get(0).asShort());
		ListTag<IntTag> i = new ListTag<>();
		i.addInt(Integer.MAX_VALUE);
		assertEquals(1, i.size());
		assertEquals(Integer.MAX_VALUE, i.get(0).asInt());
		ListTag<LongTag> lo = new ListTag<>();
		lo.addLong(Long.MAX_VALUE);
		assertEquals(1, lo.size());
		assertEquals(Long.MAX_VALUE, lo.get(0).asLong());
		ListTag<FloatTag> f = new ListTag<>();
		f.addFloat(Float.MAX_VALUE);
		assertEquals(1, f.size());
		assertEquals(Float.MAX_VALUE, f.get(0).asFloat());
		ListTag<DoubleTag> d = new ListTag<>();
		d.addDouble(Double.MAX_VALUE);
		assertEquals(1, d.size());
		assertEquals(Double.MAX_VALUE, d.get(0).asDouble());
		ListTag<StringTag> st = new ListTag<>();
		st.addString("foo");
		assertEquals(1, st.size());
		assertEquals("foo", st.get(0).getValue());
		ListTag<ByteArrayTag> ba = new ListTag<>();
		ba.addByteArray(new byte[] {Byte.MIN_VALUE, 0, Byte.MAX_VALUE});
		assertEquals(1, ba.size());
		assertTrue(Arrays.equals(new byte[] {Byte.MIN_VALUE, 0, Byte.MAX_VALUE}, ba.get(0).getValue()));
		ListTag<IntArrayTag> ia = new ListTag<>();
		ia.addIntArray(new int[] {Integer.MIN_VALUE, 0, Integer.MAX_VALUE});
		assertEquals(1, ia.size());
		assertTrue(Arrays.equals(new int[] {Integer.MIN_VALUE, 0, Integer.MAX_VALUE}, ia.get(0).getValue()));
		ListTag<LongArrayTag> la = new ListTag<>();
		la.addLongArray(new long[] {Long.MIN_VALUE, 0, Long.MAX_VALUE});
		assertEquals(1, la.size());
		assertTrue(Arrays.equals(new long[] {Long.MIN_VALUE, 0, Long.MAX_VALUE}, la.get(0).getValue()));
	}
}